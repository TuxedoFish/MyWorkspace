package utils;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import object.VertexHandler;
import shader.ShaderHandler;

public class DataUtils {
	public void setup(FloatBuffer data, VertexHandler vh, ShaderHandler sh, int textureID, int type, 
			IntBuffer indicesBuffer, FloatBuffer modelmatrixfb, int brightness) {
		
		setup(data, vh.getVboId(), vh.getVao(), sh, textureID, type, indicesBuffer, modelmatrixfb, brightness);
	}
	public void setup(FloatBuffer data, int vboID, int vaoID, ShaderHandler sh, int textureID, int type, 
			IntBuffer indicesBuffer, FloatBuffer modelmatrixfb, int brightness) {
		glBindVertexArray(vaoID);
		//Bind data
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		//Bind indices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glGenBuffers());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		//Bind arrays
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		int typeloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "type");
		glUniform1i(typeloc, type);
		if(type == 2) {
			int modelmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ModelMatrix");
			glUniformMatrix4(modelmatrixloc, false, modelmatrixfb);
		}
		int brightnessloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "brightness");
		glUniform1i(brightnessloc, brightness);
		//Setup pointers
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 40, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 40, 16);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 40, 24);
		//Enable buffers
		glEnable(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	public void drawRectangle() {
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
}
