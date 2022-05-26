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
	boolean win;
	boolean over;

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
					System.out.println(stateArr);
				}
			});
	    	stateArr[i] = '0';
	    }
	   
	        
	   
	    gbinsert(grid_panel, go_grid_panel, 0, 0, 2, 2);
	    add("Center", grid_panel);
	    this.over = false;
	}
	
	void switchbutton(ActionEvent e) {
		if(this.turn == this.ustate && this.turn != '0'){
			int i = Integer.parseInt(((JButton)e.getSource()).getText());
			this.stateArr[i] = this.ustate;
			updateGo(i);
			if(Winner()) this.over = true; this.win = true; System.out.println("Game Over. You are winner" + (!this.over && !PageManager.over));	
		}	
	}
	
	void updateGo(int i) {	
		
		go_grid_panel.remove(gobuttonL[i]);
		String path = (this.stateArr[i]=='2'?"./image/circle_b.png":"./image/circle_w.png");
		
		System.out.println("i-th: " +i+"["+this.stateArr[i]+"] "+(this.stateArr[i]=='2'));
		System.out.println(this.stateArr);
		
		JLabel picLabel = new JLabel( imageSetSize(path, 24, 24) );
		gbinsert(go_grid_panel, picLabel, (i % 19), (i / 19), 1, 1, new Insets(9, 9, 9, 9));
		
		
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
			if(this.over) {
				client.sendMessage( msg, PageManager.id, "2004");
				client.sendMessage( msg, PageManager.id, "2004");
				//implement rest
			}
			else {
				client.sendMessage( msg, PageManager.id, "2003");
				client.sendMessage( msg, PageManager.id, "2003");
			}
			
		}
		this.turn = '0';
	}
	
	public void getpackets() {
		String msg = client.receiveMessage();
		//System.out.println("read packet" + " " + PageManager.code + PageManager.code.equals("2001") + msg.equals("start"));
		if(msg == null) this.errorN += 1;
		if(PageManager.code.equals("2001") && msg.equals("start")) {
			this.ustate = PageManager.state;
			PageManager.page = 6; 
			System.out.println("user : "+this.ustate+" game2 start");
			if(this.ustate == '1') this.turn = '1';
		}
		else if(PageManager.code.equals("2002") && msg.equals("end")) PageManager.page = 1;
		else if(PageManager.code.equals("2003")) {
			char[] msgstate = msg.toCharArray();
			this.turn = msgstate[0];
			//ystem.out.println("Clinet:: turn " + this.turn + " ustate " + this.ustate);
			for(int i=0; i<361; i++) {
				if(this.stateArr[i] != msgstate[i+2]) {
					this.stateArr[i] = msgstate[i+2];
					updateGo(i);
				}
				
			}
		}
		else if(PageManager.code.equals("2004")) {
			if (!msg.equals(PageManager.id)) {
				this.win = false;	
			}
			else {
				this.win = true;
			}
			this.over = true;
		}
	}
	
	public boolean Winner() {
		for (int i=0; i < 361; i++) {
			int count, j;
			if(this.stateArr[i] == this.ustate) {
				//for x axis
				count = 1; j = i + 1;
				while(j/19 == i/19 && j >= 0 && j <= 361) {	
					if(this.stateArr[j] == this.ustate) {count += 1; j += 1;}
					else break;
				}
				j = i -1;
				while(j/19 == i/19 && j >= 0 && j <= 361) {
					if(this.stateArr[j] == this.ustate) {count += 1; j -= 1;}
					else break;
				}
				if(count == 5) return true;
				//for y axis
				count = 1; j = i + 19;
				while(j%19 == i%19 && j >= 0 && j <= 361) {	
					if(this.stateArr[j] == this.ustate) {count += 1; j += 19;}
					else break;
				}
				j = i - 19;
				while(j%19 == i%19 && j >= 0 && j <= 361) {
					if(this.stateArr[j] == this.ustate) {count += 1; j -= 19;}
					else break;
				}
				if(count == 5) return true;
				//for y=x axis
				count = 1; j = i + 18;
				while(j >= 0 && j <= 361) {	
					if(this.stateArr[j] == this.ustate) {count += 1; j += 18;}
					else break;
				}
				j = i - 18;
				while(j >= 0 && j <= 361) {
					if(this.stateArr[j] == this.ustate) {count += 1; j -= 18;}
					else break;
				}
				if(count == 5) return true;
				//for y=-x axis
				count = 1; j = i + 20;
				while(j >= 0 && j <= 361) {	
					if(this.stateArr[j] == this.ustate) {count += 1; j += 20;}
					else break;
				}
				j = i - 20;
				while(j >= 0 && j <= 361) {
					if(this.stateArr[j] == this.ustate) {count += 1; j -= 20;}
					else break;
				}
				if(count == 5) return true;
			}
		}
		return false;
	}
	
	public void cleanup() {
		isvisible = false;
		ustate = '0'; //1=white, 2=black
		turn = '0';
		errorN = 0;
		over = false;
		
		for (int i=0; i < 361; i++) {
			go_grid_panel.remove(gobuttonL[i]);
	    	gbinsert(go_grid_panel, gobuttonL[i], (i % 19), (i / 19), 1, 1, new Insets(13, 13, 13, 13));
	    	stateArr[i] = '0';
	    }
		
		go_grid_panel.revalidate();
		go_grid_panel.repaint();
	}
	
	public void G2Run() {
		
		
		System.out.println("user : " + this.ustate + " game2 waiting...");
		while(!this.over && !PageManager.over) {
			getpackets();
			if(this.errorN > 10) break;
		}
		
		
		if (this.win) {
			JOptionPane.showMessageDialog(null, "You Win");
		} else {
			JOptionPane.showMessageDialog(null, "You Lose");
		}
		PageManager.over = true;
		cleanup();
	}
	//packet: turn/user/stateArr
	
}
