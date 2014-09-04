package restaurant.restaurantGabe.util;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	
	//public List<String> choices = new ArrayList<String>();
	//public List<Integer> prices = new ArrayList<Integer>();
	
	public static List<food> foods = new ArrayList<food>();
	
	public class food{
		public food(String choice,int price){
			this.choice = choice;
			this.price = price;
		}
		public String choice;
		public int price;
	}
	
	public Menu(){
		
		foods.add(new Menu.food("Pizza",12));
		foods.add(new Menu.food("Steak",15));
		foods.add(new Menu.food("Chicken",10));
		foods.add(new Menu.food("Salad",5));
		
		//choices = new ArrayList<String>();
		
		/*choices.add("Pizza");
		choices.add("Steak");
		choices.add("Salad");
		choices.add("Chicken");*/
	}

}
