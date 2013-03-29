package shader;

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

import java.util.ArrayList;

import org.lwjgl.opengl.ARBShaderObjects;

public class Program {
	private int id;
	private ArrayList<Integer> shaderstoadd = new ArrayList<Integer>();
	
	public Program() {
		id = ARBShaderObjects.glCreateProgramObjectARB();
	}
	public void bindshader(int id) {
		shaderstoadd.add(id);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void linkup() {
		//Add shaders
		for(int i = 0;i < shaderstoadd.size();i++) {
			ARBShaderObjects.glAttachObjectARB(this.id, shaderstoadd.get(i));
		}
		shaderstoadd.clear();
		//Link
		ARBShaderObjects.glLinkProgramARB(this.id);
		//Validate
		ARBShaderObjects.glValidateProgramARB(this.id);
	}
}
