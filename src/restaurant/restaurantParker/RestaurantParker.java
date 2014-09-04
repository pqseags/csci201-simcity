package restaurant.restaurantParker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurantParker.gui.CookGui;
import restaurant.restaurantParker.gui.CustomerGui;
import restaurant.restaurantParker.gui.WaiterGui;
import astar.AStarTraversal;
import cityGui.CityRestaurantParker;
import cityGui.CityRestaurantParkerCard;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import interfaces.BaseRestaurantCustomer;
import interfaces.BaseRestaurantWaiter;
import interfaces.Person;
import interfaces.restaurantParker.Waiter;
import restaurant.Restaurant;
import role.Role;
import util.JobType;

public class RestaurantParker extends Restaurant{
	//make this like market panel
	
	//revolving stand
	private RevolvingStand stand = new RevolvingStand<RevolvingStandOrder>();
	

	public CityRestaurantParker cityRestaurant;
	public CityRestaurantParkerCard gui;
	public HostRole host;
	public CashierRole cashier;
	public CookRole cook;
	public List<BaseRestaurantWaiter> waiters;
	
	public RestaurantParker(CityRestaurantParker pr){
		this.cityRestaurant = pr;
		this.gui = pr.animationPanel;
		
		
		host = new HostRole("RestaurantHost", this); 
		cashier =  new CashierRole("RestaurantCashier", this);
		cook = new CookRole("Cook", stand, this);
		waiters = Collections.synchronizedList(new ArrayList<BaseRestaurantWaiter>());
		
		super.cityRestaurant = this.cityRestaurant;
		super.host = this.host;
		super.cashier = this.cashier;
		super.cook = this.cook;
		super.waiters = this.waiters;
		
		//add guis
		CookGui cookGui = new CookGui(cook);
		cook.setGui(cookGui);
		this.gui.addGui(cookGui);
		
	}
	
	@Override
	public Role canIStartWorking(Person p, JobType type, Role r) {
		if (type == JobType.RestaurantHost){
			//AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_PARKER, p.getName(), "I'm taking over as host",this.cityRestaurant.animationPanel.getName());
			host.changeShifts(p);
			return (Role)host;
		}
	//	CURRENTLY:  watier types
		else if (type == JobType.RestaurantParkerWaiter1){
			((WaiterRole)r).setRestaurant(this);
			((WaiterPCRole)r).setRevolvingStand(stand);
			waiterComingToWork((Waiter) r);
			return r;
		}
		else if (type == JobType.RestaurantParkerWaiter2){
			((WaiterRole)r).setRestaurant(this);
			waiterComingToWork((Waiter) r);
			return r;
	}
		else if (type == JobType.RestaurantCook){
			cook.changeShifts(p);
			return (Role)cook;
		}
		else if (type == JobType.RestaurantCashier){
			cashier.changeShifts(p);
			return (Role) cashier;
		}
		
		System.out.println("Unrecognized job type: " + type);
		return null;
	}

	
	public void waiterComingToWork(Waiter r){
		host.addWaiter((WaiterRole)r);
		waiters.add((WaiterRole)r);
		((WaiterRole)r).setCook(cook);
		((WaiterRole)r).setHost(host); 
		((WaiterRole)r).setCashier(cashier);
		
		int position = waiters.size();
		WaiterGui g = new WaiterGui((WaiterRole)r, position); 
		this.gui.addGui(g);
		((WaiterRole)r).setGui(g);
		
	}
	
	@Override
	public void customerEntering(BaseRestaurantCustomer c) {
		//System.err.println("RestaurantParker.customerEntering");
		CustomerGui g = new CustomerGui((CustomerRole)c);
		((CustomerRole)c).setGui(g);
		
		this.gui.addGui(g);
		((CustomerRole)c).setCashier(cashier);
		((CustomerRole)c).setHost(host);
		
		//g.setHungry();
		((CustomerRole)c).msgAtRestaurant(this);
		
		
	}

	@Override
	public boolean isOpen() {
		return true;
	}
	
	public RestaurantParker(){
		//for testing
	}

}
