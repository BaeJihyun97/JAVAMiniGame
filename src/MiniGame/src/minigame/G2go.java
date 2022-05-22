package minigame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


@SuppressWarnings("serial")
public class G2go extends ComponentManger{
	JPanel grid_panel;
	GridBagLayout gBag, go_gBag;
	boolean isvisible = false;
	JButton[] gobuttonL = new JButton[361];
	Client client;
	char[] stateArr = new char[361];
	char ustate = '0'; //1=white, 2=black
	char turn = '0';
	int errorN = 0;
	
	
//	JPanel go_grid_panel = new JPanel();
	JPanel go_grid_panel=new JPanel() {		
		ImageIcon background = imageSetSize("./image/g2go.png", 800, 800);
		@Override
		protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.drawImage(background.getImage(), 0, 0, this); 
		}		
	};
	
	static int pi;
	
	
	public G2go(Client client) {
		this.client = client;
		setTitle("G2go");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(900, 900));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		
		gBag = new GridBagLayout();
		grid_panel = new JPanel();
		grid_panel.setBackground(Color.WHITE);
	    grid_panel.setLayout(gBag);
	    
	    go_gBag = new GridBagLayout();
	    go_grid_panel.setBackground(Color.WHITE);
	    go_grid_panel.setLayout(go_gBag);
	    go_grid_panel.setPreferredSize(new Dimension(800, 800));
	    //go_grid_panel.setLayout(null);
	    
	    
	    for (int i=0; i < 361; i++) {
	    	gobuttonL[i] = makeJButton(new JButton( Integer.toString(i)), new Font(null, Font.BOLD, 5), null, Color.WHITE, new Dimension(16, 16));
	    	gobuttonL[i].setFocusPainted(false);
	    	gobuttonL[i].setBorderPainted(false);
	    	gobuttonL[i].setOpaque(false);
	    	gbinsert(go_grid_panel, gobuttonL[i], (i % 19), (i / 19), 1, 1, new Insets(13, 13, 13, 13));
	    	
	    	gobuttonL[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchbutton(e);
					sendpackets();
				}
			});
	    	stateArr[i] = '0';
	    }
	   
	        
	   
	    gbinsert(grid_panel, go_grid_panel, 0, 0, 2, 2);
	    add("Center", grid_panel);
	}
	
	void switchbutton(ActionEvent e) {
		if(this.turn == this.ustate && this.turn != '0'){
			int i = Integer.parseInt(((JButton)e.getSource()).getText());
			this.stateArr[i] = this.ustate;
			updateGo();
		}	
	}
	
	void updateGo() {	
		for(int j=0; j < 361; j++) {
			String path;
			if(this.stateArr[j] != '0') {
				go_grid_panel.remove(gobuttonL[j]);
				path = (this.stateArr[j]=='1'?"./image/circle_w.png":"./image/circle_b.png");
				JLabel picLabel = new JLabel( imageSetSize(path, 24, 24) );
				gbinsert(go_grid_panel, picLabel, (j % 19), (j / 19), 1, 1, new Insets(4, 4, 4, 4));
				System.out.println(j + " " +this.stateArr[j]);
			}
			
		}
		
		
		go_grid_panel.revalidate();
		go_grid_panel.repaint();
	}
	
	public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
	
	public void sendpackets() {
		if(this.turn == this.ustate && this.turn != '0'){
			String msg = String.valueOf(this.turn) + String.valueOf(this.ustate) + String.valueOf(stateArr);
			//System.out.println("Clinet:: turn " + this.turn + " ustate " + this.ustate);
			//System.out.println(msg);
			client.sendMessage( msg, PageManager.id, "2003");
		}
		this.turn = '0';
	}
	
	public void getpackets() {
		String msg = client.receiveMessage();
		//System.out.println("read packet" + " " + PageManager.code + PageManager.code.equals("2001") + msg.equals("start"));
		if(msg == null) this.errorN += 1;
		if(PageManager.code.equals("2001") && msg.equals("start")) {
			PageManager.page = 6; 
			System.out.println("user game2 start");
			if(this.ustate == '1') this.turn = '1';
		}
		if(PageManager.code.equals("2002") && msg.equals("end")) PageManager.page = 1;
		if(PageManager.code.equals("2003")) {
			char[] msgstate = msg.toCharArray();
			this.turn = msgstate[0];
			System.out.println("Clinet:: turn " + this.turn + " ustate " + this.ustate);
			for(int i=0; i<361; i++) {
				this.stateArr[i] = msgstate[i+2];
			}
			updateGo();
		}
		//implement update
		//implement game check
	}
	
	public void G2Run() {
		
		this.ustate = PageManager.state;
		System.out.println("user : " + this.ustate + " game2 waiting...");
		while(!PageManager.over) {
			getpackets();
			if(this.errorN > 10) break;
		}
		System.out.println("game over");
	}
	//packet: turn/user/stateArr
	
}
