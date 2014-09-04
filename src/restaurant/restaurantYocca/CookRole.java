package restaurant.restaurantYocca;

import UnitTests.mock.LoggedEvent;
import agent.Agent;
import restaurant.restaurantYocca.Order.OrderState;
import restaurant.restaurantYocca.gui.CookGui;
import restaurant.restaurantYocca.gui.CustomerGui;
import restaurant.restaurantYocca.gui.WaiterGui;
import interfaces.Person;
import interfaces.restaurantYocca.Waiter;
import interfaces.restaurantYocca.Cook;
import role.Role;
import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import market.Market;
import market.MarketInvoice;
import market.OrderItem;

/**
 * Restaurant Cook Agent
 */

public class CookRole extends Role implements Cook {

	public List<Order> waitingOrders
	= new ArrayList<Order>();
	Inventory inv = new Inventory();
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	public Map<String,Food> foodMap = new HashMap<String,Food>();
	private Market currentMarket = null;
	public CookGui cookGui = null;
	public Restaurant restaurant;

	boolean checkOrderStand=true;

	private boolean waitingForOrder = false;

	private int cookCount = 0;

	ProducerConsumerMonitor<RestaurantOrder> orderMonitor;

	private Semaphore atCookingStation = new Semaphore(0,true);
	private Semaphore atPlateStation = new Semaphore(0,true);
	private Semaphore atOrderStand = new Semaphore(0,true);
	private Semaphore outOfRestaurant = new Semaphore(0,true);

	Timer timer = new Timer();

	private Market market;
	public String name;

	public class Food{
		String type;
		int cookingTime;
		public int quantity;
		int capacity;
		int low;
		int onOrder=0;
		List<Market> uselessMarkets = new ArrayList<Market>();

		public Food(String t, int time, int quantity, int capacity, int low){
			type=t;
			cookingTime=time;
			this.quantity = quantity;
			this.capacity = capacity;
			this.low = low;
		}
	}
	public CookRole(String name, ProducerConsumerMonitor<RestaurantOrder> monitor, Restaurant restaurant) {
		super();
		foodMap.put("Steak", new Food("Steak",5000,500,5,1));
		foodMap.put("Chicken", new Food("Chicken",4000,500,5,1));
		foodMap.put("Salad", new Food("Salad",2000,500,5,1));
		foodMap.put("Pizza", new Food("Pizza",3000,500,5,1));
		this.name = name;
		this.orderMonitor = monitor;
		this.restaurant = restaurant;
		//checkInventory=true;
	}

	public CookRole(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}


	public class Inventory {
		private int steak = 10;
		private int chicken = 10;
		private int salad = 10;
		private int pizza = 10;
	}

	// Messages

	public void setCurrentMarket(Market m) {
		currentMarket = m;
	}

	public Market getCurrentMarket() {
		return currentMarket;
	}

	public void msgHereIsAnOrder(Order o) {
		if (checkInventory(o.getChoice())) {
			waitingOrders.add(o);
		}
		else {
			Do("We are out of that choice, customer must reorder");
			o.waiter.msgOutOfChoice(o);
		}
		p.msgStateChanged();
	}

	public void msgHereIsDelivery(MarketInvoice order){
		Do("Received a food shipment");
		String shipmentMessage="Received ";

		for (OrderItem item: order.order){
			Food food = foodMap.get(item.choice);
			int shipment = item.quantityReceived;

			shipmentMessage+= shipment+item.choice+"s, ";
			food.quantity+=shipment;
			food.onOrder-=shipment;
		}
		Do(shipmentMessage);

		p.msgStateChanged();
	}

	public void msgCannotFulfillOrder(Market m,
			Map<String, Integer> unfulfillable) {
		Do("Need to reorder from another market");

		for (String f: unfulfillable.keySet()){
			Food food = foodMap.get(f);
			food.uselessMarkets.add(m);
			food.onOrder-=unfulfillable.get(f);
		}

		p.msgStateChanged();	
	}

