package interfaces.restaurantParker;

import interfaces.BaseRestaurantCustomer;

public interface Customer extends BaseRestaurantCustomer{

	public abstract void msgGiveChange(int change);

	public abstract String getName();
	
}
