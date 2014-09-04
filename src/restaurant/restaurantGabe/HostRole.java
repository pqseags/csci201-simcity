package restaurant.restaurantGabe;

import interfaces.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;
import restaurant.restaurantGabe.interfaces.Host;
import role.Role;
import agent.Agent;



public class HostRole extends Role implements Host{
	
	//INITIALIZATION
	public HostRole(String name){
		this.name = name;
		//Initialize 3 tables
		for(int i = 0;i<N_TABLES;++i){
			Table t = new Table();
			t.n = i;
			t.s = TableState.free;
			Tables.add(t);
		}
	}
	
	//GETTERS
	public String getName(){
		return name;
	}
	
	//SETTERS
	public void addWaiter(WaiterRole w){
		DoMessage("Adding a waiter "+w.getName());
		MyWait newWaiter = new MyWait();
		newWaiter.w = w;
		newWaiter.s = WaitState.available;
		newWaiter.numTables = 0;
		MyWaiters.add(newWaiter);
		if(person!=null){
			person.msgStateChanged();
		}
	}
	
	public boolean removeWaiter(WaiterRole w){
		try{	
			DoMessage("Getting rid of waiter");
			MyWait leavingWaiter = findWait(MyWaiters,w);
			MyWaiters.remove(leavingWaiter);
			return true;
		}
		catch(Exception e){
			DoMessage("\t BIG PROBLEM: WAITER NOT LEAVING LIST");
			return false;
			//throw e;
		}
	}
	
	//USEFUL METHODS
	
	
	private WaiterRole nextAvailableWaiter(){
		int i = 0;
		synchronized(MyWaiters){
			for(MyWait mw:MyWaiters){
				if(mw.s == WaitState.available){
					this.lastWaiter = i;
					return mw.w;
				}
				i++;
			}
		}
		return null;
	}
	
	//counts how many waiters are working
	private int countAvailable(){
		int i = 0;
		synchronized(MyWaiters){
			for(MyWait mw:MyWaiters){
				if(mw.s == WaitState.available || mw.s==WaitState.wantsBreak){
					i++;
				}
			}
		}
		return i;
	}
	
	//returns the first instance of MyCust in list with customerAgent equal to c
	private MyCust findCust(List<MyCust> Custs,CustomerRole c){
		synchronized(Custs){
			for(MyCust mc : Custs){
				if(mc.c==c){
					return mc;
				}
			}
		}
		return null;
	}
	
	//returns first MyWait with WaiterAgent w
	private MyWait findWait(List<MyWait> Waits,WaiterRole w){
		synchronized(Waits){
			for(MyWait mw : Waits){
				if(mw.w==w){
					return mw;
				}
			}
		}
		return null;
	}
	
	//Gives the gui base a list of people for pausing purposes
	public List<Agent> peopleList(){
		List<Agent> people = new ArrayList<Agent>();
		people.add(this);
		synchronized(MyCustomers){
		for(MyCust mc:MyCustomers){
			people.add(mc.c);
		}
		}
		synchronized(MyWaiters){
			for(MyWait mw:MyWaiters){
				people.add(mw.w);
			}
		}
		return people;
	}
	
	//DATA
	String name;
	
	//person agent behind this role
	PersonAgent person;
	
	//Restaurant
	RestaurantGabe restaurant;
	
	//Table class that will hold table state and number
	private enum TableState {free,occupied};
	class Table{
		public int n;
		public TableState s;
		public boolean Occupied(){
			if(s==TableState.occupied){
				return true;
			}
			return false;
		}
	}
	//List of host's tables
	private List<Table> Tables = new ArrayList<Table>();
	
	//will be used to evenly distribute the customer load
	int lastWaiter = 0;
	
	//private Customer object holding host-relevant info
	private enum CustState {waiting,toldFull,stillWantsTable,seated,left};
	class MyCust{
		public MyCust(CustomerRole c,Table table,CustState s){
			this.c = c;
			this.t = table;
			this.s = s;
		}
		public CustomerRole c;
		public Table t;
		public CustState s;
	}
	//List of host's Customers
	private List<MyCust> MyCustomers = Collections.synchronizedList(new ArrayList<MyCust>());
	private int N_TABLES = 3;
	
	
	
	
	//private waiter object holding host-relevant info
	private enum WaitState {available,wantsBreak,canGoOnBreak,onBreak};
	class MyWait{
		public WaiterRole w;
		public WaitState s;
		int numTables;
	}
	//list of host's waiters
	private List<MyWait> MyWaiters = Collections.synchronizedList(new ArrayList<MyWait>());
	
	
	//MESSAGES
	
	public boolean YouAreDoneWithShift(){
		if(true){
			DoMessage("Being kicked off the job now");
			person.msgThisRoleDone(this);
			this.person = null;
			//market.DefaultName(this);
		}
		return true;
		
	}
	
	public void msgBreakOver(WaiterRole w){
		MyWait mw = findWait(MyWaiters, w);
		mw.s = WaitState.available;
	}
	
	//Waiter tells host that he's now dropping everything to go on break
	public void msgIAmOnBreak(WaiterRole w){
		MyWait mw = findWait(MyWaiters, w);
		mw.s = WaitState.onBreak;
	}
	
