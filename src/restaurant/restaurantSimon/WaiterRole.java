package restaurant.restaurantSimon;

import interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import restaurant.restaurantSimon.CookRole.OrderState;
import restaurant.restaurantSimon.HostRole.Table;
import restaurant.restaurantSimon.gui.WaiterGui;
import restaurant.restaurantSimon.interfaces.Customer;
import restaurant.restaurantSimon.interfaces.Waiter;
import role.Role;
import UnitTests.mock.LoggedEvent;
import agent.Agent;
import agent.StringUtil;


public class WaiterRole extends Role implements Waiter { 



	public enum MyCustomerState{noAction,waiting,waitingToOrder,orderPending, foodIsReady,eating,outOfFood,checkReceived,leaving};

	//inner class MyCustomer
	class MyCustomer{
		CustomerRole c;
		Table table;//hack should be int table
		String chioce=null;
		double amount=0;
		boolean checkGiven=false;
		MyCustomerState s=MyCustomerState.waiting;

		//Constructor
		public MyCustomer(CustomerRole customer, Table tab){
			c=customer;
			table=tab;
		}
	}


	protected class Dish{
		String type;
		double price;
		public Dish(String t, double p){
			type=t;
			price=p;
		}
	}
	//data
	RestaurantSimon restaurant;
	Person self;
	private String name;
	private HostRole host;
	private CookRole cook;
	private CashierRole cashier;
	public WaiterGui waiterGui = null;
	private List<MyCustomer> customers=new ArrayList<MyCustomer>();
	private List<Dish> menu=new ArrayList<Dish>();
	Timer timer=new Timer();
	private boolean onBreak=false;
	boolean AtTable=false;
	//private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private boolean wantBreak=false;
	private Semaphore customerReady = new Semaphore(0,true);

	private int waiterNum=0;

	public WaiterRole(Person p,String n, HostRole h, CookRole co, CashierRole ca,RestaurantSimon r){

		self=p;
		this.name=n+" Waiter";
		host=h;
		cook=co;
		cashier=ca;
		restaurant=r;
		Do(n);
	


		menu.add(new Dish("Salad",5.99));
		menu.add(new Dish("Pizza",8.99));
		menu.add(new Dish("Chicken",10.99));
		menu.add(new Dish("Steak",15.99));

	}




	//hack to establish connection with host and cook
	public void setHost(HostRole h)
	{
		host=h;
	}
	public void setCook(CookRole co){
		cook=co;
	}
	public void setCashier(CashierRole ca){
		cashier=ca;
	}


	public void goToHost(HostRole h){
		//Do("Going to Host");
		h.msgAddWaiter(this);//send our instance, so he can respond to us
	}

	//hack to communicate with animation
	public boolean isOnBreak(){
		return onBreak;
	}
	//msg

	public void msgYourWaiterNumber(int num){
		if(waiterNum!=num){
			waiterNum=num;
			self.msgStateChanged();
		}

		//waiterGui.DoGotoWait(waiterNum);
		//Do("number is now" + num);

	}

	public void msgCustomerReadyToBeSeated(){
		if(customerReady.availablePermits()==0){
			customerReady.release();
		}
	}
	public void msgIAmLeaving(CustomerRole cus){
		for(MyCustomer mc : customers){
			if (mc.c==cus){
				mc.s=MyCustomerState.leaving;

			}
		}
		self.msgStateChanged();
	}


	public void msgCheckIsReady(Customer cus, double amount){
		for(MyCustomer mc : customers){
			if(mc.c==cus){
				mc.amount=amount;
				mc.s=MyCustomerState.checkReceived;
			}
		}
		self.msgStateChanged();
	}

	public void OnBreak(){
		wantBreak=false;
		onBreak=true;
		Do("I am on Break");
		timer.schedule(new TimerTask() {
			public void run() {
				print("I am back from break");

				onBreak=false;
				self.msgStateChanged();

			}
		},
		(long)(10000));//getHungerLevel() * 1000);//how long to wait before running task
		self.msgStateChanged();
	}

	public void IWantBreak(){
		wantBreak=true;
		self.msgStateChanged();

	}


	public void msgSitAtTable(CustomerRole customer, Table table){

		customers.add(new MyCustomer(customer,table));

		self.msgStateChanged();


	}

	public void msgImReadyToOrder(CustomerRole c)
	{
		Do("I see you are ready to order");
		for(MyCustomer mc : customers){
			if(mc.c==c){
				mc.s=MyCustomerState.waitingToOrder;
			}
		}
		self.msgStateChanged();
	}

	public void msgHereIsMyOrder(CustomerRole c, String ch){
		for(MyCustomer mc : customers){
			if(mc.c==c){
				mc.s=MyCustomerState.orderPending;
				mc.chioce=ch;
			}
		}
		self.msgStateChanged();

	}

