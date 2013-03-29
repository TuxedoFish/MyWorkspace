#version 400
 
layout(location = 0) in vec4 VertexPosition;
layout(location = 1) in vec2 VertexTextureCoordinate;
layout(location = 2) in vec4 VertexNormal;

out vec2 TextureCoord;
out vec4 position;
out vec3 varyingNormalDirection;
out mat4 v_inv;
out vec4 color;

uniform sampler2D image; 
uniform mat4 PerspectiveMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ModelMatrix;
uniform float type;
 
void main(void)
{
	vec4 realpos = (ViewMatrix) * VertexPosition;
		
	gl_Position = vec4(realpos.x, realpos.y, VertexPosition.z, realpos.w);
	TextureCoord = VertexTextureCoordinate;
}