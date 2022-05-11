package minigame;

public class Runners {
	int[] track;
	int location;
	boolean running;
	int track_num;
	public Runners(int track_num) {
		this.track_num = track_num;
		this.running = true;
		this.location = 0;
		this.track = new int[track_num];
		for(int i=0; i<track_num; i++) {
			this.track[i] = 1;
		}
		this.track[this.location] = 0;
	}
	
	public void Run() {
		this.location += 1;
		this.track[this.location] = 0;
		this.track[this.location-1] = -1;
		if(this.location==(track_num-1)) {
			this.running = false;
		}
	}
}
