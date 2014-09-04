package interfaces.restaurantLinda;

import interfaces.BaseRestaurantCashier;

import java.util.Map;

import restaurant.restaurantLinda.Check;

public interface Cashier extends BaseRestaurantCashier{
	
	public abstract void msgPleaseComputeBill(Waiter w, String choice, Customer cust);
	
	public abstract void msgHereIsPayment(Customer cust, Check bill, int payment);
	
}
