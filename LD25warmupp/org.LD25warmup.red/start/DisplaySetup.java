package start;

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

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class DisplaySetup {
	private FloatBuffer projectionbuffer = BufferUtils.createFloatBuffer(16);
	
	private float xrot = 1; private float yrot = 0;
	
	private Matrix4f ModelViewMatrix = new Matrix4f();
	private Matrix4f xRot = new Matrix4f();
	private Matrix4f yRot = new Matrix4f();
	private Matrix4f zRot = new Matrix4f();
	private Matrix4f ProjectionMatrix  = new Matrix4f();
	private Matrix4f ModelMatrix = new Matrix4f();

	private Vector3f pos = new Vector3f(0, 0, 0);
	
	private FloatBuffer mvm = BufferUtils.createFloatBuffer(16);
	private FloatBuffer mm = BufferUtils.createFloatBuffer(16);
	
	public DisplaySetup(int w, int h, float znear, float zfar) {
		initGL(w, h, znear, zfar);
	}
	public void initGL(int w, int h, float znear, float zfar) {
		try {
			//Creates a display to work with
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.setVSyncEnabled(true);
			Display.setTitle("Shader Setup");
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("error setting display up");
			e.printStackTrace();
			System.exit(1);
		}
		ModelMatrix.translate(new Vector3f(0.0f, 0.0f, 0.0f));
		//Defines the areas of the display
		glViewport(0, 0, w, h);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1);

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		projectionbuffer.clear();
		ProjectionMatrix.store(projectionbuffer);
		projectionbuffer.flip();
		
		setupperspectivematrix(projectionbuffer, 53.0f, (w/h), znear, zfar);
	}
	public void changepos(float xinc, float yinc, float zinc) {
			ModelViewMatrix.translate(new Vector3f(xinc, yinc, zinc));
			pos.x += xinc;
			pos.y += yinc;
			pos.z += zinc;
	}
	public void rotate(float xrot, float yrot, float zrot) {
		xRot.rotate((float) Math.toRadians(xrot), new Vector3f(0, 1, 0));
		yRot.rotate((float) Math.toRadians(yrot), new Vector3f(1, 0, 0));
		zRot.rotate((float) Math.toRadians(zrot), new Vector3f(0, 0, 1));
		
		this.xrot += xrot;
		this.yrot += yrot;
		
		if(this.xrot > 360) {
			this.xrot -= 360;
		}
		if(this.xrot < 0) {
			this.xrot += 360;
		}
		if(this.yrot > 360) {
			this.yrot -= 360;
		}
		if(this.yrot < 0) {
			this.yrot += 360;
		}
	}
	public void setPos(float x, float y, float z) {
		ModelViewMatrix.translate(new Vector3f(x - pos.x, y - pos.y, z - pos.z));
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}
	public Vector3f getPos() {
		return this.pos;
	}
	public void setupperspectivematrix(FloatBuffer src, float fov, float aspect,
			float znear, float zfar) {
		
		  float ymax = (float) (znear * Math.tan(fov * (Math.PI/360)));
		  float ymin = -ymax;
		  float xmax = ymax * aspect;
		  float xmin = ymin * aspect;

		  float width = xmax - xmin;
		  float height = ymax - ymin;

		  float depth = zfar - znear;
		  float q = -(zfar + znear) / depth;
		  float qn = -2 * (zfar * znear) / depth;

		  float w = 2 * znear / width;
		  float h = 2 * znear / height;
		  
		  src.put(0, w);
		  src.put(1, 0);
		  src.put(2, 0);
		  src.put(3, 0);

		  src.put(4, 0);
		  src.put(5, h);
		  src.put(6, 0);
		  src.put(7, 0);

		  src.put(8, 0);
		  src.put(9, 0);
		  src.put(10, q);
		  src.put(11, -1);

		  src.put(12, 0);
		  src.put(13, 0);
		  src.put(14, qn);
		  src.put(15, 0);
	}
	public FloatBuffer getProjectionMatrix() {
		return projectionbuffer;
	}
	public Matrix4f getProjectionMatrixAsMatrix() {
		return ProjectionMatrix;
	}
	public FloatBuffer getModelViewMatrix() {
		Matrix4f m4f = new Matrix4f();
		
		m4f = Matrix4f.mul(xRot, ModelViewMatrix, m4f);
		m4f = Matrix4f.mul(yRot, m4f, m4f);
		m4f = Matrix4f.mul(zRot, m4f, m4f);
		
		mvm.clear();
		m4f.store(mvm);
		mvm.flip();
		return mvm;
	}
	public FloatBuffer getModelMatrix() {
		mm.clear();
		ModelMatrix.store(mm);
		mm.flip();
		return mm;
	}
	public Matrix4f getModelViewMatrixAsMatrix() {
		Matrix4f m4f = new Matrix4f();
		
		m4f = Matrix4f.mul(xRot, ModelViewMatrix, m4f);
		m4f = Matrix4f.mul(yRot, m4f, m4f);
		m4f = Matrix4f.mul(zRot, m4f, m4f);
		
		return m4f;
	}
	public float getxrot() {
		return this.xrot;
	}
	public float getyrot() {
		return this.yrot;
	}
}
