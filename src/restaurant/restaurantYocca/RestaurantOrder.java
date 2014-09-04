package restaurant.restaurantYocca;

import interfaces.restaurantYocca.Waiter;

public class RestaurantOrder {

	public Waiter w;
	public Table table;
	public String choice;
	
	public RestaurantOrder(Waiter w, Table t, String choice){
		this.w = w;
		this.table = t;
		this.choice = choice;
	}
	
}
