package cityGui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;




public class BuildingControlPanelHolder extends JPanel{
	final private int PANELX = 180;
	final private int PANELY = 500;
	
	JPanel cards;
	CardLayout layout;
	List<String> ids = new ArrayList<String>();
	
	//private BuildingControlPanel panel = new BuildingControlPanel();
	
	
	public BuildingControlPanelHolder(){
		
		Dimension dim = new Dimension(PANELX, PANELY); //x value can't be over 180
		setMaximumSize(dim);
		setMinimumSize(dim);
		setPreferredSize(dim);
		
		layout = new CardLayout();
		cards = new JPanel(layout);
		add(cards);
		
		BuildingControlPanel p = new BuildingControlPanel(24);
		cards.add(p, "default");
		layout.show(cards, "default");
		
	
	}
	
	public void addPanelCard(BuildingControlPanel p, String id){
		cards.add(p, id);
		layout.show(cards , id);
		ids.add(id);
		
	}
	
	public void showCard(String id){
		layout.show(cards, id);
		
		boolean found = false;
		for (String s : ids){
			if (s == id){
				found = true;
			}
		}
		
		if (!found){
			layout.show(cards,"default");
		}
				
	
	}
	
	
}



