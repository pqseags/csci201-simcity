package market;

import person.PersonAgent;
import restaurant.Restaurant;
import role.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.MarketMock.MockMarketPerson;
import interfaces.MarketCashier;
import interfaces.MarketCustomer;
import interfaces.MarketEmployee;
import interfaces.Person;

public class MarketCashierRole extends Role implements MarketCashier{

	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	public List<MyBusinessOrder> orders = new ArrayList<MyBusinessOrder>();
	Map<String, Integer> priceList = new HashMap<String,Integer>();
	public Map<Person, Integer> debtorsList = new HashMap<Person,Integer>();
	Market market;
	Person p;
	public String name;
	
	//SETTERS
	public void setName(String name){
		this.name = name;
	}
	
	//GETTERS
	public String getName(){
		return name;
	}
	
	public boolean canLeave() {
//		this.p = null;
//		return true;
		return false;
	}
	
	public boolean isPresent(){
		if (p==null)
			return false;
		else return true;
	}
	
	//CONSTRUCTOR
	public MarketCashierRole(String name, Person p, Market m){
		this.p = p;
		this.name = name;
		this.market = m;
		priceList.put("Steak", 2);
		priceList.put("Pizza", 1);
		priceList.put("Chicken", 2);
		priceList.put("Salad", 1);
		priceList.put("Car", 10);
	}

	
	public boolean YouAreDoneWithShift(){
	
		if(true){
			DoMessage("Being kicked off the job now");
			p.msgThisRoleDone(this);
			this.p = null;
			market.DefaultName(this);
		}
		return true;
		
		
	}
	
	Timer timer = new Timer();
	List<Timer> timers = new ArrayList<Timer>();
	
	public List<BusinessPayment> businessPayments = new ArrayList<BusinessPayment>();
	
	
	
	//Messages
	public void msgServiceCustomer(MarketCustomer c, Map<String, Integer> groceries) {
		//Do("GOT THE SERVICE MESSAGE");
		customers.add(new MyCustomer(c, groceries));
		DoInfo("Got message to service customer " + c.getName());
		log.add(new LoggedEvent("got msgServiceCustomer name: " + c.getName()));
		p.msgStateChanged();
	}
	
	public void msgFinishedComputing(MyCustomer mc){
	    mc.status = CustomerState.hasTotal;
	    DoInfo("Finished computing bill");
	    log.add(new LoggedEvent("done computing bill"));
	    p.msgStateChanged();
	}

	public void msgCustomerPayment(MarketCustomer c, int payment){
	    for (MyCustomer mc: customers){
	    	if (mc.c == c){
	    		mc.payment = payment;
	    	    mc.status = CustomerState.paid;
	    	    DoInfo("Received payment of $" + payment + " from customer " + c.getName());
	    	    log.add(new LoggedEvent("got msgCustomerPayment for: $" + payment));
	    	    break;
	    	}
	    		
	    }	
	    p.msgStateChanged();
	}
	
	public void msgCalculateInvoice(MarketEmployee employee, List<OrderItem> order, Restaurant r){
		DoInfo("Received request to calculate invoice for restaurant");		
		log.add(new LoggedEvent("got msgCalculateInvoice"));
		orders.add(new MyBusinessOrder(order, employee, r));
		p.msgStateChanged();
	}

	public void msgHereIsBusinessPayment(int pay){
		DoInfo("Received business payment of $" + pay);
		BusinessPayment payment = new BusinessPayment(pay);
	    businessPayments.add(payment);
	    p.msgStateChanged();
	}
	
	//-----------------------Scheduler---------------------------
	public boolean pickAndExecuteAnAction() {
		//Do("DECIDING WHAT TO DO "+customers.size());
		if (!orders.isEmpty()){
			ComputeBusinessPayment(orders.get(0));
			return true;
		}
		
		for (MyCustomer mc: customers){
			if (mc.status == CustomerState.needsTotal){
				ComputeTotal(mc);
				return true;
			}
		}
		for (MyCustomer mc: customers){
			if (mc.status==CustomerState.hasTotal){
				AskCustomerToPay(mc);
				return true;
			}
		}
		for (MyCustomer mc: customers){
			if (mc.status == CustomerState.paid){
				AcceptPayment(mc);
				return true;
			}
		}

		if (!businessPayments.isEmpty()){
		    ProcessBusinessPayment(businessPayments.get(0));
		    return true;
		}
		return false;
	}
	
	
	//Actions
	private void ComputeTotal(MyCustomer mc){
		DoInfo("Computing total for customer " + mc.c.getName() + " who ordered " + mc.order.toString());
		log.add(new LoggedEvent("action ComputeTotal"));
	    int total=0;
	    if (debtorsList.containsKey(mc.c.getPerson())){
	    	total+= debtorsList.get(mc.c.getPerson());
            debtorsList.remove(mc.c.getPerson());
	    }
	    
	    for (String item: mc.order.keySet()){
	        total+= mc.order.get(item) * priceList.get(item);
	    }
	    
	    mc.total = total;
	    
	    final MyCustomer cust = mc;
	    
	    //final Timer t = new Timer();
	    //timers.add(t);
	    mc.status = CustomerState.computingTotal;
//	    timer.schedule(new TimerTask(){
//	    	public void run(){
//	    		msgFinishedComputing(cust);
//	    		//timer.cancel();
//	    	}}, mc.order.size()*300);
	    
	    msgFinishedComputing(cust);
	}

