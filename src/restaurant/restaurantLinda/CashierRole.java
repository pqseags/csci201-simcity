package restaurant.restaurantLinda;

import UnitTests.mock.EventLog;
import UnitTests.mock.LoggedEvent;
import agent.Agent;

import interfaces.MarketDeliveryMan;
import interfaces.Person;
import interfaces.restaurantLinda.Cashier;
import interfaces.restaurantLinda.Customer;
import interfaces.restaurantLinda.Waiter;

import java.util.*;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

import market.MarketInvoice;


import restaurant.Restaurant;
import role.Role;



public class CashierRole extends Role implements Cashier{
	private String name;
	private Map<String,Integer> priceList = new HashMap<String,Integer>();
	private List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<MyBill> myBills = Collections.synchronizedList(new ArrayList<MyBill>());
	private Map<Customer, Integer> debtors = new HashMap<Customer, Integer>();
	private Timer timer = new Timer();
	RestaurantLinda restaurant;
	
	public CashierRole(String name, RestaurantLinda r) {
		super();	
		priceList.put("Steak", 16);
		priceList.put("Chicken", 11);
		priceList.put("Salad", 6);
		priceList.put("Pizza", 9);
		this.name = name;
		this.restaurant = r;
	}
	
	//Overloaded function with person
	public CashierRole(String name, Person p, RestaurantLinda r) {
		super();	
		priceList.put("Steak", 1599);
		priceList.put("Chicken", 1099);
		priceList.put("Salad", 599);
		priceList.put("Pizza", 899);
		this.name = name;
		this.p = p;
		this.restaurant = r;
	}
	
	//messages
	public void msgPleaseComputeBill(Waiter w, String choice, Customer cust){
		DoInfo("Received request to compute bill from waiter " + w.getName() + " for customer " + cust.getName() + " who ordered " + choice);
		bills.add(new Bill(w,choice,cust));
		p.msgStateChanged();
	}
	
	public void msgHereIsPayment(Customer cust, Check bill, int payment){
		DoInfo("Received payment of $" + payment + " from customer " + cust.getName() + " for the bill " + bill);
		customers.add(new MyCustomer(cust, bill, payment));
		p.msgStateChanged();
	}
	
	public void msgTimerDone(Bill b){
		DoInfo("Finished computing bill for waiter " + b.w.getName() + " for customer " + b.cust.getName() + ". For the order " + b.choice + ", the total is $" + b.total);
		b.status=BillState.computed;
		p.msgStateChanged();
	}
	
