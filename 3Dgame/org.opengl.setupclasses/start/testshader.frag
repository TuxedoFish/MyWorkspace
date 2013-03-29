#version 400

uniform sampler2D image; 
uniform vec4 lightpos;
uniform vec4 viewDirection;

in vec2 pass_TextureCoord;
in vec4 outVertexNormal;
in mat4 Modelmatrix;
in mat4 Perspectivematrix;
in int type2;

out vec4 FragColor;

void main() {
    vec4 Color = texture2D(image, pass_TextureCoord);
    vec4 LightDirection = lightpos;
    
    if (0.43 * max(0.0, dot(outVertexNormal*Perspectivematrix*Modelmatrix, LightDirection)) >= 0.7)
    {
    	if (0.43 * max(0.0, dot(outVertexNormal*Perspectivematrix*Modelmatrix, LightDirection)) >= 0.95)
  	    {
  	    	if(Color.x+0.3 > 1.0)Color.x=1.0; else Color.x += 0.3;
            if(Color.y+0.3 > 1.0)Color.y=1.0; else Color.y += 0.3;
            if(Color.z+0.3 > 1.0)Color.z=1.0; else Color.z += 0.3;
  	    } else {
            if(Color.x+0.2 > 1.0)Color.x=1.0; else Color.x += 0.2;
            if(Color.y+0.2 > 1.0)Color.y=1.0; else Color.y += 0.2;
            if(Color.z+0.2 > 1.0)Color.z=1.0; else Color.z += 0.2;
        }
    }
    if (0.43 * max(0.0, dot(outVertexNormal*Perspectivematrix*Modelmatrix, LightDirection)) <= 0.3)
    {
         if(Color.x-0.2 < 0.0)Color.x=0.0; else Color.x -= 0.2;
         if(Color.y-0.2 < 0.0)Color.y=0.0; else Color.y -= 0.2;
         if(Color.z-0.2 < 0.0)Color.z=0.0; else Color.z -= 0.2;
    }
    FragColor = Color;
}