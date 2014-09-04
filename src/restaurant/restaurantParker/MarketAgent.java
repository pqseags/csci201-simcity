//package restaurant.restaurantParker;
//
//import role.Role;
//import agent.Agent;
//
//import java.util.*;
//import java.util.concurrent.Semaphore;
//import java.util.Collections;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.TreeMap;
//import java.util.Map.Entry;
//
//import parkerRest.rest.MarketOrder.MarketOrderState;
//import interfaces.parkerRest.*;
//
///**
// * Restaurant Market Agent
// */
//
//public class MarketAgent extends Role implements Market{
//	
//	private final int universalStock = 5;
//	private final int locateTime = 10000;
//	
//	Timer timer = new Timer();
//	public List<MarketOrder> orders = Collections.synchronizedList(new ArrayList<MarketOrder>());
//	
//	private CookRole cook = null;
//	private CashierRole cashier = null;
//	
//	private String name;
//	
//	boolean locating = false;
//	MarketOrder currentOrder = null;
//	
//
//	//food stock map
//	private Map <String, MarketFood> foods = new TreeMap<String, MarketFood>();{
//		foods.put( "Steak", new MarketFood( "Steak", universalStock, 20));
//		foods.put( "Chicken", new MarketFood( "Chicken", universalStock, 15));
//		foods.put( "Pizza", new MarketFood( "Pizza", universalStock, 10));
//		foods.put( "Salad", new MarketFood( "Salad", universalStock, 5));
//	}
//
//
//	public MarketAgent(String name) {
//		super();
//		this.name = name;
//		
//	}
//
//
//	public String getName() {
//		return name;
//	}
//
//
//
//	// Messages
//
//	public void msgRefillStock(String choice, int quantity){
//		
//		MarketOrder mo = new MarketOrder(choice, quantity);
//		orders.add(mo);
//		stateChanged();
//		
//	}
//	
//	public void msgBillPayment(int payment){
//		print("I got a payment from the cashier for $" + payment);
//	}
//	
//	private void msgFinishedLocatingOrder(){
//		currentOrder.state = MarketOrderState.Ready;
//		stateChanged();
//	}
//
//	/**
//	 * Scheduler.  Determine what action is called for, and do it.
//	 */
//	public boolean pickAndExecuteAnAction() {
//		
//		synchronized(orders){
//			for (MarketOrder order : orders){
//				if (order.state == MarketOrderState.Ready){
//					shipOrder(order);
//					return true;
//				}
//			}
//		}
//		
//		if (!locating){
//			synchronized(orders){
//				for (MarketOrder order : orders){
//					if (order.state == MarketOrderState.Ordered){
//						locateOrder(order);
//						return true;
//					}
//				}
//			}
//		}
//		
//
//	
//		return false;
//		//we have tried all our rules and found
//		//nothing to do. So return false to main loop of abstract agent
//		//and wait.
//	}
//
//	// Actions
//
//	
//	private void locateOrder(MarketOrder order){
//		
//		print("Locating " + order.quantity + " " + order.choice);
//		
//		locating = true;
//		currentOrder = order;
//		order.state = MarketOrderState.Locating;
//		MarketFood f = foods.get(order.choice);
//		
//		int quantityToDeliver =  0;
//		
//		if (f.stock == 0){
//			cook.msgOrderFulfillment(order.choice, order.quantity, quantityToDeliver);
//			print("This market is out of " + order.choice);
//			orders.remove(order);
//			return;
//		}
//
//		
//		//start timer
//		timer.schedule(new TimerTask() {
//			public void run() {
//				
//			locating = false;
//			msgFinishedLocatingOrder();
//			
//			}
//		},
//		locateTime);//how long to wait before running task
//		
//	}		
//		
//	
//	private void shipOrder(MarketOrder order){
//	
//		MarketFood f = foods.get(order.choice);
//		orders.remove(order);
//		
//		if (f.stock < order.quantity){
//			print("Sending cook only " + f.stock + "/" + order.quantity + " "+ order.choice);
//			print("I now have no " + order.choice + " left!");
//			cook.msgOrderFulfillment(order.choice, order.quantity, f.stock);
//			int bill = f.stock * f.price;
//			print("Sending the cashier a bill for $" + bill + ": " + f.stock + " " + f.choice + " at a price of $"+f.price + " each.");
//			cashier.msgHereIsMarketBill(bill, this);
//			
//			f.stock = 0;
//		}
//	
//		else {
//			print("Sending cook all "+ order.quantity + " " + order.choice);
//			cook.msgOrderFulfillment(order.choice, order.quantity, order.quantity);
//			f.stock = f.stock - order.quantity;
//			print("I now have only " + f.stock + " " + order.choice + " left." );
//			
//			int bill = order.quantity * f.price;
//			print("Sending the cashier a bill for $" + bill + ": " + order.quantity + " " + f.choice + " at a price of $"+f.price + " each.");
//			cashier.msgHereIsMarketBill(bill, this);
//		}
//		
//
//	}
//	
//	
//	
//	// Utilities
//	
//	public void setCook (CookRole c){
//		cook = c;
//	}
//	
//	public void setCashier (CashierRole c){
//		cashier = c;
//	}
//
//}