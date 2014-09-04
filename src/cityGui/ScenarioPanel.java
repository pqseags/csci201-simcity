package cityGui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScenarioPanel  extends JFrame implements ActionListener{

	SimCityGui city;
	
	JButton run = new JButton("Run");
	
	public ScenarioPanel(SimCityGui c){
		this.city  = c;
		
		setSize(150,100);
		setResizable(false);
		setTitle("Scenario Selection Panel");
		
		
		
		//setLayout(new GridLay());       //new gridbag layout begins
		
		

		
		
		JPanel scenarioNames = new JPanel();
		String[] scenarioLabels = {
				"A. Tests all behaviors",
		};
		JList test = new JList(scenarioLabels);
		JScrollPane scenarioS = new JScrollPane(test);
	
		Dimension d2 = new Dimension(150, 50);
		scenarioNames.setMinimumSize(d2);
		scenarioNames.setPreferredSize(d2);
		scenarioNames.setMaximumSize(d2);
		scenarioS.setMinimumSize(d2);
		scenarioS.setPreferredSize(d2);
		scenarioS.setMaximumSize(d2);
		scenarioNames.add(scenarioS);
		

		this.add(scenarioNames );
		

		

		
		setVisible(true);
		//validate();
		
		
		run.addActionListener(this);
		
		
		
		
		
		
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		if(e.getSource().equals(run)){
			
//			*****Put your scenarios in here***** 
			
			
			
			
		}
		
		
		
	}
	
}
