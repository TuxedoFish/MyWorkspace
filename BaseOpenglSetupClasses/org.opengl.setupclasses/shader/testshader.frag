#version 400

uniform sampler2D image; 

in vec2 pass_TextureCoord;

out vec4 FragColor;

void main() {
  	FragColor = texture2D(image, pass_TextureCoord);
}