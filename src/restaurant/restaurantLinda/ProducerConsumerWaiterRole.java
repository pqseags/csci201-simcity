package restaurant.restaurantLinda;

import restaurant.ProducerConsumerMonitor;
import restaurant.restaurantLinda.WaiterRole.CustomerState;
import restaurant.restaurantLinda.WaiterRole.MyCustomer;
import interfaces.Person;
import interfaces.restaurantLinda.Cashier;
import interfaces.restaurantLinda.Cook;
import interfaces.restaurantLinda.Host;

public class ProducerConsumerWaiterRole extends WaiterRole{

	ProducerConsumerMonitor<RestaurantOrder> orderMonitor;
	
	public ProducerConsumerWaiterRole(String name, Person p) {
		super();
		this.name = name;
		this.p = p;
	}
	
	//messages, scheduler, and most actions belong in base waiter class
	
	//Single function that needs to be overwritten
	protected void SendOrder(MyCustomer mc){
		RestaurantOrder order = new RestaurantOrder(this, mc.table, mc.choice);
		
		DoMessage("Manually sending " + mc.c.getName() + "'s order to cook");
		
		if (waiterGui!=null){
			waiterGui.DoGoToCook();
			try{
				atDestination.acquire();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		mc.state=CustomerState.orderSent;

		orderMonitor.insert(order);
	}
	
	public void setMonitor(ProducerConsumerMonitor<RestaurantOrder> orders){
		this.orderMonitor = orders;
	}
}
