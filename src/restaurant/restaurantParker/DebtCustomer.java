package restaurant.restaurantParker;

import interfaces.restaurantParker.*;

public class DebtCustomer {

	int debt;
	Customer customer;

	enum DebtCustomerState {present, gone};
	DebtCustomerState state = DebtCustomerState.present;
	
	
	
	DebtCustomer(int debt, Customer customer){
		this.debt = debt;
		this.customer = customer;
	}
}
