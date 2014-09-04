package UnitTests.mock.restaurantLindaMock;

import java.util.Map;

import market.Market;
import market.MarketInvoice;

import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

import interfaces.restaurantLinda.*;

public class MockCook extends Mock implements Cook {

	public MockCook(String name){
		super(name);
	}
	
	@Override
	public void msgHereIsDelivery(MarketInvoice order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCannotFulfillOrder(Market m,
			Map<String, Integer> unfulfillable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void depleteInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgHereIsOrder(Waiter w, String choice, int table) {
		// TODO Auto-generated method stub
		
	}
	

	
}
