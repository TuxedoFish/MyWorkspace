package physics;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import object.Shape;
import utils.MathUtils;

public class OctreeCollision {
	private Octree octree;
	private Shape s; 
	
	public OctreeCollision(Shape s) {
		this.s = s;
		update(s);
	}
	public Octree getOctree() {
		return octree;
	}
	public Shape getShape() {
		return s;
	}
	public void update(Shape s) {
		Vector4f pos = new Vector4f(s.getdata().get(0*10), s.getdata().get((0*10)+1), s.getdata().get((0*10)+2), 1.0f);
		Matrix4f rotmatrix = s.getRotMatrix();
		Matrix4f.transform(rotmatrix, pos, pos);
		
		ArrayList<Vector4f> positions = new ArrayList<Vector4f>();
		positions.add(pos);
		
		float lowestx = pos.x, lowesty = pos.y, lowestz = pos.z;
		float highestx = pos.x, highesty = pos.y, highestz = pos.z;
		
		for(int i=1; i<s.getPoints();i++) {
			pos = new Vector4f(s.getdata().get(i*10), s.getdata().get((i*10)+1), s.getdata().get((i*10)+2), 1.0f);
			Matrix4f.transform(rotmatrix, pos, pos);
			positions.add(pos);
			
			if(pos.x>highestx) highestx=pos.x;
			if(pos.x<lowestx) lowestx=pos.x;
			
			if(pos.y>highesty) highesty=pos.y;
			if(pos.y<lowesty) lowesty=pos.y;
			
			if(pos.z>highestz) highestz=pos.z;
			if(pos.z<lowestz) lowestz=pos.z;
		}
		
		octree = new Octree(new Vector4f(lowestx + ((highestx-lowestx)/2.0f), 
				lowesty + ((highesty-lowesty)/2.0f), lowestz + ((highestz-lowestz)/2.0f), 1.0f), 
				new Vector3f(highestx-lowestx, highesty-lowesty, highestz-lowestz));
		octree.setFilled(true);
		octree.createSubTrees(); Octree[] os = octree.getSubTrees();
		os[0].createSubTrees(); os[1].createSubTrees(); os[2].createSubTrees(); os[3].createSubTrees();
		os[4].createSubTrees(); os[5].createSubTrees(); os[6].createSubTrees(); os[7].createSubTrees();
		
		for(int i=0; i<positions.size()/3;i++) {
			float lowest2x = positions.get(i*3).x; float highest2x = positions.get(i*3).x; 
			float lowest2y = positions.get(i*3).y; float highest2y = positions.get(i*3).y;
			float lowest2z = positions.get(i*3).z; float highest2z = positions.get(i*3).z;
			for(int j=1; j<=2; j++) {
				if(positions.get((i*3)+j).x>highest2x)highest2x=positions.get((i*3)+j).x;
				if(positions.get((i*3)+j).x<lowest2x)lowest2x=positions.get((i*3)+j).x;
				if(positions.get((i*3)+j).y>highest2y)highest2y=positions.get((i*3)+j).y;
				if(positions.get((i*3)+j).y<lowest2y)lowest2y=positions.get((i*3)+j).y;
				if(positions.get((i*3)+j).z>highest2z)highest2z=positions.get((i*3)+j).z;
				if(positions.get((i*3)+j).z<lowest2z)lowest2z=positions.get((i*3)+j).z;
			}
			Vector3f vertpos = new Vector3f(lowest2x, highest2y, lowest2z);
			Vector3f vertdimensions = new Vector3f((highest2x-lowest2x), (highest2y-lowest2y), (highest2z-lowest2z));
			if(vertdimensions.x==0.0f)vertdimensions.x = 0.0001f;
			if(vertdimensions.y==0.0f)vertdimensions.y = 0.0001f;
			if(vertdimensions.z==0.0f)vertdimensions.z = 0.0001f;
			Rectangle3D vertbox = new Rectangle3D(vertpos, vertdimensions);
			ArrayList<Octree> tosearch = new ArrayList<Octree>();
			ArrayList<Octree> correct = new ArrayList<Octree>();
			tosearch.add(octree);
			
			for(int j=0; j<2; j++) {
				for(int k=0; k<tosearch.size(); k++) {
					Vector3f octdimensions = new Vector3f(tosearch.get(k).getDimensions());
					Vector4f temppos = new Vector4f(tosearch.get(k).getPos());
					Vector3f octpos = new Vector3f(temppos.x - (octdimensions.x/2.0f), 
							temppos.y + (octdimensions.y/2.0f), temppos.z - (octdimensions.z/2.0f));
					Rectangle3D octbox = new Rectangle3D(octpos, octdimensions);
					if(octbox.contains(vertbox)) {
						tosearch.get(k).setFilled(true);
						correct.add(tosearch.get(k));
					}
				}
				tosearch.clear();
				for(int k=0; k<correct.size();k++) {
					Octree[] os2 = correct.get(k).getSubTrees();
					tosearch.add(os2[0]); tosearch.add(os2[1]); tosearch.add(os2[2]); tosearch.add(os2[3]);
					tosearch.add(os2[4]); tosearch.add(os2[5]); tosearch.add(os2[6]); tosearch.add(os2[7]);
				}
				correct.clear();
			}
		}
	}
	public boolean testCollision(Vector4f p1, Vector4f p2) {
		ArrayList<Octree> tosearch = new ArrayList<Octree>();
		ArrayList<Octree> correct = new ArrayList<Octree>();
		tosearch.add(octree);
		int linesize = (int) Math.abs(Math.cbrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + 
				Math.pow(p1.z - p2.z, 2)))*10;
		float reallinesize = (float) (Math.cbrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + 
				Math.pow(p1.z - p2.z, 2)));
		for(int i=0; i<1; i++) {
			for(int j=0;j<tosearch.size();j++) {
				Vector3f octdimensions = new Vector3f(tosearch.get(j).getDimensions());
				Vector4f temppos = new Vector4f(tosearch.get(j).getPos());
				temppos = Matrix4f.transform(s.getModelMatrix(), temppos, temppos);
				Vector3f octpos = new Vector3f(temppos.x - (octdimensions.x/2.0f), 
						temppos.y + (octdimensions.y/2.0f),  temppos.z - (octdimensions.z/2.0f));
				Rectangle3D octbox = new Rectangle3D(octpos, octdimensions);
				boolean add=false;
				for(int k=0;k<linesize;k++) {
					Vector3f point = new Vector3f(p1.x + (((p1.x - p2.x)/reallinesize/10.0f)*-k), 
							p1.y + (((p1.y - p2.y)/reallinesize/10.0f)*-k), 
							p1.z + (((p1.z - p2.z)/reallinesize/10.0f)*-k));
					if((octbox.contains(point) && tosearch.get(j).getfilled())) {
						add = true;
						k = linesize;
					}
				}
				if(add) correct.add(tosearch.get(j));
			}
			if(correct.size()==0) {
				return false;
			}
			tosearch.clear();
			for(int k=0; k<correct.size();k++) {
				Octree[] os2 = correct.get(k).getSubTrees();
				tosearch.add(os2[0]); tosearch.add(os2[1]); tosearch.add(os2[2]); tosearch.add(os2[3]);
				tosearch.add(os2[4]); tosearch.add(os2[5]); tosearch.add(os2[6]); tosearch.add(os2[7]);
			}
			correct.clear();
		}
		return true;
	}
	public Vector4f testCollision(OctreeCollision o) {
		ArrayList<Octree> tosearch = new ArrayList<Octree>();
		ArrayList<Octree> correct = new ArrayList<Octree>();
		tosearch.add(octree);
		ArrayList<Octree> tosearch2 = new ArrayList<Octree>();
		ArrayList<Octree> correct2 = new ArrayList<Octree>();
		ArrayList<Octree> end = new ArrayList<Octree>();
		tosearch2.add(o.getOctree());
		for(int i=0; i<2; i++) {
			for(int j=0;j<tosearch.size();j++) {
				Vector3f octdimensions = new Vector3f(tosearch.get(j).getDimensions());
				Vector4f temppos = new Vector4f(tosearch.get(j).getPos());
				temppos = Matrix4f.transform(s.getModelMatrix(), temppos, temppos);
				Vector3f octpos = new Vector3f(temppos.x - (octdimensions.x/2.0f), 
						temppos.y + (octdimensions.y/2.0f),  temppos.z - (octdimensions.z/2.0f));
				Rectangle3D octbox = new Rectangle3D(octpos, octdimensions);
				boolean add=false;
				for(int k=0;k<tosearch2.size();k++) {
					Vector3f octdimensions2 = new Vector3f(tosearch2.get(k).getDimensions());
					Vector4f temppos2 = new Vector4f(tosearch2.get(k).getPos());
					temppos2 = Matrix4f.transform(o.getShape().getModelMatrix(), temppos2, temppos2);
					Vector3f octpos2 = new Vector3f(temppos2.x - (octdimensions2.x/2.0f), 
							temppos2.y + (octdimensions2.y/2.0f),  temppos2.z - (octdimensions2.z/2.0f));
					Rectangle3D octbox2 = new Rectangle3D(octpos2, octdimensions2);
					if((octbox2.contains(octbox) || octbox.contains(octbox2))) {
						add = true;
						correct2.add(tosearch2.get(k));
					}
				}
				if(add) correct.add(tosearch.get(j));
			}
			if(correct.size()==0 || correct2.size()==0) {
				return null;
			}
			tosearch.clear();
			for(int k=0; k<correct.size();k++) {
				Octree[] os2 = correct.get(k).getSubTrees();
				tosearch.add(os2[0]); tosearch.add(os2[1]); tosearch.add(os2[2]); tosearch.add(os2[3]);
				tosearch.add(os2[4]); tosearch.add(os2[5]); tosearch.add(os2[6]); tosearch.add(os2[7]);
			}
			tosearch2.clear();
			for(int k=0; k<correct2.size();k++) {
				Octree[] os2 = correct2.get(k).getSubTrees();
				tosearch2.add(os2[0]); tosearch2.add(os2[1]); tosearch2.add(os2[2]); tosearch2.add(os2[3]);
				tosearch2.add(os2[4]); tosearch2.add(os2[5]); tosearch2.add(os2[6]); tosearch2.add(os2[7]);
			}
			if(i==1) {
				for(int j=0; j<correct.size(); j++) {
					end.add(correct.get(j));
				}
				for(int j=0; j<correct2.size(); j++) {
					end.add(correct2.get(j));
				}
			}
			correct2.clear();
			correct.clear();
		}
		MathUtils util = new MathUtils();
		Vector4f centre = end.get(0).getPos();
		Vector3f d = end.get(0).getDimensions();
		Vector4f coll = new Vector4f(centre.x + d.x, centre.y + d.y, centre.z + d.z, 1.0f);
		
		for(int i=1; i<end.size(); i++) {
			centre = end.get(i).getPos();
			d = end.get(i).getDimensions();
			coll = util.addVectors(new Vector4f[] {coll, new Vector4f(centre.x + d.x, centre.y + d.y, 
					centre.z + d.z, 1.0f)});
		}
		coll = util.scaleVector(coll, 1.0f/(float)end.size());
		return coll;
	}
}