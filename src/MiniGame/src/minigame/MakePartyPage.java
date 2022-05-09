package minigame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class MakePartyPage extends PageManager{
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
        
        gbinsert(id, 0, 0, 1, 1); 
        gbinsert(participate, 0, 2, 1, 1);
        gbinsert(makeRoom, 0, 4, 1, 1);
		gbinsert(pnl, 1, 0, 1, 1); 
		gbinsert(pnl, 1, 2, 1, 1); 
		gbinsert(pnl, 1, 4, 1, 1);      
        gbinsert(ip, 2, 2, 1, 1);    
        gbinsert(portC, 2, 3, 1, 1);
        gbinsert(portS, 2, 4, 1, 1);       
        gbinsert(idText, 3, 0, 3, 1);
        gbinsert(ipText, 3, 2, 3, 1);
        gbinsert(portCText, 3, 3, 3, 1);
        gbinsert(portSText, 3, 4, 3, 1);
        
        //add panel to frame
        add("Center", grid_panel);
        setvisibility(false);
    }
    
    private void addListener() {
    	
    	ActionListener listener = new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			if(participate.equals(e.getSource())) {
    				String data1 = idText.getText();
    				String data2 = ipText.getText();
    				String data3 = portCText.getText();
        			System.out.println(data1 + data2 + data3);
    			}
    			
    			if(makeRoom.equals(e.getSource())) {
    				String data1 = idText.getText();
    				String data3 = portSText.getText();
        			System.out.println(data1 + data3);
    			}
        	}
    	};
    	
    	participate.addActionListener(listener);
    	makeRoom.addActionListener(listener);
    }
    
    private JLabel makeLabel(JLabel c, Font font, Color color, Dimension size) {
    	if(font != null) c.setFont(font);
    	if(color != null) c.setForeground(color);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
    
    private TextField makeTextField(TextField c, Font font, Color color, Dimension size) {
    	if(font != null) c.setFont(font);
    	if(color != null) c.setForeground(color);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
    
    private JButton makeJButton(JButton c, Font font, Color color, Color bcolor, Dimension size) {
    	if(font != null) c.setFont(font);
    	if(color != null) c.setForeground(color);
    	if(bcolor != null) c.setBackground(bcolor);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
    
    private JPanel makeJPanel(JPanel c, Color bcolor, Dimension size) {
    	if(bcolor != null) c.setBackground(bcolor);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
   
    private void gbinsert(Component c, int x, int y, int w, int h){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill= GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.insets = new Insets(10,10,10,10);
        this.grid_panel.add(c, gbc);
       
       
    }
    
    public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
}