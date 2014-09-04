package cityGui;

import java.awt.Rectangle;

import restaurant.restaurantLinda.RestaurantLinda;
import restaurant.restaurantLinda.gui.RestaurantLindaControlPanel;
import restaurant.restaurantParker.RestaurantParker;
import restaurant.restaurantParker.gui.RestaurantParkerControlPanel;

public class CityRestaurantParker extends CityRestaurant{

        public CityRestaurantParkerCard animationPanel = null;
        
        public CityRestaurantParker(int x, int y) {
                super(x, y);
                // TODO Auto-generated constructor stub
        }

        public CityRestaurantParker(int x, int y, String ID) {

                super(x, y, ID);
                
                type="Restaurant";
                rectangle = new Rectangle(x, y, buildingSize, buildingSize);
                
                
        }
        
        @Override
        public void initializeRestaurant() {
                restaurant = new RestaurantParker(this);
                
        }

        @Override
        public void createAnimationPanel(SimCityGui city) {
                animationPanel = new CityRestaurantParkerCard(city);
                initializeRestaurant();
                super.animationPanel = animationPanel;
        }

		@Override
		public void createControlPanel() {
			animationPanel.controlPanel = new RestaurantParkerControlPanel(animationPanel, (RestaurantParker) restaurant);
			
		}

}