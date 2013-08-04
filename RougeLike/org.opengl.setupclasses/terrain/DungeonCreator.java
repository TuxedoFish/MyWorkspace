package terrain;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import start.DisplaySetup;

public class DungeonCreator {
//	ArrayList<Block> blocks = new ArrayList<Block>();
//	ArrayList<CaveLine> cavelines =  new ArrayList<CaveLine>();
//
//	public DungeonCreator() {
//		addLine(new Vector2f(0.0f, -1.1f), 2.2f, 0.6f, 0.0f, 0.0f);
//		addLine(new Vector2f(-1.1f, 0.0f), 2.2f, 0.6f, 90.0f, 0.0f);
//		addCaveLine(0.0f, -1.1f, 2.2f, 0.0f, new Vector2f(0.0f, -1.1f));
//		addCaveLine(90.0f, -1.1f, 2.2f, 0.0f, new Vector2f(-1.1f, 0.0f));
//	}
//	public void addCaveLine(float d, float s, float l, float offset, Vector2f st) {
//		cavelines.add(new CaveLine(new Vector2f((float)((Math.sin(Math.toRadians(d))*s)+(Math.sin(Math.toRadians(d+90.0f))*offset)), 
//				(float)((Math.cos(Math.toRadians(d))*s)+(Math.cos(Math.toRadians(d+90.0f))*offset))),
//					new Vector2f((float)((Math.sin(Math.toRadians(d))*(s+l))+(Math.sin(Math.toRadians(d+90.0f))*offset)), 
//				(float)((Math.cos(Math.toRadians(d))*(s+l))+(Math.cos(Math.toRadians(d+90.0f))*offset))), (int)d, offset, st));
//	}
//	public void update(DisplaySetup d) {
//		for(int i=0; i<cavelines.size(); i++) {
//			Vector4f p1 = new Vector4f(cavelines.get(i).getP1().x, cavelines.get(i).getP1().y, 0.0f, 1.0f);
//			Vector4f p2 = new Vector4f(cavelines.get(i).getP2().x, cavelines.get(i).getP2().y, 0.0f, 1.0f);
//			p1 = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p1, p1);
//			p2 = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p2, p2);
//			if(p1.x < 1.0f && p1.x > -1.0f && p1.y < 1.0f && p1.y > -1.0f) {
//				float cosd = (float)Math.cos(Math.toRadians(cavelines.get(i).getDirection()));
//				float sind = (float)Math.sin(Math.toRadians(cavelines.get(i).getDirection()));
//				addLine(cavelines.get(i).getP1(), 
//						1.0f, 0.6f, cavelines.get(i).getDirection(), cavelines.get(i).getOffset());
//				Vector2f cvp = cavelines.get(i).getP1();
//				cavelines.get(i).setP1(new Vector2f(cvp.x - (1.0f*sind), cvp.y - (1.0f*cosd)));
//				int dist = (int)((Math.sqrt(Math.pow((cvp.x - ((1.0f*sind)))-cavelines.get(i).getStart().x, 2) + 
//						Math.pow((cvp.y - ((1.0f*cosd)))-cavelines.get(i).getStart().y, 2)))/(2.5f));
//				System.out.println(cvp + " : " + cavelines.get(i).getStart() + " : " + i);
//				if(dist>cavelines.get(i).getIntersect1()) {
//					cavelines.get(i).setIntersect1(dist);
//				//	if(cosd > sind) {
////						addLine(cavelines.get(i).getOffset()-1.1f, cavelines.get(i).getOffset()+2.2f, 0.6f, 
////								cavelines.get(i).getDirection()+90, (sind*(cvp.x+0.5f))+(cosd*(cvp.y+0.5f)));
////						addCaveLine(cavelines.get(i).getDirection()+90, cavelines.get(i).getOffset()-1.1f, 
////								cavelines.get(i).getOffset()+2.2f, (sind*(cvp.x-0.5f))+(cosd*(cvp.y+0.5f))
////								, new Vector2f(sind*((sind*(cvp.x-0.5f))+(cosd*(cvp.y+0.5f))), cosd*((sind*(cvp.x-0.5f))+(cosd*(cvp.y+0.5f)))));
////						//System.out.println(new Vector2f(sind*((sind*(cvp.x-0.5f))+(cosd*(-cvp.y+0.5f))), cosd*((sind*(cvp.x-0.5f))+(cosd*(-cvp.y+0.5f)))));
////					} else {
////						addLine(cavelines.get(i).getOffset()-1.1f, cavelines.get(i).getOffset()+2.2f, 0.6f, cavelines.get(i).getDirection()-90, (sind*(cvp.x+0.5f))+(cosd*(cvp.y-0.5f)));
////						addCaveLine(cavelines.get(i).getDirection()-90, cavelines.get(i).getOffset()-1.1f, 
////								cavelines.get(i).getOffset()+2.2f, (sind*(cvp.x-0.5f))+(cosd*(-cvp.y+0.5f))
////								, new Vector2f(sind*((sind*(cvp.x-0.5f))+(cosd*(cvp.y-0.5f))), cosd*((sind*(cvp.x-0.5f))+(cosd*(cvp.y-0.5f)))));
////						//System.out.println(new Vector2f(sind*((sind*(cvp.x-0.5f))+(cosd*(-cvp.y+0.5f))), cosd*((sind*(cvp.x-0.5f))+(cosd*(-cvp.y+0.5f)))));
////					}
//					System.out.println(cavelines.get(cavelines.size()-1).getStart());
//				}
//			}
//			if(p2.x < 1.0f && p2.x > -1.0f && p2.y < 1.0f && p2.y > -1.0f) {
//				float cosd = (float)Math.cos(Math.toRadians(cavelines.get(i).getDirection()));
//				float sind = (float)Math.sin(Math.toRadians(cavelines.get(i).getDirection()));
//				addLine(cavelines.get(i).getP2(), 
//						1.0f, 0.6f, cavelines.get(i).getDirection(), cavelines.get(i).getOffset());
//				Vector2f cvp = cavelines.get(i).getP2();
//				cavelines.get(i).setP2(new Vector2f(cvp.x + (1.0f*sind), cvp.y + (1.0f*cosd)));
//			}
//		}
//	}
//	public void addLine(Vector2f p1, Vector2f p2) {
//		float cosd = (float)Math.cos(Math.toRadians(direction));
//		float sind = (float)Math.sin(Math.toRadians(direction));
//		float cosd2 = (float)Math.cos(Math.toRadians(direction+90)); 
//		float sind2 = (float)Math.sin(Math.toRadians(direction+90));
//		float rat = ((cosd*Display.getWidth()) + (sind*Display.getHeight()));
//		float bsx = 50.0f/Display.getWidth(); float bsy = 50.0f/Display.getHeight();
//		width = (width*(Display.getWidth()/rat));
//		float abswidth = Math.abs(width);
//		width = width/width;
//		for(float i=0.0f;i<length;i+=0.01f) {
//			Vector2f sp = new Vector2f(start.x + (sind*i)-(sind2*(width/2))+(sind2*offset), 
//					start.y + (cosd*i)-(cosd2*(width/2))+(cosd2*offset));
//			for(float j=0.0f; j<abswidth; j+=0.01f) {
//				Vector2f pos = new Vector2f(((int)((sp.x+(sind2*(j*width)))/bsx))*bsx, ((int)((sp.y+(cosd2*(j*width)))/bsy))*bsy);
//				int coll = contains(pos);
//				if(coll == -1) {
//					if((j*width) <= 0.0f+(50.0f/rat) || (j*width) >= (abswidth*width)-(50.0f/rat)) {
//						blocks.add(new Block(pos.x, pos.y, 1, 50, 50));
//					} else {
//						blocks.add(new Block(pos.x, pos.y, 0, 50, 50));
//					}
//				} else {
//					if(coll == 1) {
//						if(!((j*width) <= 0.0f+(50.0f/rat) || (j*width) >= (abswidth*width)-(50.0f/rat))) {
//							delete(pos);
//							blocks.add(new Block(pos.x, pos.y, 0, 50, 50));
//						}
//					}
//				}
//			}
//		}
//	}
//	public int contains(Vector2f pos) {
//		for(int i=0; i<blocks.size(); i++) {
//			if(blocks.get(i).getX() == pos.x && blocks.get(i).getY() == pos.y) {
//				return blocks.get(i).getTexid();
//			}
//		}
//		return -1;
//	}
//	public void delete(Vector2f pos) {
//		for(int i=0; i<blocks.size(); i++) {
//			if(blocks.get(i).getX() == pos.x && blocks.get(i).getY() == pos.y) {
//				blocks.remove(i);
//				return;
//			}
//		}
//	}
//	public ArrayList<Block> getBlocks() {
//		return blocks;
//	}
}