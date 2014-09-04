package restaurant.restaurantGabe.util;

import restaurant.restaurantGabe.CustomerRole;
import restaurant.restaurantGabe.interfaces.Customer;

public class Check {
	public Check(Customer c,int amount){
		this.c = c;
		this.amount = amount;
	}
	public Customer c;
	public int amount;
}
