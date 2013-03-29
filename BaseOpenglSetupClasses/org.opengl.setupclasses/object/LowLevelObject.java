package object;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import texture.TextureHandler;

public class LowLevelObject {
	private float defaultlength = 0.4f;
	
	public void setDefaultLength(int defaultlength) {
		this.defaultlength = defaultlength;
	}
	public void newTriangle(Vector4f[] points, Vector4f[] norms, Vector2f[] texcoords, int textureQuality, Color c,
			VertexHandler vh, TextureHandler th) {
		int p=points.length; boolean n=true; boolean tc=true;
		if(norms == null || norms.length < 3) n = false;
		if(texcoords == null || texcoords.length < 3) tc = false;
		if(p==1) {
			points = new Vector4f[] {
				new Vector4f(points[0].x + (defaultlength/2), points[0].y - (defaultlength/2), points[0].z, points[0].w),
				new Vector4f(points[0].x - (defaultlength/2), points[0].y - (defaultlength/2), points[0].z, points[0].w),
				new Vector4f(points[0].x, points[0].y  + (defaultlength/2.0f), points[0].z, points[0].w),
			};
		} else if(p==2){
			
		}
		if(!tc) texcoords = th.addTriangleTexture(points[0], points[1], points[2], textureQuality , c);	
		if(!n) {
			vh.addpolygon(vh.newvertex(points[0],texcoords[0], points[0]),
					vh.newvertex(points[1], texcoords[1], points[1]),
					vh.newvertex(points[2], texcoords[2], points[2]));
		} else {
			vh.addpolygon(vh.newvertex(points[0],texcoords[0], norms[0]),
					vh.newvertex(points[1], texcoords[1], norms[1]),
					vh.newvertex(points[2], texcoords[2], norms[2]));
		}
		
		if(!tc) th.getTextureAtlas().updatetexcoods(th.getShape());
	}
}
