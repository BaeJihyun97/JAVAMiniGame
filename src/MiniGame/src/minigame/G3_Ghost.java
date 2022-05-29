package minigame;

public class G3_Ghost extends G3_Objects{
	boolean vanished;
	int angry;
		
	G3_Ghost(int x,int y,int passed){
		this.setX(x);
		this.setY(y);
		this.vanished = false;
		this.angry = 0;
	}
	
	public void GMove(int px, int py){
		this.setX(px);
		this.setX(py);
	}
	
}
