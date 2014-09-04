package restaurant.restaurantGabe;

import agent.Agent;
//import restaurant.CustomerAgent.CustEvent;
import restaurant.restaurantGabe.gui.WaiterGui;
//import restaurant.restaurantGabe.gui.WaiterPanel;
import restaurant.restaurantGabe.interfaces.Customer;
import restaurant.restaurantGabe.interfaces.Waiter;
import restaurant.restaurantGabe.util.*;
import role.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;


public class WaiterRole extends Role implements Waiter{
	
	
	//INITIALIZATION
	public WaiterRole(String name, PersonAgent person){
		this.name = name;
		/*menu.choices.add("Pizza");
		menu.choices.add("Steak");
		menu.choices.add("Chicken");
		menu.choices.add("Salad");*/
		this.person = person;
		
	}
	
	//GETTERS
	public String getName(){
		return name;
	}
	
	//hack for unit testing
	public boolean ordered(int index){
		return MyCustomers.get(index).s==CustState.ordered;
	}
	
	//SETTERS
	
	//this is a hack for unit testing
	public void addNewCustomer(Customer c){
		MyCustomers.add(new MyCust((CustomerRole)c,0,CustState.readyToOrder));
	}
	
	//hack, gives the waiter the restaurant pointer
	public void setRestaurant(RestaurantGabe r){
		this.restaurant = r;
		this.Cook = r.cook;
		this.Cashier = r.cashier;
		this.Host = r.host;
	}
	
	//hack, puts cashier in
	public void setCashier(CashierRole c){
		this.Cashier = c;
	}
	
	//set break boolean
	public void setWantsBreak(boolean wants){
		//wantsBreak = wants;
		if(wants){
			//this.askedForBreak = false;
			//this.CanGoOnBreak = false;
			state = MyState.wantsBreak;
		}
		
		person.msgStateChanged();
		
	}
	
//	//set waiterPanel
//	public void setWaiterPanel(WaiterPanel wp){
//		this.waiterPanel = wp;
//	}
	
	//set waiter gui
	public void setGui(WaiterGui wGui){
		waiterGui = wGui;
		waiterGui.DoGoToHome();
	}
	
	//set the waiter's host pointer
	public void setHost(HostRole h){
		Host = h;
	}
	
	//set the waiter's cook pointer
	public void setCook(CookRole c){
		Cook = c;
		/*if(name.equals("Scenario2")){
			Cook.setNoFoodsMarketInvSpeed(1000,5);
		}
		if(name.equals("NN-Scenario1")){
			Cook.setNoFoodsMarketInvSpeed(1000,40);
		}
		if(name.equals("NN-Scenario2")){
			Cook.setNoFoodsMarketInvSpeed(0,5);
		}*/
	}
	
	//USEFUL METHODS
	//returns the first instance of MyCust in list with customerAgent equal to c
	protected MyCust findCust(List<MyCust> Custs,Customer c){
		
		for(MyCust mc : Custs){
			if(mc.c==c){
				return mc;
			}
		}
		return null;
	}
	
	//allows us to print
	public String toString(){
		return "waiter"+getName();
	}
		
	
	//DATA
	
	//tells whether the host will keep assigning customers to this waiter
	public boolean inWaiterList = true;
	
	public PersonAgent person;
	
	protected String name;
	
	protected Menu menu = new Menu();
	protected Semaphore atTable = new Semaphore(0,true);
	protected Semaphore atCook = new Semaphore(0,true);
	
	public WaiterGui waiterGui = null;
//	protected WaiterPanel waiterPanel = null;
	
	//really should be called AskToGoOnBreak, but will keep it this way for consistency with class
	/*private boolean CanGoOnBreak = false;
	private boolean OnBreak = false;
	private boolean askedForBreak = false;
	private boolean wantsBreak = false;
	private boolean doneWithWork = false;
	private boolean breakOver = false;*/
	
	protected enum MyState {working,wantsBreak,askedForBreak,canGoOnBreak,onBreak};
	protected enum MyEvent {none,deniedBreak,breakOver};
	protected MyState state = MyState.working;
	protected MyEvent event = MyEvent.none;
	
	
	//private Customer variable containing all waiter-relevant information
	private enum CustState {waiting,seated,readyToOrder,needsReorder,ordered,served,needsCheck,fulfilled,leaving,left};
	public class MyCust{
		public MyCust(CustomerRole c,int loc,CustState s){
			this.c = c;
			this.loc = loc;
			this.s = s;
		}
		public CustomerRole c;
		public String choice;
		public int loc;
		public CustState s;
		public Check check;
	}
	
