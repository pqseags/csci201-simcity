package restaurant.restaurantLinda;

import UnitTests.mock.LoggedEvent;
import agent.Agent;
import restaurant.Restaurant;
import restaurant.restaurantLinda.gui.WaiterGui;
import role.Role;

import interfaces.Person;
import interfaces.restaurantLinda.Cashier;
import interfaces.restaurantLinda.Customer;
import interfaces.restaurantLinda.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the restaurant.host. A restaurant.host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class WaiterRole extends Role implements Waiter{
	public List<MyCustomer> customers	= new ArrayList<MyCustomer>();
	protected String name;
	WaiterState state=WaiterState.ready;
	private enum WaiterState {ready, takingOrder};
	BreakStatus breakStatus = BreakStatus.none;
	public enum BreakStatus {none, wantBreak, asked, hasPermission, onBreak, finished}
	protected RestaurantLinda restaurant;
	protected List<String> unavailableFoods;
	
	protected Semaphore atDestination = new Semaphore(0,true);
	protected WaiterGui waiterGui=null;

	
	public WaiterRole(){
		super();
	}
	
	//Messages
	public void msgPleaseServeCustomer(Customer c, int table){		
		customers.add(new MyCustomer(c,table,CustomerState.waiting));
		log.add(new LoggedEvent("Received request to set customer "+c.getName()+" at table "+table));
		
		//Just in case the restaurant.host screws up somehow
		if(breakStatus == BreakStatus.onBreak){
			breakStatus = BreakStatus.none;
		}
		
		//hack to make the waiter want a break
		if (name.compareToIgnoreCase("onBreak")==0)
			msgWantBreak();
		
		p.msgStateChanged();
	}
	
	public void msgAtDestination() {//from animation
		atDestination.release();// = true;
		waiterGui.DoTalk(null);
		p.msgStateChanged();
	}
	

	public void msgReadyToOrder(Customer c){
		try{
			for (MyCustomer mc: customers){
				if (mc.c==c && mc.state!=CustomerState.done){
					Do("Received readyToOrder from " + c.getName());
					mc.state=CustomerState.readyToOrder;
					p.msgStateChanged();
					break;
				}				
			}
		}
		catch(ConcurrentModificationException e){
			msgReadyToOrder(c);
		}
	}
	
	public void msgHereIsChoice(Customer c, String choice){
		state=WaiterState.ready; 
		
		try{
			for (MyCustomer mc: customers){
				if (mc.c==c && mc.state!=CustomerState.done){				
					mc.state=CustomerState.ordered;
					mc.choice=choice;
					Do(mc.c.getName() + " has ordered " + mc.choice);
					p.msgStateChanged();
					break;
				}
			}
		}
		catch(ConcurrentModificationException e){
			msgHereIsChoice(c,choice);
		}
		
	}
	
	public void msgOutOfChoice(String choice, int table, List<String> unavailable){
		unavailableFoods = unavailable;
		try{
			for (MyCustomer mc: customers){
				if (mc.table==table && mc.choice.compareToIgnoreCase(choice)==0 && mc.state==CustomerState.orderSent){
					mc.state=CustomerState.noFood;
					p.msgStateChanged();
					break;
				}
			}
		}
		catch(ConcurrentModificationException e){
			msgOutOfChoice(choice,table,unavailable);
		}
	}
	
	public void msgOrderDone(String choice,int table){
		try{
			for (MyCustomer mc: customers){
				if (mc.table==table && mc.choice.compareToIgnoreCase(choice)==0 && mc.state==CustomerState.orderSent){
					mc.state=CustomerState.foodReady;
					p.msgStateChanged();
					break;
				}
			}
		}
		catch(ConcurrentModificationException e){
			msgOrderDone(choice,table);
		}
	}
	
	public void msgHereIsBill(String choice, Customer c, int total){
		Do("Received bill for "+c.getName());
		try{
			for (MyCustomer mc: customers){
				if (mc.c==c && mc.state!=CustomerState.done){
					mc.bill=new Check(choice,total);
					mc.needsCheck=true;
					p.msgStateChanged();
					break;
				}
			}
		}
		catch(ConcurrentModificationException e){
			msgHereIsBill(choice,c,total);
		}
	}
	
	public void msgLeaving(Customer c){
		try{
			for (MyCustomer mc: customers){
				if (mc.c==c && mc.state!=CustomerState.done){
					mc.state=CustomerState.leaving;
					p.msgStateChanged();
					break;
				}
			}
		}
		catch(ConcurrentModificationException e){
			msgLeaving(c);
		}
	}
	
	public void msgDoneAndPaying(Customer c){
		try{
			for (MyCustomer mc: customers){
				if (mc.c==c && mc.state!=CustomerState.done){
					mc.state=CustomerState.paying;
					p.msgStateChanged();
					break;
				}
			}
		}
		catch(ConcurrentModificationException e){
			msgDoneAndPaying(c);
		}
	}
	
	public void msgWantBreak(){
		//Don't update if waiter is already on break/ has asked the restaurant.host
		if (breakStatus == BreakStatus.none)
			breakStatus = BreakStatus.wantBreak;
		
		waiterGui.UpdateInfo();
		p.msgStateChanged();
	}
	
	public void msgBreakDone(){
		breakStatus = BreakStatus.finished;
		waiterGui.UpdateInfo();
		p.msgStateChanged();
	}
	
	public void msgBreakStatus(boolean permission){
		if (permission)
			breakStatus=BreakStatus.hasPermission;
		else
			breakStatus=BreakStatus.none;
		
		waiterGui.UpdateInfo();
		p.msgStateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (breakStatus==BreakStatus.wantBreak){
			AskForBreak();
			return true;
		}
		else if (breakStatus==BreakStatus.finished){
			GoOffBreak();
			return true;
		}
		
		if (state==WaiterState.ready){
			try{
				for (MyCustomer mc: customers){
					if (mc.needsCheck){
						GiveCheck(mc);
						return true;
					}
				}
				for (MyCustomer mc: customers){
					if (mc.state==CustomerState.paying || mc.state==CustomerState.leaving){
						Notifyrestauranthost(mc);
						return true;
					}
				}
				for (MyCustomer mc: customers){
					if (mc.state==CustomerState.foodReady){
						ServeFood(mc);
						return true;
					}
				}
				for (MyCustomer mc: customers){
					if (mc.state==CustomerState.waiting){
						SeatCustomer(mc);
						return true;
					}
				}
				for (MyCustomer mc: customers){
					if (mc.state==CustomerState.noFood){
						RedoOrder(mc);
					}
				}
				for (MyCustomer mc: customers){
					if (mc.state==CustomerState.readyToOrder){
						TakeOrder(mc);
						return true;
					}
				}
				for (MyCustomer mc: customers){
					if (mc.state==CustomerState.ordered){
						SendOrder(mc);
						return true;
					}
				}
				
				DoGoToDefault();
				
				//Check whether we still have customers
				for (MyCustomer mc: customers){
					if (mc.state != CustomerState.done){
						//Do(mc.c.getName() + " is in state " + mc.state);
						return false;
					}
				}
			}
			catch(ConcurrentModificationException e){
				return false;
			}
			
			if (breakStatus==BreakStatus.hasPermission || breakStatus==BreakStatus.onBreak)
				DoGoOnBreak();
		}
		
		//we have tried all our rules and found
				//nothing to do. So return false to main loop of abstract agent
				//and wait.
		return false;	
	}
	
	//actions	
	private void SeatCustomer(MyCustomer mc){
		log.add(new LoggedEvent("Walking to door to fetch customer " + mc.c.getName()));
		
		if (waiterGui!=null){
			waiterGui.DoGoToCustomer(mc.c.getGui());
			
			try{
				atDestination.acquire();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		mc.c.msgFollowMe(this,new Menu());
		mc.state=CustomerState.seated;
		
		if (waiterGui!=null){
			waiterGui.DoSeatCustomer(mc.c.getGui(),mc.table);	
			
			try{
				atDestination.acquire();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	private void TakeOrder(MyCustomer mc){
		DoMessage(mc.c.getName() + ", may I take your order?");
		state=WaiterState.takingOrder;
		
		if (waiterGui!=null){
			waiterGui.DoTalk("table"+mc.table);
			waiterGui.DoGoToTable(mc.table);
			try{
				atDestination.acquire();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		mc.state=CustomerState.askedToOrder;	
		mc.c.msgWhatDoYouWant();		
	}
	
	abstract void SendOrder(MyCustomer mc);
	
	private void RedoOrder(MyCustomer mc){
		waiterGui.DoGoToTable(mc.table);
		try{
			atDestination.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		mc.state=CustomerState.seated;
		waiterGui.DoTalk("No " + mc.choice);		
		mc.c.msgRedoOrder(new Menu(unavailableFoods), mc.choice);
	}
	
	private void ServeFood(MyCustomer mc){
		waiterGui.DoGoToPlatingArea(mc.choice);
		try{
			atDestination.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
		waiterGui.DoServeFood(mc.choice,mc.table);
		try{
			atDestination.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		waiterGui.DoRelinquishFood();
		mc.state=CustomerState.eating;
		mc.c.msgHereIsFood(mc.choice);
		((Cashier)restaurant.cashier).msgPleaseComputeBill(this, mc.choice, mc.c);
		
	}
	
	private void GiveCheck(MyCustomer mc){
		waiterGui.DoTalk("$"+mc.bill);
		Do("Delivering check to "+mc.c.getName()+": $"+mc.bill);
		waiterGui.DoGoToTable(mc.table);
		try{
			atDestination.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		waiterGui.DoTalk(null);
		mc.needsCheck=false;
		mc.c.msgHereIsCheck(mc.bill, (Cashier)restaurant.cashier);
	}
	
	private void Notifyrestauranthost(MyCustomer mc){
		mc.state=CustomerState.done;
		restaurant.host.msgCustomerLeaving(this,mc.c, mc.table);	
	}
	
	private void DoGoToDefault(){
		if (waiterGui!=null)
			waiterGui.DoGoToDefault();
	}
	
	private void AskForBreak(){
		breakStatus = BreakStatus.asked;
		restaurant.host.msgIWantBreak(this);
	}
	
	private void GoOffBreak(){
		waiterGui.DoGoToDefault();
		breakStatus=BreakStatus.none;
		restaurant.host.msgOffBreak(this);
		waiterGui.UpdateInfo();
	}
	
	private void DoGoOnBreak(){
		waiterGui.DoGoOnBreak();
		
		if (breakStatus!=BreakStatus.onBreak)
			Do("Going on break");
		breakStatus = BreakStatus.onBreak;
		waiterGui.UpdateInfo();
	}
	//inner classes
	public class MyCustomer{
		Customer c;
		int table;
		String choice;
		CustomerState state;
		boolean needsCheck=false;
		Check bill;
		
		MyCustomer(Customer cus, int tableNum, CustomerState s){
			c=cus;
			table=tableNum;
			state=s;
		}
	}
	enum CustomerState {waiting, seated, readyToOrder, askedToOrder, ordered, orderSent, noFood, foodReady, eating, paying, leaving, done}
	
	//utilities	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public BreakStatus getBreakStatus(){
		return breakStatus;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRestaurant(Restaurant r){
		this.restaurant = (RestaurantLinda)r;
	}
	
	@Override
	public boolean canLeave() {
		if (breakStatus==BreakStatus.onBreak){
			LeaveRestaurant();
			return true;
		}
		
		msgWantBreak();
		return false;
		
	}
	
	public void LeaveRestaurant(){
		waiterGui.DoLeaveRestaurant();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.p.msgThisRoleDone(this);
		DoMessage("Leaving work");
		restaurant.leaveRestaurant(waiterGui);
	}
	
	public void DoInfo(String message){
		//super.Do(message);
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

