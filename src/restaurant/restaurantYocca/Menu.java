package restaurant.restaurantYocca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu {
	//public static final String[] choices = {"St","C","Sa","P"};
	public List<HashMap<String,Double>> menuList
	= new ArrayList<>();

	HashMap<String, Double> choice;
	
	public String getCheapestItemOnMenu() {
		String cheapestItem = new String();
		double cheapest = 20;
		for (int i=0;i<menuList.size();i++) {
			if (menuList.get(i).containsKey("St")) {
				if (menuList.get(i).get("St") < cheapest) {
					cheapest = menuList.get(i).get("St");
					cheapestItem = "St";
				}
			}
			if (menuList.get(i).containsKey("C")) {
				if (menuList.get(i).get("C") < cheapest) {
					cheapest = menuList.get(i).get("C");
					cheapestItem = "C";
				}
			}
			if (menuList.get(i).containsKey("Sa")) {
				if (menuList.get(i).get("Sa") < cheapest) {
					cheapest = menuList.get(i).get("Sa");
					cheapestItem = "Sa";
				}
			}
			if (menuList.get(i).containsKey("P")) {
				if (menuList.get(i).get("P") < cheapest) {
					cheapest = menuList.get(i).get("P");
					cheapestItem = "P";
				}
			}				
		}
		return cheapestItem;
	}
	
	public double getCheapestPriceOnMenu() {
		double cheapest = 20;
		for (int i=0;i<menuList.size();i++) {
			if (menuList.get(i).containsKey("St")) {
				if (menuList.get(i).get("St") < cheapest) {
					cheapest = menuList.get(i).get("St");
				}
			}
			if (menuList.get(i).containsKey("C")) {
				if (menuList.get(i).get("C") < cheapest) {
					cheapest = menuList.get(i).get("C");
				}
			}
			if (menuList.get(i).containsKey("Sa")) {
				if (menuList.get(i).get("Sa") < cheapest) {
					cheapest = menuList.get(i).get("Sa");
				}
			}
			if (menuList.get(i).containsKey("P")) {
				if (menuList.get(i).get("P") < cheapest) {
					cheapest = menuList.get(i).get("P");
				}
			}				
		}
		return cheapest;
	}
	
	Menu() {	
		choice = new HashMap<String, Double>();
		choice.put("St",15.99);
		menuList.add(choice);
		choice = new HashMap<String, Double>();
		choice.put("C",10.99);
		menuList.add(choice);
		choice = new HashMap<String, Double>();
		choice.put("Sa",5.99);
		menuList.add(choice);
		choice = new HashMap<String, Double>();
		choice.put("P",8.99);
		menuList.add(choice);	
	}
	
}
