package UnitTests.mock.restaurantLindaMock;

import java.util.List;

import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

import interfaces.restaurantLinda.*;


public class MockWaiter extends Mock implements Waiter{

	public MockWaiter(String name){
		super(name);
	}
	
	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgPleaseServeCustomer(Customer c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChoice(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(String choice, int table,
			List<String> unavailable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderDone(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsBill(String choice, Customer c, int total) {
		log.add(new LoggedEvent("Received message from cashier with the newly computed bill for customer " + c.getName() + " who ordered Steak. Total is $" + total));
		
	}

	@Override
	public void msgLeaving(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAndPaying(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakStatus(boolean permission) {
		// TODO Auto-generated method stub
		
	}
	

}
