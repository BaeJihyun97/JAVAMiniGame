package minigame;

public class Runners {
	int location;
	boolean running;
	int track_num;
	public Runners(int track_num) {
		this.track_num = track_num;
		this.running = true;
		this.location = 0;
	}
	
	public void Run() {
		this.location += 1;
		if(this.location==(track_num-1)) {
			this.running = false;
		}
	}
}
