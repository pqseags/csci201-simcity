package UnitTests.mock.restaurantParkerMock;

import restaurant.restaurantParker.CashierRole;
import interfaces.restaurantParker.*;
import UnitTests.mock.*;

public class MockWaiter extends Mock implements Waiter{

	public CashierRole cashier;
	public int check = 0;
	
	//had to add myself
	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
	}

	
	@Override
	public void msgHereIsCheck(int check, Customer cust) {
		log.add(new LoggedEvent("I was given a check of " + check + " for " + cust.getName()));
		this.check = check;
		
	}


	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
