package interfaces.restaurantLinda;

import interfaces.BaseRestaurantWaiter;
import interfaces.Occupation;

import java.util.List;

public interface Waiter extends BaseRestaurantWaiter{

	public abstract void msgPleaseServeCustomer(Customer c, int table);

	public abstract void msgReadyToOrder(Customer c);
	
	public abstract void msgHereIsChoice(Customer c, String choice);
	
	public abstract void msgOutOfChoice(String choice, int table, List<String> unavailable);
	
	public abstract void msgOrderDone(String choice,int table);
	
	public abstract void msgHereIsBill(String choice, Customer c, int total);
	
	public abstract void msgLeaving(Customer c);
	
	public abstract void msgDoneAndPaying(Customer c);
	
	public abstract void msgBreakStatus(boolean permission);
	
	public abstract String getName();
}
