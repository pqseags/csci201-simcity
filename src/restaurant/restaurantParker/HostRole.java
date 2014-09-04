package restaurant.restaurantParker;

import role.Role;
import agent.Agent;
import restaurant.restaurantParker.gui.HostGui;
import interfaces.Person;
import interfaces.restaurantParker.Host;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import restaurant.restaurantParker.MyWaiter;
import restaurant.restaurantParker.MyWaiter.MyWaiterState;
import restaurant.restaurantParker.LobbyCustomer.CustomerState;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role implements Host{
	
	//lab3
	static final int NTABLES = 3;//a global for the number of tables.

	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<LobbyCustomer> customers = Collections.synchronizedList(new ArrayList<LobbyCustomer>());

	
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public List<Boolean> lobbySeats = Collections.synchronizedList(new ArrayList<Boolean>());
	public List<Integer> freeSeats = Collections.synchronizedList(new ArrayList<Integer>());
	private final int numSeats = 9;
	
	RestaurantParker restaurant;
	
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	
	
	//public WaiterAgent waiter = null;
	public HostGui hostGui = null;

	public HostRole(String name, RestaurantParker rest) {
		super();
		this.name = name;
		this.restaurant = rest;
		
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		//make seats
		for (int i =0; i < numSeats; i++){
			lobbySeats.add(false);
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	/*public List getWaitingCustomers() {
		return waitingCustomers;
	}*/

	public Collection getTables() {
		return tables;
	}
	// Messages
	
	public void msgWantToGoOnBreak(WaiterRole w){
		print("Recieved break request from " + w.getName());
		
		synchronized (waiters){
			for (int i=0; i< waiters.size(); i++) {
				if (waiters.get(i).waiter == w){
					waiters.get(i).state = MyWaiterState.wantBreak;
					p.msgStateChanged();
				}
			}
		}
	}
	
	public void msgImOffBreak(WaiterRole w){
		synchronized (waiters){
			for (int i=0; i<waiters.size(); i++){
				if (waiters.get(i).waiter == w){
					waiters.get(i).state = MyWaiterState.working;
					//p.msgStateChanged();
				}
			}
		}
	}

	public void msgIWantFood(CustomerRole cust) {
		print("I see a customer "+ cust.getName());
		LobbyCustomer lc = new LobbyCustomer(cust);
		lc.state = CustomerState.entered;
		customers.add(lc);
		p.msgStateChanged();
	}
	
	public void msgIWillStay(CustomerRole cust){
		
		synchronized (customers){
			for (LobbyCustomer lc : customers){
				if (lc.customer == cust){
					lc.state = CustomerState.staying;
					p.msgStateChanged();
				}
			}
		}
	}
	
	public void msgIWontStay(CustomerRole cust){
		synchronized (customers){
			for (LobbyCustomer lc : customers){
				if (lc.customer == cust){
					lc.state = CustomerState.leaving;
					p.msgStateChanged();
				}
			}
		}
	}

	public void msgTableIsFree(CustomerRole cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print("Recording " + cust + " leaving " + table);
				table.setUnoccupied();
				p.msgStateChanged();
			}
		}
	}
	
	public void msgLobbySeatFree(int position){
		freeSeats.add(position);
		p.msgStateChanged();
	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		synchronized (waiters){
			for (MyWaiter mw : waiters){
				if (mw.state == MyWaiterState.wantBreak){
					replyWaiterBreakRequest(mw);
					return true;
				}
			}
		}
		
		synchronized (customers){
			for (LobbyCustomer lc : customers){
				if (lc.state == CustomerState.entered){
					replyCustomer(lc);
					return true;
				}
			}
		}
		
		synchronized (customers){
			for (LobbyCustomer lc : customers){
				if (lc.state == CustomerState.leaving){
					removeSeat(lc);
					return true;
				}
			}
		}
	
		if (!freeSeats.isEmpty()){
			freeSeat(freeSeats.get(0));
			return true;
		}
		
		synchronized (waiters){
			synchronized (customers){
				for (Table table : tables) {
					if (!table.isOccupied()) {
						for(LobbyCustomer lc : customers) {
							if (lc.state == CustomerState.staying){
								if (!waiters.isEmpty()){
									seatCustomer(lc, table);//the action
									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
						}
					}
				}
			}
		}
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void replyWaiterBreakRequest(MyWaiter w){
		

		int workingWaiters = 0;
		for (MyWaiter mw : waiters){
			if (mw.state == MyWaiterState.working){
				workingWaiters = workingWaiters+1;
			}
		}
		
		if (workingWaiters < 1){
			print("Sorry " + w.waiter.getName() + ", you cannot go on break.");
			w.waiter.msgBreakRequestReply(false);
			w.state = MyWaiterState.working;
			return;
		}
		//otherwise....
		print(w.waiter.getName() + ", go ahead and take a break!");
		w.waiter.msgBreakRequestReply(true);
		w.state = MyWaiterState.onBreak;
		
	}
	
	private void replyCustomer(LobbyCustomer cust){
		
		cust.state = CustomerState.passive;
		
		int numberCustomers = 0;
		for (Table table : tables) {
			if (table.isOccupied()) {
				numberCustomers ++ ;
			}
		}
		
		//check open lobby seats
		synchronized (lobbySeats){
			for(int i=0; i<lobbySeats.size(); i++){
				if (lobbySeats.get(i).booleanValue() == false){
					cust.seat = i;
					lobbySeats.set(i,true);
					break;
				}
			}
		}
		//System.err.println(cust.seat);
		
		if (numberCustomers == tables.size()){
			print("I'm sorry "+ cust.customer.getName() + ", there are no open tables. Should I add you to the waitlist?");
			cust.customer.msgIsThereRoom(false, cust.seat);
		}
		else {
			print("There is an open table " + cust.customer.getName() + ", I'll get a waiter to seat you shortly.");
			cust.customer.msgIsThereRoom(true, cust.seat);
		}
		
	}
	
	private void removeSeat(LobbyCustomer lc){
		lobbySeats.set(lc.seat, false);
		customers.remove(lc);
	}
	
	private void freeSeat(Integer position){
		freeSeats.remove(position);
		lobbySeats.set(position.intValue(), false);
	}
	
	private void seatCustomer(LobbyCustomer cust, Table table) {
		
		//choose waiter and load balance
		MyWaiter chosenWaiter = new MyWaiter();
		int i = 100;
		synchronized (waiters){
			for (MyWaiter waiter : waiters){
				if (waiter.state == MyWaiterState.working){
					if (waiter.numberCustomers < i){
						i = waiter.numberCustomers;
						chosenWaiter = waiter;
					}
				}
			}
		}
		chosenWaiter.numberCustomers = chosenWaiter.numberCustomers+1;
		
		DoSeatCustomer(cust.customer, table, chosenWaiter.waiter);
		//System.err.println("telling waiter to go to " + cust.seat);
		chosenWaiter.waiter.msgSeatCustomer(cust.customer, table.tableNumber, cust.seat);
		table.setOccupant(cust.customer);
		customers.remove(cust);
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerRole customer, Table table, WaiterRole waiter) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Telling " + waiter.getName() + " to seat" + customer + " at " + table); 

	}


	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	
	public void addWaiter(WaiterRole w){
		waiters.add(new MyWaiter(w));
		if (waiters.size() == 1){
			if (p!=null){
				p.msgStateChanged();
			}
		}
	}

	private class Table {
		CustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
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
	
	public void changeShifts(Person p){
		if (this.p!=null)
			this.p.msgThisRoleDone(this);
		
		this.p = p;
		this.name = p.getName();
	}
	
	protected void print(String message){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_PARKER, name, message, restaurant.cityRestaurant.ID);
	}
}