	//Waiter tells host that he wants a break
	public void msgIWantABreak(WaiterRole w){
		MyWait mw = findWait(MyWaiters,w);
		mw.s = WaitState.wantsBreak;
		person.msgStateChanged();
	}
	
	//Customer sends message that he wants a table
	public void msgIWantToEat(CustomerRole c){
		//System.out.println("Here in the host method");
		MyCust mc = findCust(MyCustomers,c);
		if(mc==null){
			
			DoMessage("Added new Customer " + c.getName());
			MyCustomers.add(new MyCust(c,null,CustState.waiting));
		}
		else if(mc.s== CustState.left){
			mc.s = CustState.waiting;
			mc.t = null;
		}
		if(person!=null){
			person.msgStateChanged();
		}
	}
	
	public void msgStayOrNot(CustomerRole c,boolean stay){
		
		MyCust mc = findCust(MyCustomers,c);
		if(stay){
			mc.s = CustState.stillWantsTable;
			person.msgStateChanged();
			return;
		}
		mc.s = CustState.left;
	}
	
	//waiter tells host that table is free
	public void msgTableIsFree(WaiterRole w,int table){
		MyWait mw = findWait(MyWaiters,w);
		if(mw!=null){
			mw.numTables -=1;
		}
		
		//find the table, free it
		for(Table t:Tables){
			if(t.n==table){
				t.s = TableState.free;
			}
		}
		
		//find the customer, change his info
		synchronized(MyCustomers){
			for(MyCust mc:MyCustomers){
				if(mc.t!=null && mc.t.n==table){
					mc.t = null;
					mc.s = CustState.left;
				}
			}
		}
		
		person.msgStateChanged();
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
		//System.out.println("In the scheduler");
		//System.out.println(MyWaiters.get(0).numTables);
		/*for(MyWait mw:MyWaiters){
			System.out.println(mw.w.getName());
			System.out.println(mw.s);
		}*/
		//System.out.println(MyWaiters);
		
		//Do("I have "+MyWaiters.size()+" waiters");
		
		
		boolean full = true;
		for(Table t:Tables){
			if(!t.Occupied()){
				full = false;
				break;
			}
		}
		
		//THIS CONDITION ACTUALLY WILL ONLY FIRE IF THE tables ARE OCCUPIED, NOT IF
		//THE LINE IS LONG
		if(full){
			tellCustomersFull();
			return true;
		}
		
		//If a waiter wants a break, decide if he can
		synchronized(MyWaiters){
			for(MyWait mw:MyWaiters){
				if(mw.s==WaitState.wantsBreak){
					decideBreak(mw);
					return true;
				}
				
			}
		}
		
		for(Table t: Tables){
			synchronized(MyCustomers){
				for(MyCust mc:MyCustomers){
					int num_searched = 0;
					if(MyWaiters.size()==0){
						return false;
					}
					synchronized(MyWaiters){
						for(int i = (lastWaiter+1)%MyWaiters.size();num_searched<MyWaiters.size();i = (i+1)%MyWaiters.size()){
							//if there's a free table and a waiting customer
							//and available waiter
							//THIS IS WHERE WAITER ASSIGNMENT HAPPENS
							num_searched++;
							if(!t.Occupied() && (mc.s == CustState.waiting || mc.s==CustState.stillWantsTable) && MyWaiters.get(i).s==WaitState.available){
								seatCustomer(MyWaiters.get(i),mc,t);
								lastWaiter = i;
								return true;
							}
							
							/*if((!t.Occupied()) && mc.s == CustState.waiting && MyWaiters.get(i).s==WaitState.available
									&& (MyWaiters.get(i).numTables < MyWaiters.get((i+1)%MyWaiters.size()).numTables
											|| (i == MyWaiters.size()-1))){
								//seatCustomer()
								seatCustomer(MyWaiters.get(i),mc,t);
								//System.out.println("In the scheduler and past the condition");
								return true;
							}*/
						}
					}
				}
			}
		}
		
		
			
		
		return false;
	}
	
	
	//ACTIONS
	
	//tell customers that restaurant it totally full
	private void tellCustomersFull(){
		synchronized(MyCustomers){
			for(MyCust mc:MyCustomers){
				if(mc!=null && mc.s == CustState.waiting){
					mc.c.msgRestaurantFull();
					mc.s = CustState.toldFull;
				}
			}
		}
	}
	
	//decide if a waiter can take a break. For now, this is a yes unless there's only one waiter
	private void decideBreak(MyWait mw){
		if(countAvailable()>1){
			mw.s = WaitState.canGoOnBreak;
			mw.w.msgBreakAnswer(true);
			DoMessage(mw.w.getName()+" can go on break.");
			return;
		}
		mw.w.msgBreakAnswer(false);
		mw.s = WaitState.available;
		DoMessage(mw.w.getName()+", NO BREAKS.");
	}
	
	//tell a waiter to seat the customer
	private void seatCustomer(MyWait w,MyCust mc,Table t){
		int tn = t.n;
		w.numTables +=1;
		w.w.msgSitAtTable(mc.c,tn);
		t.s = TableState.occupied;
		mc.s = CustState.seated;
		mc.t = t;
	}
	
	//LOG FILTERING
	public void DoInfo(String message){
		//super.Do(message);
		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return person!=null;
	}

//	@Override
//	public boolean canLeave() {
//		// TODO Auto-generated method stub
//		return restaurant.numCustomers==0;
//	}
	
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

}
