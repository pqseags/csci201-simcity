package restaurant.restaurantLinda;

import restaurant.restaurantLinda.WaiterRole.CustomerState;
import restaurant.restaurantLinda.WaiterRole.MyCustomer;
import interfaces.Person;
import interfaces.restaurantLinda.Cashier;
import interfaces.restaurantLinda.Cook;
import interfaces.restaurantLinda.Host;

public class OriginalWaiterRole extends WaiterRole{

	public OriginalWaiterRole(String name, Person p) {
		super();
		this.name = name;
		this.p = p;
	}
	
	//messages, scheduler, and most actions belong in base waiter class
	
	//single function that needs to be overridden
	protected void SendOrder(MyCustomer mc){
		DoMessage("Magically sending " + mc.c.getName() + "'s order to cook");
		mc.state=CustomerState.orderSent;
		((CookRole)restaurant.cook).msgHereIsOrder(this, mc.choice, mc.table);		
	}
}
