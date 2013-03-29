#version 400

in vec2 TextureCoord;
uniform sampler2D image;
uniform int opacity;

out vec4 FragColor;

void main() {
	if(opacity == -1) {
		vec4 col = texture2D(image, TextureCoord);
  		FragColor =  vec4(col.x, col.y, col.z, col.w);
  	} else {
  		vec4 col = texture2D(image, TextureCoord);
  		FragColor =  vec4(col.x, col.y, col.z, float(opacity));
  	}
}