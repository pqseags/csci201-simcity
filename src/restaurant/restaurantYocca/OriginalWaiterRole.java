package restaurant.restaurantYocca;

import restaurant.restaurantYocca.WaiterRole.CustomerState;
import restaurant.restaurantYocca.WaiterRole.MyCustomer;
import interfaces.Person;
import interfaces.restaurantYocca.Cashier;
import interfaces.restaurantYocca.Cook;
import interfaces.restaurantYocca.Host;

public class OriginalWaiterRole extends WaiterRole{

	public OriginalWaiterRole(String name, Person p) {
		super();
		this.name = name;
		this.p = p;
	}
	
	//messages, scheduler, and most actions belong in base waiter class
	
	//single function that needs to be overridden
	public void submitOrder(Order o){
		//Do("Magically sending " + mc.customer.getName() + "'s order to cook");
		//mc.state=CustomerState.orderSent;
		((CookRole)restaurant.cook).msgHereIsAnOrder(o);	
		waiterGui.DoLeaveCustomer();
	}
}
