package minigame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class StartPage extends ComponentManger{
	JPanel main_panel;
	JButton[] buttonArr = new JButton[3];

	String[] lbArr = { "달리기", "오목", "술래잡기" };
	String[] imgArr = { "./image/run.png", "./image/go.png", "./image/halloween.png" };
	String[] imgArr2 = { "./image/run2.png", "./image/go2.png", "./image/halloween2.png" };
	boolean isvisible;
	public StartPage() {
		setTitle("StartPage");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		main_panel = new JPanel(new BorderLayout());
		main_panel.setBackground(Color.WHITE);
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.X_AXIS));

		JPanel grid_panel = new JPanel();
		grid_panel.setBackground(Color.WHITE);
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		GridBagLayout gbl = new GridBagLayout();
		grid_panel.setLayout(gbl);

		for (int i = 0; i < buttonArr.length; i++) {
			JPanel tmp = new JPanel(new BorderLayout());
			// tmp.setBorder(BorderFactory.createEmptyBorder(50 , 50 , 50 , 50));
			tmp.setLayout(new BorderLayout());
			tmp.setBackground(Color.WHITE);
			tmp.setPreferredSize(new Dimension(400, 500));

			buttonArr[i] = new JButton(imageSetSize(imgArr[i], 200, 200));
			buttonArr[i].setPressedIcon(imageSetSize(imgArr2[i], 200, 200));
			buttonArr[i].setPreferredSize(new Dimension(200, 200));
			buttonArr[i].setContentAreaFilled(false);
			buttonArr[i].setFocusPainted(false);
			buttonArr[i].setBorderPainted(false);
			buttonArr[i].setRolloverEnabled(true);
			buttonArr[i].setToolTipText("Go Game " + (i + 1));

			JLabel lb1 = new JLabel((i + 1) + "인");
			JLabel lb2 = new JLabel(lbArr[i]);
			lb1.setHorizontalAlignment(SwingConstants.CENTER);
			lb2.setHorizontalAlignment(SwingConstants.CENTER);
			lb1.setVerticalAlignment(SwingConstants.BOTTOM);
			lb2.setVerticalAlignment(SwingConstants.TOP);
			lb1.setFont(lb1.getFont().deriveFont(30.0f));
			lb2.setFont(lb2.getFont().deriveFont(30.0f));

			tmp.add("North", lb1);
			tmp.add("Center", buttonArr[i]);
			tmp.add("South", lb2);

			gbc[i] = new GridBagConstraints();
			gbc[i].gridx = i;
			gbc[i].gridy = 1;
			grid_panel.add(tmp, gbc[i]);

		}

		
		buttonArr[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PageManager.page = 7; // 여기가 프레임 전환 역할
				PageManager.gameN = 7;
				setvisibility(false);
			}
		});
		
		buttonArr[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PageManager.page = 2; // 여기가 프레임 전환 역할
				PageManager.gameN = 6;
				setvisibility(false);
			}
		});
		
		buttonArr[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PageManager.page = 5; // 여기가 프레임 전환 역할
				PageManager.gameN = 5;
				setvisibility(false);
			}
		});
		
		main_panel.add("Center", grid_panel);
		add("Center", main_panel);
		setvisibility(false);

	}

	
	public void setvisibility(boolean isvisible) {
    	this.isvisible = isvisible;
    	this.setVisible(isvisible);
    }
	
	public boolean getvisibility() {
		return this.isvisible;
	}
	

}
