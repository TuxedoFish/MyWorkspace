#version 330

uniform sampler2D image; 
uniform int brightness;

in vec2 pass_TextureCoord;
in vec4 outVertexNormal;
in mat4 Modelmatrix;
in mat4 Perspectivematrix;

layout( location = 0 ) out vec4 FragColor;

void main() {
	if(brightness == 1) {
  	  vec4 Color = texture2D(image, pass_TextureCoord);
  	  FragColor = vec4(Color.x +0.75f, Color.y+0.75f, Color.z+0.75f, Color.w);
    } else {
      vec4 Color = texture2D(image, pass_TextureCoord);
      FragColor = Color;
    }
}