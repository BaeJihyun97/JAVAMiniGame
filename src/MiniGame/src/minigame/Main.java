package minigame;

@SuppressWarnings("serial")
public class Main extends PageManager implements Runnable{
	Server server = new Server();
	//Server server;
	Client client = new Client();
	StartPage page1 = new StartPage();
	MakePartyPage page2 = new MakePartyPage();
	Wait page3 = new Wait();
	G2go page6 = new G2go(client);
	Game1 page5 = new Game1();
	ReadyG1 page51 = new ReadyG1();
	Game3 page7 = new Game3();
	public Main() {
		
	}
	
	@Override
    public void run() {
		/* 스레드 실행코드 */
		try {
			if(PageManager.page == 4) {
				System.out.println("new thread for server");
				//this.server = new Server();
				this.server.init(PageManager.portS);
				this.server.startS();
				System.out.println("1");
				page3.setvisibility(false);
				System.out.println("2");
				this.server.closeS();
				//this.server = null;
				System.out.println("close the server");
			}
			else if(PageManager.page == 44) {
				System.out.println("new thread for game2");
				this.page6.G2Run();
				this.client.close();
				System.out.println("game over");
				this.page6.setvisibility(false);	
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			PageManager.page = 2;	
		}
		PageManager.page = 1;
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
						//main.server.init(PageManager.portS);
						Thread subTread1 = new Thread(main);
						subTread1.start();
						
						Thread.sleep(200);
						main.client.init("127.0.0.1", PageManager.portS);
						main.client.sendMessage("hello", PageManager.id, "0000");
						System.out.println("1111");
						String msg = main.client.receiveMessage().trim();
						if (msg.contains("connection allowed")) {
							System.out.println("2222");
							PageManager.page = 44;
							Thread subTread2 = new Thread(main);
							PageManager.state = msg.charAt(msg.length() - 1);
							System.out.println(PageManager.state);
							subTread2.start();
						}
						else PageManager.page = 2;
					}
					else { //client
						
						main.client.init(PageManager.ip, PageManager.portC);
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
			if(PageManager.page == 5) {
				main.page51.setvisibility(true);
				try {
					Thread.sleep(3000);
					main.page51.setvisibility(false);
					main.page5.setvisibility(true);
					main.page5.Start();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					main.page51.setvisibility(false);
					PageManager.page = 1;
				}
				
			}
			
			
			if(PageManager.page == 6) {
				System.out.println("game2");
				main.page6.setvisibility(true);	
				main.page3.setvisibility(false);
			}
			
			if(PageManager.page == 7) {
				main.page7.setvisibility(true);
				main.page7.Init();
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
