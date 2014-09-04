package restaurant.restaurantGabe.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class inputPanel extends JFrame {
	
	public JButton okB;
	
	public JCheckBox hungryB;
	public JLabel hungryLabel;
	public JPanel hungryPanel;
	
	public JTextField nameField;
	public JLabel nameLabel;
	public JPanel namePanel;
	
	public GridLayout input_layout;
	
	inputPanel(){
		okB = new JButton("Ok");
		
		hungryLabel = new JLabel("Hungry?");
		hungryB = new JCheckBox();
		hungryPanel = new JPanel();
		//hungryPanel.setLayout(new GridLayout(2,1));
		hungryPanel.setLayout(new BorderLayout());
		
		hungryPanel.add(hungryLabel,BorderLayout.NORTH);
		hungryPanel.add(hungryB,BorderLayout.SOUTH);
		
		nameLabel = new JLabel("Name:");
		nameField = new JTextField();
		namePanel = new JPanel();
		namePanel.setLayout(new GridLayout(2,1));
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		input_layout = new GridLayout(3,1,20,0);
		setLayout(input_layout);
		
		add(namePanel);
		add(hungryPanel);
		add(okB);
		
		setVisible(true);
		
		
		
	}

}
