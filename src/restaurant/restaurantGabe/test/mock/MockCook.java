package restaurant.restaurantGabe.test.mock;

import java.util.Map;

import restaurant.restaurantGabe.WaiterRole;
import restaurant.restaurantGabe.util.Order;
import market.Market;
import market.MarketInvoice;

public class MockCook extends Mock implements restaurant.restaurantGabe.interfaces.Cook {

	
	public EventLog log = new EventLog();
	
	
	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsAnOrder(WaiterRole w, Order o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Just got an order from waiter "+w.getName()+" for "+o.choice));
		
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
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void depleteInventory() {
		// TODO Auto-generated method stub
		
	}


	public void addMarket(Market m) {
		// TODO Auto-generated method stub
		
	}



}
