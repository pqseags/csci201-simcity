package cityGui;

import java.awt.event.ActionEvent;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurantLinda.gui.CookGui;
import restaurant.restaurantLinda.gui.MyImage;
import public_Gui.Gui;


public class CityRestaurantCard extends CityCard{

	public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	BuildingControlPanel controlPanel;
	
	//For Linda
	public List<MyImage> platedFoods = Collections.synchronizedList(new ArrayList<MyImage>());
	
	public CityRestaurantCard(SimCityGui city) {
		super(city);
	}

    public void addGui(Gui gui) {
    	//System.out.println("Adding cook gui");
        guis.add(gui);
    }
    
	
	public void removeGui(Gui gui) {
		guis.remove(gui);		
	}
	
	//Override this if you don't use this super gui list
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("repaint being called!");
		//moveComponents();
		
		if(!guis.isEmpty()){
			synchronized(guis){
				for(Gui gui : guis) {
	    			if (gui.isPresent()) {
	    				gui.updatePosition();//the number here doesn't actually matter.
	    			}
	    		}
			}
		}
		
		repaint();
	}

}
