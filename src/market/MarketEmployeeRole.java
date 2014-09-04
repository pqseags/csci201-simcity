package market;

import java.util.ArrayList;

import interfaces.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

import UnitTests.mock.LoggedEvent;
import UnitTests.mock.MarketMock.MockMarketPerson;
import market.gui.MarketEmployeeGui;
import person.PersonAgent;
import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import role.Role;
import interfaces.MarketCustomer;
import interfaces.MarketDeliveryMan;
import interfaces.MarketEmployee;
import interfaces.Person;

public class MarketEmployeeRole extends Role implements MarketEmployee{

	public List<CustomerOrder> customerOrders = Collections.synchronizedList(new ArrayList<CustomerOrder>());
	public List<MyBusinessOrder> businessOrders = new ArrayList<MyBusinessOrder>();
	public Person p;
	public String name;
	private Market market;
	public boolean inEmployeeList;
	
	//List<MarketDeliveryMan> deliveryMen = new ArrayList<MarketDeliveryMan>();
	ProducerConsumerMonitor<MarketInvoice> monitor;
	public MarketCashier cashier; 
	
	private MarketEmployeeGui gui;
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	//private Semaphore receivedInvoice = new Semaphore(0,true);
	
	boolean startedWorking = false;
	
	//SETTERS
	public void setName(String name){
		this.name = name;
	}
	
	public void setGui(MarketEmployeeGui g){
		gui = g;
		//this.gui = null;
	}
	public void setCashier(MarketCashier cash){
		cashier = cash;
	}
	
	/*public void addDeliveryMan(MarketDeliveryMan d){
		deliveryMen.add(d);
	}*/
	
	public void setPerson(PersonAgent p){
		this.p= p;
	}
	
	//GETTERS
	public String getName(){
		return name;
	}
	
	public boolean canLeave() {
		Do("Trying to GET OFF WORK");
		boolean busy;
		
		if(!inEmployeeList){
			busy = !(customerOrders.isEmpty() && businessOrders.isEmpty());
			return !busy;
		}
	
		
		if(!market.employeeLeaving(this)){
			
			Do("This is weird, MarketHostRole @ line 76");
			
			//System.err.println();
		}
		
		inEmployeeList = false;
		busy = !(customerOrders.isEmpty() && businessOrders.isEmpty());
		
		Do("CANLEAVE RETURNING "+!busy);
		
		if(busy){
			return false;
		}
		
		market.employeeLeft(this);
		
		return true;
	
		
	}
	
	public MarketEmployeeGui getGui(){
		return gui;
	}
	
	
	//constructor
	public MarketEmployeeRole(String name, Person p){
		this.name = name;
		this.p = p;
	}
	
	//Messages
	public void msgGetItemsForCustomer(MarketCustomer c, Map<String, Integer> orderList){
	    Do("Better get the customer's items");
	    log.add(new LoggedEvent("got msgGetItemsForCustomer from " + c.getName()));
		customerOrders.add(new CustomerOrder(c, orderList));
		p.msgStateChanged();
	}

	public void msgGetThis(List<OrderItem> order, Restaurant r){
		log.add(new LoggedEvent("got msgGetThis with an order size of " + order.size()));
	    businessOrders.add(new MyBusinessOrder(order, r));
	    p.msgStateChanged();
	}
	
	public void msgGiveInvoice(List<OrderItem> order, Restaurant r, int total){

		//receivedInvoice.release();

		log.add(new LoggedEvent("got msgGiveInvoice with order size: " + order.size() + " for a total of: $" + total));

		for(MyBusinessOrder o : businessOrders){
			if (o.order.equals(order) && o.restaurant==r){
				o.invoice = new MarketInvoice(o.order, market, o.restaurant, total);
				o.state = OrderState.gotInvoice;
			}
		}
		p.msgStateChanged();
		
	}
	//fromAnimation
	public void msgAtDestination(){
		atDestination.release();
		//p.msgStateChanged();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction() {
		
		/*if(!startedWorking){
			startedWorking = market.CanIStartWorking(this);
			return startedWorking;
		}*/
		
		//Do("SCHEDULER");
		for (CustomerOrder co: customerOrders){
			if (co.status == CustomerOrderState.fulfilled){
				GiveItemsToCustomer(co);
				return true;
			}
		}
		
		synchronized(customerOrders){
			for (CustomerOrder co: customerOrders){
				if (co.status == CustomerOrderState.none){
					CollectItems(co);
					return true;
				}
			}
		}
		
		for(MyBusinessOrder bo : businessOrders){
			if (bo.state == OrderState.gotInvoice){
				PlaceOrderOnDock(bo);
				return true;
			}
		}
		
		for(MyBusinessOrder bo : businessOrders){
			if (bo.state == OrderState.ordered){
			    GetBusinessOrder(bo);
			    return true;
			}
		}
		return false;
	}
	
