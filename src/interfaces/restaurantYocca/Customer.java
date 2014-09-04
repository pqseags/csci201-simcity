package interfaces.restaurantYocca;

import interfaces.BaseRestaurantCustomer;
import restaurant.restaurantYocca.Bill;
import restaurant.restaurantYocca.Menu;
import restaurant.restaurantYocca.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer extends BaseRestaurantCustomer {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	//public abstract void msgFollowMeToTable(Menu m);

	Menu menu = null;
	boolean isAtWaitingArea = false;

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	public abstract void msgWhatWouldYouLike();


	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	
	public abstract void msgHereIsYourCheck(Bill c);
	
	public abstract void msgHereIsYourChange(double amount);

	public abstract int getCustomerTableNum();

	public abstract double getAmountOwed();

	public abstract void setAmountOwed(double temp);

	public abstract String getCustomerName();

	public abstract void setWaiter(Waiter waiter);

	public abstract void setCashier(Cashier cashier);

	public abstract String getName();

	public abstract double getMoney();

	public abstract Object getGui();

	public abstract void startThread();

	public abstract void setGui(CustomerGui g);

	public abstract void setHost(Host host);

	public abstract void msgAtCashier();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void gotHungry();

	public abstract void msgHereIsYourFood(String choice);

	public abstract void msgWhatWouldYouLikeInstead();


	public abstract void msgFollowMeToTable(Menu menu2);


	public abstract void setTableLocation(int tableNumber);


	public abstract boolean isAtWaitingArea();
	
	public abstract void setAtWaitingArea(boolean atWaitingArea);




	//public abstract void setAmountOwed(int temp);


}