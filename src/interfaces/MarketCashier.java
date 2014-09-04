package interfaces;

import java.util.List;
import java.util.Map;

import restaurant.Restaurant;

import market.MarketInvoice;
import market.Market;
import market.MarketCashierRole.MyCustomer;
import market.OrderItem;

public interface MarketCashier extends Occupation{
	


	public void msgServiceCustomer(MarketCustomer c, Map<String, Integer> groceries);
	public void msgFinishedComputing(MyCustomer mc);
	public void msgCustomerPayment(MarketCustomer c, int payment);
	public void msgHereIsBusinessPayment(int payment);
	public void msgCalculateInvoice(MarketEmployee employee, List<OrderItem> order, Restaurant r);

	public String getName();
	public boolean isPresent();
	public abstract boolean YouAreDoneWithShift();
	
	public abstract void setMarket(Market m);
	

}

