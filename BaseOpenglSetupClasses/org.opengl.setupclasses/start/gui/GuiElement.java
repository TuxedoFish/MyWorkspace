package start.gui;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.util.vector.Vector2f;

public interface GuiElement {
	public Vector2f getPos();
	public BufferedImage getImg();
	public FloatBuffer getData();
	public IntBuffer getIndices();
	public Rectangle2D getBounds();
	
	public void setImg(BufferedImage img);
	public void setPos(Vector2f pos);
}
