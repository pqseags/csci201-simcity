package interfaces;

import market.MarketInvoice;

public interface MarketDeliveryMan extends Occupation {

	//public void msgDeliverThisOrder(MarketInvoice order);
	public void msgDockTimerDone();
	public void msgHereIsPayment(int payment, MarketInvoice invoice);
	
	public String getName();
	
}
