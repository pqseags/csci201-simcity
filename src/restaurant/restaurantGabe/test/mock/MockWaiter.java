package restaurant.restaurantGabe.test.mock;

import restaurant.restaurantGabe.interfaces.Waiter;
import restaurant.restaurantGabe.util.Check;

public class MockWaiter extends Mock implements Waiter{

	public EventLog log = new EventLog();
	
	public MockWaiter(String name){
		super(name);
	}
	
	@Override
	public String getName(){
		return super.getName();
	}
	
	@Override
	public void msgHereIsCheck(Check c){
		
		log.add(new LoggedEvent("Recieved a check for customer "+c.c.getName()+" for $"+c.amount));
		
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}
}
