package restaurant.restaurantGabe.interfaces;

import interfaces.BaseRestaurantWaiter;
import restaurant.restaurantGabe.util.Check;

public interface Waiter extends BaseRestaurantWaiter{

	public abstract String getName();
	
	public abstract void msgHereIsCheck(Check c);
}
