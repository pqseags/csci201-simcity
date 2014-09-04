package interfaces.restaurantYocca;

import interfaces.BaseRestaurantHost;

import java.util.ArrayList;
import java.util.List;

import restaurant.restaurantYocca.CustomerRole;
import restaurant.restaurantYocca.WaiterRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Host extends BaseRestaurantHost {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	//public abstract void msgCanIGoOnBreak(WaiterAgent waiter);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	//public abstract void msgIWantToEat(CustomerAgent cust);
	public List<Customer> waitingCustomers
	= new ArrayList<Customer>();
	public List<Waiter> waiterList = new ArrayList<Waiter>();
	
	
	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	//public abstract void msgTableIsFree(int tableNum);

	public abstract boolean allTablesOccupied();

	public abstract void msgCanIGoOnBreak(Waiter waiterAgent);

	public abstract void msgIWantToEat(Customer customerAgent);

	public abstract void startThread();

	public abstract void setCook(Cook cook);

	public abstract void setCashier(Cashier cashier);

	public abstract void setWaiter(Waiter waiter);

	public abstract void msgTableIsFree(int customerTableNum);

	public abstract void setPaused(boolean b);

}