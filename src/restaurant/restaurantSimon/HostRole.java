package restaurant.restaurantSimon;

import agent.Agent;
import restaurant.restaurantGabe.interfaces.Host;
import restaurant.restaurantSimon.gui.HostGui;
import restaurant.restaurantSimon.gui.CustomerGui;



import role.Role;
import interfaces.Person;

import java.util.*;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role  implements Host{

	public enum MyWaiterState{available,notAvailable,wantBreak,onBreak};
	class MyWaiter{
		WaiterRole waiter;
		MyWaiterState s=MyWaiterState.available;

		public MyWaiter(WaiterRole w){
			waiter=w;
		}

	}
	public enum MyCustomerState{pending, waiting, deciding, leaving, waitingToBeSeated};
	class MyCustomer{
		CustomerRole customer;
		MyCustomerState s=MyCustomerState.pending;

		public MyCustomer(CustomerRole c){
			customer=c;
		}

	}

	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	Person self;
	private String name;
	public HostGui hostGui = null;
	private List<MyWaiter> myWaiters= Collections.synchronizedList(new ArrayList<MyWaiter>());
	private List<MyCustomer> waitingCustomers=  Collections.synchronizedList(new ArrayList<MyCustomer>());



	public HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, (2*ix-1)*CustomerGui.xTableUnit, CustomerGui.yTableUnit));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages
	public void msgIGotCustomer(CustomerRole cus){
		synchronized(waitingCustomers){
			for(MyCustomer mc : waitingCustomers){
				if(mc.customer==cus){
					waitingCustomers.remove(mc);
					self.msgStateChanged();
				}
			}
		}
		
	}

	public void msgIStay(CustomerRole cus){
		synchronized(waitingCustomers){
			for(MyCustomer mc : waitingCustomers){
				if(mc.customer==cus){
					mc.s=MyCustomerState.waiting;
					self.msgStateChanged();
				}
			}
		}
	}
	public void msgILeave(CustomerRole cus){

		synchronized(waitingCustomers){
			for(MyCustomer mc : waitingCustomers){

				if(mc.customer==cus){
					mc.s=MyCustomerState.leaving;
					self.msgStateChanged();
				}
			}
		}
	}

	public void msgOnBreak(WaiterRole waiter){//From OnBreak Button
		synchronized(myWaiters){
			for(MyWaiter mw :myWaiters){
				if(mw.waiter==waiter){
					mw.s=MyWaiterState.wantBreak;
					self.msgStateChanged();
				}
			}
		}

	}

	public void msgIWantFood(CustomerRole cust) {
		waitingCustomers.add(new MyCustomer(cust));
		self.msgStateChanged();
	}

	public void msgLeavingTable(CustomerRole cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				self.msgStateChanged();
			}
		}
	}

	public void msgAddWaiter(WaiterRole w){
		myWaiters.add(new MyWaiter(w));
		self.msgStateChanged();
	}

	public void msgImAvailable(WaiterRole w){
		synchronized(myWaiters){
			for(MyWaiter waiter: myWaiters){
				if(waiter.waiter==w)
				{
					waiter.s=MyWaiterState.available;
				}
			}
		}
		self.msgStateChanged();
	}

	public void AddWaiter(MyWaiter mw){
		myWaiters.add(mw);
		self.msgStateChanged();
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

		//deal with the waiter break
		synchronized(myWaiters){
			int waiterNum=0;
			for(MyWaiter mw : myWaiters){
				if(mw.s==MyWaiterState.wantBreak){
					for(MyWaiter w : myWaiters){
						if(w!=mw){
							if(w.s==MyWaiterState.available){
								mw.waiter.OnBreak();
								mw.s=MyWaiterState.onBreak;
								return true;
							}
						}
					}

					mw.s=MyWaiterState.available;
					return true;
				}
				if(mw.s==MyWaiterState.available){
					mw.waiter.msgYourWaiterNumber(waiterNum);
					//Do("given "+ waiterNum+" to "+ mw.waiter);
					waiterNum++;
				}
			}
		}
		//Deal with seating customer
		for (Table table : tables) {
			if ((!table.isOccupied()) && (!waitingCustomers.isEmpty())) {
				synchronized(waitingCustomers){
					for(MyCustomer mc : waitingCustomers){
						if(mc.s==MyCustomerState.waiting){
							synchronized(myWaiters){
								for(MyWaiter myWaiter : myWaiters){
									if(myWaiter.s==MyWaiterState.available){
										SeatAtTable(mc,myWaiter,table);
										return true;//return true to the abstract agent to reinvoke the scheduler.

									}
								}
							}
						}
					}
				}
			}
		}

		//Deal with customer waiting or leaving
		if(!waitingCustomers.isEmpty()){
			int customerNumber=0;
			synchronized(waitingCustomers){
				for(MyCustomer mc : waitingCustomers){
					if(mc.s==MyCustomerState.pending||mc.s==MyCustomerState.waiting||mc.s==MyCustomerState.deciding||mc.s==MyCustomerState.waitingToBeSeated){
						mc.customer.msgYourWaitingNumber(customerNumber);
						customerNumber++;
					}
					if(mc.s==MyCustomerState.pending){
						boolean openTable=false;
						for(Table table : tables){
							if(!table.isOccupied()){
								openTable=true;
							}
						}

						if(openTable){
							mc.customer.msgNotFull();
							mc.s=MyCustomerState.deciding;
							return true;
						}
						else{
							mc.customer.msgFull();
							mc.s=MyCustomerState.deciding;
							return true;

						}

					}
					if(mc.s==MyCustomerState.leaving){
						waitingCustomers.remove(mc);
						return true;
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
	private void SeatAtTable(MyCustomer mc,MyWaiter mw, Table table){
		print("Telling "+ mw.waiter +" to sit " + mc.customer + " at table" + (table.getTableNum()+1) );
		mw.s=MyWaiterState.notAvailable;
		mw.waiter.msgSitAtTable(mc.customer, table);
		table.setOccupant((mc.customer));
		mc.s=MyCustomerState.waitingToBeSeated;

	}

	/*
	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table.tableX, table.tableY); 

	}
	 */


	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	public void changeShifts(Person p){
		if (this.self!=null)
			this.self.msgThisRoleDone(this);
		
		this.self = p;
		this.name = p.getName()+" Host";
	}

	protected class Table {
		CustomerRole occupiedBy;
		int tableNumber;
		int tableY;
		int tableX;

		Table(int tableNumber, int x, int y) {
			this.tableNumber = tableNumber;
			this.tableX=x;
			this.tableY=y;
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
		public int getTableNum()
		{
			return tableNumber;
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return self!=null;
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return waitingCustomers.size()==0;
	}
	
}

