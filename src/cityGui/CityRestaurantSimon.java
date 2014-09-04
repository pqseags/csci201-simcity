package cityGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import restaurant.restaurantSimon.RestaurantSimon;
import restaurant.restaurantSimon.gui.RestaurantSimonControlPanel;
import util.Loc;
import util.RestaurantMapLoc;


public class CityRestaurantSimon extends CityRestaurant {

	//public CityRestaurantSimonCard animationPanel;
	
	
	
	public CityRestaurantSimon(int x, int y, String ID){
		super(x, y, ID);
		
		type="RestaurantSimon";
		rectangle = new Rectangle(x, y, buildingSize, buildingSize);
		
	}

	@Override
	public void initializeRestaurant() {
		restaurant = new RestaurantSimon(this);
		((RestaurantSimon)restaurant).animation=(CityRestaurantSimonCard) animationPanel;
		
	}

	@Override
	public void createAnimationPanel(SimCityGui city) {
		animationPanel=new CityRestaurantSimonCard(city);
		initializeRestaurant();
	}
	 public JPanel addAgentObjectToMap(){
	    	RestaurantMapLoc rMap = new RestaurantMapLoc(restaurant);
			rMap.loc = new Loc(sidewalkX(x,y),sidewalkY(x,y));
			this.cityObject.cityMap.map.get("Restaurant").add(rMap);
			return null;
	    }
	    
public void updatePosition() {
		
	}

@Override
public void createControlPanel() {
	animationPanel.controlPanel = new RestaurantSimonControlPanel((CityRestaurantSimonCard)animationPanel, (RestaurantSimon)restaurant);
	
}

}
