package restaurant.restaurantLinda;

import interfaces.restaurantLinda.Waiter;

public class RestaurantOrder {

	public Waiter w;
	public int table;
	public String choice;
	
	public RestaurantOrder(Waiter w, int table, String choice){
		this.w = w;
		this.table = table;
		this.choice = choice;
	}
	
}
