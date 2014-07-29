package utils;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glViewport;
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
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import object.VertexHandler;
import shader.ShaderHandler;
import start.DisplaySetup;

public class DataUtils {
	public void setup(FloatBuffer data, VertexHandler vh, ShaderHandler sh, int textureID, int type, 
			IntBuffer indicesBuffer, FloatBuffer modelmatrixfb, int brightness) {
		
		setup(data, vh.getVboId(), vh.getVao(), sh, textureID, type, indicesBuffer, modelmatrixfb, brightness);
	}
	public IntBuffer getScreemnIndices() {
		int[] bgindices = {
				0, 1, 2,
				3, 4, 5
		};
		IntBuffer bgindicesb = BufferUtils.createIntBuffer(bgindices.length);
		bgindicesb.put(bgindices);
		bgindicesb.flip();
		
		return bgindicesb;
	}
	public FloatBuffer getScreen(Vector2f pos, int width, int height) {
		float realwidth = (float)width/Display.getWidth();
		float realheight = (float)height/(Display.getHeight()/2);
		
		float[] bgdata = {
				//0
				pos.x-realwidth, pos.y, 0.0f, 1.0f,
				0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//1
				pos.x+realwidth, pos.y, 0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//2
				pos.x+realwidth, pos.y-realheight, 0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//0
				pos.x-realwidth, pos.y, 0.0f, 1.0f,
				0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//2
				pos.x+realwidth, pos.y-realheight, 0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//3
				pos.x-realwidth, pos.y-realheight, 0.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f
		};
		FloatBuffer bgdatafb = BufferUtils.createFloatBuffer(bgdata.length);
		bgdatafb.put(bgdata);
		bgdatafb.flip();
		
		return bgdatafb;
	}
	public void begin(ShaderHandler sh, DisplaySetup d) {
			//Enable Transparency
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			//Clears display
			glClear(GL_COLOR_BUFFER_BIT |
				GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			glPushMatrix();

			ARBShaderObjects.glUseProgramObjectARB(sh.getPrograms().get(0).getId());
			
			int projectionmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "PerspectiveMatrix");
			glUniformMatrix4(projectionmatrixloc, false, d.getProjectionMatrix());
			
			int viewmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ViewMatrix");
			glUniformMatrix4(viewmatrixloc, false, d.getModelViewMatrix());
			
			int lightloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "lightpos");
			int viewdirloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "viewDirection");
			
			Vector4f viewdir = new Vector4f(0.0f, 0.0f, -1.0f, 1.0f);
			viewdir = Matrix4f.transform(d.getRotMatrix(), viewdir, viewdir);
			viewdir.normalise();
			Vector4f light = new Vector4f();
			glUniform4f(lightloc, light.x, light.y, light.z, light.w);
			glUniform4f(viewdirloc, viewdir.x, viewdir.y, viewdir.z, viewdir.w);
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
