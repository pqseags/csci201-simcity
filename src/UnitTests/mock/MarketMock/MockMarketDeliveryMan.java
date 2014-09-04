package UnitTests.mock.MarketMock;

import market.MarketInvoice;
import market.OrderItem;
import interfaces.MarketDeliveryMan;
import UnitTests.mock.EventLog;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

public class MockMarketDeliveryMan extends Mock implements MarketDeliveryMan{
	
	public EventLog log = new EventLog();
	String name;
	
	public MockMarketDeliveryMan(String name){
		this.name = name;
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgHereIsPayment(int payment, MarketInvoice invoice) {
		String list = "{";
		for (OrderItem o: invoice.order){
			list+=o.quantityReceived+o.choice+", ";
		}
		list+="}";
		log.add(new LoggedEvent("Received payment of " + payment + " from cashier for order " + list));		
	}

	@Override
	public void msgDockTimerDone() {
		// TODO Auto-generated method stub
		
	}
	
	

}
