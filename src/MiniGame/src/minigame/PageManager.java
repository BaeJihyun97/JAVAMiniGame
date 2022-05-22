package minigame;

import javax.swing.JFrame;

@SuppressWarnings("serial")
abstract class PageManager extends JFrame {
	public static int page = 1; 
	public static boolean finish = false;
	public static int portC = 0, portS = 0;
	public static String ip = "", id = "", code="";
	public static int gameN = 0;
	public static int connection = 0;
	public static boolean over = false;
	public static char state = '0';
	
	
	
	public PageManager() {

	}
	
	public int getPage() {
		return PageManager.page;
	}
	
	public void setPage(int page){
		PageManager.page = page;
	}
	
	

}
