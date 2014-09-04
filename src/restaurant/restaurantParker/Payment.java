package restaurant.restaurantParker;

import interfaces.restaurantParker.*;

public class Payment {
	
	int bill;
	int cash;
	Customer customer;
	
	Payment(int bill, int cash, Customer cust){
		this.bill = bill;
		this.cash = cash;
		this.customer = cust;
	}
}
