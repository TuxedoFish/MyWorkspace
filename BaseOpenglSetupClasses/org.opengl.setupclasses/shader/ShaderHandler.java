package shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;


public class ShaderHandler {
	private ArrayList<Program> programs = new ArrayList<Program>();

	public int createprogram() {
		Program p = new Program();
		programs.add(p);
		return p.getId();
	}
	public void addshader(int pId, String shaderloc, int shadertype) {
		int program = -1;
		int shader = -1;
		
		for(int i = 0;i < programs.size();i++) {
			if(programs.get(i).getId() == pId) program = programs.get(i).getId();
		}
		
		if(program == -1) {
			return;
		}else {
			try {
				shader = createShader(shaderloc, shadertype);
				programs.get(program - 1).bindshader(shader);
			} catch (Exception e) {
				System.err.println("error binding shader @ " + shaderloc + " @ program " + pId);
				System.err.println("shader = " + shader);
				System.err.println(e.getLocalizedMessage());
				System.exit(1);
			}
		}
	}
	/**
	 * Loads text file as a shader
	 * @param shaderloc
	 * @param shadertype
	 * @return
	 * @throws Exception
	 */
	private int createShader(String shaderloc, int shadertype) throws Exception{
		//Shader object id
		int shader = 0;
		
		try{
			//Creates shader based on type of shader
			shader = ARBShaderObjects.glCreateShaderObjectARB(shadertype);
			//Stops an invalid shader
			if(shader == 0) {
				return 0;
			}
			//Makes our effectively ".txt" files into a stream
			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(shaderloc));
			//Compiles the glsl
			ARBShaderObjects.glCompileShaderARB(shader);
			//Tests for error
			if(ARBShaderObjects.glGetObjectParameterfARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			//Returns shader id
			return shader;
		}catch(Exception e) {
			//Deletes if their is a caught exception
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw e;
		}
	}
	private String getLogInfo(int obj) {
		//Returns info about error
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	private StringBuilder readFileAsString(String filename) {
		//Creates a variable to hold the source code
		StringBuilder source = new StringBuilder();
		
		try {
			//Creates a reader to get data from the file
			BufferedReader reader = new BufferedReader(new FileReader(
					new File(getClass().getResource(filename).getFile())));
			//Temporary holder variable
			String line;
			//Reads whole file
			while((line = reader.readLine()) != null) {
				//Reads it
				source.append(line).append("\n");
			}
			//Closes the stream
			reader.close();
		} catch (IOException e) {
			//Terminates if there is an exception
			e.printStackTrace();
			System.exit(1);
		}
		//Return file as a string
		return source;
	}
	public void finishprogram(int pId) {
		int program = -1;
		
		for(int i = 0;i < programs.size();i++) {
			if(programs.get(i).getId() == pId) program = programs.get(i).getId();
		}
		programs.get(program - 1).linkup();
	}
	public ArrayList<Program> getPrograms() {
		return programs;
	}
}
