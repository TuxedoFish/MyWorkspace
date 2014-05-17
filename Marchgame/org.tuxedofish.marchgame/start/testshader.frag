#version 400

uniform sampler2D image; 
uniform vec4 lightpos;
uniform vec4 viewDirection;
uniform int brightness;

in vec2 pass_TextureCoord;
in vec4 outVertexNormal;
in mat4 Modelmatrix;
in mat4 Perspectivematrix;
in int type2;

out vec4 FragColor;

void main() {
	if(brightness == 1) {
  	  vec4 Color = texture2D(image, pass_TextureCoord);
  	  FragColor = vec4(Color.x, Color.y, Color.z, Color.w);
    } else {
      vec4 Color = texture2D(image, pass_TextureCoord);
      FragColor = Color;
    }
}