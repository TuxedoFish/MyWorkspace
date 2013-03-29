package utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;

public class GenrealRenderer {
	private int vaoId = glGenVertexArrays();
	private int vboId = glGenBuffers();
	
	public void renderLineCollection(LineCollection lc, ShaderHandler sh, DisplaySetup d, Controller parent) {
		
		if(parent.getColors().indexOf(new Color(0.0f, 0.0f, 0.0f, 1.0f)) == -1) {
			parent.addColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
		}
		int typeloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "type");
		glUniform1i(typeloc, 1);
		
		FloatBuffer Lines = BufferUtils.createFloatBuffer(lc.getData().capacity());
		Lines.position(0);
		Lines.put(lc.getData());
		Lines.rewind();
		
		//Bind to a Vertex Array Object
		glBindVertexArray(vaoId);
		//Bind data
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, Lines, GL_STATIC_DRAW);
		//Bind arrays
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		//Setup pointers
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 40, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 40, 16);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 40, 24);
		//Enable buffers
		glEnable(GL_TEXTURE0);
		
	    glBindTexture(GL_TEXTURE_2D, parent.getColorsID().get(parent.getColors().indexOf(new Color(0.0f, 0.0f, 0.0f, 1.0f))));
	    //Draw them as triangles
		glDrawArrays(GL_LINES, 0, Lines.capacity()/10);
	}
}
