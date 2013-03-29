package start;

import logic.Player;

public class TransparencyTimer extends Thread{
	Controller parent;
	
	public TransparencyTimer(Controller parent) {
		this.parent = parent;
		this.start();
	}
	@Override
	public void run() {
		int done = 0;
		while(!parent.getStopped() && done < 10) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			parent.updateStage();
			done += 1;
		}
	}
}
