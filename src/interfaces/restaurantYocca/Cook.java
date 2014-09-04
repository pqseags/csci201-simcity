package interfaces.restaurantYocca;

import interfaces.BaseRestaurantCook;
import market.Market;
import restaurant.restaurantYocca.Order;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cook extends BaseRestaurantCook {

	void addMarket(Market market1);
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	//public abstract void msgHereIsAnOrder(Order o);

	void startThread();
	void msgHereIsAnOrder(Order o);
	void msgStateChanged();

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	//public abstract void msgHereIsFood(String choice, int amount);


	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	//public abstract void msgOutOfFood(MarketAgent m, String choice);

}