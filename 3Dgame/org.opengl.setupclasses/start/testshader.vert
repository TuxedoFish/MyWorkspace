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
    if(type==1) {
  		gl_Position = (PerspectiveMatrix*ViewMatrix*ModelMatrix)*VertexPosition;
  	} else {
  		gl_Position = VertexPosition;
  	}
}