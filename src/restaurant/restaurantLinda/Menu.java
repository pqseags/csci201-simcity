package restaurant.restaurantLinda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
	private Map<String, Integer> foodList = new HashMap<String,Integer>();
	
	public Menu(){
		foodList.put("Steak", 16);
		foodList.put("Chicken", 11);
		foodList.put("Salad", 6);
		foodList.put("Pizza", 9);
	}
	
	public Menu(List<String> exclusionList){
		if (!exclusionList.contains("Steak"))
			foodList.put("Steak", 16);
		if (!exclusionList.contains("Chicken"))
			foodList.put("Chicken", 11);
		if (!exclusionList.contains("Salad"))
			foodList.put("Salad", 6);
		if (!exclusionList.contains("Pizza"))
			foodList.put("Pizza", 9);
	}
	
	public Map<String,Integer> getMenu(){
		return foodList;
	}
	
	public String toString(){
		return foodList.toString();
	}
}
