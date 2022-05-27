package minigame;

public class G3_Player extends G3_Objects{
	private boolean caught;
	private boolean getKey;
	boolean left;
		
	G3_Player(int x,int y){
		this.setX(x);
		this.setY(y);
		this.caught = false;
		this.getKey = false;
		this.left = true;
	}
	
	public void PMove(int px, int py){
		this.setX(px);
		this.setY(py);
	}
	
	public boolean hasKey() {
		return this.getKey;
	}
	
	public void gotKey() {
		this.getKey = true;
	}
	
	public boolean getcaught() {
		return this.caught;
	}
	
	public void gotcaught() {
		this.caught = true;
	}
	
}
