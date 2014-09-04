package restaurant.restaurantLinda;

import UnitTests.mock.LoggedEvent;
import agent.Agent;
import interfaces.Person;
import interfaces.restaurantLinda.Cashier;
import interfaces.restaurantLinda.Cook;
import interfaces.restaurantLinda.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

import market.Market;
import market.MarketInvoice;
import market.OrderItem;

import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import restaurant.restaurantLinda.gui.CookGui;
import role.Role;
import util.Place;


public class CookRole extends Role implements Cook{
	String name;
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	Timer timer = new Timer();
	public Map<String,Food> foodMap = new HashMap<String,Food>();
	public boolean checkInventory=true;
	public boolean checkOrderStand=true;
	List<Market> marketMap;
	Restaurant restaurant;
	
	ProducerConsumerMonitor<RestaurantOrder> orderMonitor;
	
	private Semaphore atDestination = new Semaphore(0,true);
	private CookGui cookGui = null;
	
	public CookRole(String name, ProducerConsumerMonitor<RestaurantOrder> monitor, Restaurant restaurant) {
		super();
		foodMap.put("Steak", new Food("Steak",5000,50,100,20));
		foodMap.put("Chicken", new Food("Chicken",4000,50,100,20));
		foodMap.put("Salad", new Food("Salad",2000,50,100,20));
		foodMap.put("Pizza", new Food("Pizza",3000,50,100,20));
		this.name = name;
		this.orderMonitor = monitor;
		this.restaurant = restaurant;
		//checkInventory=true;
		
	}
	
	//messages
	public void msgHereIsOrder(Waiter w, String choice, int table){
		DoInfo(w.getName() + " says table " + table + " wants " + choice);
		orders.add(new Order(w,choice,table,OrderState.pending));
		log.add(new LoggedEvent("Got a new order from waiter "+w.getName()+" for "+choice));
		p.msgStateChanged();
	}
	
	public void msgTimerDone(Order o){
		DoInfo("Finished cooking" + o.choice);
		o.state=OrderState.done;
		p.msgStateChanged();		
	}
	
	public void msgCannotFulfillOrder(Market m, Map<String,Integer> unfulfillable){
		DoInfo("Need to reorder " + unfulfillable.toString() + " from another market");
		
		for (String f: unfulfillable.keySet()){
			Food food = foodMap.get(f);
			food.status=FoodState.partialOrdering;
			food.onOrder-=unfulfillable.get(f);
		}
		p.msgStateChanged();		
	}
	
	public void msgHereIsDelivery(MarketInvoice order){
		DoDebug("Received a food shipment");
		String shipmentMessage="Received ";
		
		for (OrderItem item: order.order){
			Food food = foodMap.get(item.choice);
			int shipment = item.quantityReceived;
			if (shipment==food.onOrder && food.status==FoodState.fullOrdering)
				food.status=FoodState.none;
			
			shipmentMessage+= shipment+item.choice+"s, ";
			food.quantity+=shipment;
			food.onOrder-=shipment;
		}
		DoInfo(shipmentMessage);
		
		p.msgStateChanged();
	}
	
	public void msgAtDestination() {//from animation
		atDestination.release();// = true;
		p.msgStateChanged();
	}
	
