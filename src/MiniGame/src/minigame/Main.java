package minigame;

@SuppressWarnings("serial")
public class Main extends PageManager{
	public static void main(String args[]) {
		StartPage page1 = new StartPage();
		MakePartyPage page2 = new MakePartyPage();
 
		
		while(!finish) {
			int pageN = PageManager.page;
					
			if(PageManager.page == 1) page1.setvisibility(true);
			if(PageManager.page == 2) page2.setvisibility(true);
			
			while(pageN == PageManager.page) {
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
}