	public void msgAtCookingStation() {//from animation
		cookGui.setArrived(true);
		atCookingStation.release();// = true;
		p.msgStateChanged();
	}	

	public void msgAtPlateStation() {//from animation
		cookGui.setArrived(true);
		atPlateStation.release();// = true;
		p.msgStateChanged();
	}	

	public void msgAtOrderStand() {//from animation
		cookGui.setArrived(true);
		atOrderStand.release();// = true;
		p.msgStateChanged();
	}	

	public void msgOutOfRetaurant() {
		cookGui.setArrived(true);
		outOfRestaurant.release();// = true;
		p.msgStateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		if (!((RestaurantYocca)this.restaurant).orderMonitor.isEmpty()){
			CheckOrderStand();
			return true;
		}

		synchronized(waitingOrders) {
			try {
				for (Order o: waitingOrders) {
					if (!o.isBeingCooked()) {
						cook(o);
						o.setBeingCooked(true);
					}		
				}
			}
			catch (ConcurrentModificationException e) {
			}
			return false;
		}
	}

	// Actions

	public void cook(final Order o) {
		cookCount++;
		cookGui.DoGoToCookingStation();
		cookGui.setArrived(false);
		try {
			atCookingStation.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (cookCount % 4 == 0) {
			cookGui.setAString(o.getChoice());
		}
		else if (cookCount % 4 == 1) {
			cookGui.setBString(o.getChoice());
		}
		else if (cookCount % 4 == 2) {
			cookGui.setCString(o.getChoice());
		}
		else cookGui.setDString(o.getChoice());
		timer.schedule(new TimerTask() {
			public void run() {
				Do("Order for Table " + o.getTable().getTableNumber() + " is ready!");
				cookGui.setAString("");
				cookGui.setBString("");
				cookGui.setCString("");
				cookGui.setDString("");
				cookGui.DoGoToPlateStation();
				cookGui.setArrived(false);
				try {
					atPlateStation.acquire();
					p.msgStateChanged();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				o.waiter.msgOrderIsReady(o);
				waitingOrders.remove(o);
				p.msgStateChanged();
				cookGui.DoGoToHomePosition();
			}
		},
		2000);
	}

	public void CheckOrderStand(){
		Do("Checking order stand");
		cookGui.DoGoToOrderStand();
		cookGui.setArrived(false);
		try {
			atOrderStand.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!orderMonitor.isEmpty()){
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, p.getName(), "Just got an order from the monitor",this.restaurant.cityRestaurant.animationPanel.getName());
			RestaurantOrder o = orderMonitor.remove();
			waitingOrders.add(new Order(o.w,o.choice,o.table,OrderState.pending));
		}
		else{
			checkOrderStand = false;
			timer.schedule(new TimerTask(){
				public void run(){
					checkOrderStand = true;
					p.msgStateChanged();
				}				
			}, 5000);		//Wake up every 5 seconds to check the stand
		}
	}

	public boolean checkInventory(String c) {	
		Map<String,Integer> shoppingList = new HashMap<String, Integer>();	
		synchronized(markets) {
			boolean hasFood = false;
			if (c == "St" && foodMap.get("Steak").quantity > 0 && !waitingForOrder) {
				Do("Inventory of steak is low, time to order more");
				foodMap.get("Steak").quantity--;
				if (foodMap.get("Steak").quantity <= foodMap.get("Steak").low) {
					shoppingList.put("Steak", foodMap.get("Steak").capacity-foodMap.get("Steak").quantity-foodMap.get("Steak").onOrder);
					foodMap.get("Steak").onOrder=foodMap.get("Steak").capacity-foodMap.get("Steak").quantity;
				}
				hasFood = true;
			}
			if (c == "C" && foodMap.get("Chicken").quantity > 0 && !waitingForOrder) {
				Do("Inventory of chicken is low, time to order more");
				foodMap.get("Chicken").quantity--;
				if (foodMap.get("Chicken").quantity <= foodMap.get("Chicken").low) {
					shoppingList.put("Chicken", foodMap.get("Chicken").capacity-foodMap.get("Chicken").quantity-foodMap.get("Chicken").onOrder);
					foodMap.get("Chicken").onOrder=foodMap.get("Chicken").capacity-foodMap.get("Chicken").quantity;
				}
				hasFood = true;
			}
			if (c == "Sa" && foodMap.get("Salad").quantity > 0 && !waitingForOrder) {
				Do("Inventory of salad is low, time to order more");
				foodMap.get("Salad").quantity--;
				if (foodMap.get("Salad").quantity <= foodMap.get("Salad").low) {
					shoppingList.put("Salad", foodMap.get("Salad").capacity-foodMap.get("Salad").quantity-foodMap.get("Salad").onOrder);
					foodMap.get("Salad").onOrder=foodMap.get("Salad").capacity-foodMap.get("Salad").quantity;
				}
				hasFood = true;
			}
			if (c == "P" && foodMap.get("Pizza").quantity > 0 && !waitingForOrder) {
				Do("Inventory of pizza is low, time to order more");
				foodMap.get("Pizza").quantity--;
				if (foodMap.get("Pizza").quantity <= foodMap.get("Pizza").low) {
					shoppingList.put("Pizza", foodMap.get("Pizza").capacity-foodMap.get("Pizza").quantity-foodMap.get("Pizza").onOrder);
					foodMap.get("Pizza").onOrder=foodMap.get("Pizza").capacity-foodMap.get("Pizza").quantity;
				}
				hasFood = true;
			}
			for (Market m: markets) {
				if (!shoppingList.isEmpty()) {
					m.host.msgBusinessWantsThis(this.restaurant, shoppingList);
				}	
			}
			return hasFood;
		}
	}

	public void addInventory(String c, int a) {
		if (c == "St") {
			inv.steak += a;
		}
		if (c == "C") {
			inv.chicken += a;
		}
		if (c == "Sa") {
			inv.salad += a;
		}
		if (c == "P") {
			inv.pizza += a;
		}
	}

	public void addMarket(Market m) {
		markets.add(m);
		market = markets.get(0);
	}

	public CookGui getGui() {
		return cookGui;
	}	

	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public void changeShifts(Person p) {
		if (this.p!=null){
			this.p.msgThisRoleDone(this);
		}	
		this.p = p;
		this.name = p.getName();
	}

	private void LeaveRestaurant() {
		cookGui.DoLeaveRestaurant();
		try {
			outOfRestaurant.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.p.msgThisRoleDone(this);
	}
	
	@Override
	public boolean canLeave() {
//		((RestaurantYocca)restaurant).leaveRestaurant(this);
//		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, p.getName(), "New cook is taking over for my Cook Role");
//		return true;
		return false;
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public void depleteInventory() {
		foodMap.get("Steak").quantity=0;
		foodMap.get("Chicken").quantity=0;
		foodMap.get("Salad").quantity=0;
		foodMap.get("Pizza").quantity=0;

	}
	
	public void decreaseInventory(String choice){
		foodMap.get(choice).quantity--;
		
		DoMessage(choice + " is now at " + foodMap.get(choice).quantity);
		
		if (p!=null)
			p.msgStateChanged();
	}
	
	public void increaseInventory(String choice){
		foodMap.get(choice).quantity++;
		
		DoMessage(choice + " is now at " + foodMap.get(choice).quantity);
		
		if (p!=null)
			p.msgStateChanged();
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_YOCCA, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));		
	}
	
	public void DoDebug(String message){
		//super.Do(message);
		AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_YOCCA, name, message, restaurant.cityRestaurant.ID);	
	}
	
	public void DoError(String message){
		//super.Do(message);
		AlertLog.getInstance().logError(AlertTag.RESTAURANT_YOCCA, name, message, restaurant.cityRestaurant.ID);	
	}

	@Override
	public void msgStateChanged() {
		p.msgStateChanged();
		
	}
}

