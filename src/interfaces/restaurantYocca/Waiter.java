package interfaces.restaurantYocca;

import interfaces.BaseRestaurantWaiter;
import restaurant.restaurantYocca.Bill;
import restaurant.restaurantYocca.CustomerRole;
import restaurant.restaurantYocca.Order;
import restaurant.restaurantYocca.Table;
import restaurant.restaurantYocca.gui.WaiterGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Waiter extends BaseRestaurantWaiter {

	int serves = 0;

	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	public abstract void msgSitAtTable(Customer c, Table t);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	public abstract void msgImReadyToOrder(int tableNum);


	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	public abstract void msgHereIsMyChoice(Customer c, String choice);
	public abstract void msgOutOfChoice(Order o);
	public abstract void msgOrderIsReady(Order o);
	public abstract void msgImDone(int tableNum);
	public abstract void msgCheckComputed(Bill c);
	public abstract void msgDoneAndPaid(int tableNum);

	public abstract boolean isOnBreak();

	public abstract void msgCantGoOnBreak();

	public abstract void msgGoOnBreak();


	public abstract void msgAtCook();

	public abstract void msgAtCashier();

	public abstract void msgAtTable();

	public abstract void msgAtStart();

	public abstract void TryGoOnBreak();

	public abstract String getName();

	public abstract void setHost(Host host);

	public abstract void setCashier(Cashier cashier);

	public abstract void startThread();

	public abstract void setGui(WaiterGui waiterGui);

	public abstract void setOnBreak(boolean b);

	public abstract void setCook(Cook cookAgent);

	public abstract void addServes();

	public abstract int getServes();

	public abstract void submitOrder(Order o);
	
}