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
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class DisplaySetup {
	private FloatBuffer projectionbuffer = BufferUtils.createFloatBuffer(16);
	
	private float xrot = 1; private float yrot = 0;
	
	private Matrix4f ModelViewMatrix = new Matrix4f();
	private Matrix4f RotMatrix = new Matrix4f();
	private Matrix4f ProjectionMatrix  = new Matrix4f();
	private Matrix4f ModelMatrix = new Matrix4f();
	
	private float fov;
	private float near, far;
	
	private Quaternion permangle = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);

	private Vector3f pos = new Vector3f(0, 0, 0);
	
	private FloatBuffer mvm = BufferUtils.createFloatBuffer(16);
	private FloatBuffer mm = BufferUtils.createFloatBuffer(16);
	
	public DisplaySetup(int w, int h, float znear, float zfar, ContextAttribs contextAtrributes) {
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
	public Matrix4f getRotMatrix() {
		return RotMatrix;
	}
	public void rotate(float xrot, float yrot, float zrot) {
		rotate(xrot, yrot, zrot, new Vector3f(0.0f, 0.0f, 0.0f));
	}
	public void rotate(float xrot, float yrot, float zrot, Vector3f rotpoint) {
		this.xrot += xrot;
		if(this.xrot > 360) {
			this.xrot -= 360;
		}
		if(this.xrot < 0) {
			this.xrot += 360;
		}
		double PI = Math.PI/180;
		float p = (float) (xrot*PI/2.0);
		float y = (float) (zrot*PI/2.0);
		float r = (float) (yrot*PI/2.0);
		
		double sinp = Math.sin(p);
		double siny = Math.sin(y);
		double sinr = Math.sin(r);
		double cosp = Math.cos(p);
		double cosy = Math.cos(y);
		double cosr = Math.cos(r);
		
		Quaternion local_rotation = new Quaternion();
		local_rotation.w  = (float) (cosr * cosp * cosy + sinr * sinp * siny);
		local_rotation.x = (float) (sinr * cosp * cosy - cosr * sinp * siny);
		local_rotation.y = (float) (cosr * sinp * cosy + sinr * cosp * siny);
		local_rotation.z = (float) (cosr * cosp * siny - sinr * sinp * cosy);
		local_rotation.normalise();
		
		Quaternion.mul(local_rotation, permangle, permangle);
		permangle.normalise();
		
		float x2 = permangle.x * permangle.x;
		float y2 = permangle.y * permangle.y;
		float z2 = permangle.z * permangle.z;
		float xy = permangle.x * permangle.y;
		float xz = permangle.x * permangle.z;
		float yz = permangle.y * permangle.z;
		float wx = permangle.w * permangle.x;
		float wy = permangle.w * permangle.y;
		float wz = permangle.w * permangle.z;
		
		RotMatrix.m00 = 1.0f - 2.0f * (y2 + z2);
		RotMatrix.m10 = 2.0f * (xy - wz);
		RotMatrix.m20 = 2.0f * (xz + wy);
		RotMatrix.m30 = rotpoint.x;
		
		RotMatrix.m01 = 2.0f * (xy + wz);
		RotMatrix.m11 = 1.0f - 2.0f * (x2 + z2);
		RotMatrix.m21 = 2.0f * (yz - wx);
		RotMatrix.m31 = rotpoint.y;
		
		RotMatrix.m02 = 2.0f * (xz - wy);
		RotMatrix.m12 = 2.0f * (yz + wx);
		RotMatrix.m22 = 1.0f - 2.0f * (x2 + y2);
		RotMatrix.m32 = rotpoint.z;
		
		RotMatrix.m03 = 0.0f;
		RotMatrix.m13 = 0.0f;
		RotMatrix.m23 = 0.0f;
		RotMatrix.m33 = 1.0f;
		
		RotMatrix.translate(new Vector3f(-rotpoint.x, -rotpoint.y, -rotpoint.z));
	}
	public Quaternion getRotation() {
		return permangle;
	}
	public Vector3f getPos() {
		return this.pos;
	}
	public float getFov() {
		return fov;
	}
	public float getNear() {
		return near;
	}
	public float getFar() {
		return far;
	}
	public void setupperspectivematrix(FloatBuffer src, float fov, float aspect,
			float znear, float zfar) {
		  this.fov = fov;
		  
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
		  
		  this.near = znear;
		  this.far = zfar;
	}
	public FloatBuffer getProjectionMatrix() {
		return projectionbuffer;
	}
	public Matrix4f getProjectionMatrixAsMatrix() {
		return ProjectionMatrix;
	}
	public FloatBuffer getModelViewMatrix() {
		Matrix4f m4f = new Matrix4f();
		
		m4f = Matrix4f.mul(RotMatrix, ModelViewMatrix, m4f);
		
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
		
		m4f = Matrix4f.mul(RotMatrix, ModelViewMatrix, m4f);
		
		return m4f;
	}
	public float getxrot() {
		return this.xrot;
	}
	public float getyrot() {
		return this.yrot;
	}
}
