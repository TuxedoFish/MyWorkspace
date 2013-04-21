package texture;

import java.awt.image.BufferedImage;

import org.lwjgl.util.vector.Vector3f;

public class Material {
	private String mtlname;
	private float ns, ni, d;
	private int illum;
	private Vector3f ka, kd, ks;
	private String map_kd, map_ka, map_ks, map_ns, map_d, map_bump;
	private BufferedImage texture;
	
	public Material(String mtlname, float ns, float ni, float d, int illum, Vector3f ka, Vector3f kd, Vector3f ks, String map_kd, 
			String map_ka, String map_ks, String map_ns, String map_d, String map_bump, BufferedImage texture) {
		this.mtlname = mtlname;
		this.ns = ns;
		this.d = d; 
		this.illum = illum;
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.map_d = map_d;
		this.map_kd = map_kd;
		this.map_ka = map_ka;
		this.map_ks = map_ks;
		this.map_ns = map_ns;
		this.map_d = map_d;
		this.map_bump = map_bump;
		this.texture = texture;
	}
	public String getMtlname() {
		return mtlname;
	}
	public float getNs() {
		return ns;
	}
	public BufferedImage getTexture() {
		return texture;
	}
	public float getNi() {
		return ni;
	}
	public float getD() {
		return d;
	}
	public int getIllum() {
		return illum;
	}
	public Vector3f getKa() {
		return ka;
	}
	public Vector3f getKd() {
		return kd;
	}
	public Vector3f getKs() {
		return ks;
	}
	public String getMap_kd() {
		return map_kd;
	}
	public String getMap_ka() {
		return map_ka;
	}
	public String getMap_ks() {
		return map_ks;
	}
	public String getMap_ns() {
		return map_ns;
	}
	public String getMap_d() {
		return map_d;
	}
	public String getMap_bump() {
		return map_bump;
	}
}
