package minigame;

public class G3_Ghost extends G3_Objects{
	int passed;
		
	G3_Ghost(int x,int y,int passed){
		this.setX(x);
		this.setY(y);
		this.passed = passed;
	}
	
	public void GMove(int px, int py){
		this.setX(px);
		this.setX(py);
	}
	
}
