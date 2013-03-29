package object;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.DisplaySetup;
import utils.Line;
import utils.LineCollection;

public class HighLevelObject {
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