	public void msgHereIsInvoice(MarketDeliveryMan deliveryMan, MarketInvoice order) {
		DoInfo("Received invoice from market delivery man to pay $" + order.total + " for order of " + order.order);
		myBills.add(new MyBill(order, deliveryMan));
		p.msgStateChanged();
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(myBills){
			for (MyBill b: myBills){
				if (b.status==MyBillState.newlyReceived){
					PayBill(b);
					return true;
				}
			}
		}
		synchronized(myBills){
			for (MyBill b: myBills){
				if (b.status==MyBillState.partiallyPaid && restaurant.cash>=b.owed){
					PayBill(b);
					return true;
				}
			}
		}
		synchronized(bills){
			for (Bill b: bills){
				if (b.status==BillState.computed){
					NotifyWaiter(b);
					return true;
				}
			}
		}
		if (!customers.isEmpty()){
			AcceptPayment(customers.get(0));
			return true;
		}
		synchronized(bills){
			for (Bill b: bills){
				if (b.status==BillState.pending){
					ComputeBill(b);
					return true;
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	//actions
	private void ComputeBill(Bill b){
		DoInfo("Computing bill from waiter " + b.w.getName() + " for customer " + b.cust.getName() + " who ordered " + b.choice);
		b.status=BillState.computing;
		int debt = 0;
		if (debtors.containsKey(b.cust)){
			debt=debtors.get(b.cust);
			debtors.remove(b.cust);
		}
		
		b.total = priceList.get(b.choice) + debt;
		
		final Bill bill = b;
		timer.schedule(new TimerTask(){
			public void run(){
				msgTimerDone(bill);
			}
		}, 3000);
	}
	
	private void NotifyWaiter(Bill b){
		DoMessage("Notifying waiter of finished bill for customer " + b.cust.getName() + " who ordered " + b.choice + ". Total is $" + b.total);
		b.w.msgHereIsBill(b.choice, b.cust, b.total);
		b.status=BillState.done;
	}
	
	private void AcceptPayment(MyCustomer mc){
		DoMessage("Processing payment from customer. Payment received: $" + mc.payment);
		restaurant.cash+=mc.payment;
		int debt = mc.bill.getTotal()-mc.payment;
		if (mc.bill.getTotal()>mc.payment){
			Do(mc.c.getName()+" owes $"+debt);
			
			if (debtors.containsKey(mc.c))
				debtors.put(mc.c, debtors.get(mc.c)+debt);
			else
				debtors.put(mc.c, debt);
		}
		
		mc.c.msgPaymentReceived(debt);
		customers.remove(mc);
	}
	
	private void PayBill(MyBill b){
	
		int payment;
		
		if (restaurant.cash >= b.owed){
			payment = b.order.total;
			myBills.remove(b);
		}
		else{
			payment = restaurant.cash;
			b.owed=b.order.total-payment;
			b.status = MyBillState.partiallyPaid;
		}
		
		restaurant.cash -= payment;
			
		DoMessage("Paying market $" + payment + " for food shipment. Still owe " + b.owed + ". $" + restaurant.cash + " left in cash.");
		
		b.deliveryMan.msgHereIsPayment(payment, b.order);
		
	}
	
	
	//inner classes
	public class Bill{
		Waiter w;
		String choice;
		Customer cust;
		int total=0;
		BillState status;
		
		public Bill(Waiter w, String choice, Customer cust){
			this.w=w;
			this.choice=choice;
			this.cust=cust;
			status = BillState.pending;
		}
		public boolean equals (Object anObject){
			if (anObject instanceof Bill){
				Bill b = (Bill) anObject;
				return b.w==w && b.choice.equals(choice) && b.cust == cust && b.total==total && b.status.equals(status);
			}
			return false;
		}
	}
	enum BillState{pending, computing, computed, done}
	
	public class MyCustomer{
		Customer c;
		Check bill;
		int payment;
		
		public MyCustomer(Customer cust, Check bill, int payment){
			c=cust;
			this.bill = bill;
			this.payment = payment;
		}
		
		public boolean equals (Object anObject){
			if (anObject instanceof MyCustomer){
				MyCustomer mc = (MyCustomer) anObject;
				return mc.c==c && mc.bill.equals(bill) && mc.payment==payment;
			}
			return false;
		}
	}
	
	public class MyBill{
		MarketInvoice order;
		MarketDeliveryMan deliveryMan;
		int owed;
		public MyBillState status;
		
		public MyBill(MarketInvoice o, MarketDeliveryMan deliveryMan){
			this.order = o;
			this.deliveryMan = deliveryMan;
			status = MyBillState.newlyReceived;
		}
		
		public boolean equals (Object anObject){
			if (anObject instanceof MyBill){
				MyBill mb = (MyBill) anObject;
				return order.equals(mb.order);
			}
			return false;
		}
	}
	public enum MyBillState {newlyReceived, fullyPaid, partiallyPaid};
	
	//utilities
	
	public List<Bill> getBills(){
		return bills;
	}
	
	public List<MyCustomer> getCustomers(){
		return customers;
	}
	
	public Map<Customer, Integer> getDebtors(){
		return debtors;
	}
	
	public void addDebtor(Customer c,int debt){
		debtors.put(c, debt);
	}
	
	public List<MyBill> getMyBills(){
		return myBills;
	}
	
	public void setCash(int c){
		restaurant.cash = c;
		p.msgStateChanged();
	}
	
	public int getCash(){
		return restaurant.cash;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean canLeave() {
		return false;
	}
	
	public void changeShifts(Person p){
		if (this.p!=null){
			this.p.msgThisRoleDone(this);
		}
		
		this.p = p;
		this.name = p.getName();
	}
	
	public boolean isPresent(){
		return this.p!=null;
	}
	
	public void setRestaurant(RestaurantLinda r){
		this.restaurant = r;
	}
	
	public void setPerson(Person p){
		this.p = p;
	}
	
	
	public void DoInfo(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_LINDA, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_LINDA, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));		
	}
}