	//List of all customers being handled by waiter
	public List<MyCust> MyCustomers = new ArrayList<MyCust>();
	
	//will keep waiter at table while customer is ordering
	public Semaphore waitingForOrder = new Semaphore(0,true);
	protected Semaphore waitingForHomePosition = new Semaphore(0,true);
	
	//will hold waiter's list of orders
	public List<Order> Orders = new ArrayList<Order>();
	
	//hack - will give waiter a pointer to the restaurant's host and cook
	public CookRole Cook;
	protected HostRole Host;
	protected CashierRole Cashier;
	
	RestaurantGabe restaurant;
	
	
	//MESSAGES
	
	public void msgAtHomePosition(){
		waitingForHomePosition.release();
	}
	
	public void msgHereIsCheck(Check check){
		//Do("Got "+ check.c.getName()+ "'s check");
		MyCust mc = findCust(MyCustomers,check.c);
		mc.check = check;
		mc.s = CustState.needsCheck;
		person.msgStateChanged();
	}
	
	//gui tells waiter that break is over
	public void msgBreakOver(){
		event = MyEvent.breakOver;
		//Do("Gui just told me to get off break");
		person.msgStateChanged();
	}
	
	//host says waiter can go on break;
	public void msgBreakAnswer(boolean ans){
		if(ans){
			/*CanGoOnBreak = true;
			this.wantsBreak = false;
			askedForBreak = true;*/
			state = MyState.canGoOnBreak;
			person.msgStateChanged();
			return;
		}
		event = MyEvent.deniedBreak;
		//waiterPanel.resetWaiter(this);
		/*this.CanGoOnBreak = false;
		this.wantsBreak = false;
		this.askedForBreak = false;*/
		person.msgStateChanged();
	}
	
	
	//seat a customer at the designated table
	public void msgSitAtTable(CustomerRole c,int table){
		//Do("Got the message to seat");
		MyCust mc = findCust(MyCustomers,c);
		if(mc==null){
			//System.out.println(name+" has a new waiting customer " + c.getName());
			MyCustomers.add(new MyCust(c,table,CustState.waiting));
		}
		else if(mc.s == CustState.left){
			mc.s = CustState.waiting;
			mc.loc = table;
		}
		person.msgStateChanged();
	}
	
	//GUI message that waiter/cust is at table
	public void msgAtTable(){
		atTable.release();
		//person.msgStateChanged();
	};
	
	//customer indicates that he's ready to order
	public void msgImReadyToOrder(CustomerRole c){
		print("Got it");
		MyCust mc = findCust(MyCustomers,c);
		mc.s = CustState.readyToOrder;
		person.msgStateChanged();
	}
	
	//customer is ordering
	public void msgHereIsMyChoice(Customer customer,String choice){
		MyCust mc = findCust(MyCustomers, customer);
		mc.choice = choice;
		mc.s = CustState.ordered;
		Orders.add(new Order(mc.c,mc.choice,OrderState.ordered,this));
		waitingForOrder.release();
		//Do("Got your order");
		person.msgStateChanged();
	}
	
	//tells the waiter that he can ask the cook for order now
	public void msgAtCook(){
		atCook.release();
	}
	
	public void msgOutOfFood(Order o){
		/*Menu.food outFood = null;
		//List<Menu.food> menuCopy = Menu.foods; 
		for(Menu.food checkFood:Menu.foods){
			if(checkFood.choice == o.choice){
				outFood = checkFood;
				break;
			}
		}
		menu.foods.remove(outFood);*/
		MyCust mc = findCust(MyCustomers,o.c);
		mc.s = CustState.needsReorder;
		person.msgStateChanged();
	}
	
	//cook tells waiter order is ready - order state is changed by cook
	public void msgOrderIsReady(Order o){
		person.msgStateChanged();
	}
	
	//customer is leaving - host will need to know eventually
	public void msgDoneEatingAndLeaving(CustomerRole c){
		MyCust mc = findCust(MyCustomers,c);
		mc.s = CustState.leaving;
		person.msgStateChanged();
	}
	
