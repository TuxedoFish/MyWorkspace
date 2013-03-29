package terrain;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class LevelRenderer {
	private Matrix4f modelmatrix = new Matrix4f();
	private FloatBuffer modelmatrixfb = BufferUtils.createFloatBuffer(16);
	private IntBuffer indices;
	
	public LevelRenderer() {
		modelmatrix.store(modelmatrixfb);
		modelmatrixfb.flip();
	}
	public void update(ArrayList<Block> blocks, DisplaySetup d) {
		indices = getIndices(blocks, d);
	}
	public LevelHolder getLevelData(ArrayList<Block> blocks, TextureHolder th) {
		float[] data;
		FloatBuffer f = BufferUtils.createFloatBuffer(blocks.size() * 40);
		IntBuffer indices = BufferUtils.createIntBuffer(blocks.size() * 6);
		
		for(int i = 0;i < blocks.size(); i++) {
			FloatBuffer fb = getBlock(blocks.get(i).getTexid(), new Vector2f(blocks.get(i).getX(), 
					blocks.get(i).getY()), th, new Vector2f(blocks.get(i).getWidth(), 
					blocks.get(i).getHeight()));
			
			int[] fs = {
				(int)(i*4), (int)((i*4) + 1), (int)(i*4 + 2),
				(int)(i*4), (int)((i*4) + 2), (int)(i*4 + 3),
			};
			
			IntBuffer ib = BufferUtils.createIntBuffer(fs.length);
			ib.put(fs);
			ib.flip();
			
			for(int k = 0; k < ib.capacity(); k++) {
				indices.put((i*6) + k, ib.get(k));
			}
			for(int j = 0; j < fb.capacity(); j++) {
				f.put((i*40) + j, fb.get(j));
			}
		}
		
		f.position(0);
		f.rewind();
		
		indices.position(0);
		indices.rewind();
		
		return new LevelHolder(f, indices);
	}
	public FloatBuffer getBlock(int texid, Vector2f pos, TextureHolder th, Vector2f inaccsize) {
		Vector2f realpos = (pos);
		Vector2f[] texturecoords = th.getTextureCoords(texid);
		
		final Vector2f size = getRealLengths(new Vector2f(inaccsize.x, inaccsize.y));
		
		float[] data = {
				//top-left
				realpos.x, realpos.y, 0.0f, 1.0f,
				texturecoords[0].x, texturecoords[0].y,
				0.0f, 0.0f, 0.0f, 0.0f,
				//top-right
				realpos.x + size.x, realpos.y, 0.0f, 1.0f,
				texturecoords[1].x, texturecoords[1].y,
				0.0f, 0.0f, 0.0f, 0.0f,
				//bottom-right
				realpos.x + size.x, realpos.y - size.y, 0.0f, 1.0f,
				texturecoords[2].x, texturecoords[2].y,
				0.0f, 0.0f, 0.0f, 0.0f,
				//bottom-left
				realpos.x, realpos.y - size.y, 0.0f, 1.0f,
				texturecoords[3].x, texturecoords[3].y,
				0.0f, 0.0f, 0.0f, 0.0f,
		};
		FloatBuffer datafb = BufferUtils.createFloatBuffer(data.length);
		datafb.put(data);
		datafb.flip();
		
		return datafb;
	}
	public Vector2f getRealCoords(Vector2f v) {
		Vector2f rv = new Vector2f();
		
		rv.x = (v.x/(Display.getWidth()/2)) - 1.0f;
		rv.y = (v.y/(Display.getHeight()/2));
		
		return rv;
	}
	public IntBuffer getIndices(ArrayList<Block> blocks, DisplaySetup d) {
		int amount;
		ArrayList<Integer> blockstoadd = new ArrayList<Integer>(); 
		Vector4f realspace = new Vector4f();
		Vector4f eyespace = new Vector4f();
		
		for(int i = 0; i < blocks.size(); i++) {
			realspace = new Vector4f(blocks.get(i).getX(), blocks.get(i).getX(), 0.0f, 1.0f);
			Matrix4f.transform(d.getModelViewMatrixAsMatrix(), realspace, eyespace);
			
			if((eyespace.x > -1.1f && eyespace.x < 1.1f)) {
				blockstoadd.add(i);
			}
		}
		IntBuffer indices = BufferUtils.createIntBuffer(blockstoadd.size() * 6);
		for(int j = 0; j < blockstoadd.size(); j++) {
			int i = blockstoadd.get(j);
			
			int[] fs = {
					(int)(i*4), (int)((i*4) + 1), (int)(i*4 + 2),
					(int)(i*4), (int)((i*4) + 2), (int)(i*4 + 3),
			};
			IntBuffer ib = BufferUtils.createIntBuffer(fs.length);
			ib.put(fs);
			ib.flip();
			
			for(int k = 0; k < ib.capacity(); k++) {
				indices.put((j*6) + k, ib.get(k));
			}
		}
		return indices;
	}
	public Vector2f getRealLengths(Vector2f v) {
		Vector2f rv = new Vector2f();
		
		rv.x = v.x/Display.getWidth();
		rv.y = v.y/Display.getHeight();
		
		return rv;
	}
	public void render(LevelHolder lh, ShaderHandler sh, TextureHolder th,ArrayList<Block> blocks, DisplaySetup d) {
		DataUtils util = new DataUtils();
		
		util.setup(lh.getData(), lh.getVaoid(), lh.getVboid(), sh, th.getTexid(), 2, lh.getIndices());
		
		int modelmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ModelMatrix");
		glUniformMatrix4(modelmatrixloc, false, modelmatrixfb);
		
		glDrawElements(GL_TRIANGLES, lh.getIndices().capacity(), GL_UNSIGNED_INT, 0);
	}
}
