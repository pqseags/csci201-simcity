package restaurant.restaurantLinda;

import agent.Agent;
import interfaces.Person;
import interfaces.restaurantLinda.Customer;
import interfaces.restaurantLinda.Host;
import interfaces.restaurantLinda.Waiter;

import java.util.*;

import role.Role;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role implements Host{

	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<Customer> uninformedCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	private List<Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	private Collection<Table> tables = Collections.synchronizedList(new ArrayList<Table>()); 
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private String name;

	Person backupWorker;

	public HostRole(String name) {
		super();
		this.name = name;
	}


	// Messages
	public void msgIWantFood(Customer cust) {
		boolean full=true;

		synchronized(tables){
			for (Table table : tables) {
				if (table.occupiedBy.size()<table.tableMax) {
					full = false;
					break;
				}
			}
		}

		if (full)
			uninformedCustomers.add(cust);
		else{
			//hack for the customerGui to know its line position
			cust.getGui().DoWaitInLine(waitingCustomers.size());

			waitingCustomers.add(cust);
		}

		stateChanged();
	}

	public void msgIWillWait(Customer cust, boolean wait){
		if (wait){		
			//hack for the customerGui to know its line position
			cust.getGui().DoWaitInLine(waitingCustomers.size());

			waitingCustomers.add(cust);
		}
		stateChanged();
	}

	public void msgCustomerLeaving(Waiter waiter, Customer cust, int t) {
		synchronized(tables){	
			for (Table table : tables) {
				if (table.tableNumber==t){
					table.occupiedBy.remove(cust);
					print(cust + " leaving " + table.tableNumber);
					break;
				}
			}
		}
		synchronized(waiters){
			for (MyWaiter mw : waiters){
				if (mw.w==waiter){
					mw.customers--;
					stateChanged();
					break;
				}
			}
		}
	}

	public void msgIWantBreak(Waiter waiter){
		synchronized(waiters){
			for (MyWaiter mw: waiters){
				if (mw.w == waiter){
					mw.state=WaiterState.askedBreak;
					stateChanged();
				}
			}
		}
	}

	public void msgOffBreak(Waiter waiter){
		synchronized(waiters){
			for (MyWaiter mw: waiters){
				if (mw.w == waiter){
					mw.state=WaiterState.ready;
					stateChanged();
				}
			}
		}
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
		if (!waitingCustomers.isEmpty()&& !waiters.isEmpty()) {
			synchronized(tables){
				for (Table table : tables) {
					if (table.occupiedBy.size()<table.tableMax) {
						seatCustomer(waitingCustomers.get(0), table);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
		}

		if (!uninformedCustomers.isEmpty()){
			NotifyCustomer(uninformedCustomers.get(0));
			return true;
		}
		try {
			for (MyWaiter mw: waiters){
				if (mw.state==WaiterState.askedBreak){
					GiveBreakStatus(mw);
					return true;
				}
			}
		}
		catch (ConcurrentModificationException e) {
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void NotifyCustomer(Customer customer){
		Do("restaurant full");
		customer.msgRestaurantFull();
		uninformedCustomers.remove(customer);
	}

	private void seatCustomer(Customer customer, Table table) {
		customer.getGui().DoWaitForWaiter();

		//Find the first waiter not on break
		int i=0;
		try{
			while (waiters.get(i).state==WaiterState.onBreak && i<waiters.size()){
				i++;
			}
		}
		catch(IndexOutOfBoundsException e){
			i=0;	//If, for some reason all waiters on break, we'll grab the first waiter
		}


		//Loops through the waiter list to find the one with the lowest number of customers assigned
		MyWaiter waiter=waiters.get(i);
		if (waiter.state==WaiterState.onBreak){
			Do("Error? Forcing " + waiter.w.getName() + "off break");
			waiter.state=WaiterState.ready;
		}
		for (MyWaiter w: waiters){
			if (w.customers<waiter.customers && w.state!=WaiterState.onBreak)
				waiter=w;
		}
		table.occupiedBy.add(customer);
		waitingCustomers.remove(customer);
		waiter.customers++;
		waiter.w.msgPleaseServeCustomer(customer, table.tableNumber);	

		updateCustomerLine();
	}

	private void GiveBreakStatus(MyWaiter waiter){
		for (MyWaiter mw: waiters){
			if (mw.state!=WaiterState.onBreak && mw!=waiter){
				Do(waiter.w.getName() + " can go on break");
				waiter.state=WaiterState.onBreak;
				waiter.w.msgBreakStatus(true);
				return;
			}
		}

		waiter.state=WaiterState.ready;
		Do(waiter.w.getName() + " cannot go on break");
		waiter.w.msgBreakStatus(false);				
	}	

	//private classes
	class Table {
		private List<Customer> occupiedBy= new ArrayList<Customer>();
		private int tableNumber;
		private int tableMax;

		Table(int t, int max)
		{
			occupiedBy= new ArrayList<Customer>();
			tableNumber=t;
			tableMax=max;
		}
	}

	class MyWaiter{
		Waiter w;
		int customers;
		WaiterState state;

		MyWaiter(Waiter w){
			this.w = w;
			customers=0;
			state = WaiterState.ready;
		}
	}
	enum WaiterState {ready, askedBreak, onBreak};



	//utilities

	//Gui hack
	private void updateCustomerLine(){
		for (Customer c: waitingCustomers){
			c.getGui().DoMoveInLine();
		}
	}

	public void addTable(int tableSize){
		tables.add(new Table(tables.size()+1,tableSize));
		stateChanged();
	}

	public void addWaiter(Waiter w){
		waiters.add(new MyWaiter(w));
		stateChanged();
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}


	public void setPerson(Person person) {
		p = person;

	}


	@Override
	public boolean canLeave() {
		return false;
	}

	public void changeShifts(Person p){
		if (this.p!=null)
			this.p.msgThisRoleDone(this);

		this.p = p;
		this.name = p.getName();
	}

	public boolean isPresent(){
		return this.p!=null;
	}

}

