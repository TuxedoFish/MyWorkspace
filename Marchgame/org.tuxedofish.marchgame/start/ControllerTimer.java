package start;

import java.util.ArrayList;

public class ControllerTimer extends Thread{
	private Controller parent;
	private ArrayList<Integer> temp = new ArrayList<Integer>();
	private ArrayList<Integer> stages = new ArrayList<Integer>();
	private ArrayList<Boolean> disabled = new ArrayList<Boolean>();
	
	public ControllerTimer(Controller parent) {
		this.parent = parent;
		this.start();
	}
	public int addTimeStep(int timestep) {
		stages.add(timestep);
		disabled.add(false);
		temp.add(0);
		return stages.size()-1;
	}
	public void resetTimeStep(int index) {
		temp.set(index, 0);
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1);
				for(int i=0; i<temp.size(); i++) {
					if(i < temp.size() && !disabled.get(i)) {
						temp.set(i, temp.get(i) + 1);
						if(temp.get(i) >= stages.get(i)) {
							temp.set(i, 0);
							parent.update(stages.get(i), i);
						}
					}
				}
			} catch (InterruptedException e) {
				System.exit(1);
			}
		}
	}
	public int getThreadDuration(int threadid) {
		return temp.get(threadid);
	}
	public void removeTimeStep(Integer index) {
		disabled.set(index, true);
	}
}
