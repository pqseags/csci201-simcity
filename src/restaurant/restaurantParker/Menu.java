package restaurant.restaurantParker;

import java.util.*;


public class Menu {

	Map <String, Integer> menu = new TreeMap<String, Integer>();{
		
		menu.put("Steak", 40);
		menu.put( "Chicken", 30);
		
		menu.put("Pizza", 20);
		menu.put( "Salad" , 10);
		
	}
	
	List<String> keys ;
	
	Menu(){
		keys = new ArrayList<String>(menu.keySet());
	}
	
	Menu(List<String> shortages){
		for (String food : shortages){
			menu.remove(food);
		}
		
		keys = new ArrayList<String>(menu.keySet());
	}
	
	
}
