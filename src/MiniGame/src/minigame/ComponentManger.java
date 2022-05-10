package minigame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.TextField;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ComponentManger extends PageManager{
	
	public ComponentManger() {
		
	}
	
	public JLabel makeLabel(JLabel c, Font font, Color color, Dimension size) {
    	if(font != null) c.setFont(font);
    	if(color != null) c.setForeground(color);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
    
	public TextField makeTextField(TextField c, Font font, Color color, Dimension size) {
    	if(font != null) c.setFont(font);
    	if(color != null) c.setForeground(color);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
    
	public JButton makeJButton(JButton c, Font font, Color color, Color bcolor, Dimension size) {
    	if(font != null) c.setFont(font);
    	if(color != null) c.setForeground(color);
    	if(bcolor != null) c.setBackground(bcolor);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
    
	public JPanel makeJPanel(JPanel c, Color bcolor, Dimension size) {
    	if(bcolor != null) c.setBackground(bcolor);
    	if(size != null) c.setPreferredSize(size);
    	return c;
    }
   
	public void gbinsert(JPanel grid_panel, Component c, int x, int y, int w, int h){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill= GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.insets = new Insets(10,10,10,10);
        grid_panel.add(c, gbc);
    }
    
	public ImageIcon imageSetSize(String path, int i, int j) { // image Size Setting
		ImageIcon icon = new ImageIcon(path);
		Image ximg = icon.getImage(); // ImageIcon을 Image로 변환.
		Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
		ImageIcon xyimg = new ImageIcon(yimg);
		return xyimg;
	}
	
    public void setvisibility(boolean isvisible) {}
}
