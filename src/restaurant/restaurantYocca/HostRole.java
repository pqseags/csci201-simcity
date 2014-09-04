package restaurant.restaurantYocca;

import agent.Agent;
import restaurant.restaurantYocca.WaiterRole.CustomerState;
import restaurant.restaurantYocca.gui.CustomerGui;
import restaurant.restaurantYocca.gui.WaiterGui;
import interfaces.Person;
import interfaces.restaurantYocca.Cashier;
import interfaces.restaurantYocca.Cook;
import interfaces.restaurantYocca.Customer;
import interfaces.restaurantYocca.Host;
import interfaces.restaurantYocca.Waiter;
import role.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

/**
 * Restaurant Host Agent
 */
public class HostRole extends Role implements Host {
	static final int NTABLES = 4;//a global for the number of tables.
	public List<Customer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<Customer>());
	public List<MyWaiter> waiterList = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public ArrayList<Table> tables;

	RestaurantYocca restaurant;

	public String name;

	public WaiterGui waiterGui = null;
	public Waiter waiterAgent = null;
	public Cook cookAgent = null;
	public Cashier cashier = null;	

	public HostRole(String name, RestaurantYocca rest) {
		super();

		restaurant = rest;

		//		if (restaurant.customers != null) {
		//			for (int i=0; i< restaurant.customers.size();i++) {
		//				waitingCustomers.add((Customer) restaurant.customers.get(i));
		//			}
		//		}

		this.name = name;
		int xLoc = 0;
		int yLoc = 0;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			if (ix == 1) {
				xLoc = 100;
				yLoc = 300;
			}
			if (ix == 2) {
				xLoc = 200;
				yLoc = 300;
			}
			if (ix == 3) {
				xLoc = 300;
				yLoc = 300;
			}
			if (ix == 4) {
				xLoc = 400;
				yLoc = 300;
			}
			tables.add(new Table(ix,xLoc,yLoc));//how you add to a collections
		}
	}

	public class MyWaiter {

		public Waiter waiter;
		private int serves;

		public MyWaiter(Waiter w) {
			waiter = w;
			serves = 0;
		}
	}

	public boolean allTablesOccupied() {
		int tCount = 0;
		for (Table t: tables) {
			if (t.isOccupied()) {
				tCount++;
			}
		}
		if (tCount == 4) {
			return true;
		}
		return false;
	}

	public void addWaiter(WaiterRole w) {
		waiterList.add(new MyWaiter(w));
		Do("Adding Waiter");
	}

	public void removeWaiter(WaiterRole waiterRole) {
		for (MyWaiter mw: waiterList) {
			if (waiterRole.getName() == ((WaiterRole)mw.waiter).getName()) {
				waiterList.remove(mw);
				return;
			}
		}
	}
	
	public boolean inWaiterList(WaiterRole waiterRole) {
		for (MyWaiter mw: waiterList) {
			if (waiterRole.getName() == ((WaiterRole)mw.waiter).getName()) {
				return true;
			}
		}
		return false;
	}


	public String getName() {
		return name;
	}

	// Messages

	public void msgCanIGoOnBreak(Waiter waiter) {
		int workingWaiters = 0;
		for (MyWaiter w: waiterList) {
			if (!w.waiter.isOnBreak()) {
				workingWaiters++;
			}
		}
		if (workingWaiters > 1) {
			Do("Yes you can go on break.");
			waiter.msgGoOnBreak();
		} else {
			Do("Sorry, you cannot go on break right now.");
			waiter.msgCantGoOnBreak();
		}
	}

	public void msgIWantToEat(Customer cust) {
		//		System.out.println("Received customer message that he wants to eat");
		//		waitingCustomers.add(cust);
		//		Do("Size of cust: " + waitingCustomers.size());
		stateChanged();
	}

	public void msgTableIsFree(int tableNum) {
		Do("Table " + tableNum + " is now unoccupied!");
		tables.get(tableNum-1).setUnoccupied();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.getName(), waitingCustomers.size() + " customers to seat");
		for (Table table : tables) {
			if (!table.isOccupied()) {
				for (Customer c: waitingCustomers) {	
					if (c.equals(waitingCustomers.get(0)) && waitingCustomers.get(0).isAtWaitingArea() && waiterList.size() > 0) {
						Do("About to call DoSeatCustomer");
						DoSeatCustomer(c,table);
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
		}
		return false;
	}

	//Actions

	private void DoSeatCustomer(Customer c, Table table) {
		try {
			int waiterBalance = -1;
			int serveCount = 100;
			for (int i=0;i<waiterList.size();i++) {
				if ((waiterList.get(i).serves < serveCount && waiterList.get(i).waiter.isOnBreak() == false)) {			
					serveCount = waiterList.get(i).serves;
					waiterBalance = i;
				}
			}	
			waiterList.get(waiterBalance).waiter.msgSitAtTable(c, table);//the action
			waiterList.get(waiterBalance).waiter.setCook(cookAgent);
			waiterList.get(waiterBalance).serves++;				
			c.setWaiter(waiterList.get(waiterBalance).waiter);
			c.setCashier(cashier);
			table.setOccupant(c);	
			Do("Removing Customer: " + c.getCustomerName());
			waitingCustomers.remove(c);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	//utilities

	public void setWaiter(Waiter waiter) {
		waiterAgent = waiter;
	}

	public void setCook(Cook c) {
		cookAgent = c;
	}

	public void setCashier(Cashier c) {
		cashier = c;		
	}

	@Override
	public void setPaused(boolean b) {
		// TODO Auto-generated method stub

	}

	public void changeShifts(Person p){
		if (this.p!=null)
			this.p.msgThisRoleDone(this);

		this.p = p;
		this.name = p.getName();
	}

	@Override
	public boolean isPresent() {
		return this.p!=null;
	}

	@Override
	public boolean canLeave() {
		return false;
	}

	public boolean YouAreDoneWithShift() {
		if(true){
			Do("Being kicked off the job now");
			p.msgThisRoleDone(this);
			this.p = null;
			//market.DefaultName(this);
		}
		return true;
	}
}