	public void msgStandTimerDone(){
		checkOrderStand = true;
		p.msgStateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (checkInventory && restaurant.markets.size()>0){
			OrderFoodThatIsLow();
			return true;
		}
		synchronized(orders){
			for (Order o: orders){
				if (o.state==OrderState.done){
					PlateIt(o);
					return true;
				}
			}
		}
		synchronized(orders){
			for (Order o: orders){
				if (o.state==OrderState.pending){
					TryToCookIt(o);
					return true;
				}
			}
		}
		for (String f: foodMap.keySet()){
			if (foodMap.get(f).status==FoodState.partialOrdering){
				OrderFoodThatIsLow();
				return true;
			}
		}
		if (checkOrderStand){
			CheckOrderStand();
			return true;
		}
		
		if (cookGui!=null)
			cookGui.DoGoToDefault();
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	//actions
	private void TryToCookIt(Order o){
		Food food = foodMap.get(o.choice);
		log.add(new LoggedEvent("Try cooking of " + food.type));
		
		if (food.quantity==0){
			orders.remove(o);
			Do("Out of " + food.type);
			o.w.msgOutOfChoice(o.choice,o.table, getEmptyFoodList());
			return;
		}
		if (food.quantity<0){
			Do("Error: less than 0 " + food.type);
			orders.remove(o);
			o.w.msgOutOfChoice(o.choice,o.table, getEmptyFoodList());
			return;
		}
		
		food.quantity--;
		DoInfo(food.quantity + " " + food.type + "left");
		if (food.quantity == food.low && food.status != FoodState.fullOrdering){
			OrderFoodThatIsLow();
		}
		
		if (cookGui!=null){
			cookGui.DoCooking();
			try{
				atDestination.acquire();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		DoInfo("Started cooking");
		final Order order = o;
		timer.schedule(new TimerTask(){
			public void run(){
				msgTimerDone(order);
			}
		},foodMap.get(o.choice).cookingTime);
		o.state=OrderState.cooking;
		
	}
	
	private void PlateIt(Order o){
		DoMessage(foodMap.get(o.choice).type + " is done");
		cookGui.DoPlating(o.choice);
		try{
			atDestination.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
		o.w.msgOrderDone(o.choice,o.table);
		orders.remove(o);
	}
	
	private void OrderFoodThatIsLow(){
		checkInventory=false;
		Map<String,Integer> shoppingList = new HashMap<String, Integer>();				
		String message = "Need ";
		
		for (String f: foodMap.keySet()){
			Food food = foodMap.get(f);
			
			if (food.quantity<=food.low && food.status!=FoodState.fullOrdering){
				shoppingList.put(food.type, food.capacity-food.quantity-food.onOrder);
				message += food.capacity-food.quantity-food.onOrder+" "+food.type+", ";
				food.onOrder=food.capacity-food.quantity;
				food.status=FoodState.fullOrdering;
			}
		}
		
		DoInfo(message);
		
		if (shoppingList.size()==0){
			DoMessage("No foods are low");
			return;
		}
		
		if (restaurant.markets.size()==1){
			DoMessage("ordering more food");
			restaurant.markets.get(0).host.msgBusinessWantsThis(restaurant, shoppingList);
		}
		else if (restaurant.markets.size()>1){
			DoMessage("ordering more food");
			Market m = restaurant.markets.remove(0);		//Rearrange markets, kind of like a circular queue?
			restaurant.markets.add(m);
			m.host.msgBusinessWantsThis(restaurant, shoppingList);
		}
	}
	
	public void CheckOrderStand(){
		log.add(new LoggedEvent("Checking order stand"));
		if (!orderMonitor.isEmpty()){
			DoInfo("Found a new order");
			RestaurantOrder o = orderMonitor.remove();
			orders.add(new Order(o.w,o.choice,o.table,OrderState.pending));
			//log.add(new LoggedEvent(""));
		}
		else{
			log.add(new LoggedEvent("No new orders"));
			checkOrderStand = false;
			timer.schedule(new TimerTask(){
				public void run(){
					msgStandTimerDone();
				}				
			}, 5000);		//Wake up every 5 seconds to check the stand
		}
	}
	
	
	//inner classes
	public class Order{
		public Waiter w;
		public String choice;
		public int table;
		OrderState state;
		
		Order(Waiter waiter, String ch, int t, OrderState s){
			w=waiter;
			choice=ch;
			table=t;
			state=s;
		}		
	}
	enum OrderState{pending,cooking,done,finished}
	
	public class Food{
		String type;
		int cookingTime;
		public int quantity;
		int capacity;
		int low;
		int onOrder=0;
		FoodState status = FoodState.none;
		
		public Food(String t, int time, int quantity, int capacity, int low){
			type=t;
			cookingTime=time;
			this.quantity = quantity;
			this.capacity = capacity;
			this.low = low;
		}
	}
	enum FoodState{none,fullOrdering,partialOrdering};
	
	//utilities
	private List<String> getEmptyFoodList(){
		List<String> emptyFoods = new ArrayList<String>();
		for (String food: foodMap.keySet()){
			if (foodMap.get(food).quantity<=0)
				emptyFoods.add(food);
		}
		return emptyFoods;
	}
	
	public void setRestaurant (Restaurant r){
		restaurant = r;
	}

	
	public void setGui(CookGui cg){
		cookGui = cg;
	}
	
	public CookGui getGui(){
		return cookGui;
	}
	
	public String getName() {
		return name;
	}

	public void setPerson(Person p) {
		this.p = p;		
	}

	@Override
	public boolean canLeave() {
		return false;
	}
	
	public void changeShifts(Person p){
		if (this.p!=null){
			LeaveRestaurant();
		}
		
		this.p = p;
		this.name = p.getName();
	}

	public void LeaveRestaurant(){
		/*cookGui.DoLeaveRestaurant();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		this.p.msgThisRoleDone(this);
	}
	
	public boolean isPresent(){
		return this.p!=null;
	}
	

	@Override
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
	
	public void DoInfo(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_LINDA, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));		
	}
	
	public void DoDebug(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, name, message, restaurant.cityRestaurant.ID);	
	}
	
	public void DoError(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logError(AlertTag.RESTAURANT_LINDA, name, message, restaurant.cityRestaurant.ID);	
	}

}

