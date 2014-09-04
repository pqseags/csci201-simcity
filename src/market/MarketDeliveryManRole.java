package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

import UnitTests.mock.LoggedEvent;
//import restaurant.Restaurant;
import restaurant.ProducerConsumerMonitor;
import restaurant.restaurantLinda.RestaurantOrder;
import role.Role;
import interfaces.MarketDeliveryMan;
import interfaces.Person;

public class MarketDeliveryManRole extends Role implements MarketDeliveryMan {

	
	//-----------------------------DATA--------------------------------
	public List<MarketInvoice> orders = new ArrayList<MarketInvoice>();
	public List<MyPayment> payments = new ArrayList<MyPayment>();
	private Market market;
	private String name;
	private Person p;
	ProducerConsumerMonitor<MarketInvoice> monitor;
	public boolean checkDock = true;
	public boolean checkPending = true;
	private Timer timer = new Timer();
	public List<MarketInvoice> pendingOrders = new ArrayList<MarketInvoice>();
	
	//DeliveryMan is just going to wait for the restaurant cashier
	private Semaphore receivedPayment = new Semaphore(0,true);
	
	public MarketDeliveryManRole(String name, Person p, Market m){
		this.name = name;
		this.p = p;
		this.market = m;
		
	}
	
	public boolean canLeave() {

		market.deliveryMen.remove(this);
		if (market.deliveryMen.size()==0){
			market.deliveryMen.add(this);
			return false;
		}
		
		DoMessage("Delivery man leaving work");
		return true;
	}
	
	
	//-----------------------------MESSAGES--------------------------------
	/*public void msgDeliverThisOrder(MarketInvoice order){
		log.add(new LoggedEvent("got msgDeliverThisOrder"));
		DoInfo("Received message to deliver order");
		orders.add(order);
		p.msgStateChanged();
		
	}*/
	
	public void msgDockTimerDone(){
		checkDock=true;
		checkPending = true;
		p.msgStateChanged();
	}
	
	
	public void msgHereIsPayment(int payment, MarketInvoice invoice){
		log.add(new LoggedEvent("got msgHereIsPayment"));
		DoInfo("Received payment from restaurant of $" + payment);
		MyPayment pay = new MyPayment(payment, invoice);
		payments.add(pay);
		receivedPayment.release();
		p.msgStateChanged();
		
	}
	
	
	//-----------------------------SCHEDULER--------------------------------
	public boolean pickAndExecuteAnAction() {
		
		
		if (!orders.isEmpty()){
			DeliverOrder(orders.get(0));
			return true;
		}
		
		if (!payments.isEmpty()){
			DeliverPayment(payments.get(0));
			return true;
		}
		
		if (checkDock){
			CheckDockStand();
			return true;
		}
		
		if (checkPending){
			CheckPendingOrders();
			return true;
		}
		
		return false;
	}

	
	//-----------------------------ACTIONS--------------------------------
	private void DeliverOrder(MarketInvoice order){
		DoMessage("Delivering an order to a restaurant");
		log.add(new LoggedEvent("action DeliverOrder"));
		
		orders.remove(order);
		
		if (!order.restaurant.isOpen()){
			DoMessage("The restaurant is closed. I'll have to check back later.");
			log.add(new LoggedEvent("The restaurant is closed. I'll have to check back later."));
			pendingOrders.add(order);
			return;
		}
		
		if (order.restaurant != null) {
			order.restaurant.cook.msgHereIsDelivery(order);
			order.restaurant.cashier.msgHereIsInvoice(this, order);

			try {
				receivedPayment.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
//	private void DoGoToRestaurant(Restaurant rest){
//		//go to the restaurant
//	}
	private void DeliverPayment(MyPayment payment){
		DoGoToCashier();
		DoMessage(market.cashier.getName() + ", here is a business payment.");
		log.add(new LoggedEvent("action DeliverPayment"));
		market.cashier.msgHereIsBusinessPayment(payment.amount);
		
		payments.remove(payment);

		
	}
	
	private void CheckDockStand(){
		log.add(new LoggedEvent("Checking dock stand"));
		if (!monitor.isEmpty()){
			DoInfo("Found a new order");
			log.add(new LoggedEvent("Found a new order"));
			MarketInvoice invoice = monitor.remove();
			orders.add(invoice);
		}
		else{
			checkDock = false;
			timer.schedule(new TimerTask(){
				public void run(){
					msgDockTimerDone();
				}
			}, 5000);
		}
	}
	
	private void CheckPendingOrders(){
		checkPending = false;
		log.add(new LoggedEvent("Checking pending orders"));
		MarketInvoice temp = null;
		for(MarketInvoice order : pendingOrders){
			if (order.restaurant.isOpen()){
				DoMessage("It looks like a restaurant has opened, I need to deliver their order.");
				DeliverOrder(order);
				temp = order;
				break;
			}
		}
		if (temp!=null)
			pendingOrders.remove(temp);
	}
	
	private void DoGoToCashier(){
		//go back to the market, go to the cashier
		
	}
	
	
	
	//-----------------------------UTILITIES--------------------------------
	public String getName(){
		return name;
	}
	
	public void setMarket(Market m){
		market = m;
	}
	
	private class MyPayment {
		int amount;
		MarketInvoice invoice;
	
		MyPayment(int payment, MarketInvoice invoice){
			amount = payment;
			this.invoice = invoice;			
		}
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
	
	public void DoDebug(String message){
		if (market.gui!=null)
			AlertLog.getInstance().logDebug(AlertTag.MARKET, name, message, market.gui.ID);
	}


	public void setMonitor(ProducerConsumerMonitor<MarketInvoice> m){
		monitor = m;
	}
	

}
