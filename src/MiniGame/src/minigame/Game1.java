package minigame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

//아직 미완. 테스트용 main 함수도 들어 있음.
public class Game1 extends PageManager{
	final static int TRACK_NUM = 100;
	Dimension dim = new Dimension(300,300);
	JPanel main;
	Runners player = new Runners(TRACK_NUM);
	Runners com = new Runners(TRACK_NUM);
	Runners[] r = {com,player};
	String[] arrow_img = {"src\\minigame\\image\\arrowLEFT.png",
			"src\\minigame\\image\\arrowUP.png",
			"src\\minigame\\image\\arrowRIGHT.png",
			"src\\minigame\\image\\arrowDOWN.png"};
	String[] track_img = {"src\\minigame\\image\\run.png",
			"src\\minigame\\image\\gofor.png"};
	JLabel[] labels = new JLabel[4];
	JPanel[] pc = new JPanel[2];
	int[] track = new int[TRACK_NUM];
	JLabel[] tracks = new JLabel[TRACK_NUM];
	
	boolean isvisible;
	public Game1() {
		setTitle("Game1");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(1920, 1080));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setvisibility(true);
		setFocusable(true);
	
		main = new JPanel();
		main.setBackground(Color.BLACK);
		main.setLayout(new GridLayout(3,1,10,1));
		
		for(int i=0; i<2; i++) {
			String s = "";
			pc[i] = new JPanel();
			pc[i].setLayout(new GridBagLayout());			
			pc[i].setBackground(Color.WHITE);
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
		arrows.setLayout(new GridBagLayout());
		arrows.setBackground(Color.WHITE);
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		for(int i=0; i<TRACK_NUM; i++) {			
			int rand = random.nextInt(4);
			track[i] = rand + 37;			
			if(i<6) {
				tracks[i] = new JLabel(imageSetSize(arrow_img[rand],230,230));			
				tracks[i].setPreferredSize(dim);
				arrows.add(tracks[i],0);
			}
			else if(i<12) {
				tracks[i] = new JLabel(imageSetSize(arrow_img[rand],230,230));			
				tracks[i].setPreferredSize(dim);
				arrows.add(tracks[i],1);
			}
		}	
		
		
			class key implements KeyListener{
	
				public void keyTyped(KeyEvent e) {
				}
				public void keyPressed(KeyEvent e) {					
				}
	
				public void keyReleased(KeyEvent e) {
					Point p = labels[3].getLocation();
					Point p2 = arrows.getLocation();
					if(e.getKeyCode()==track[player.location]) {
						labels[3].setLocation(p.x+15,p.y);
						arrows.setLocation(p2.x-300,p2.y);
						player.Run();
					}
				}
					
			}
			addKeyListener(new key());
		
			main.add(arrows);
			add("Center",main);
		

	}
	
	public static void main(String args[]) {
		Game1 run = new Game1();
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
	

}
