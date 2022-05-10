package minigame;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Wait extends ComponentManger{
	JPanel grid_panel;
	GridBagLayout gBag;
	boolean isvisible = false;
	
	public Wait() {
		setTitle("Wait");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		
		gBag = new GridBagLayout();
		grid_panel = new JPanel();
		grid_panel.setBackground(Color.WHITE);
	    grid_panel.setLayout(gBag);
	    
	    JLabel waiting = makeLabel(new JLabel("Waiting",JLabel.LEFT), new Font(null, Font.BOLD, 100), Color.BLACK, null);
	    gbinsert(grid_panel, waiting, 0, 0, 2, 2);
	    add("Center", grid_panel);
	}
	
	public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
	
}
