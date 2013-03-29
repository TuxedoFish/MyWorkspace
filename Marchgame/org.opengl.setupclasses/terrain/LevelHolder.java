package terrain;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
	
	private int texid;
	
	public LevelHolder(FloatBuffer data, IntBuffer indices) {
		this.data = data;
		this.indices = indices;
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
}
