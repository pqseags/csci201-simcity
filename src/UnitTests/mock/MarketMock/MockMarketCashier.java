package UnitTests.mock.MarketMock;

import java.util.List;
import java.util.Map;

import restaurant.Restaurant;
import market.Market;
import market.MarketCashierRole.MyCustomer;
import market.OrderItem;
import interfaces.MarketCashier;
import interfaces.MarketCustomer;
import interfaces.MarketEmployee;
import UnitTests.mock.EventLog;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

public class MockMarketCashier extends Mock implements MarketCashier{

	
	public EventLog log = new EventLog();
	
	String name;
	public MarketCustomer customer;
	
	
	public MockMarketCashier(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgServiceCustomer(MarketCustomer c, Map<String, Integer> groceries) {
		log.add(new LoggedEvent("got msgServiceCustomer for customer " + c.getName() + " for order " + groceries.toString()));
		
	}

	@Override
	public void msgFinishedComputing(MyCustomer mc) {
		log.add(new LoggedEvent("Finished computing order for customer " + mc.c.getName()));
		
	}

	@Override
	public void msgCustomerPayment(MarketCustomer c, int payment) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("got msgCustomerPayment"));
	}

	@Override
	public void msgHereIsBusinessPayment(int payment) {
		log.add(new LoggedEvent("Received business payment of $" + payment));
		
	}


	@Override
	public boolean YouAreDoneWithShift() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMarket(Market m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCalculateInvoice(MarketEmployee employee, List<OrderItem> order, Restaurant r) {
		String list = "{";
		for(OrderItem o: order){
			list+=o.quantityReceived + o.choice + ", ";
		}
		list+="}";
		
		log.add(new LoggedEvent("got msgCalculateInvoice from employee " + employee.getName() + " for order " + list + " for restaurant " + r.toString()));
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

}
