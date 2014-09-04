package interfaces;

import java.util.List;
import java.util.Map;


import market.MarketInvoice;
import market.OrderItem;
import restaurant.Restaurant;

public interface MarketEmployee extends Occupation {
	
	public void msgGetItemsForCustomer(MarketCustomer c, Map<String, Integer> orderList);
	public void msgGetThis(List<OrderItem> order, Restaurant r);
	public void msgGiveInvoice(List<OrderItem> order, Restaurant r, int total);
	//public void addDeliveryMan(MarketDeliveryMan m);
	public String getName();
	
}
