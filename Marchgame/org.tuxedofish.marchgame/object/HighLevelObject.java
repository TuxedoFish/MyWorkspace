package object;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.DisplaySetup;
import texture.TextureHandler;
import utils.Line;
import utils.LineCollection;

public class HighLevelObject {
	public void newCube(Vector4f pos, float width, float height, float depth, Color c, int texturequality, VertexHandler vh, TextureHandler th) {
		float realheight = height*(Display.getWidth()/Display.getHeight());
		Vector4f[] points = {
				new Vector4f(pos.x - (width/2), pos.y - (realheight/2), pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y - (realheight/2), pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y + (realheight/2), pos.z - (depth/2), pos.w),
				new Vector4f(pos.x - (width/2), pos.y + (realheight/2), pos.z - (depth/2), pos.w),
				
				new Vector4f(pos.x - (width/2), pos.y - (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y - (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y + (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x - (width/2), pos.y + (realheight/2), pos.z + (depth/2), pos.w),
		};
		Vector4f[] norms = {
				new Vector4f(pos.x - (width/2), pos.y - (realheight/2), pos.z - (depth/2) - 1.0f, pos.w),
				new Vector4f(pos.x + (width/2), pos.y - (realheight/2), pos.z - (depth/2) - 1.0f, pos.w),
				new Vector4f(pos.x + (width/2), pos.y + (realheight/2), pos.z - (depth/2) - 1.0f, pos.w),
				new Vector4f(pos.x - (width/2), pos.y + (realheight/2), pos.z - (depth/2) - 1.0f, pos.w),
				
				new Vector4f(pos.x - (width/2) - 1.0f, pos.y - (realheight/2), pos.z - (depth/2), pos.w),
				new Vector4f(pos.x - (width/2) - 1.0f, pos.y - (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x - (width/2) - 1.0f, pos.y + (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x + (width/2) - 1.0f, pos.y + (realheight/2), pos.z - (depth/2), pos.w),
				
				new Vector4f(pos.x + (width/2) + 1.0f, pos.y - (realheight/2), pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2) + 1.0f, pos.y - (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x + (width/2) + 1.0f, pos.y + (realheight/2), pos.z + (depth/2), pos.w),
				new Vector4f(pos.x + (width/2) + 1.0f, pos.y + (realheight/2), pos.z - (depth/2), pos.w),
				
				new Vector4f(pos.x - (width/2), pos.y - (realheight/2), pos.z + (depth/2) + 1.0f, pos.w),
				new Vector4f(pos.x + (width/2), pos.y - (realheight/2), pos.z + (depth/2) + 1.0f, pos.w),
				new Vector4f(pos.x + (width/2), pos.y + (realheight/2), pos.z + (depth/2) + 1.0f, pos.w),
				new Vector4f(pos.x - (width/2), pos.y + (realheight/2), pos.z + (depth/2) + 1.0f, pos.w),
				
				new Vector4f(pos.x - (width/2), pos.y + (realheight/2) + 1.0f, pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y + (realheight/2) + 1.0f, pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y + (realheight/2) + 1.0f, pos.z + (depth/2), pos.w),
				new Vector4f(pos.x - (width/2), pos.y + (realheight/2) + 1.0f, pos.z + (depth/2), pos.w),
				
				new Vector4f(pos.x - (width/2), pos.y - (realheight/2) - 1.0f, pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y - (realheight/2) - 1.0f, pos.z - (depth/2), pos.w),
				new Vector4f(pos.x + (width/2), pos.y - (realheight/2) - 1.0f, pos.z + (depth/2), pos.w),
				new Vector4f(pos.x - (width/2), pos.y - (realheight/2) - 1.0f, pos.z + (depth/2), pos.w),
		};
		LowLevelObject util = new LowLevelObject();
		util.newTriangle(new Vector4f[]{points[0],points[1],points[2]}, new Vector4f[]{norms[0],norms[1],norms[2]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[0],points[2],points[3]}, new Vector4f[]{norms[0],norms[2],norms[3]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[0],points[4],points[7]}, new Vector4f[]{norms[4],norms[5],norms[6]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[0],points[7],points[3]}, new Vector4f[]{norms[4],norms[6],norms[7]}, null, texturequality, c, vh, th);
		
		util.newTriangle(new Vector4f[]{points[1],points[5],points[6]}, new Vector4f[]{norms[8],norms[9],norms[10]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[1],points[6],points[2]}, new Vector4f[]{norms[8],norms[10],norms[11]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[4],points[5],points[6]}, new Vector4f[]{norms[12],norms[13],norms[14]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[4],points[6],points[7]}, new Vector4f[]{norms[12],norms[14],norms[15]}, null, texturequality, c, vh, th);
		
		util.newTriangle(new Vector4f[]{points[3],points[2],points[6]}, new Vector4f[]{norms[16],norms[17],norms[18]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[3],points[6],points[7]}, new Vector4f[]{norms[16],norms[18],norms[19]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[0],points[1],points[5]}, new Vector4f[]{norms[20],norms[21],norms[22]}, null, texturequality, c, vh, th);
		util.newTriangle(new Vector4f[]{points[0],points[5],points[4]}, new Vector4f[]{norms[20],norms[22],norms[23]}, null, texturequality, c, vh, th);
	}
	public LineCollection get3dRectWireframe(Vector4f pos, ShaderHandler sh, DisplaySetup d) {
		VertexHandler vh = new VertexHandler();
		
		LineCollection lc = new LineCollection();
		Line l;
		
		float le = 0.05f;
		
		Vector4f p1 = vh.vectorconvert(new Vector3f(pos.x - le, pos.y - le, pos.z + le));
		Vector4f p2 = vh.vectorconvert(new Vector3f(pos.x + le, pos.y - le, pos.z + le));
		Vector4f p3 = vh.vectorconvert(new Vector3f(pos.x - le, pos.y - le, pos.z - le));
		Vector4f p4 = vh.vectorconvert(new Vector3f(pos.x + le, pos.y - le, pos.z - le));
		Vector4f p5 = vh.vectorconvert(new Vector3f(pos.x - le, pos.y + le, pos.z + le));
		Vector4f p6 = vh.vectorconvert(new Vector3f(pos.x + le, pos.y + le, pos.z + le));
		Vector4f p7 = vh.vectorconvert(new Vector3f(pos.x - le, pos.y + le, pos.z - le));
		Vector4f p8 = vh.vectorconvert(new Vector3f(pos.x + le, pos.y + le, pos.z - le));
		
		l = new Line(p1, p2); lc.addLine(l);
		l = new Line(p2, p4); lc.addLine(l);
		l = new Line(p4, p3); lc.addLine(l);
		l = new Line(p3, p1); lc.addLine(l);
		l = new Line(p1, p5); lc.addLine(l);
		l = new Line(p5, p6); lc.addLine(l);
		l = new Line(p6, p8); lc.addLine(l);
		l = new Line(p8, p7); lc.addLine(l);
		l = new Line(p7, p5); lc.addLine(l);
		l = new Line(p7, p3); lc.addLine(l);
		l = new Line(p8, p4); lc.addLine(l);
		l = new Line(p6, p2); lc.addLine(l);
		
		return lc;
	}
}
