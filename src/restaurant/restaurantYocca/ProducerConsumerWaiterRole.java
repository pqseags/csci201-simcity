package restaurant.restaurantYocca;

import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import restaurant.ProducerConsumerMonitor;
import restaurant.restaurantYocca.WaiterRole.CustomerState;
import restaurant.restaurantYocca.WaiterRole.MyCustomer;
import interfaces.Person;
import interfaces.restaurantYocca.Cashier;
import interfaces.restaurantYocca.Cook;
import interfaces.restaurantYocca.Host;

public class ProducerConsumerWaiterRole extends WaiterRole{

	ProducerConsumerMonitor<RestaurantOrder> orderMonitor;
		
	public ProducerConsumerWaiterRole(String name, Person p) {
		super();
		this.name = name;
		this.p = p;
	}
	
	//messages, scheduler, and most actions belong in base waiter class
	
	@Override
	public void submitOrder(Order o){
		RestaurantOrder order = new RestaurantOrder(this, o.getTable(), o.getChoice());
		
		waiterGui.DoGoToTheOrderStand();
		waiterGui.setArrived(false);
		try{
			atTheOrderStand.acquire();
			p.msgStateChanged();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		//mc.state=CustomerState.orderSent;

		this.restaurant.orderMonitor.insert(order);
		this.restaurant.cook.msgStateChanged();
		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, p.getName(), "Just submitted an order to the monitor",this.restaurant.cityRestaurant.animationPanel.getName());
		waiterGui.DoLeaveCustomer();
	}
	
	public void setMonitor(ProducerConsumerMonitor<RestaurantOrder> orders){
		this.orderMonitor = orders;
	}
}
