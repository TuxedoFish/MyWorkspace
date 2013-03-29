#version 400
 
layout(location = 0) in vec4 VertexPosition;
layout(location = 1) in vec2 VertexTextureCoordinate;
layout(location = 2) in vec4 VertexNormal;

out vec2 pass_TextureCoord;
out vec4 position;
out vec3 varyingNormalDirection;
out mat4 v_inv;
out vec4 color;

uniform sampler2D image; 
uniform mat4 PerspectiveMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ModelMatrix;
uniform int type;

struct lightSource
{
  vec4 position;
  vec4 diffuse;
  vec4 specular;
  float constantAttenuation, linearAttenuation, quadraticAttenuation;
  float spotCutoff, spotExponent;
  vec3 spotDirection;
};
lightSource light0 = lightSource(
  vec4(0.0,  0.0,  5.5, 1.0),
  vec4(1.0,  1.0,  1.0, 1.0),
  vec4(1.0,  1.0,  1.0, 1.0),
  0.0, 1.0, 0.0,
  180.0, 0.0,
  vec3(0.0, 0.0, 0.0)
);
vec4 scene_ambient = vec4(0.2, 0.2, 0.2, 1.0);
struct material
{
  vec4 ambient;
  vec4 diffuse;
  vec4 specular;
  float shininess;
};
vec4 texCol = texture2D(image, pass_TextureCoord);
material mymaterial = material(
  texCol,
  texCol,
  texCol,
  1.0
);
 
void main(void)
{
  if(type == 1) {
  mat4 mvp = PerspectiveMatrix*ViewMatrix*ModelMatrix;
  vec3 normalDirection = normalize(vec3(VertexNormal));
  vec3 viewDirection = normalize(vec3(inverse(ViewMatrix) * vec4(0.0, 0.0, 0.0, 1.0) - ModelMatrix*VertexPosition));
  vec3 lightDirection;
  float attenuation;
 
  if (light0.position.w == 0.0) // directional light
    {
      attenuation = 1.0; // no attenuation
      lightDirection = normalize(vec3(light0.position));
    }
  else // point or spot light (or other kind of light)
    {
      vec3 vertexToLightSource = vec3(light0.position - ModelMatrix*VertexPosition);
      float distance = length(vertexToLightSource);
      lightDirection = normalize(vertexToLightSource);
      attenuation = 1.0 / (light0.constantAttenuation
                           + light0.linearAttenuation * distance
                           + light0.quadraticAttenuation * distance * distance);
 
      if (light0.spotCutoff <= 90.0) // spotlight
        {
          float clampedCosine = max(0.0, dot(-lightDirection, normalize(light0.spotDirection)));
          if (clampedCosine < cos(radians(light0.spotCutoff))) // outside of spotlight cone
            {
              attenuation = 0.0;
            }
          else
            {
              attenuation = attenuation * pow(clampedCosine, light0.spotExponent);
            }
        }
    }
 
  vec3 ambientLighting = vec3(scene_ambient) * vec3(mymaterial.ambient);
 
  vec3 diffuseReflection = attenuation
    * vec3(light0.diffuse) * vec3(mymaterial.diffuse)
    * max(0.0, dot(normalDirection, lightDirection));
 
  vec3 specularReflection;
  if (dot(normalDirection, lightDirection) < 0.0) // light source on the wrong side?
    {
      specularReflection = vec3(0.0, 0.0, 0.0); // no specular reflection
    }
  else // light source on the right side
    {
      specularReflection = attenuation * vec3(light0.specular) * vec3(mymaterial.specular)
        * pow(max(0.0, dot(reflect(-lightDirection, normalDirection), viewDirection)),
              mymaterial.shininess);
    }
  		color = vec4(ambientLighting + diffuseReflection + specularReflection, 1.0);
  		gl_Position = mvp * VertexPosition;
    } else {
    	color = texture2D(image, pass_TextureCoord);
  		gl_Position = VertexPosition;
    }
}