package restaurant.restaurantSimon.interfaces;

import interfaces.BaseRestaurantWaiter;

public interface Waiter extends BaseRestaurantWaiter {

	public abstract void msgCheckIsReady(Customer cus, double amount);
}
