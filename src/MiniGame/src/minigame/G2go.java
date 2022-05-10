package minigame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class G2go extends ComponentManger{
	JPanel grid_panel;
	GridBagLayout gBag;
	boolean isvisible = false;
	
	public G2go() {
		setTitle("G2go");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		
		gBag = new GridBagLayout();
		grid_panel = new JPanel();
		grid_panel.setBackground(Color.WHITE);
	    grid_panel.setLayout(gBag);
	    
	    JLabel waiting = makeLabel(new JLabel("GAME2 TMP PAGE",JLabel.LEFT), new Font(null, Font.BOLD, 100), Color.BLACK, null);
	    gbinsert(grid_panel, waiting, 0, 0, 2, 2);
	    add("Center", grid_panel);
	}
	
	public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
}
