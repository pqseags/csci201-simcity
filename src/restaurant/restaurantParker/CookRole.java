package restaurant.restaurantParker;

import role.Role;
import agent.Agent;
import interfaces.Person;
import interfaces.restaurantParker.*;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Map.Entry;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import market.Market;
import market.MarketInvoice;
import restaurant.restaurantParker.CustomerRole.AgentEvent;
import restaurant.restaurantParker.Order.OrderState;
import restaurant.restaurantParker.gui.CookGui;

/**
 * Restaurant Cook Agent
 */

public class CookRole extends Role implements Cook{
	
	RevolvingStand<RevolvingStandOrder> revolvingStand;
	boolean checkStand = true;
	
	private final int capacity = 500;
	private final int stock = 500;
	private final int threshold = 1;
	
	boolean timeToCheck = false;

	Timer timer = new Timer();
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	//private List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	private List<CookOrder> cookOrders = Collections.synchronizedList(new ArrayList<CookOrder>());
	private List<Boolean> grills = Collections.synchronizedList(new ArrayList<Boolean>());
	private List<Boolean> plates = Collections.synchronizedList(new ArrayList<Boolean>());
	private List<Integer> takenPlates = Collections.synchronizedList(new ArrayList<Integer>());
	
	private String name;
	
	private CookGui cookGui = null;
	RestaurantParker restaurant;
	private final int numTables = 3;
	
	//cook timing map
	public Map <String, Food> foods = new TreeMap<String, Food>();{
		foods.put( "Steak", new Food( "Steak", 8000, stock));
		foods.put( "Chicken", new Food( "Chicken", 7000, stock));
		foods.put( "Pizza", new Food( "Pizza", 4000, stock));
		foods.put( "Salad", new Food( "Salad", 3000, stock));
	}
	


