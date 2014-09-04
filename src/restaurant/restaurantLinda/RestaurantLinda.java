package restaurant.restaurantLinda;

import interfaces.BaseRestaurantCashier;
import interfaces.BaseRestaurantCook;
import interfaces.BaseRestaurantCustomer;
import interfaces.BaseRestaurantHost;
import interfaces.BaseRestaurantWaiter;
import interfaces.Person;
import interfaces.restaurantLinda.Cashier;
import interfaces.restaurantLinda.Cook;
import interfaces.restaurantLinda.Customer;
import interfaces.restaurantLinda.Host;
import interfaces.restaurantLinda.Waiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import astar.AStarTraversal;
import cityGui.CityRestaurant;
import cityGui.CityRestaurantLinda;
import cityGui.CityRestaurantYoccaCard;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import restaurant.restaurantLinda.gui.CookGui;
import restaurant.restaurantLinda.gui.CustomerGui;
import restaurant.restaurantLinda.gui.Gui;
import restaurant.restaurantLinda.gui.WaiterGui;
import role.Role;
import util.JobType;

public class RestaurantLinda extends Restaurant{
		
	ProducerConsumerMonitor<RestaurantOrder> orderMonitor = new ProducerConsumerMonitor<RestaurantOrder>();
	
	public CityRestaurantLinda cityRestaurant;
	public Host host;
	public Cashier cashier;
	public Cook cook;
	public List<BaseRestaurantWaiter> waiters;
	
	//public boolean isOpen = true;
	
	public RestaurantLinda(CityRestaurantLinda cr){
		this.cityRestaurant = cr;
		
		cash = 5000;
		
		host = new HostRole("RestaurantHost"); 
		cashier =  new CashierRole("RestaurantCashier", this);
		cook = new CookRole("Cook", orderMonitor, this);
		waiters = Collections.synchronizedList(new ArrayList<BaseRestaurantWaiter>());
		
		super.cityRestaurant = this.cityRestaurant;
		super.host = this.host;
		super.cashier = this.cashier;
		super.cook = this.cook;
		super.waiters = this.waiters;

//		
//		CookGui cg = new CookGui(cook);
//		cg.setPlates(cityRestaurant.animationPanel.platedFoods);
//		cook.setGui(cg);
//		cityRestaurant.animationPanel.addGui(cg);
		/*AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, "RestaurantLinda", "Debug message", cityRestaurant.ID);
		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, "RestaurantLinda", "Info message", cityRestaurant.ID);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_LINDA, "RestaurantLinda", "Message message", cityRestaurant.ID);
		AlertLog.getInstance().logError(AlertTag.RESTAURANT_LINDA, "RestaurantLinda", "Error message", cityRestaurant.ID);*/
	}
	
	//dummy constructor for agent-only unit tests
	public RestaurantLinda(){
		
	}
	
	@Override
	public Role canIStartWorking(Person p, JobType type, Role r) {
		if (type == JobType.RestaurantHost){
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, p.getName(), "I'm taking over as host",this.cityRestaurant.animationPanel.getName());

			((HostRole)host).changeShifts(p);
			return (Role)host;
		}
		else if (type == JobType.RestaurantLindaWaiter1){
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, p.getName(), "I'm taking over as waiter",this.cityRestaurant.animationPanel.getName());

			((WaiterRole)r).setRestaurant(this);
			((ProducerConsumerWaiterRole)r).setMonitor(orderMonitor);
			waiterComingToWork((Waiter) r);
			return r;
		}
		else if (type == JobType.RestaurantLindaWaiter2){
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, p.getName(), "I'm taking over as waiter",this.cityRestaurant.animationPanel.getName());
			((WaiterRole)r).setRestaurant(this);

			waiterComingToWork((Waiter) r);
			return r;
		}
		else if (type == JobType.RestaurantCook){
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, p.getName(), "I'm taking over as cook",this.cityRestaurant.animationPanel.getName());
			//cityRestaurant.animationPanel.removeGui(cook.getGui());
			((CookRole)this.cook).changeShifts(p);
			if (((CookRole)cook).getGui() == null) {
				CookGui cg = new CookGui((CookRole)this.cook);
				cg.setPlates(cityRestaurant.animationPanel.platedFoods);
				((CookRole)this.cook).setGui(cg);
				cityRestaurant.animationPanel.addGui(cg);
			}
			return (Role)cook;
		}
		else if (type == JobType.RestaurantCashier){
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, p.getName(), "I'm taking over as cashier",this.cityRestaurant.animationPanel.getName());

			((CashierRole)cashier).changeShifts(p);
			return (Role) cashier;
		}
		
		AlertLog.getInstance().logError(AlertTag.RESTAURANT_LINDA, p.getName(), "Unrecognized job type: " + type, cityRestaurant.ID);
		return null;
	}
		
	public void waiterComingToWork(Waiter r){
		WaiterGui wg = new WaiterGui((WaiterRole)r, waiters.size(), new AStarTraversal(cityRestaurant.grid));
		waiters.add((WaiterRole)r);
		((WaiterRole)r).setGui(wg);
		wg.setTables(((CityRestaurantLinda)cityRestaurant).getTables());
		wg.setPlates(cityRestaurant.animationPanel.platedFoods);
		((HostRole)host).addWaiter(r);
		cityRestaurant.animationPanel.addGui(wg);
	}
	
	public BaseRestaurantCook cookComingToWork(Person p){
		((CookRole)cook).changeShifts(p);
		return cook;
	}
	
	public void waiterLeaving(Waiter w){
		waiters.remove(w);
		leaveRestaurant(((WaiterRole)w).getGui());
	}
	
	public void leaveRestaurant(Gui gui){
		cityRestaurant.animationPanel.removeGui(gui);
	}
	
	//Override this to get your restaurant started
	@Override
	public void customerEntering(BaseRestaurantCustomer c){
		CustomerGui cg = new CustomerGui((CustomerRole)c, new AStarTraversal(cityRestaurant.grid));
		((CustomerRole)c).setGui(cg);
		cityRestaurant.animationPanel.addGui(cg);
	}
	
	public boolean unStaffed(){
		return !host.isPresent() || !cook.isPresent() || !cashier.isPresent() || waiters.isEmpty();
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return isOpen;
	}

}
