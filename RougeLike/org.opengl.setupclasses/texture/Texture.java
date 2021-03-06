package texture;

import org.lwjgl.util.vector.Vector2f;

public class Texture {
	private int texid;
	private Vector2f[] texcoords;
	
	public Texture(int texid, Vector2f pos, float length, float parentlength) {
		this.texid = texid;
		
		texcoords = new Vector2f[] {
			new Vector2f((float)pos.x/parentlength, (float)(pos.y/parentlength)),
			new Vector2f((float)((pos.x + length)/parentlength) - 0.012f, (float)(pos.y/parentlength)),
			new Vector2f((float)((pos.x + length)/parentlength) - 0.012f, (float)((pos.y + length)/parentlength) - 0.012f),
			new Vector2f((float)pos.x/parentlength, (float)((pos.y + length)/parentlength) - 0.012f)
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
