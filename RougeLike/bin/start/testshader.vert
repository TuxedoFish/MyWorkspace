#version 400
 
layout(location = 0) in vec4 VertexPosition;
layout(location = 1) in vec2 VertexTextureCoordinate;
layout(location = 2) in vec4 VertexNormal;

out vec2 pass_TextureCoord;
out vec4 position;
out vec4 outVertexNormal;
out mat4 Modelmatrix;
out mat4 Perspectivematrix;
out vec4 color;
out int type2;

uniform mat4 PerspectiveMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ModelMatrix;
uniform int type;

void main(void)
{
	type2 = type;
	pass_TextureCoord = VertexTextureCoordinate;
	outVertexNormal = VertexNormal;
	Modelmatrix = ModelMatrix;
	Perspectivematrix = PerspectiveMatrix;
  		
  	vec4 pos = ModelMatrix*ViewMatrix*VertexPosition;
  	if(type == 2) {
  		gl_Position = vec4(pos.x, pos.y, pos.z, 1.0f);
  	} else {
  		gl_Position = vec4(VertexPosition.x, VertexPosition.y, VertexPosition.z, 1.0f);
  	}
}