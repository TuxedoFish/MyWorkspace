package start;

public class ControllerTimer extends Thread{
	Controller parent;
	
	public ControllerTimer(Controller parent) {
		this.parent = parent;
		this.start();
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				parent.update();
			} catch (InterruptedException e) {
				System.exit(1);
			}
		}
	}
}
