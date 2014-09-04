package restaurant.restaurantParker;

import restaurant.restaurantParker.CustomerRole;
import restaurant.restaurantParker.WaiterRole;
import interfaces.restaurantParker.*;


public class Order {

	
	WaiterRole waiter = null;
	CustomerRole customer = null;
	
	String choice;
	int tableNumber;
	int grill;
	int plate;
	
	enum OrderState {Ordered, Cooking, Cooked, Finished};
	OrderState state = OrderState.Ordered;
	
	
	Order(String c, int tn, CustomerRole cust, WaiterRole wait){
		choice = c;
		tableNumber = tn;
		customer= cust;
		waiter = wait;
		
		grill = -1;
		plate = -1;
	}
	
	public void setWaiter(WaiterRole w){
		this.waiter = w;
	}
	
	
}


