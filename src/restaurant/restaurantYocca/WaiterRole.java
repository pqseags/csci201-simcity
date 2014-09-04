package restaurant.restaurantYocca;

import agent.Agent;
import restaurant.restaurantYocca.CustomerRole.AgentEvent;
import restaurant.restaurantYocca.CustomerRole.AgentState;
import restaurant.restaurantYocca.gui.CustomerGui;
//import restaurant.restaurantYocca.gui.RestaurantGui;
import restaurant.restaurantYocca.gui.WaiterGui;
import interfaces.Person;
import interfaces.restaurantYocca.Cashier;
import interfaces.restaurantYocca.Cook;
import interfaces.restaurantYocca.Customer;
import interfaces.restaurantYocca.Host;
import interfaces.restaurantYocca.Waiter;
import role.Role;

import java.awt.Graphics2D;
import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;

/**
 * Restaurant Waiter Agent
 */
public class WaiterRole extends Role implements Waiter {
	public List<MyCustomer> waiterCustomers
	= new ArrayList<MyCustomer>();
	protected String name;
	private Semaphore atTable = new Semaphore(0,true);
	public Semaphore atCook = new Semaphore(0,true);
	private Semaphore atStart = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atWArea = new Semaphore(0,true);
	public Semaphore atTheOrderStand = new Semaphore(0,true);


	Timer timer = new Timer();

	//	RestaurantGui gui;

	private boolean onBreak = false;

	public WaiterGui waiterGui = null;
	public Host host = null;
	public Cook cook = null;
	public Cashier cashier = null;

	RestaurantYocca restaurant;

	public int serves = 1;

	public enum CustomerState
	{DoingNothing,Waiting, Seated, ReadyToOrder, Ordering, Ordered, ReOrder, WaitingForFood, EatingFood, FinishedEating, WaitingForCheck, NeedsToPay, Leaving};
	private CustomerState state = CustomerState.Waiting;

	public class MyCustomer {

		public Customer customer;
		public Order o = new Order();
		private CustomerState state;
		private Bill check;

		public MyCustomer(Customer c, Table table, Waiter w) {
			customer = c;
			state = CustomerState.Waiting;
			o.setTable(table);
			o.setWaiter(w);
		}
	}

	public WaiterRole(String name, PersonAgent personAgent) {
		super();
		this.name = name;
		p = personAgent;
	}

	public WaiterRole(){
		super();
	}

