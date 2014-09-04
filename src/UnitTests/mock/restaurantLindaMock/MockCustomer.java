package UnitTests.mock.restaurantLindaMock;


import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;
import restaurant.Restaurant;
import restaurant.restaurantLinda.*;
import restaurant.restaurantLinda.gui.CustomerGui;
import interfaces.restaurantLinda.*;

/**
* A sample MockCustomer built to unit test a CashierAgent.
*
* @author Monroe Ekilah
*
*/
public class MockCustomer extends Mock implements Customer {

	public MockCustomer(String name){
		super(name);
	}
	
	@Override
	public void msgAtRestaurant(Restaurant r) {
		log.add(new LoggedEvent("Reached restaurant"));
		
	}

	@Override
	public void msgRestaurantFull() {
		log.add(new LoggedEvent("Restaurant is full"));
		
	}

	@Override
	public void msgFollowMe(Waiter w, Menu m) {
		log.add(new LoggedEvent("Received followMe from waiter " + w.getName() + ", and received a menu"));
		
	}

	@Override
	public void msgWhatDoYouWant() {
		log.add(new LoggedEvent("Was asked what I wanted"));
		
	}

	@Override
	public void msgRedoOrder(Menu menu, String food) {
		log.add(new LoggedEvent("Was asked to reorder, since " + food + " was out"));
		
	}

	@Override
	public void msgHereIsFood(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Check bill, Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPaymentReceived(int owed) {
		log.add(new LoggedEvent("Received message from cashier and owe " + owed));
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

}