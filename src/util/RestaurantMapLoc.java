package util;

import cityGui.CityRestaurantSimon;
import restaurant.Restaurant;
import role.Role;
import interfaces.Person;
import interfaces.PlaceOfWork;

public class RestaurantMapLoc extends Place/* implements PlaceOfWork*/{

	public Restaurant restaurant;
	public int restaurantTopLeftX;
	public int restaurantTopLeftY;

	public RestaurantMapLoc(Restaurant r) {
		this.restaurant = r;
	}

//	@Override
//	public Role canIStartWorking(Person p, JobType type, Role r) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