	public void msgOrderIsReady(int table, String choice)
	{
		Do("A order is ready");
		for(MyCustomer mc : customers){
			if(mc.table.tableNumber==table){
				mc.s=MyCustomerState.foodIsReady;
			}
		}
		self.msgStateChanged();
	}


	public void msgAtCook(){

		atCook.release();
		self.msgStateChanged();
	}




	public void msgOutOf(String choice, int table){
		for(MyCustomer mc : customers){
			if(mc.table.tableNumber==table){
				mc.s=MyCustomerState.outOfFood;
			}
		}
		self.msgStateChanged();

	}


	//scheduler

	@Override
	public boolean pickAndExecuteAnAction() {
		try{
			if(wantBreak){
				host.msgOnBreak(this);
				return true;
			}


			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.foodIsReady){
					Do("Give "+mc.chioce +" to "+ mc.c);
					giveFoodToCustomer(mc);
					return true;
				}
			}

			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.waiting){
					SeatCustomer(mc);
					return true;
				}
			}


			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.waitingToOrder){
					takeOrder(mc);
					return true;
				}
			}
			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.orderPending){
					giveOrderToCook(mc);
					return true;
				}
			}


			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.outOfFood){
					pleaseReThink(mc);
					return true;
				}
			}

			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.checkReceived){
					sendCheckToCustomer(mc);
					return true;
				}
			}

			for(MyCustomer mc : customers){
				if(mc.s==MyCustomerState.leaving){
					cleanTable(mc);
					return true;
				}
			}

			/*
		for(MyCustomer mc : customers){
			if(mc.s==MyCustomerState){

				return true;
			}
		}
			 */
			if(!onBreak)
			{
				Do("telling host I am available");
				host.msgImAvailable(this);
				waiterGui.DoGotoWait(waiterNum);

			}
			else{
				waiterGui.DoLeaveCustomer();
			}


			return false;
		}
		catch(ConcurrentModificationException e){
			//System.err.println("CONCURRENT MODIFICATION EXCEPTION IN WAITER");
			return true;

		}
	}




	//actions
	private void cleanTable(MyCustomer mc){
		Do("cleaning table "+mc.table.tableNumber);
		mc.s=MyCustomerState.noAction;
		waiterGui.DoGoToTable(mc.c, mc.table.tableX, mc.table.tableY );
		host.msgLeavingTable(mc.c);
		customers.remove(mc);
	}

	private void sendCheckToCustomer(MyCustomer mc){
		waiterGui.DoGoToTable(mc.c, mc.table.tableX, mc.table.tableY );
		mc.c.msgYourCheck(mc.amount);
		mc.s=MyCustomerState.noAction;




	}

	private void pleaseReThink(MyCustomer mc){
		waiterGui.DoGoToTable(mc.c, mc.table.tableX, mc.table.tableY);
		mc.s =MyCustomerState.noAction;
		List<Dish> newMenu=new ArrayList<Dish>();
		for(Dish d : menu){
			if(d.type!=mc.chioce){
				newMenu.add(d);
			}
		}
		mc.c.msgPleaseReOrder(newMenu);
		//mc.c.msgAnimationFinishedGoToSeat();

	}

	private void giveFoodToCustomer(MyCustomer mc){
		mc.s=MyCustomerState.noAction;
		mc.s=MyCustomerState.noAction;
		mc.s=MyCustomerState.noAction;
		//go to cook to get food
		waiterGui.DoWalkingToCook();
		Do("im at cook");
		cook.msgGiveMeFood(this);
		//bring food to customer
		waiterGui.setWithFood(mc.chioce);
		waiterGui.DoGoToTable(mc.c, mc.table.tableX, mc.table.tableY);

		waiterGui.setWithFood(null);
		mc.c.msgHereIsYourFood();
		Do("food delivered");

		//send check to cashier
		cashier.msgHereIsARequest(this, mc.chioce, mc.c);
	}



	private void SeatCustomer(MyCustomer mc) {
		Do("Seating " + mc.c + " at " + mc.table);
		waiterGui.DoGoToLobby();
		host.msgIGotCustomer(mc.c);
		mc.c.msgFollowMeToTable(this, mc.table.getTableNum(), menu);
		try {
			customerReady.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		waiterGui.DoGoToTable(mc.c,  mc.table.tableX, mc.table.tableY);
		mc.s=MyCustomerState.noAction;


	}




	private void giveOrderToCook(MyCustomer mc){
		mc.s=MyCustomerState.noAction;
		cook.msgHereIsAnOrder(this,mc.chioce, mc.table.tableNumber);
		Do("Order placed");


	}

	private void takeOrder(MyCustomer mc){
		mc.s =MyCustomerState.noAction;
		waiterGui.DoGoToTable(mc.c, mc.table.tableX, mc.table.tableY);

		mc.c.msgWhatWouldYouLike();
	}

	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	
	public String getName(){
		return name;
	}
	public String toString() {
		return "waiter " + getName();
	}




	@Override
	public boolean canLeave() {
		return onBreak;
	}
	public void Do(String message){
		
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);
		
	}
}
