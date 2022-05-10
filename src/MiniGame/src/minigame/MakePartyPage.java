package minigame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class MakePartyPage extends ComponentManger{
	TextField idText, ipText, portCText, portSText;
	JButton participate, makeRoom;
	JPanel grid_panel;
	GridBagLayout gBag;
	boolean isvisible;
	
    public MakePartyPage() {
    	init();
    	addListener();
    }
    
    private void init() {
    	setTitle("MakePartyPage");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
    	
        //삽입할 컴포넌트 생성.
		//Label
        JLabel id = makeLabel(new JLabel("ID",JLabel.LEFT), new Font(null, Font.BOLD, 30), Color.BLACK, new Dimension(200, 30));
        JLabel ip = makeLabel(new JLabel("IP",JLabel.LEFT), new Font(null, Font.BOLD, 30), Color.BLACK, null);
        JLabel portC = makeLabel(new JLabel("PORT",JLabel.LEFT), new Font(null, Font.BOLD, 30), Color.BLACK, null);
        JLabel portS = makeLabel(new JLabel("PORT",JLabel.LEFT), new Font(null, Font.BOLD, 30), Color.BLACK, null);
        //Button
        participate = makeJButton(new JButton("participate"), new Font(null, Font.BOLD, 30), null, Color.WHITE, new Dimension(200, 30));
        makeRoom = makeJButton(new JButton("make room"), new Font(null, Font.BOLD, 30), null, Color.WHITE, new Dimension(200, 30));
        //TextField
        idText = makeTextField(new TextField(40), new Font(null, Font.BOLD, 30), null, null);
        ipText = makeTextField(new TextField(40), new Font(null, Font.BOLD, 30), null, null);
        portCText = makeTextField(new TextField(30), new Font(null, Font.BOLD, 30), null, null);
        portSText = makeTextField(new TextField(30), new Font(null, Font.BOLD, 30), null, null);
        //Panel
        JPanel pnl = makeJPanel(new JPanel(), Color.WHITE, new Dimension(50, 20));
        pnl.setLayout(null);
        //Grid
        gBag = new GridBagLayout(); //GridBagLayout 생성
        grid_panel = new JPanel();
        grid_panel.setBackground(Color.WHITE);
        grid_panel.setLayout(gBag);
        
        gbinsert(grid_panel, id, 0, 0, 1, 1); 
        gbinsert(grid_panel, participate, 0, 2, 1, 1);
        gbinsert(grid_panel, makeRoom, 0, 4, 1, 1);
		gbinsert(grid_panel, pnl, 1, 0, 1, 1); 
		gbinsert(grid_panel, pnl, 1, 2, 1, 1); 
		gbinsert(grid_panel, pnl, 1, 4, 1, 1);      
        gbinsert(grid_panel, ip, 2, 2, 1, 1);    
        gbinsert(grid_panel, portC, 2, 3, 1, 1);
        gbinsert(grid_panel, portS, 2, 4, 1, 1);       
        gbinsert(grid_panel, idText, 3, 0, 3, 1);
        gbinsert(grid_panel, ipText, 3, 2, 3, 1);
        gbinsert(grid_panel, portCText, 3, 3, 3, 1);
        gbinsert(grid_panel, portSText, 3, 4, 3, 1);
        
        //add panel to frame
        add("Center", grid_panel);
        setvisibility(false);
    }
    
    private void addListener() {
    	
    	ActionListener listener = new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			if(participate.equals(e.getSource())) {	
    				
    				try {
    					PageManager.page = 3;
        				PageManager.portC = Integer.parseInt(portCText.getText());
        				PageManager.ip = ipText.getText();
        				PageManager.id = idText.getText();

            			System.out.println(PageManager.id + PageManager.ip + PageManager.portC);
            			setvisibility(false);
    				}
    				catch (Exception ex){
    					//ex.printStackTrace();
    					System.out.println("invalid value");
    					PageManager.page = 2;
    					PageManager.portC = 0;
        				PageManager.ip = "";
        				PageManager.id = "";
    				}
    				
    			}
    			
    			if(makeRoom.equals(e.getSource())) {
    				try {
    					PageManager.page = 3;
        				PageManager.portS = Integer.parseInt(portSText.getText());
        				PageManager.id = idText.getText();

            			System.out.println(PageManager.id + PageManager.portS);
            			setvisibility(false);
    				}
    				catch (Exception ex) {
    					//ex.printStackTrace();
    					System.out.println("invalid value");
    					PageManager.page = 2;
    					PageManager.portS = 0;
        				PageManager.id = "";
    				}
    				
    			}
        	}
    	};
    	
    	participate.addActionListener(listener);
    	makeRoom.addActionListener(listener);
    }
    
    public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
}