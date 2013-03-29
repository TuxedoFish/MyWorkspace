#version 400

in vec2 TextureCoord;
uniform sampler2D image;

out vec4 FragColor;

void main() {
  	FragColor =  texture2D(image, TextureCoord);
}