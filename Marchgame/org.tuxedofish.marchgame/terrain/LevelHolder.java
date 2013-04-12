package terrain;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import texture.TextureHolder;

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

public class LevelHolder {
	private FloatBuffer data;
	private IntBuffer indices;
	
	private int vboid = glGenBuffers();
	private int vaoid = glGenVertexArrays();
	private ArrayList<Integer> upordown = new ArrayList<Integer>();
	private ArrayList<Integer> texids = new ArrayList<Integer>();
	private ArrayList<Integer> currenttexids = new ArrayList<Integer>();
	private ArrayList<Integer> animationlength = new ArrayList<Integer>();
	
	private int texid;
	
	public LevelHolder(FloatBuffer data, IntBuffer indices, TextureHolder th) {
		this.data = data;
		this.indices = indices;
		for(int i=0; i<data.capacity()/40; i++) {
			upordown.add(0);
			texids.add(th.getTexID(new Vector2f(data.get((i*40)+4), 
					data.get((i*40)+5))));
			currenttexids.add(th.getTexID(new Vector2f(data.get((i*40)+4), 
					data.get((i*40)+5))));
		}
		for(int i=0; i<th.size(); i++) {
			animationlength.add(0);
		}
		animationlength.set(1, 3);
	}
	public FloatBuffer getData() {
		return data;
	}
	public IntBuffer getIndices() {
		return indices;
	}
	public int getVboid() {
		return vboid;
	}
	public int getVaoid() {
		return vaoid;
	}
	public int getTexid() {
		return texid;
	}
	public void update(TextureHolder th) {
		for(int i=0; i<data.capacity()/40; i++) {
			if(animationlength.get(texids.get(i)) != 0) {
				Vector2f[] texcoords = null;
				if(upordown.get(i) == 0) {
					if(currenttexids.get(i) >= texids.get(i) + animationlength.get(texids.get(i))) {
						upordown.set(i, 1);
						texcoords = th.getTextureCoords(currenttexids.get(i)-1);
						currenttexids.set(i, currenttexids.get(i)-1);
					} else {
						texcoords = th.getTextureCoords(currenttexids.get(i)+1);
						currenttexids.set(i, currenttexids.get(i)+1);
					}
				} else {
					if(currenttexids.get(i) <= texids.get(i)) {
						upordown.set(i, 0);
						texcoords = th.getTextureCoords(currenttexids.get(i)+1);
						currenttexids.set(i, currenttexids.get(i)+1);
					} else {
						texcoords = th.getTextureCoords(currenttexids.get(i)-1);
						currenttexids.set(i, currenttexids.get(i)-1);
					}
				}
				data.put((i*40) + 4, texcoords[0].x); data.put((i*40) + 5, texcoords[0].y);
				data.put((i*40) + 14, texcoords[1].x); data.put((i*40) + 15, texcoords[1].y);
				data.put((i*40) + 24, texcoords[2].x); data.put((i*40) + 25, texcoords[2].y);
				data.put((i*40) + 34, texcoords[3].x); data.put((i*40) + 35, texcoords[3].y);
			}
		}
	}
}
