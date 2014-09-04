package UnitTests.mock.RestaurantMock;

import market.MarketInvoice;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;
import interfaces.BaseRestaurantCashier;
import interfaces.MarketDeliveryMan;

public class MockBaseRestaurantCashier extends Mock implements BaseRestaurantCashier{

	public MockBaseRestaurantCashier(String name){
		super(name);
	}
	
	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgHereIsInvoice(MarketDeliveryMan deliveryMan,	MarketInvoice order) {
		log.add(new LoggedEvent("Received invoice from deliveryMan " + deliveryMan.getName() + " to pay $" + order.total + " for order " + order.order));
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
