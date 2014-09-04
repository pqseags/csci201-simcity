package interfaces.restaurantYocca;

import interfaces.BaseRestaurantCashier;
import restaurant.restaurantYocca.Bill;
import restaurant.restaurantYocca.CustomerRole;
import restaurant.restaurantYocca.WaiterRole;
//import restaurant.restaurantYocca.gui.AnimationPanel;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier extends BaseRestaurantCashier{
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	public abstract void msgComputeCheck(String choice, Customer cust, Waiter w);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	public abstract void msgPayment(Bill c, double money);

//	public abstract void setAnimationPanel(AnimationPanel animationPanel);

	public abstract void startThread();

	public abstract void setWaiter(Waiter waiter);


}
