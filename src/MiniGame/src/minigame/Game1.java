package minigame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")
public class Game1 extends PageManager{
	final static int TRACK_NUM = 100;
	Dimension dim = new Dimension(300,300);
	JPanel main;
	Runners player = new Runners(TRACK_NUM);
	Runners com = new Runners(TRACK_NUM);
	Computer c;
	Runners[] r = {com,player};
	String[] arrow_img = {"./image/arrowLEFT.png",
			"./image/arrowUP.png",
			"./image/arrowRIGHT.png",
			"./image/arrowDOWN.png"};
	String[] track_img = {"./image/run.png",
			"./image/gofor2.png"};
	JLabel[] labels = new JLabel[4];
	JPanel[] pc = new JPanel[2];
	int[] track = new int[TRACK_NUM];
	JLabel[] tracks = new JLabel[TRACK_NUM];
	JPanel[] board = new JPanel[6];
	int a = 1;
	boolean isvisible;
	boolean penalty = false;
	
	public Game1() {
		setTitle("Game1");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(1920, 1080));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		
		main = new JPanel();
		main.setBackground(Color.WHITE);
		
		add("Center",main);
	}
	
	public void Start() {
		a = 1;
		main.removeAll();
		main.setBackground(Color.WHITE);
		main.setLayout(new GridLayout(3,1,10,0));
		
		for(int i=0; i<2; i++) {
			String s = "";
			pc[i] = new JPanel();
			pc[i].setLayout(new GridBagLayout());			
			//pc[i].setBackground(Color.WHITE);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			if(i==0)	s = "COM       ";
			if(i==1)	s = "YOU       ";
			JLabel temp = new JLabel(s);
			temp.setFont(temp.getFont().deriveFont(40.0f));
			labels[0+2*i] = new JLabel(imageSetSize(track_img[1],1500,270));
			labels[0+2*i].setPreferredSize(new Dimension(1500,270));
			labels[1+2*i] = new JLabel(imageSetSize(track_img[0],190,190));
			labels[1+2*i].setPreferredSize(new Dimension(190,190));
			pc[i].add(temp);
			pc[i].add(labels[1+2*i]);
			pc[i].add(labels[0+2*i]);
			main.add(pc[i]);
		}
		
		JPanel arrows = new JPanel();
		arrows.setLayout(new GridLayout());
		arrows.setBackground(Color.WHITE);
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		for(int i=0; i<TRACK_NUM; i++) {			
			int rand = random.nextInt(4);
			track[i] = rand + 37;
			tracks[i] = new JLabel(imageSetSize(arrow_img[track[i]-37],230,230));			
			tracks[i].setPreferredSize(dim);
		}
		
		for(int j=0; j<6; j++) {
			board[j] = new JPanel();
			board[j].add(tracks[j]);
			arrows.add(board[j]);
		}	
		main.add(arrows);
		main.revalidate();
		add("Center",main);
		this.RunGame();

		
		
	}
	
	public void RunGame() {
		c = new Computer(com);
		player.initialize(TRACK_NUM);
		com.initialize(TRACK_NUM);
		Thread subTread1 = new Thread(c);
		subTread1.start();
		addKeyListener(new key());
	}
	
	class key implements KeyListener{
		
		public void keyTyped(KeyEvent e) {
		}
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {				
			if(e.getKeyCode()==track[player.location]) {
				if(!(penalty)) {
					Player_move();
					slide(board,a);
					a++;					
					if(!(player.running)) {
						JOptionPane.showMessageDialog(null, "Win");
						PageManager.page = 1;
						setvisibility(false);
					}
				}
				else	penalty = false;
				}
			else {
				penalty = true;
			}
		}
			
	}
	
	public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
	
	public boolean getvisibility() {
		return this.isvisible;
	}
	
	public ImageIcon imageSetSize(String path, int i, int j) { // image Size Setting
		ImageIcon icon = new ImageIcon(path);
		Image ximg = icon.getImage(); // ImageIcon을 Image로 변환.
		Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
		ImageIcon xyimg = new ImageIcon(yimg);
		return xyimg;
	}
	
	public void slide(JPanel[] board, int a) {
		int k=6;
		if(a>94)	k=100-a;
		for(int i=0; i<k; i++) {
			board[i].removeAll();
			board[i].add(tracks[a+i]);
			board[i].revalidate();
			board[i].repaint();
		}
	}
	
	public void Player_move() {
		pc[1].setLayout(null);
		Point p = labels[3].getLocation();
		pc[1].remove(1);
		pc[1].add(labels[3]);
		labels[3].setLocation(p.x+15,p.y);
		pc[1].add(labels[2]);
		pc[1].revalidate();
		pc[1].repaint();
		player.Run();
	}
	
	public void Com_move() {
		pc[0].setLayout(null);
		Point p = labels[1].getLocation();
		pc[0].remove(1);
		pc[0].add(labels[1]);
		labels[1].setLocation(p.x+15,p.y);
		pc[0].add(labels[0]);
		pc[0].revalidate();
		pc[0].repaint();
		com.Run();
	}
	
	public class Computer implements Runnable{
		private Thread t;
		private String ThreadName = "com";
		int location;
		Computer(Runners com){
			this.location = com.location;
		}
		
		public void start() {
			if(t==null) {
				t = new Thread(this, ThreadName);
				t.start();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Com_move();
				if(!(com.running))	{
					JOptionPane.showMessageDialog(null, "Lose");
					
					break;
				}
				if(!(player.running))
				{
					
					break;
				}
			}
			PageManager.page = 1;
			setvisibility(false);
		}
		
	}

}
