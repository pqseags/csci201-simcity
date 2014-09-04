package restaurant.restaurantSimon.interfaces;

import interfaces.BaseRestaurantCashier;

public interface Cashier  extends BaseRestaurantCashier{
	public abstract void msgHereIsMoney(Customer cus, double amount);
}
