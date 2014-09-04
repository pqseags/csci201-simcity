package cityGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import agent.Agent;
import restaurant.restaurantGabe.CashierRole;
import restaurant.restaurantGabe.CookRole;
import restaurant.restaurantGabe.CustomerRole;
import restaurant.restaurantGabe.HostRole;
import restaurant.restaurantGabe.RestaurantGabe;
import restaurant.restaurantGabe.gui.CookGui;
import restaurant.restaurantGabe.gui.CustomerGui;
import restaurant.restaurantGabe.gui.RestaurantGabeControlPanel;
//import restaurant.restaurantGabe.gui.ListPanel;
//import restaurant.restaurantGabe.gui.MarketRole;
//import restaurant.restaurantGabe.gui.RestaurantGui;
//import restaurant.restaurantGabe.gui.WaiterPanel;
import restaurant.restaurantGabe.util.RevolvingStand;
import util.Loc;
import util.RestaurantMapLoc;

public class CityRestaurantGabe extends CityRestaurant{


	//public CityRestaurantCardGabe animationPanel;
	public RevolvingStand stand  = new RevolvingStand();
	
	public int numWaitingCusts;
    
    public int numWaitingSpots = 10;
    public boolean[] waitingSpots = new boolean[numWaitingSpots];
    
    public int[] x_table = {100,175,250};
    public int[] y_table = {275,235,195};
	
    
    public CityRestaurantGabe(int x, int y, String ID) {
		super(x, y, ID);
		
		type="RestaurantGabe";
		rectangle = new Rectangle(x, y, buildingSize, buildingSize);
		//System.out.println("Second");

	}
    
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    @Override
    public void initializeRestaurant() {
    	
    	//super(x, y);
    	restaurant = new RestaurantGabe(this);
    	
    	numWaitingCusts = 0;
    	
    	//numWaitingSpots = 10;
    	for(int i = 0;i<numWaitingSpots;i++){
    		waitingSpots[i] = true;
    	}
    	
 
    	
        int WINDOWX = 850;
        int WINDOWY = 850;
        
        //x_table  = {200,300,400};
        //y_table = {250,250,250};
        
        ((CityRestaurantCardGabe)animationPanel).x_table = x_table;
        ((CityRestaurantCardGabe)animationPanel).y_table = y_table;
        

    }

    
    public JPanel addAgentObjectToMap(){
    	RestaurantMapLoc rMap = new RestaurantMapLoc(restaurant);
		rMap.loc = new Loc(sidewalkX(x,y),sidewalkY(x,y));
		this.cityObject.cityMap.map.get("Restaurant").add(rMap);
		return null;
    }
    
    public void actionPerformed(ActionEvent e){
    	/*if(e.getSource() == pauseButton){
    		pauseSystem();
    		
    	}*/
    }
    
    
    
    
    

//    public static void main(String[] args) {
//        RestaurantGui gui = new RestaurantGui();
//        gui.setTitle("csci201 Restaurant");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//    
    
    
    
    
    
	
	

	@Override
	public void createAnimationPanel(SimCityGui city) {
		// TODO Auto-generated method stub
		//
		//System.out.println("\tIN HERE!!!!!!");
		animationPanel = new CityRestaurantCardGabe(city);
		
		initializeRestaurant();
		
	}
	
	
    public void setCustomerHungry(CustomerRole c){
    	//System.out.println("Setting GUI to hungry");
    	c.getGui().setHungry();
    	
    }
    
    public void createControlPanel(){
    	animationPanel.controlPanel = new RestaurantGabeControlPanel((CityRestaurantCardGabe)animationPanel, (RestaurantGabe) restaurant);
    }
	
	

}