	public WaiterRole(String name) {
		super();
		this.name = name;	
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public int getServes() {
		return serves;
	}

	public void addServe() {
		serves++;
	}

	public List getWaitingCustomers() {
		return waiterCustomers;
	}

	public void changeWaiterState() {
		p.msgStateChanged();
	}

	public boolean isOnBreak() {
		return onBreak;
	}

	public void setOnBreak(boolean b) {
		onBreak = b;
	}

	// Messages

	public void msgGoOnBreak() {
		Do("I will go on break");
		onBreak = true;
	}

	public void msgCantGoOnBreak() {
		Do("I cannot go on break now");
		onBreak = false;
		//		waiterGui.cantGoOnBreak();
	}

	public void msgSitAtTable(Customer c, Table t) {
		print("Adding Customer");
		waiterCustomers.add(new MyCustomer(c,t,this));
		p.msgStateChanged();
	}

	public void msgImReadyToOrder(int tableNum) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.o.getTable().getTableNumber() == tableNum) {
				mc.state = CustomerState.ReadyToOrder;
				p.msgStateChanged();
			}
		}
	}

	public void msgHereIsMyChoice(Customer c, String choice) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.customer.getCustomerTableNum() == c.getCustomerTableNum()) {
				mc.o.setChoice(choice);
				mc.state = CustomerState.Ordered;
				p.msgStateChanged();
			}
		}
	}

	public void msgOutOfChoice(Order o) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.o.getTable().getTableNumber() == o.getTable().getTableNumber()) {
				mc.state = CustomerState.ReOrder;
				p.msgStateChanged();
			}
		}
	}

	public void msgOrderIsReady(Order o) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.o.getTable().getTableNumber() == o.getTable().getTableNumber()) {
				mc.state = CustomerState.WaitingForFood;
				p.msgStateChanged();
			}
		}
	}

	public void msgImDone(int tableNum) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.customer.getCustomerTableNum() == tableNum) {
				mc.state = CustomerState.FinishedEating;
				p.msgStateChanged();
			}
		}
	}

	public void msgCheckComputed(Bill c) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.customer.getCustomerTableNum() == c.customer.getCustomerTableNum()) {
				mc.state = CustomerState.NeedsToPay;
				mc.check = c;
				Do("Take the check to the customer please");
				p.msgStateChanged();
			}
		}
	}

	public void msgDoneAndPaid(int tableNum) {
		for (MyCustomer mc : waiterCustomers) {
			if (mc.customer.getCustomerTableNum() == tableNum) {
				Do(mc.customer.getName() + " is finished with meal");
				mc.state = CustomerState.Leaving;
				p.msgStateChanged();
			}
		}
	}

	public void msgAtTable() {//from animation
		waiterGui.setArrived(true);
		atTable.release();// = true;
		p.msgStateChanged();
	}

	public void msgAtCook() {//from animation
		waiterGui.setArrived(true);
		atCook.release();// = true;
		p.msgStateChanged();
	}

	public void msgAtOrderStand() {//from animation
		waiterGui.setArrived(true);
		atTheOrderStand.release();// = true;
		p.msgStateChanged();
	}	

	public void msgAtStart() {//from animation
		waiterGui.setArrived(true);
		atStart.release();// = true;
		p.msgStateChanged();
	}	

	public void msgAtCashier() {
		waiterGui.setArrived(true);
		atCashier.release();// = true;
		p.msgStateChanged();

	}	

	public void msgAtWaitingArea() {
		waiterGui.setArrived(true);
		atWArea.release();// = true;
		p.msgStateChanged();	
	}	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.Waiting)) {	
						mc.state = CustomerState.Seated;
						seatCustomer(mc.customer,mc.o.getTable());		
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			}
		}
		catch (ConcurrentModificationException e) {
		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.ReadyToOrder)) {
						Do("I am going to take your order!");
						mc.state = CustomerState.Ordering;
						takeOrder(mc);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.Ordered)) {
						mc.state = CustomerState.DoingNothing;
						submitOrder(mc.o);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			} 
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.ReOrder)) {
						takeOrderAgain(mc);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			} 	
		}
		catch (ConcurrentModificationException e) {
		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.WaitingForFood)) {
						mc.state = CustomerState.EatingFood;
						Do("Picking up and Delivering food");
						deliverOrder(mc);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}	
			}
		}
		catch (ConcurrentModificationException e) {

		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try{
					if (mc.state.equals(CustomerState.FinishedEating)) {
						Do("Customer " + mc.customer.getName() + " is Finished");
						mc.state = CustomerState.WaitingForCheck;
						getCheck(mc);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			}
		}
		catch (ConcurrentModificationException e) {

		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.NeedsToPay)) {
						mc.state = CustomerState.EatingFood;
						Do("Giving Customer his check");
						deliverCheck(mc);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			}
		}
		catch (ConcurrentModificationException e) {
		}
		try {
			for (MyCustomer mc : waiterCustomers) {
				try {
					if (mc.state.equals(CustomerState.Leaving)) {
						mc.state = CustomerState.DoingNothing;
						customerPaidAndFinished(mc);
					}
				} catch (ConcurrentModificationException e) {
					return false;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}

		return false;
	}

	// Actions

	private void seatCustomer(Customer customer, Table table) {	
		waiterGui.DoGetCustomer(((CustomerRole)customer).getGui().getWaitingYLocation());
		waiterGui.setArrived(false);
		Do("Going through waiting area");
		try {
			atWArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Do("Passed through waiting area");
		customer.setTableLocation(table.getTableNumber());
		waiterGui.setTableLocation(table.getXLocation(), table.getYLocation());
		customer.msgFollowMeToTable(new Menu());
		DoSeatCustomer(customer, table);
		waiterGui.setArrived(false);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
	}

	private void takeOrder(MyCustomer mc) {
		waiterGui.DoGoToTable(mc.o.getTable().getTableNumber());
		waiterGui.setArrived(false);
		try {
			atTable.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Do("What would you like?");
		mc.customer.msgWhatWouldYouLike();
	}

	public void submitOrder(Order o) {
	}

	//	private void submitOrder(Order o) {
	//		waiterGui.setStatusString("O");
	//		waiterGui.DoGoToCook();
	//		waiterGui.setArrived(false);
	//		try {
	//			atCook.acquire();
	//			p.msgStateChanged();
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		}
	//		waiterGui.setStatusString("");
	//		Do("Here is an order");
	//		((Cook) restaurant.cook).msgHereIsAnOrder(o);
	//	}
	//	
	private void takeOrderAgain(MyCustomer mc) {
		waiterGui.DoGoToTable(mc.o.getTable().getTableNumber());
		waiterGui.setArrived(false);
		try {
			atTable.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mc.customer.msgWhatWouldYouLikeInstead();
		for (HashMap<String,Double> c: mc.customer.menu.menuList) {
			if (c.containsKey(mc.o.getChoice())) {
				mc.customer.menu.menuList.remove(c);
			}
		}
		mc.state = CustomerState.DoingNothing;
		Do("We are out of your choice. Can you please make a different choice?");
	}

	private void deliverOrder(MyCustomer mc) {
		waiterGui.DoGoToCook();
		waiterGui.setArrived(false);
		try {
			atCook.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setStatusString(mc.o.getChoice());
		System.out.println(mc.o.getTable().getTableNumber());
		waiterGui.DoGoToTable(mc.o.getTable().getTableNumber());
		waiterGui.setArrived(false);
		try {
			atTable.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Do("Here is your Food");
		waiterGui.setStatusString("");
		mc.customer.msgHereIsYourFood(mc.o.getChoice());
		waiterGui.DoLeaveCustomer();
		p.msgStateChanged();
	}

	private void getCheck(MyCustomer mc) {
		waiterGui.DoGoToCashier();
		waiterGui.setArrived(false);
		try {
			atCashier.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Do("Please compute the customer's check");
		((Cashier) restaurant.cashier).msgComputeCheck(mc.o.getChoice(), mc.customer, (Waiter)this);
	}

	private void deliverCheck(MyCustomer mc) {
		waiterGui.DoGoToCashier();
		waiterGui.setArrived(false);
		try {
			atCashier.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setStatusString("B");
		waiterGui.DoGoToTable(mc.customer.getCustomerTableNum());
		waiterGui.setArrived(false);
		try {
			atTable.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Do("Here is your Check");
		waiterGui.setStatusString("");
		mc.customer.msgHereIsYourCheck(mc.check);
		waiterGui.DoLeaveCustomer();
	}

	private void customerPaidAndFinished(MyCustomer mc) {
		Do("Customer is finished and has paid");
		if (host == null) {
			System.out.println("Host is null");
		}
		else System.out.println("Host isn''t null");
		restaurant.host.msgTableIsFree(mc.customer.getCustomerTableNum());
		waiterCustomers.remove(mc); //Properly removes customer
		waiterGui.DoLeaveCustomer();
		DoGoOnBreak();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, Table table) {			
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"		
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer);
	}

	private void DoGoOnBreak() {
		if (isOnBreak() == true) {
			Do("I am on break");		
		}
	}

	public void TryGoOnBreak() {
		Do("Can I go on break?");
		host.msgCanIGoOnBreak(this);	
	}

	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}	


	public void setHost(Host h) {
		this.host = h;
	}

	public void setCook(Cook c) {
		this.cook = c;
	}

	public void setCashier(Cashier c) {
		this.cashier = c;
	}

	@Override
	public void addServes() {
		// TODO Auto-generated method stub

	}

	public void setRestaurant(RestaurantYocca restaurantYocca) {
		restaurant = restaurantYocca;		
	}

	@Override
	public boolean canLeave() {
		//Do("\t TRYING TO GET OFF WORK");

		if(waiterCustomers.isEmpty() && !restaurant.host.inWaiterList(this)){			
			restaurant.leaveRestaurant(this);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, this.name, "Leaving Restaurant as Waiter",this.restaurant.cityRestaurant.animationPanel.getName());
			return true;
		}
		else if (restaurant.host.waiterList.size() > 1 && restaurant.host.inWaiterList(this)) {
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, this.name, "Removing myself from waiter list: " + restaurant.host.waiterList.size() + " waiters before I remove myself",this.restaurant.cityRestaurant.animationPanel.getName());
			restaurant.host.removeWaiter(this);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, this.name, "Removing myself from waiter list: " + restaurant.host.waiterList.size() + " waiters after I remove myself",this.restaurant.cityRestaurant.animationPanel.getName());

		}

		return false;
	}
}

