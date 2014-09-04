package cityGui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class BuildingControlPanel extends JPanel{
	final private int PANELX = 180;
	final private int PANELY = 500;
	
	
public BuildingControlPanel(){
		
		Dimension dim = new Dimension(PANELX, PANELY); //x value can't be over 180
		setMaximumSize(dim);
		setMinimumSize(dim);
		setPreferredSize(dim);
		//setBackground(Color.BLACK);
	}

public BuildingControlPanel(int doot){
	Dimension dim = new Dimension(PANELX, PANELY); //x value can't be over 180
	setMaximumSize(dim);
	setMinimumSize(dim);
	setPreferredSize(dim);
	setBackground(Color.BLACK);
}

}
