package interfaces;

import market.MarketInvoice;

public interface BaseRestaurantCashier extends Occupation {
	
	public abstract void msgHereIsInvoice(MarketDeliveryMan deliveryMan, MarketInvoice order);

	public abstract boolean isPresent();

}
