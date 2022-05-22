package minigame;

@SuppressWarnings("serial")
public class Main extends PageManager implements Runnable{
	Server server = new Server();
	Client client = new Client();
	StartPage page1 = new StartPage();
	MakePartyPage page2 = new MakePartyPage();
	Wait page3 = new Wait();
	G2go page6 = new G2go(client);
	
	public Main() {
		
	}
	
	@Override
    public void run() {
		/* 스레드 실행코드 */
		try {
			if(PageManager.page == 4) {
				System.out.println("new thread for server");
				this.server.startS();
				page3.setvisibility(false);
				System.out.println("close the server");
			}
			else if(PageManager.page == 44) {
				System.out.println("new thread for game2");
				this.page6.G2Run();
				this.page6.setvisibility(false);	
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			PageManager.page = 2;	
		}
		
    }
	
	public static void main(String args[]) {
		Main main = new Main();
		
		while(!finish) {
			int pageN = PageManager.page;
			
			if(PageManager.page == 1) main.page1.setvisibility(true);
			if(PageManager.page == 2) main.page2.setvisibility(true);
			if(PageManager.page == 3) {
				main.page3.setvisibility(true);
				
				try {
					PageManager.page = 4;
					if(PageManager.ip == "") { //server
						main.server.init(PageManager.portS);
						Thread subTread1 = new Thread(main);
						subTread1.start();
						//participate as client too. do not make global memory
						//implement this
						
						main.client.init("127.0.0.1", PageManager.portS);
						//check connection number
						//synchronize data along clients
						//implement game algorithm.
						//after game over terminate above thread.
						main.client.sendMessage("hello", PageManager.id, "0000");	
						String msg = main.client.receiveMessage().trim();
						if (msg.contains("connection allowed")) {
							PageManager.page = 44;
							Thread subTread2 = new Thread(main);
							PageManager.state = msg.charAt(msg.length() - 1);
							subTread2.start();
						}
						else PageManager.page = 2;
					}
					else { //client
						
						main.client.init(PageManager.ip, PageManager.portC);
						//have to implement actions
						main.client.sendMessage("hello", PageManager.id, "0000");						
						String msg = main.client.receiveMessage().trim();
						if (msg.contains("connection allowed")) {
							PageManager.page = 44;
							Thread subTread2 = new Thread(main);
							PageManager.state = msg.charAt(msg.length() - 1);
							subTread2.start();
						}
						else PageManager.page = 2;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					PageManager.page = 2;		
				}
				
			}
			
			//have to implement game2 game3 page
			if(PageManager.page == 6) {
				System.out.println("game2");
				main.page6.setvisibility(true);	
				main.page3.setvisibility(false);
			}
			
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
