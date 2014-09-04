package restaurant.restaurantParker;

import role.Role;
import agent.Agent;
import restaurant.restaurantParker.MyCustomer.MyCustomerState;
import restaurant.restaurantParker.MyWaiter.MyWaiterState;
import restaurant.restaurantParker.gui.WaiterGui;
import restaurant.restaurantParker.gui.CookGui;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import interfaces.restaurantParker.*;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class WaiterRole extends Role implements Waiter{
	
	//lab3
//	public Boolean isReady;
	

	public RestaurantParker restaurant;

	public List<MyCustomer> myCustomers
	= new ArrayList<MyCustomer>();
	
	public List<Order> cookedOrders
	= new ArrayList<Order>();

	private List<String> shortages = new ArrayList<String>();
	private Menu menu = null;
	
	private HostRole host = null;
	protected CookRole cook = null;
	private CashierRole cashier = null;
	

	
	protected String name;
	private Semaphore atDestination = new Semaphore(0,true);

	public WaiterGui waiterGui = null;
	
	enum MyState {passive, wantBreak, breakRefused, onBreak, leavingBreak};
	MyState state = MyState.passive;
	

	public WaiterRole(String name) {
		super();

		menu = new Menu();
	//	this.isReady = false;
		this.name = name;
		
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getMyCustomers() {
		return myCustomers;
	}


	// Messages
	
	public void gotWantBreak(){   //from animation
		print("I feel like going on break.");
		state = MyState.wantBreak;
		p.msgStateChanged();
	}
	
	public void gotOffBreak(){  //from animation
		print("My break is over");
		state=MyState.leavingBreak;
		p.msgStateChanged();
	}

	public void msgBreakRequestReply(boolean reply){
		if (reply == false){
			state = MyState.breakRefused;
			p.msgStateChanged();
			return;
		}
	//otherwise.....
		 state = MyState.onBreak;
		 p.msgStateChanged();
	}
	
	public void msgSeatCustomer(CustomerRole cust, int tableNumber, int lobbyPosition) {
	
		print("Going to seat "+ cust + " at Table " + tableNumber);
		MyCustomer newCustomer = new MyCustomer(tableNumber, cust, lobbyPosition);
		newCustomer.state = MyCustomerState.waiting;
		myCustomers.add(newCustomer);
		p.msgStateChanged();
	}

	public void msgImReadyToOrder(CustomerRole cust){
		
		for (int i=0; i< myCustomers.size(); i++) {
			if (myCustomers.get(i).customer == cust){
				myCustomers.get(i).state = MyCustomerState.readyToOrder;
				p.msgStateChanged();
			}
		}
		
	}
	
	public void msgHereIsMyChoice(String choice, CustomerRole cust){

		for (int i=0; i< myCustomers.size(); i++) {
			if (myCustomers.get(i).customer == cust){
				myCustomers.get(i).choice = choice;
				myCustomers.get(i).state = MyCustomerState.ordered;
				p.msgStateChanged();
			}
		}
	}
	
	public void msgOrderNotAvailable( CustomerRole cust, List<String> shortages){
		for (int i=0; i< myCustomers.size(); i++) {
			if (myCustomers.get(i).customer == cust){
				myCustomers.get(i).state = MyCustomerState.refused;
				this.shortages = shortages;
				p.msgStateChanged();
			}
		}
	}
	
	public void msgOrderIsReady(String choice, int tableNumber, CustomerRole customer, WaiterRole waiter, int platePosition){
		Order o = new Order(choice, tableNumber, customer, waiter);
		o.plate = platePosition;
		cookedOrders.add(o);
		p.msgStateChanged();
	}
	
	public void msgImDone(String choice, CustomerRole cust){
		for (int i=0; i< myCustomers.size(); i++) {
			if (myCustomers.get(i).customer == cust){
				myCustomers.get(i).state = MyCustomerState.wantCheck;
				p.msgStateChanged();
			}
		}
	}
	
	public void msgHereIsCheck(int check, Customer cust){
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).customer == cust){
				myCustomers.get(i).state = MyCustomerState.waitingCheck;
				myCustomers.get(i).billTotal = check;
				p.msgStateChanged();
			}
		}
	}
	
	public void msgLeavingTable(CustomerRole cust) {

		for (int i=0; i< myCustomers.size(); i++) {
			if (myCustomers.get(i).customer == cust){
				myCustomers.get(i).state = MyCustomerState.leaving;
				p.msgStateChanged();
			}
		}
			

		
	}

	public void msgAtDestination() {//from animation
	
		atDestination.release();// = true;
		p.msgStateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		try{
			if (state == MyState.wantBreak){
				askHostForBreak();
				return true;
			}

			if (state == MyState.breakRefused){
				noBreak();
				return true;
			}

			if (state == MyState.leavingBreak){
				offBreak();
				return true;
			}


			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.waiting)
				{
					seatCustomer(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}

			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.readyToOrder){
					takeOrder(mc);
					return true;
				}

			}

			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.ordered){
					placeOrder(mc);
					return true;
				}

			}

			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.refused){
					reportFoodShortage(mc);
					return true;
				}
			}

			if (!cookedOrders.isEmpty()){
				serveOrder(cookedOrders.get(0));
				return true;
			}

			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.wantCheck) {
					getCheck(mc);
					return true;
				}
			}

			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.waitingCheck){
					deliverCheck(mc);
					return true;
				}
			}

			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.leaving) {
					releaseCustomer(mc);
					return true;
				}
			}


			if (state == MyState.onBreak){
				goOnBreak();
				return true;
			}
		}
		catch(ConcurrentModificationException e){
			e.printStackTrace();
			return true;
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void askHostForBreak(){
		host.msgWantToGoOnBreak(this);
	//	waiterGui.DoAskForBreak();  //////setting the checkbox disabled causes all sorts of problems
		
		state = MyState.passive;
	}
	
	private void noBreak(){
		waiterGui.CantGoOnBreak();
		state = MyState.passive;
	}
	
	private void goOnBreak(){
		if (myCustomers.size() == 0){
			print("My break is starting!");
			waiterGui.DoGoOnBreak();
			state = MyState.passive;
		}
	}
	
	private void offBreak(){
		waiterGui.DoLeaveCustomer();
		host.msgImOffBreak(this);
		state = MyState.passive;
	}
	private void seatCustomer(MyCustomer mc) {
		
	waiterGui.DoGoToLobbyPosition(mc.lobbyPosition);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		mc.customer.msgSitAtTable(mc.tableNumber, menu, this);
		host.msgLobbySeatFree(mc.lobbyPosition);
		DoSeatCustomer(mc);
		
		
		mc.state = MyCustomerState.seated;
	
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(MyCustomer mc) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + mc.customer + " at Table " + mc.tableNumber);
		waiterGui.DoBringToTable(mc.customer, mc.tableNumber); 
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		waiterGui.DoLeaveCustomer();

	}

	private void takeOrder(MyCustomer mc){
		
		DoTakeOrder(mc);
	
		mc.state = MyCustomerState.asked;
		mc.customer.msgWhatWouldYouLike();
		
	}
	
	private void DoTakeOrder(MyCustomer mc){
		//make gui move to table
		waiterGui.DoGoToTable(mc.tableNumber);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print(mc.customer.getName() + ", what would you like?");
		waiterGui.DoLeaveCustomer();
		
	}
	
	abstract void placeOrder(MyCustomer mc);
	
	protected void DoPlaceOrder(MyCustomer mc){
	
		waiterGui.DoGoToKitchen();
		print ("Taking " + mc.customer.getName() + "'s order to the cook.");
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		waiterGui.DoLeaveCustomer();
		
	}
	
	private void reportFoodShortage(MyCustomer mc){
		DoReportFoodShortage(mc);
		
		mc.state = MyCustomerState.asked;
		
		Menu newMenu = new Menu(shortages);
	
		mc.customer.msgOutOfFood(newMenu);
		
		
	}
	
	private void DoReportFoodShortage(MyCustomer mc){
		waiterGui.DoGoToTable(mc.tableNumber);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (shortages.size() == menu.menu.size()){
			print ("Sorry "+mc.customer.getName() + ", we are completely out of food!" );
		}
		else print("Sorry "+mc.customer.getName() + ", we are currently out of " + mc.choice + ". Make another choice?");
		
		
		waiterGui.DoLeaveCustomer();
	}
	
	private void serveOrder(Order order){
		
		DoServeOrder(order);
		
		cookedOrders.remove(order);
		order.customer.msgHereIsYourFood(order.choice);
	}
	
	private void DoServeOrder(Order order){
		
		waiterGui.DoGoToKitchen();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("Grabbing the " + order.choice);
		waiterGui.gotFood(order.choice);
		cook.msgGotPlate(order.plate);
		
		waiterGui.DoGoToTable(order.tableNumber);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("Serving " + order.choice);
		waiterGui.deliveredFood();
		
		waiterGui.DoLeaveCustomer();
	}
	
	private void getCheck(MyCustomer mc){
		print("Okay, I'll get your check");
		mc.state = MyCustomerState.askedCheck;
		
		waiterGui.DoGoToCashier();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("Cashier, can you print out a bill for " + mc.customer.getName() + " please?");
		waiterGui.DoLeaveCustomer();
		cashier.msgComputeBill(mc.choice, mc.customer, this);
	}
	
	private void deliverCheck(MyCustomer mc){
		
		waiterGui.DoGoToTable(mc.tableNumber);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mc.state = MyCustomerState.gotCheck;
		print(mc.customer.getName() + ", here is your bill.");
		waiterGui.DoLeaveCustomer();
		mc.customer.msgHereIsBill(mc.billTotal);
	}
	
	private void releaseCustomer(MyCustomer mc){
		print("Telling the host that " + mc.customer + " is leaving.");
		host.msgTableIsFree(mc.customer);
		myCustomers.remove(mc);
	}
	
	
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public void setHost(HostRole host){
		this.host = host;
	}
	
	public void setCook(CookRole cook){
		this.cook = cook;
	}
	
	public void setCashier(CashierRole cashier){
		this.cashier = cashier;
	}
	
	
	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void setRestaurant(RestaurantParker r){
		this.restaurant = r;
	}
	
	protected void print(String message){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_PARKER, name, message, restaurant.cityRestaurant.ID);
	}
	
}
