package minigame;

@SuppressWarnings("serial")
public class Main extends PageManager{
	public static void main(String args[]) {
		Server server = new Server();
		Client client = new Client();
		StartPage page1 = new StartPage();
		MakePartyPage page2 = new MakePartyPage();
		Wait page3 = new Wait();
		
		while(!finish) {
			int pageN = PageManager.page;
			
			if(PageManager.page == 1) page1.setvisibility(true);
			if(PageManager.page == 2) page2.setvisibility(true);
			if(PageManager.page == 3) {
				page3.setvisibility(true);
				
				try {
					if(PageManager.ip == "") { //server
						
						server.init(PageManager.portS);
						//implement check the success of connection
					}
					else { //client
						client.init(PageManager.ip, PageManager.portC);
						//have to implement actions
						client.sendMessage("hello");
						client.sendMessage("This is the test");
						client.sendMessage("by~");
					}
					PageManager.page = 4;
				}
				catch (Exception e) {
					e.printStackTrace();
					PageManager.page = 2;
					
				}
				page3.setvisibility(false);
				// have to implement game2 and game 3
				
			}
			//have to implement game2 game3 page
			if(PageManager.page == 4) page1.setvisibility(true);
			
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