	//Actions
	private void CollectItems(CustomerOrder co){
		
		//System.err.println("Am collecting items");
	    co.status = CustomerOrderState.fulfilled;
	
		
		//^^^this should go inside the for loop for each item needing to be collected
		
	    for (String item: co.order.keySet()){
	    	//DoCollectItem(item, co.order.get(item));
	    	if(gui!=null){
				gui.DoGetItem(item);
			}
			else{
				atDestination.release();
			}
			
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	//Do("Collecting " + co.order.get(item) + " " + item + "s.");
	    }
	    Do("Collecting items");
	    log.add(new LoggedEvent("action CollectItems"));
	    log.add(new LoggedEvent("collecting item"));
	    
	   
	}

	
	private void GiveItemsToCustomer(CustomerOrder co){
		
		log.add(new LoggedEvent("action GiveItemsToCustomer"));
		if(gui!=null){
			gui.DoGiveCustomerItems();
		}
		else{
			atDestination.release();
		}
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	    
	    Do("Giving " + co.c.getName() + " their order.");
	    co.c.msgHereAreItems(co.order);
	    
	    if(gui!=null){
			gui.DoGoHomePosition();
		}
	    
	    
	    customerOrders.remove(co);
	

	}

	private void GetBusinessOrder(MyBusinessOrder order){
		
		order.state = OrderState.none;
		Do("Better fill this business order");
		log.add(new LoggedEvent("action GetBusinessOrder"));
		
		//Discuss changing this so that BusinessOrder is no longer public
		for (OrderItem item: order.order){
			if(gui!=null){
				gui.DoGetItem(item.choice);
			}
			else{
				atDestination.release();
			}
			
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Do("Collecting " + item.quantityReceived + " " + item.choice + "s.");
	    }
		
		/////////--------------------------after getting the order-----------------//////////////
	    
	    Do("Got all of the items in the order.");
	    
	    if(gui!=null){
			gui.DoGoToCashier();
		}
		else{
			atDestination.release();
		}
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		//got to cashier
		//DoMessage(market.cashier.getName() + ", can you please calculate the invoice for this order?");
		market.cashier.msgCalculateInvoice(this, order.order, order.restaurant);

		if (gui!=null)
			gui.DoGoHomePosition();


	
	}
	
	private void PlaceOrderOnDock(MyBusinessOrder order){
		log.add(new LoggedEvent("action PlaceOrderOnDock"));
		DoMessage("Placing order on dock");
		businessOrders.remove(order);
		
		 if(gui!=null){
				gui.DoGoToDock();
			}
			else{
				atDestination.release();
			}
			
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			Do("Putting completed order and invoice on dock for delivery man.");
			/*if (deliveryMen.isEmpty()){
				Do("There are no delivery men.");
				
				if(gui!=null){
					gui.DoGoHomePosition();
				}
//				else{
//					atDestination.release();
//				}
//				
//				try {
//					atDestination.acquire();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				return;
			}*/
			//otherwise...
			//load balance deliverymen
			//deliveryMen.get(0).msgDeliverThisOrder(order.invoice);
			monitor.insert(order.invoice);
			
			if(gui!=null){
				gui.DoGoHomePosition();
			}
//			else{
//				atDestination.release();
//			}
//			
//			try {
//				atDestination.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	}
	
	
	//Inner classes
	class CustomerOrder{
	    MarketCustomer c;
	    Map<String, Integer> order;
	    CustomerOrderState status;
	    
	    CustomerOrder(MarketCustomer c, Map<String, Integer> order){
	    	this.c = c;
	    	this.order = order;
	    	status = CustomerOrderState.none;
	    }
	}
	enum CustomerOrderState {none,  fulfilled};
	
	class MyBusinessOrder{
		List<OrderItem> order = new ArrayList<OrderItem>();
		Restaurant restaurant;
		MarketInvoice invoice;
		OrderState state = OrderState.ordered;
		
		MyBusinessOrder(List<OrderItem> o, Restaurant r){
			this.order = o;
			this.restaurant = r;
		}
		
	}
	public enum OrderState {ordered, acquired, gotInvoice, none};
	
	public void setMarket(Market market) {
		// TODO Auto-generated method stub
		this.market = market;
		
	}
	
	public void setMonitor(ProducerConsumerMonitor<MarketInvoice> m){
		monitor = m;
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