	private void ComputeBusinessPayment(MyBusinessOrder order){
		DoMessage("Starting to compute business Payment");
		log.add(new LoggedEvent("action ComputeBusinessPayment"));
		orders.remove(order);
		int total = 0;
		 for (OrderItem item: order.order){
		        total+= item.quantityReceived * priceList.get(item.choice);
		    }
		 if (!(p instanceof MockMarketPerson))
			 DoMessage("This order from " + order.restaurant.cityRestaurant.ID + " will cost $" + total + ". Here is the invoice.");
		 log.add(new LoggedEvent("The order will cost $" + total));
		 order.employee.msgGiveInvoice(order.order, order.restaurant, total);
		
		
		
		
	}
	
	private void AskCustomerToPay(MyCustomer mc){
		DoMessage(mc.c.getName() + ", you owe $" + mc.total);
		log.add(new LoggedEvent("action AskCustomerToPay"));
	    mc.status = CustomerState.askedToPay;
	    mc.c.msgHereIsTotal(mc.total);
	}

	private void AcceptPayment(MyCustomer mc){
		DoInfo("Accepting Payment of $" + mc.payment + " from customer " + mc.c.getName());
		log.add(new LoggedEvent("action AcceptPayment"));
	    int change = mc.payment-mc.total;
	    if (change>=0){
	    	DoMessage(mc.c.getName() + ", here is your change.");
	    	log.add(new LoggedEvent("I owe the customer change: $" + change));
	        mc.c.msgHereIsYourChange(new Receipt(mc.order, mc.total, mc.payment, this), change);
	        customers.remove(mc);

	    }
	    else{
	    	DoInfo("The customer still owes me $" + -change + " and is now on my debtors list");
	    	log.add(new LoggedEvent("The customer still owes me $" + -change + " and is now on my debtors list"));
	    	DoMessage(mc.c.getName() + ", you owe $" + -change);
	        mc.c.msgYouOweMoney(new Receipt(mc.order, mc.total, mc.payment, this), -1*change);
	        debtorsList.put(mc.c.getPerson(), -1*change);
	        customers.remove(mc);
	    }
	}

	private void ProcessBusinessPayment(BusinessPayment payment){
		DoInfo("Processing the business payment in the amount of $" + payment.amount);
		log.add(new LoggedEvent("action ProcessBusinessPayment"));
		log.add(new LoggedEvent("Received payment for total of $" + payment));
	    market.cash+= payment.amount;
	    businessPayments.remove(payment);
	}
	
	//Inner Classes
	public class MyCustomer{
	    public MarketCustomer c;
	    public Map<String, Integer> order;
	    public int total;
	    public int payment;
	    CustomerState status;
	    
	    MyCustomer(MarketCustomer c, Map<String, Integer> order){
	    	this.c = c;
	    	this.order = order;
	    	status = CustomerState.needsTotal;
	    }
	}
	enum CustomerState{needsTotal, computingTotal, hasTotal, askedToPay, paid }

	//Thinking we should pass the entire order over so the cashier knows how much to have expected...
	public class BusinessPayment{
		public int amount;
		
		BusinessPayment(int pay){
			amount = pay;
		}
		
	}

	public class MyBusinessOrder{
		public List<OrderItem> order;
		public MarketEmployee employee;
		public Restaurant restaurant;
		
		public MyBusinessOrder(List<OrderItem> o, MarketEmployee e, Restaurant r){
			order = o;
			employee = e;
			restaurant = r;
		}
	}
	
	public void setMarket(Market market){
		this.market = market;
	}
	
	
	public void DoInfo(String message){
		//super.Do(message);
		if (market.gui!=null)
			AlertLog.getInstance().logInfo(AlertTag.MARKET, name, message, market.gui.ID);
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		if (market.gui!=null)
			AlertLog.getInstance().logMessage(AlertTag.MARKET, name, message, market.gui.ID);
	}
}
