package minigame;

public class G3_Player extends G3_Objects{
	boolean caught;
	boolean getKey;
	int passed;
		
	G3_Player(int x,int y){
		this.setX(x);
		this.setY(y);
		this.caught = false;
		this.getKey = false;
		passed = 0;
	}
	
	public void PMove(int px, int py){
		this.setX(px);
		this.setY(py);
	}
}
