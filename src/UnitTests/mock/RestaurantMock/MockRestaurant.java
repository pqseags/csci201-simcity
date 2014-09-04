package UnitTests.mock.RestaurantMock;

import interfaces.BaseRestaurantCustomer;
import interfaces.Person;
import restaurant.Restaurant;
import role.Role;
import util.JobType;

public class MockRestaurant extends Restaurant{
	
	public MockRestaurant(){
		
	}

	@Override
	public Role canIStartWorking(Person p, JobType type, Role r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public void customerEntering(BaseRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

}
