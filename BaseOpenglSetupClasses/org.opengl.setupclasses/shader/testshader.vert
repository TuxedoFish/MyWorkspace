#version 400
 
layout(location = 0) in vec4 VertexPosition;
layout(location = 2) in vec4 VertexNormal;
layout(location = 1) in vec2 VertexTextureCoordinate;

out vec2 pass_TextureCoord;
out vec4 position;
out vec3 varyingNormalDirection;
out mat4 v_inv;

uniform mat4 PerspectiveMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ModelMatrix;
uniform int type;

void main(void)
{
  pass_TextureCoord = VertexTextureCoordinate;
  varyingNormalDirection = normalize((inverse(transpose(mat3(ModelMatrix)))) * vec3(VertexNormal));
  v_inv = (transpose(ViewMatrix));
  
  if(type == 1) {
 		mat4 mvp = PerspectiveMatrix*ViewMatrix*ModelMatrix;
  		position = mvp * VertexPosition;
  		gl_Position = mvp * VertexPosition;
    } else {
    	position = VertexPosition;
  		gl_Position = VertexPosition;
    }
}