package UnitTests.mock.restaurantLindaMock;

import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;
import interfaces.restaurantLinda.*;

public class MockHost extends Mock implements Host{

	public MockHost(String name){
		super(name);
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
	public void msgIWantFood(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWillWait(Customer cust, boolean wait) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCustomerLeaving(Waiter waiter, Customer cust, int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantBreak(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOffBreak(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTable(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWaiter(Waiter m) {
		// TODO Auto-generated method stub
		
	}



}
