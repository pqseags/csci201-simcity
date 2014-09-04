package restaurant.restaurantParker;

import interfaces.restaurantParker.*;

public class RevolvingStandOrder {

	public WaiterRole waiter;
	public int tableNumber;
	public String choice;
	public CustomerRole customer;
	
	public RevolvingStandOrder(String choice, int tableNumber, CustomerRole customer, WaiterRole waiter){
		this.choice = choice;
		this.tableNumber = tableNumber;
		this.customer = customer;
		this.waiter = waiter;
	}
	
}
