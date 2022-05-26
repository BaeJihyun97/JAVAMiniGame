package minigame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")
public class Game3 extends ComponentManger {
	boolean isvisible;
	int[][] G3_board;
	G3_Player player;
	G3_Ghost ghost;
	G3_Key key;
	G3_Door door;
	
	String[] bimg_path = {"./image/space.png","./image/wall.png","./image/opened.png","./image/ghostangry.png","./image/key.png","./image/locked.png"};
	String[] obimg_path = {"./image/playerL.png","./image/playerR.png","./image/whiteghost.png","./image/blackghost.png"};
		
	JPanel main;
	JPanel[][] board = new JPanel[15][15];
	JLabel[][] boardlabel = new JLabel[15][15];
	Dimension dim = new Dimension(66,66);
	JLabel[] movlabel = new JLabel[2];
	JLabel[] nmovlabel = new JLabel[3];
	
	Game3(){
		//0: space		1: wall		2: player		3: ghost	4: key		5: door
		 G3_board = new int[][]{
				{0,1,0,0,0,0,1,0,1,0,0,0,0,0,0},
				{0,1,0,1,1,1,1,0,1,0,1,0,1,1,0},
				{0,1,0,0,0,1,0,0,1,0,1,0,0,1,0},
				{0,1,0,1,0,1,0,1,1,0,1,1,0,1,0},
				{0,0,0,1,0,1,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,1,1,1,0},
				{0,0,0,0,0,1,0,0,0,0,0,0,0,1,0},
				{1,1,0,1,1,1,0,1,1,1,1,1,0,1,0},
				{0,0,0,0,0,0,0,1,0,0,0,0,0,1,0},
				{0,1,1,0,1,1,0,1,0,1,1,1,0,1,0},
				{0,1,0,0,0,0,0,1,0,0,0,1,0,1,0},
				{0,1,0,1,1,1,1,1,1,1,1,1,0,1,0},
				{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0},
				{0,1,1,1,0,1,1,1,1,1,1,1,0,1,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
				};
				
		setTitle("Game3");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(990, 990));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		
		main = new JPanel();
		main.setBackground(Color.WHITE);
		main.setLayout(new GridLayout(15,15,5,0));
		main.setPreferredSize(new Dimension(990, 990));
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				board[i][j] = new JPanel();
				board[i][j].setPreferredSize(dim);
				board[i][j].setBackground(Color.WHITE);
				if(G3_board[i][j]==0)	boardlabel[i][j] = new JLabel(imageSetSize(bimg_path[0],66,66));
				else	boardlabel[i][j] = new JLabel(imageSetSize(bimg_path[1],66,66));
				board[i][j].add(boardlabel[i][j]);
				main.add(board[i][j]);	
			}
		}
		
		movlabel[0] = new JLabel(imageSetSize(obimg_path[0],66,66));
		movlabel[1] = new JLabel(imageSetSize(obimg_path[2],66,66));
		nmovlabel[0] = new JLabel(imageSetSize(bimg_path[4],66,66));
		nmovlabel[1] = new JLabel(imageSetSize(bimg_path[5],66,66));
		nmovlabel[2] = new JLabel(imageSetSize(bimg_path[2],66,66));
		
		Dimension frameSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2, 0);
		
		add("Center",main);
		pack();
	}
	
	public void Init() {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		int i=1;
		
		while(true) {
			int rand1 = random.nextInt(15);
			int rand2 = random.nextInt(15);
			if(i!=2 && G3_board[rand1][rand2]==0) {
				if(i==1) {
					player = new G3_Player(rand1,rand2);
					G3_board[rand1][rand2] = 2;
					board[rand1][rand2].removeAll();
					board[rand1][rand2].add(movlabel[0]);
					board[rand1][rand2].revalidate();
					board[rand1][rand2].repaint();
					G3_board[rand1][rand2] = 2;
					i++;
				}
				else if(i==3) {
					key = new G3_Key(rand1,rand2);
					G3_board[rand1][rand2] = 4;
					board[rand1][rand2].removeAll();
					board[rand1][rand2].add(nmovlabel[0]);
					board[rand1][rand2].revalidate();
					board[rand1][rand2].repaint();
					G3_board[rand1][rand2] = 4;
					System.out.println(key.getX()+" "+key.getY());
					i++;
				}
				else if(i==4) {
					door = new G3_Door(rand1,rand2);
					G3_board[rand1][rand2] = 5;
					board[rand1][rand2].removeAll();
					board[rand1][rand2].add(nmovlabel[1]);
					board[rand1][rand2].revalidate();
					board[rand1][rand2].repaint();
					G3_board[rand1][rand2] = 5;
					break;
				}
			}
			if(i==2 && G3_board[rand1][rand2]!=2) {
				ghost = new G3_Ghost(rand1,rand2,G3_board[rand1][rand2]);
				G3_board[rand1][rand2] = 3;
				board[rand1][rand2].removeAll();
				board[rand1][rand2].add(movlabel[1]);
				board[rand1][rand2].revalidate();
				board[rand1][rand2].repaint();
				G3_board[rand1][rand2] = 3;
				i++;
			}
		}
		
		
	}
	
	public void Start() {
		
	}
		
	public boolean getvisibility() {
		return this.isvisible;
	}
	
	public void PlayerMove() {
		
	}
}
