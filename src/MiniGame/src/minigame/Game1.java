package minigame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

//플레이어의 아이콘이 움직이지 않는 문제 있음. CardLayout의 화면을 넘길 때마다 전체 화면이 초기화되는 듯함.
@SuppressWarnings("serial")
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
	JPanel[] board = new JPanel[6];
	int a = 1;
	
	boolean isvisible;
	public Game1() {
		setTitle("Game1");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(1920, 1080));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		arrows.setLayout(new GridLayout());
		arrows.setBackground(Color.WHITE);
		Random random = new Random();
		//random.setSeed(System.currentTimeMillis());
		for(int i=0; i<TRACK_NUM; i++) {			
			int rand = random.nextInt(4);
			track[i] = rand + 37;
		}
		
		CardLayout[] cards = new CardLayout[6];
		for(int i=0; i<6; i++) {
			CardLayout card = new CardLayout(0,0);
			cards[i] = card;
		}
		
		for(int j=0; j<6; j++) {
			JPanel d = new JPanel();
			d.setLayout(cards[j]);
			d.setBackground(Color.WHITE);
			for(int i=0; i<TRACK_NUM; i++) {					
				tracks[i] = new JLabel(imageSetSize(arrow_img[track[i]-37],230,230));			
				tracks[i].setPreferredSize(dim);
				d.add(Integer.toString(i),tracks[i]);
			}
			board[j] = d;
			cards[j].show(board[j],Integer.toString(j));
			arrows.add(board[j]);
		}	
		
			class key implements KeyListener{
	
				public void keyTyped(KeyEvent e) {
				}
				public void keyPressed(KeyEvent e) {
				}
	
				public void keyReleased(KeyEvent e) {				
					if(e.getKeyCode()==track[player.location]) {
						Point p = labels[3].getLocation();
						labels[3].setLocation(p.x+15,p.y);
						for(int i=0; i<6; i++)
							cards[i].show(board[i],Integer.toString(a+i));
						a++;
						player.Run();
						if(!(player.running))
							JOptionPane.showMessageDialog(null, "Win");
						if(!(com.running))
							JOptionPane.showMessageDialog(null, "Lose");
					}
					else {
						try{Thread.sleep(300);
						}catch(InterruptedException E) {
							E.printStackTrace();
						}
					}
				}
					
			}
					
			main.add(arrows);
			add("Center",main);
			addKeyListener(new key());

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