	public CookRole(String name, RevolvingStand<RevolvingStandOrder> stand, RestaurantParker rest) {
		super();
		this.name = name;
		this.revolvingStand = stand;
		this.restaurant = rest;
		
		for (int i=0; i<numTables; i++){
			grills.add(false);
			plates.add(false);
		}
		
	
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getOrders() {
		return orders;
	}


	// Messages

	public void msgHereIsAnOrder(String choice, int tableNumber, CustomerRole cust, WaiterRole w){
		print ("I got an order.");
		Order o = new Order(choice, tableNumber, cust, w);
		orders.add(o);
		p.msgStateChanged();
		
	}
	
	public void msgGotPlate(int position){
		takenPlates.add(position);
		p.msgStateChanged();
	}
	
	public void msgOrderFulfillment(String choice, int quantityOrdered, int quantityReceived){
		
		cookOrders.add(new CookOrder(choice, quantityOrdered, quantityReceived));
		p.msgStateChanged();
		
	}
	
	public void msgDepleteInventory(){
		timeToCheck = true;
		p.msgStateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
		if (!takenPlates.isEmpty()){
			removePlate(takenPlates.get(0));
			return true;
		}
	
		synchronized (orders){
			for (Order order : orders){
				if (order.state == OrderState.Ordered){
					cookOrder(order);
					return true;
				}
			}
		}

		synchronized (orders){
			for (Order order : orders){
				if (order.state == OrderState.Cooked){
					AlertWaiter(order);
					return true;
				}
			}
		}
		
		if (!cookOrders.isEmpty()){
			addToStock(cookOrders.get(0));
			return true;
		}
	
		if (timeToCheck){
			checkInventories();
			return true;
		}
		
		if (checkStand){
			checkRevolvingStand();
			return true;
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void removePlate(Integer position){
		print("Plate at position " + position.intValue() + " was taken.");
		cookGui.unDrawPlate(position.intValue());
		plates.set(position.intValue(), false);
		takenPlates.remove(position);
	}
	
	private void cookOrder( final Order order){
		
		Food f = foods.get(order.choice);
		if (f.amount == 0){
			print("Out of " + order.choice);
			//msg to Waiter
			List<String> shortages= outOfInventory();
			order.waiter.msgOrderNotAvailable( order.customer, shortages);
			orders.remove(order);
			
			if (f.amountOrdered<= threshold){
				checkInventory(order.choice);
			}
			return;
		}
		
		
		
		order.state = OrderState.Cooking;
		print("Cooking a "+ order.choice);

		//draw grill
		synchronized(grills){
			for (int i =0; i < grills.size(); i++){
				if (grills.get(i).booleanValue() == false){
					order.grill = i;
					grills.set(i,true);
					break;
				}
			}
		}
	
		 cookGui.drawGrill(order.grill);
		

		f.amount --;
		print(f.amount + " "+ f.choice + " remaining");

		if ((f.amount + f.amountOrdered) <= threshold){
			//send msg to Market
			checkInventory(order.choice);
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				order.state = OrderState.Cooked;
				p.msgStateChanged();
				
			
			}
		},
		foods.get(order.choice).cookTime);//how long to wait before running task
		
	}
	

	private List<String> outOfInventory(){
		List<String> shortages= new ArrayList<String>();
		
		for (Map.Entry<String, Food> entry : foods.entrySet()){
			if (entry.getValue().amount == 0){
				shortages.add(entry.getValue().choice);
			}
		}
		
		return shortages;
	}
	private void AlertWaiter(Order order){
		
		//undraw the grill
		cookGui.unDrawGrill(order.grill);
		grills.set(order.grill,false);
		
		
		orders.remove(order);
		DoAlertWaiter(order);
		
		//draw to plating area
		synchronized (plates){
			for (int i =0; i < plates.size(); i++){
				if (plates.get(i).booleanValue() == false){
					order.plate = i;
					plates.set(i,true);
					break;
				}
			}
		}
		 cookGui.drawPlate(order.plate, order.choice);
		
		
		order.waiter.msgOrderIsReady(order.choice, order.tableNumber, order.customer, order.waiter, order.plate);
		
	}

	private void DoAlertWaiter(Order order){
		print ("Telling " + order.waiter.getName() + " that " + order.choice + " is finished cooking.");
	}

	private void checkInventories(){
		Food steak = foods.get("Steak");
		Food chicken = foods.get("Chicken");
		Food pizza = foods.get("Pizza");
		Food salad = foods.get("Salad");
		
		steak.amount = 0;
		chicken.amount = 0;
		pizza.amount = 0;
		salad.amount = 0;
		
		checkInventory("Steak");
		checkInventory("Chicken");
		checkInventory("Pizza");
		checkInventory("Salad");
		
		timeToCheck = false;
	}
	
	private void checkInventory(String choice){

		 Food toOrder = foods.get(choice);
		 int orderAmount = capacity - toOrder.amount;
		 toOrder.amountOrdered = orderAmount;
		 
		 //check market!!!!!
//		 if (toOrder.marketIndex != markets.size()){
//			 print("Ordering "+ orderAmount + " " + choice + " from " + markets.get(toOrder.marketIndex).getName());
//			 markets.get(toOrder.marketIndex).msgRefillStock(toOrder.choice, orderAmount);
//		 }
		 
//		 else {  //if have checked all markets
			 print("No markets have any of " + choice + " left!");
			 toOrder.amountOrdered = 0;
//		 }
	}
	
	private void addToStock(CookOrder marketOrder){
		
		Food receivedFood = foods.get(marketOrder.choice);
		
		if (marketOrder.amountOrdered == marketOrder.amountReceived){
			print("I received all " + marketOrder.amountReceived + " " + marketOrder.choice + " that I ordered.");
			receivedFood.amount = receivedFood.amount + marketOrder.amountReceived;
			print ("I now have " + receivedFood.amount + " " + receivedFood.choice);
			receivedFood.amountOrdered = 0;
		}
		
		else {
			print("I only received " + marketOrder.amountReceived + " of " + marketOrder.amountOrdered + " " + marketOrder.choice + " ordered.");
			receivedFood.amount = receivedFood.amount + marketOrder.amountReceived;
			print ("I now have " + receivedFood.amount + " " + receivedFood.choice);
			receivedFood.amountOrdered = 0;
			
			//now to order from next market
			receivedFood.marketIndex ++;
			checkInventory(marketOrder.choice);
		}
		

		
		cookOrders.remove(marketOrder);
	}
	
	
	private void checkRevolvingStand(){
		print("Checking the revolving stand.");
		if (!revolvingStand.isEmpty()){
			print("Found an order.");
			RevolvingStandOrder o = revolvingStand.remove();
			orders.add(new Order(o.choice, o.tableNumber, o.customer, o.waiter));
			
		}
		
		else{
			checkStand= false;
			timer.schedule(new TimerTask(){
				public void run(){
					checkStand = true;
					p.msgStateChanged();
				}				
			}, 5000);		//Wake up every 5 seconds to check the stand
		}
	}
	
	// Utilities
	
//	public void addMarket(MarketAgent market){
//		markets.add(market);
//	}
	
	public void setGui(CookGui gui){
		cookGui = gui;
	}

	@Override
	public void msgHereIsDelivery(MarketInvoice order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCannotFulfillOrder(Market m,
			Map<String, Integer> unfulfillable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}

	public void changeShifts(Person p) {
		if (this.p!=null){
			this.p.msgThisRoleDone(this);
		}
		
		this.p = p;
		this.name = p.getName();
		
	}

	@Override
	public void depleteInventory() {
		foods.put( "Steak", new Food( "Steak", 8000, 0));
		foods.put( "Chicken", new Food( "Chicken", 7000, 0));
		foods.put( "Pizza", new Food( "Pizza", 4000, 0));
		foods.put( "Salad", new Food( "Salad", 3000, 0));
		
	}
	
	public void decreaseInventory(String choice){
		foods.get(choice).amount--;
		
		Do(choice + " is now at " + foods.get(choice).amount);
		
		if (p!=null)
			p.msgStateChanged();
	}
	
	public void increaseInventory(String choice){
		foods.get(choice).amount++;
		
		Do(choice + " is now at " + foods.get(choice).amount);
		
		if (p!=null)
			p.msgStateChanged();
	}
	
	protected void print(String message){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_PARKER, name, message, restaurant.cityRestaurant.ID);
	}

}