package restaurant.restaurantSimon.gui;
import restaurant.restaurantSimon.CustomerRole;
import restaurant.restaurantSimon.HostRole;
import restaurant.restaurantSimon.WaiterRole;

import java.awt.*;


public class HostGui implements Gui {
	
	 private HostRole agent = null;

	    private  int xPos = -20;//default waiter position
		private  int yPos = -20;
	    private int xDestination = -20, yDestination = -20;//default start position


	    public HostGui(HostRole agent) {
	        this.agent = agent;
	    }


		@Override
		public void updatePosition() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void draw(Graphics2D g) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean isPresent() {
			// TODO Auto-generated method stub
			return false;
		}

}
