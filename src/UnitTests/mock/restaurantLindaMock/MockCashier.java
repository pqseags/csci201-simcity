package UnitTests.mock.restaurantLindaMock;

import java.util.Map;

import market.MarketInvoice;

import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

import restaurant.restaurantLinda.Check;
import interfaces.MarketDeliveryMan;
import interfaces.restaurantLinda.*;

public class MockCashier extends Mock implements Cashier{

	public MockCashier(String name){
		super(name);
	}
	
	@Override
	public void msgHereIsInvoice(MarketDeliveryMan deliveryMan,
			MarketInvoice order) {
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
	public void msgPleaseComputeBill(Waiter w, String choice, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(Customer cust, Check bill, int payment) {
		// TODO Auto-generated method stub
		
	}


}
