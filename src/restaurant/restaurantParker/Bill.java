package restaurant.restaurantParker;

import interfaces.restaurantParker.*;

public class Bill {
	Customer customer;
	Waiter waiter;
	String choice;
	int check;

	
	Bill(String choice, Customer customer, Waiter waiter){
		this.choice = choice;
		this.customer = customer;
		this.waiter = waiter;
		
		check = 0;
	}
}
