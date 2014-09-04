package UnitTests.mock.RestaurantMock;

import java.util.Map;

import market.Market;
import market.MarketInvoice;
import market.OrderItem;
import interfaces.BaseRestaurantCook;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

public class MockBaseRestaurantCook extends Mock implements BaseRestaurantCook{

	public MockBaseRestaurantCook(String name){
		super(name);
	}
	
	
	@Override
	public boolean canLeave() {
		return false;
	}

	@Override
	public void msgHereIsDelivery(MarketInvoice order) {
		String list = "{";
		for (OrderItem o: order.order){
			list+=order.order.toString();
		}
		list+="}";
		
		log.add(new LoggedEvent("Received delivery of food: " + list));		
	}

	@Override
	public void msgCannotFulfillOrder(Market m,	Map<String, Integer> unfulfillable) {
		log.add(new LoggedEvent("Received message from market that these items from the order are unfulfillable: " + unfulfillable.toString()));
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void depleteInventory() {
		// TODO Auto-generated method stub
		
	}
	

}
