package restaurant.restaurantParker;

import restaurant.restaurantParker.MyCustomer.MyCustomerState;
import person.PersonAgent;


public class WaiterOriginalRole extends WaiterRole{
	
	
	public WaiterOriginalRole(String name, PersonAgent p) {
		super(name);
		this.p = p;
	}

	//everything else in WaiterAgent class
	
	
	@Override
	protected void placeOrder(MyCustomer mc) {
		DoPlaceOrder(mc);
		
		mc.state = MyCustomerState.orderedAndWaiting;
		//Order order = new Order(mc.choice, mc.tableNumber, mc.customer, this);
		cook.msgHereIsAnOrder(mc.choice, mc.tableNumber, mc.customer, this);
		
		
		
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

}
