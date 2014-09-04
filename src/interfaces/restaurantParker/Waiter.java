package interfaces.restaurantParker;

import interfaces.BaseRestaurantWaiter;

public interface Waiter extends BaseRestaurantWaiter{

	public abstract void msgHereIsCheck(int check, Customer cust);
	public abstract String getName();
	
}
