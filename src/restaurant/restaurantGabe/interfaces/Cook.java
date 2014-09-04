package restaurant.restaurantGabe.interfaces;

import restaurant.restaurantGabe.WaiterRole;
import restaurant.restaurantGabe.util.Order;
import interfaces.BaseRestaurantCook;

public interface Cook extends BaseRestaurantCook{

	
	public abstract void msgHereIsAnOrder(WaiterRole w,Order o);
	
	
}
