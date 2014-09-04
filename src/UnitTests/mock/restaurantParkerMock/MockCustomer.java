package UnitTests.mock.restaurantParkerMock;

import UnitTests.mock.*;
import interfaces.restaurantParker.Customer;
import restaurant.Restaurant;
import restaurant.restaurantParker.CashierRole;

public class MockCustomer extends Mock implements Customer{

	public CashierRole cashier;
	
	//had to add myself
	public EventLog log = new EventLog();
	
	public MockCustomer(String name) {
		super(name);
	}

	@Override
	public void msgGiveChange(int change) {
		log.add(new LoggedEvent("Received change of $" + change));
	}

	@Override
	public void msgAtRestaurant(Restaurant r) {
		// TODO Auto-generated method stub
		
	}
	
	
}




