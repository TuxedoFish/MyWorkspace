package texture;

import org.lwjgl.util.vector.Vector2f;

public class Texture {
	private int texid;
	private Vector2f[] texcoords;
	
	public Texture(int texid, Vector2f pos, float length, float parentlength) {
		this.texid = texid;
		
		texcoords = new Vector2f[] {
			new Vector2f(pos.x/parentlength, (pos.y/parentlength)),
			new Vector2f(((pos.x + length)/parentlength), (pos.y/parentlength)),
			new Vector2f(((pos.x + length)/parentlength), ((pos.y + length)/parentlength)),
			new Vector2f(pos.x/parentlength, ((pos.y + length)/parentlength))
		};
	}
	public Texture(int texid, Vector2f pos, float width, float height, float parentwidth, float parentheight) {
		this.texid = texid;
		
		texcoords = new Vector2f[] {
			new Vector2f((float)pos.x/parentwidth, (float)(pos.y/parentheight)),
			new Vector2f((float)(pos.x + width)/parentwidth, (float)(pos.y/parentheight)),
			new Vector2f((float)(pos.x + width)/parentwidth, (float)((pos.y + height)/parentheight)),
			new Vector2f((float)pos.x/parentwidth, (float)((pos.y + height)/parentheight))
		};
	}
	public int getTexid() {
		return texid;
	}
	public Vector2f[] getTextureCoords() {
		return texcoords;
	}
}
