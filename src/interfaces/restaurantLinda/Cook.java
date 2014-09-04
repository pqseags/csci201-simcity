package interfaces.restaurantLinda;

import interfaces.BaseRestaurantCook;

import java.util.Map;

import restaurant.restaurantLinda.CookRole.Food;

public interface Cook extends BaseRestaurantCook{

	public abstract void msgHereIsOrder(Waiter w, String choice, int table);

}