	//food is to expensive
	public void msgFoodTooExpensiveLeaving(CustomerRole c){
		MyCust mc = findCust(MyCustomers,c);
		mc.s = CustState.leaving;
		person.msgStateChanged();
	}
	
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
		//System.out.println(name+ " the waiter deciding what to do");
		/*for(MyCust mc:MyCustomers){
			Do(mc.c.getName() + mc.s);
		}*/
		//when break ends, tell host
		try{
			if(state == MyState.onBreak && event == MyEvent.breakOver){
				getBackToWork();
				return true;
			}
			
			if(state == MyState.askedForBreak && event == MyEvent.deniedBreak){
				//Do("Darn");
				state = MyState.working;
				event = MyEvent.none;
				//waiterPanel.resetWaiter(this);
				return true;
			}
			
			//if waiter wants a break, tell host
			if(state == MyState.wantsBreak){
				DoMessage("Can I get a break?");
				state = state.askedForBreak;
				Host.msgIWantABreak(this);
				return true;
			}
			
	
			//if a cust needs check, give it
			for(MyCust mc:MyCustomers){
				if(mc.s==CustState.needsCheck){
					//Do(mc.c.getName());
					giveCheck(mc);
					return true;
				}
			}
			
			//if we need to give a customer a menu, give it
			for(MyCust mc:MyCustomers){
				if(mc.s==CustState.needsReorder){
					reorderCustomer(mc);
					
					return true;
				}
			}
			
			//if MyCustomers has cust that is leaving
			for(MyCust mc:MyCustomers){
				if(mc.s==CustState.leaving){
					//FreeTable()
					freeTable(mc);
					return true;
				}
			}
	
			//if Orders has a cooked order
			for(Order o:Orders){
				if(o.s==OrderState.available){
					//ServeOrder()
					serveOrder(o);
					return true;
				}
			}
	
			//if Orders an open order
			for(Order o:Orders){
				if(o.s==OrderState.ordered){
					//GiveOrderToCook()
					giveOrderToCook(o);
					return true;
				}
			}
			
			//if MyCustomers has a waiting cust
			for(MyCust mc:MyCustomers){
				if(mc.s == CustState.waiting){
					//SeatCustomer()
					//System.out.println("Decided to seat cust "+mc.c.getName());
					seatCustomer(mc);
					return true;
				}
			}
			
			//if MyCustomers has a cust ready to order
			for(MyCust mc:MyCustomers){
				if(mc.s==CustState.readyToOrder){
					//AskForOrder()
					askForOrder(mc);
					return true;
				}
			}
			
			
			//If all customers have left, waiter can go on break
			boolean doneWithWork = true;
			for(MyCust mc: MyCustomers){
				if(mc.s != CustState.left){
					doneWithWork = false;
				}
			}
			if(state == MyState.canGoOnBreak && doneWithWork){
				state = MyState.onBreak;
				DoBreak();
			}
		}
		catch(ConcurrentModificationException e){
			return true;
		}
			

		
		return false;
		
	}
	
	
	//ACTIONS
	
	protected void giveCheck(MyCust mc){
		
		mc.c.msgHereIsCheck(mc.check);
		mc.s = CustState.fulfilled;
	}
	
	//seats customer at the table in MyCust variable
	protected void seatCustomer(MyCust mc){
		//Animation call
		//Do("I've gotta seat "+mc.c.getName());
		
		DoSeatCustomer(mc.c,mc.loc);
		
		//HACK. Sets waiter to want break if customer name is break
		if(mc.c.getName().equals("Break")){
			/*askedForBreak = false;
			this.wantsBreak = true;*/
			state = MyState.wantsBreak;
		}
		
		//Message to customer to change state
		mc.c.msgFollowMeToTable(this, menu);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mc.s = CustState.seated;
		waiterGui.DoLeaveCustomer();
	}
	
	//tells customer that waiter is ready to recieve order
	protected void askForOrder(MyCust mc){
		DoAskForOrder(mc);
		
		//waiter waits until he gets to table to ask for order
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//sends message that waiter is ready to recieve order
		//Do("What would you like?");
		mc.c.msgWhatWouldYouLike();
		
		
		//waiter waits for order to leave table
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Do("We'll have that to you right away");
		waiterGui.DoLeaveCustomer();
	}
	
	protected void reorderCustomer(MyCust mc){
		DoGoToTableAndWait(mc.loc);
		DoMessage("So sorry, we're out of "+mc.choice);
		mc.c.msgOutOfChoice(menu);
		mc.s = CustState.seated;
		waiterGui.DoLeaveCustomer();
	}
	
	//gives the order variable to the cook - contains Customer pointer
	protected void giveOrderToCook(Order o){
		if(waiterGui==null){
			Cook.msgHereIsAnOrder(this, o);
			return;
		}
		
		DoGiveOrderToCook();
		
		//forces waiter to wait until he gets to the cook to ask for food
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Do("Giving order to cook");
		//The cook will change the order state
		//o.s = OrderState.requested;
		Cook.msgHereIsAnOrder(this,o);
		DoGoToHome();
	}
	
	//gives the customer his order
	private void serveOrder(Order o){
		MyCust mc = findCust(MyCustomers,o.c);
		
		DoGetOrder(o);
		
		//will force waiter to go to cook to get the order
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cook.msgGotFood(o.choice);
		DoServeOrder(mc);
		DoMessage("Got "+mc.c.getName()+"'s "+o.choice);
		waiterGui.setFood(o.choice.substring(0,2));
		//once waiter gets to cook, he can pick up food
		o.s = OrderState.inTransit;
		
		//waiter waits until he's at the table to serve
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		o.s = OrderState.served;
		mc.s = CustState.served;
		//Do("Here's your food");
		mc.c.msgHereIsYourFood();
		Cashier.msgComputeBill(this,mc.c,mc.choice);
		waiterGui.setFood(null);
		waiterGui.DoLeaveCustomer();
		
	}
	
	//tells host that the table is no longer occupied
	protected void freeTable(MyCust mc){
		mc.s = CustState.left;
		mc.choice = null;
		Host.msgTableIsFree(this,mc.loc);
		mc.loc = -1;
		MyCustomers.remove(mc);
	}
	
	protected void DoBreak(){
		DoMessage("finally I get a break");
		Host.msgIAmOnBreak(this);
		//waiterPanel.enableWaiter(this);
		/*Timer breakTimer = new Timer();
		breakTimer.schedule(new TimerTask() {
			public void run() {
				print("I'm back");
				//breakOver = true;
				event = MyEvent.breakOver;
				person.msgStateChanged();
			}
		},
		10000);*/
	}
	
	protected void getBackToWork(){
		DoMessage("Back to work");
		Host.msgBreakOver(this);
		//this.OnBreak = false;
		//this.breakOver = false;
		state = MyState.working;
		//waiterPanel.enableWaiter(this);
	}
	
	
	//GUI
	
	protected void DoGoToTableAndWait(int loc){
		waiterGui.DoGoToTable(loc, false);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void DoSeatCustomer(CustomerRole c, int loc){
		print("Seating " + c + " at " + (loc+1));
		waiterGui.DoBringToTable(c, loc);
		
	}
	
	protected void DoAskForOrder(MyCust mc){
		waiterGui.DoGoToTable(mc.loc,false);
	}
	
	protected void DoGiveOrderToCook(){
		waiterGui.DoGiveCookOrder();
	}
	
	protected void DoGetOrder(Order o){
		waiterGui.DoGetOrder();
	}
	
	protected void DoServeOrder(MyCust mc){

		waiterGui.DoGoToTable(mc.loc,false);
	}
	
	protected void DoGoToHome(){
		waiterGui.DoGoToHome();
	}
	
	
	//LOG FILTER
	
	public void DoInfo(String message){
		//super.Do(message);
		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		if(restaurant==null){
			return;
		}
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));		
	}

	@Override
	public boolean canLeave() {
		//Do("\t TRYING TO GET OFF WORK");
		if(inWaiterList){
			if(!restaurant.leaveWaiterList(this)){
				//System.err.println("Waiter wasn't removed");
			}
			else{
				inWaiterList = false;
			}
		}
		
		if(MyCustomers.isEmpty()){
			
			
			waiterGui.DoGoToHome(true);
			
			try {
				waitingForHomePosition.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			restaurant.leaveRestaurant(this);
			return true;
		}
		
		
		return false;
	}
	
	
	
	
	

}
