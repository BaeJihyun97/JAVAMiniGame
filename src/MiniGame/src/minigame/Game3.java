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
	boolean finish;
	
	
	String[] bimg_path = {"./image/space.png","./image/wall.png","./image/opened.png","./image/ghostangry.png","./image/key.png","./image/locked.png"};
	String[] obimg_path = {"./image/playerL.png","./image/playerR.png","./image/whiteghost.png","./image/blackghost.png"};
		
	JPanel main = new JPanel();
	JPanel[][] board = new JPanel[15][15];
	JLabel[][] boardlabel = new JLabel[15][15];
	Dimension dim = new Dimension(66,66);
	JLabel[] movlabel = new JLabel[4];
	JLabel[] nmovlabel = new JLabel[3];
	JLabel space;
	JLabel wall;
	Keyboard keyboard = new Keyboard();
	
	Game3(){
		//0: space		1: wall		2: player		3: ghost	4: key		5: door
				this.G3_board = new int[][]{
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
		
		this.main.setBackground(Color.WHITE);
		this.main.setLayout(new GridLayout(15,15,5,0));
		this.main.setPreferredSize(new Dimension(990, 990));
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				this.board[i][j] = new JPanel();
				this.board[i][j].setPreferredSize(dim);
				this.board[i][j].setBackground(Color.WHITE);
				if(this.G3_board[i][j]==0)	this.boardlabel[i][j] = new JLabel(imageSetSize(bimg_path[0],66,66));
				else	this.boardlabel[i][j] = new JLabel(imageSetSize(bimg_path[1],66,66));
				this.board[i][j].add(this.boardlabel[i][j]);
				this.main.add(this.board[i][j]);	
			}
		}
		
		this.movlabel[0] = new JLabel(imageSetSize(obimg_path[0],66,66));
		this.movlabel[1] = new JLabel(imageSetSize(obimg_path[1],66,66));
		this.movlabel[2] = new JLabel(imageSetSize(obimg_path[2],66,66));
		this.movlabel[3] = new JLabel(imageSetSize(obimg_path[3],66,66));
		this.nmovlabel[0] = new JLabel(imageSetSize(bimg_path[4],66,66));
		this.nmovlabel[1] = new JLabel(imageSetSize(bimg_path[5],66,66));
		this.nmovlabel[2] = new JLabel(imageSetSize(bimg_path[2],66,66));
		this.space = new JLabel(imageSetSize(bimg_path[0],66,66));
		this.wall = new JLabel(imageSetSize(bimg_path[1],66,66));
		
		Dimension frameSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2, 0);
		
		add("Center",main);
		pack();
		
		this.finish = false;
	}
	
	public void Init() {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		int i=1;
		
		while(true) {
			int rand1 = random.nextInt(15);
			int rand2 = random.nextInt(15);
			if(this.G3_board[rand1][rand2]==0) {
				if(i==1) {
					this.player = new G3_Player(rand2,rand1);
					//this.G3_board[rand1][rand2] = 2;
					this.board[rand1][rand2].removeAll();
					this.board[rand1][rand2].add(movlabel[0]);
					this.board[rand1][rand2].revalidate();
					this.board[rand1][rand2].repaint();
					this.G3_board[rand1][rand2] = 2;
					System.out.println(player.getX()+" "+player.getY());
					i++;
				}
				else if(i==2) {
					if(Math.abs(rand2-player.getX())+Math.abs(rand1-player.getY())>10) {
						this.ghost = new G3_Ghost(rand2,rand1,G3_board[rand1][rand2]);
						//this.G3_board[rand1][rand2] = 3;
						this.board[rand1][rand2].removeAll();
						this.board[rand1][rand2].add("W",movlabel[2]);
						this.board[rand1][rand2].revalidate();
						this.board[rand1][rand2].repaint();
						this.G3_board[rand1][rand2] = 3;
						System.out.println(ghost.getX()+" "+ghost.getY());
						i++;
					}
				}
				else if(i==3) {
					this.key = new G3_Key(rand2,rand1);
					//this.G3_board[rand1][rand2] = 4;
					this.board[rand1][rand2].removeAll();
					this.board[rand1][rand2].add(nmovlabel[0]);
					this.board[rand1][rand2].revalidate();
					this.board[rand1][rand2].repaint();
					this.G3_board[rand1][rand2] = 4;
					System.out.println(key.getX()+" "+key.getY());
					i++;
				}
				else if(i==4) {
					if(Math.abs(rand2-key.getX())+Math.abs(rand1-key.getY())>10) {
						this.door = new G3_Door(rand2,rand1);
						this.G3_board[rand1][rand2] = 5;
						this.board[rand1][rand2].removeAll();
						this.board[rand1][rand2].add(nmovlabel[1]);
						this.board[rand1][rand2].revalidate();
						this.board[rand1][rand2].repaint();
						this.G3_board[rand1][rand2] = 5;
						System.out.println(door.getX()+" "+door.getY());
						break;
					}
				}
			}
		}
		this.Start();
		
	}
	
	public void Start() {
		finish = false;
		GhostChase g = new GhostChase();
		g.start();
		addKeyListener(keyboard);	
	}
	
	class Keyboard implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {	
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keycode = e.getKeyCode();
			int x = player.getX(), y = player.getY();
			boolean able = false;
			if((keycode == 37 || keycode == 65) && x!=0 && G3_board[y][x-1]!=1) {
				able = true;
				System.out.println("able turns into true");
				
			}
			if((keycode == 38 || keycode == 87) && y!=0 && G3_board[y-1][x]!=1) {
				able = true;
				System.out.println("able turns into true");
				
			}
			if((keycode == 39 || keycode == 68) && x!=14 && G3_board[y][x+1]!=1){
				able = true;
				System.out.println("able turns into true");
				
			}
			if((keycode == 40 || keycode == 83) && y!=14 && G3_board[y+1][x]!=1){	
				able = true;
				System.out.println("able turns into true");
			
			}
			
			if(able) {
				PlayerMove(keycode,x,y);
				if(player.getX()==key.getX() && player.getY()==key.getY())	{
					player.gotKey();
					ghost.angry = 1;
					board[door.getY()][door.getX()].removeAll();
					board[door.getY()][door.getX()].add(nmovlabel[2]);
					board[door.getY()][door.getX()].revalidate();
					board[door.getY()][door.getX()].repaint();
					board[key.getY()][key.getX()].revalidate();
					board[key.getY()][key.getX()].repaint();
					G3_board[door.getY()][door.getX()] = 6;
				}
				if(player.getX()==door.getX() && player.getY()==door.getY() && player.hasKey()) {
					finish = true;
					removeKeyListener(keyboard);
					ghost.angry = 0;
					JOptionPane.showMessageDialog(null, "Win: escape success");
					G3_board[door.getY()][door.getX()] = 0;
					Reset();
					PageManager.page = 1;
					setvisibility(false);
				}
				System.out.println("player: "+player.getX()+" "+player.getY());
			}
			
		}	
		
	}
	
	public class GhostChase implements Runnable{
		private Thread t;
		private String ThreadName = "ghost";
		long restTime;
		
		GhostChase(){
			this.restTime = 500;
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
				if(ghost.angry == 1)	gotAngry(this);
				try {
					Thread.sleep(restTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				GhostMove(this);
				System.out.println("ghost: "+ghost.getX()+" "+ghost.getY());
				if(finish)	break;
			}
		}
		
	}
	
	public void PlayerMove(int keycode, int x, int y) {
		if(keycode == 37 || keycode == 65) {
			board[y][x].removeAll();
			if(G3_board[y][x] == 0)		board[y][x].add(space);
			if(G3_board[y][x] == 5)		board[y][x].add(nmovlabel[1]);
			board[y][x].revalidate();
			board[y][x].repaint();
			board[y][x-1].removeAll();
			board[y][x-1].add(movlabel[0]);
			board[y][x-1].revalidate();
			board[y][x-1].repaint();
			player.setX(x-1);
			player.left = true;
		}
		
		if(keycode == 38 || keycode == 87) {
			board[y][x].removeAll();
			if(G3_board[y][x] == 0)		board[y][x].add(space);
			if(G3_board[y][x] == 5)		board[y][x].add(nmovlabel[1]);
			board[y][x].revalidate();
			board[y][x].repaint();
			board[y-1][x].removeAll();
			if(player.left)		board[y-1][x].add(movlabel[0]);
			else	board[y-1][x].add(movlabel[1]);
			board[y-1][x].revalidate();
			board[y-1][x].repaint();
			player.setY(y-1);
			
		}
		
		if(keycode == 39 || keycode == 68) {
			board[y][x].removeAll();
			if(G3_board[y][x] == 0)		board[y][x].add(space);
			if(G3_board[y][x] == 5)		board[y][x].add(nmovlabel[1]);
			board[y][x].revalidate();
			board[y][x].repaint();
			board[y][x+1].removeAll();
			board[y][x+1].add(movlabel[1]);
			board[y][x+1].revalidate();
			board[y][x+1].repaint();
			player.setX(x+1);
			player.left = false;
		}
		
		if(keycode == 40 || keycode == 83) {
			board[y][x].removeAll();
			if(G3_board[y][x] == 0)		board[y][x].add(space);
			if(G3_board[y][x] == 5)		board[y][x].add(nmovlabel[1]);
			board[y][x].revalidate();
			board[y][x].repaint();
			board[y+1][x].removeAll();
			if(player.left)		board[y+1][x].add(movlabel[0]);
			else	board[y+1][x].add(movlabel[1]);
			board[y+1][x].revalidate();
			board[y+1][x].repaint();
			player.setY(y+1);
		}
	}
	
	public void GhostMove(GhostChase g) {
		int lr=0,ud=0;
		int x = ghost.getX(), y = ghost.getY();
		if(ghost.angry == 2) {
			ghost.vanished = false;
			ghost.angry = 3;
		}
		if(player.getX()-ghost.getX()<0)	lr = -1;
		else if(player.getX()-ghost.getX()>0)	lr = +1;
		if(player.getY()-ghost.getY()<0)	ud = -1;
		else if(player.getY()-ghost.getY()>0)	ud = +1;
		if(!ghost.vanished) {
			board[y][x].removeAll();
			if(G3_board[y][x] == 0)		board[y][x].add(space);
			if(G3_board[y][x] == 1)		board[y][x].add(wall);
			if(x==key.getX() && y==key.getY() && !player.hasKey())		board[y][x].add(nmovlabel[0]);
			if(G3_board[y][x] == 5)		board[y][x].add(nmovlabel[1]);
			if(G3_board[y][x] == 6)		board[y][x].add(nmovlabel[2]);
			board[y][x].revalidate();
			board[y][x].repaint();			
			ghost.vanished = true;
			if(ghost.angry==0)	g.restTime = 200;
			else	g.restTime = 150;
		}
		else if(ghost.vanished) {
			x += lr;	y += ud;
			ghost.setX(x);	ghost.setY(y);
			board[y][x].removeAll();
			if(ghost.angry >= 2) board[y][x].add(movlabel[3]);
			else	board[y][x].add(movlabel[2]);
			board[y][x].revalidate();
			board[y][x].repaint();
			ghost.vanished = false;
			if(ghost.angry==0)	g.restTime = 600;
			else	g.restTime = 150;
			
			if(ghost.getX()==player.getX() && ghost.getY()==player.getY()) {
				finish = true;
				removeKeyListener(keyboard);
				if(ghost.angry >= 2) {			
					JLabel hiddenghost = new JLabel(imageSetSize("./image/hiddenghost.jpg",990,990));
					main.removeAll();
					main.setLayout(new GridLayout(1,0,0,0));
					main.add(hiddenghost);
					main.revalidate();
					main.repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(ghost.angry == 0) {			
					JLabel realghost = new JLabel(imageSetSize("./image/realghost.jfif",990,990));
					main.removeAll();
					main.setLayout(new GridLayout(1,0,0,0));
					main.add(realghost);
					main.revalidate();
					main.repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, "Lose: escape failed");
				G3_board[door.getY()][door.getX()] = 0;
				ghost.angry = 0;
				Reset();				
				PageManager.page = 1;
				setvisibility(false);
			}
		}
		
	}
	
	public void gotAngry(GhostChase g) {
		int x = ghost.getX(), y = ghost.getY();
		board[y][x].removeAll();
		JLabel angryghost = new JLabel(imageSetSize(bimg_path[3],66,66));
		board[y][x].add(angryghost);
		board[y][x].revalidate();
		board[y][x].repaint();
		g.restTime = 200;
		ghost.angry = 2;
	}
	
	public void Reset() {
		this.main.removeAll();
		this.main.setLayout(new GridLayout(15,15,5,0));
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++) {
				this.board[i][j].removeAll();
				this.board[i][j].add(this.boardlabel[i][j]);
				this.board[i][j].revalidate();
				this.board[i][j].repaint();
				this.main.add(this.board[i][j]);	
			}
		}
		this.main.revalidate();
		this.main.repaint();
	}
	
	public boolean getvisibility() {
		return this.isvisible;
	}
	
	public void setvisibility(boolean isvisible) {
		setVisible(isvisible);
	}
}
