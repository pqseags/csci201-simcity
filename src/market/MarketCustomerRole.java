package market;

import interfaces.MarketCashier;
import market.gui.*;
import interfaces.MarketCustomer;
import interfaces.MarketHost;
import interfaces.Person;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import UnitTests.mock.LoggedEvent;
import UnitTests.mock.MarketMock.MockMarketHost;
import person.PersonAgent;
import public_Object.Food;
import role.Role;

public class MarketCustomerRole extends Role implements MarketCustomer {
	public Person p;
	public RoleState state;
	public enum RoleState {JustEnteredMarket, Ordered, ReceivedItems, WaitingForTotal, Paying, Leaving, Done}
	public RoleEvent event;
	public enum RoleEvent {none, itemsArrived, askedToPay, paymentReceived, allowedToLeave }
	public Map<String, Integer> shoppingList = new HashMap<String, Integer>();    
	public Map<String, Integer> groceries = new HashMap<String,Integer>();
	public int bill;
	public Receipt receipt;
	public Market market;
	public MarketHost host;
	MarketCashier cashier;
	public String name;
	public boolean sad = false;
	boolean noEmployees = false;
	
	public MarketCustomerGui gui;
	private Semaphore atDestination = new Semaphore(0,true);
	
	public void addToShoppingList(String food, int amount){
		this.shoppingList.put(food, amount);
	}

	public MarketCustomerRole(String name, Person p){		
		this.name = name;
		this.p = p;
	}
	
	public void msgHereAreItems(Map<String, Integer> groceries){
		Do("Got my MARKET items");
		log.add(new LoggedEvent("got msgHereAreItems total items: " + groceries.size()));
		this.event = RoleEvent.itemsArrived;
	    this.groceries = groceries;
	    //this.cashier = cashier;
	    p.msgStateChanged();
	}

	public void msgHereIsTotal(int total){
		Do("Got the MARKET bill");
		log.add(new LoggedEvent("Got total for: $" + total));
	    bill = total;
	    event = RoleEvent.askedToPay;
	    p.msgStateChanged();
	}

	public void msgHereIsYourChange(Receipt receipt, int change){
		Do("got change");
		log.add(new LoggedEvent("got msgHereIsYourChange"));
	    this.receipt = receipt;
	    p.addToWallet(change);
	    event = RoleEvent.paymentReceived;
	    p.msgStateChanged();
	}

	public void msgYouOweMoney(Receipt receipt, int debt){
		log.add(new LoggedEvent("got msgYouOweMoney amount: $" + debt));
	    this.receipt = receipt;
	    event = RoleEvent.paymentReceived;
	    p.msgStateChanged();
	}

	public void msgYouCanLeave(){
	
		log.add(new LoggedEvent("got msgYouCanLeave"));
	    event = RoleEvent.allowedToLeave;
	    p.msgStateChanged();
	}

	public void msgOutOfStock(Map<String, Integer> unfullfillable){
		//what do I do if they don't have what I want??
		p.msgStateChanged();
	}
	
	public void msgWeHaveNothing(){
		log.add(new LoggedEvent("got msgWeHaveNothing"));
		sad = true;
		p.msgStateChanged();
	}
	
	public void msgNoEmployees(){
		noEmployees = true;
		p.msgStateChanged();
	}
	
	public void msgYouAreAtMarket(Market m){
		Do("I'm at the market.");
		log.add(new LoggedEvent("got msgYouAreAtMarket"));
		setMarket(m);
		state = RoleState.JustEnteredMarket;
		p.msgStateChanged();
	}
	
	//from animation
	public void msgAtDestination(){
		//System.err.println("got msgAtDestination");
		atDestination.release();
		//System.err.println("released semaphore");
		//p.msgStateChanged();
		
	}
	//--------Scheduler-------

	
	public boolean pickAndExecuteAnAction() {
		if(noEmployees){
			tryAgainLater();
			return false;
		}
		if (state==RoleState.JustEnteredMarket && host!=null){
		    state = RoleState.Ordered;        
		    MakeOrder();
		    return true;
		}
		if (state==RoleState.Ordered && event == RoleEvent.itemsArrived){
		    state = RoleState.WaitingForTotal;         
		    GoPay();
		    return true;
		}
		if (state == RoleState.WaitingForTotal && event == RoleEvent.askedToPay){
		    state = RoleState.Paying;         
		    MakePayment();
		    return true;
		}
		if (state == RoleState.Paying && event == RoleEvent.paymentReceived){
		    state = RoleState.Leaving;
		    TryToLeave();
		    return true;
		}
		if (state == RoleState.Leaving && event == RoleEvent.allowedToLeave){
		    state = RoleState.Done;
			LeaveMarket();
		    return true;
		}

		if (sad){
			LeaveSad();
			return true;
		}
		return false;
	}
	
	
	//Actions
	protected void tryAgainLater(){
		noEmployees = false;
		this.state = RoleState.JustEnteredMarket;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.msgYouAreAtMarket(this.market);
	}
	
	private void MakeOrder(){
		//enter door
		
		
		//System.err.println("Walking in the door");
		
		if(gui!=null){
			Do("Calling gui");
			gui.DoGoToExit();
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
		//go to host
		
		//System.err.println("Got to the entrance, now to the host");
		if(gui!=null){
			gui.DoGoToHost();
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

		
		//System.err.println("Got to the host");
		
		if(!market.host.isPresent() && !(market.host instanceof MockMarketHost)){
			tryAgainLater();
			return;
		}
		
		Do("Giving my order to the host.");
		log.add(new LoggedEvent("MakeOrder action"));
     	host.msgCustomerWantsThis(this, shoppingList);
     	
     	
     	if(gui!=null){
     		gui.DoGoToItemDrop();
     	}
     	
	}

	private void GoPay(){
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

		Do("Hi " + cashier.getName() + ", how much do I owe you?");
		log.add(new LoggedEvent("action GoPay"));
	    cashier.msgServiceCustomer(this, groceries);
	}

	private void MakePayment(){                //Right now markets letting customer do an IOU
	    
		Do("Making payment");
		log.add(new LoggedEvent("action MakePayment"));
		int payment;
	    if (p.getWalletAmount()>=bill)
	        payment = bill;
	    else
	        payment = p.getWalletAmount();

	    p.takeFromWallet(payment);
	    cashier.msgCustomerPayment(this, payment);    
	}

	private void TryToLeave(){
		//System.err.println("MarketCustomerRole.TryToLeave");
		if(gui!=null){
			gui.DoGoToExit();
		//	System.err.println("Go to the exit.");
		}
		else{
			atDestination.release();
			//System.err.println("Releasgin the semaphor cause no gui");
		}
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.err.println("Aquired atDestination");
		Do("Okay, time to leave!");
		log.add(new LoggedEvent("action TryToLeave"));
	    host.msgCustomerLeaving(this, receipt, groceries);
	}

	private void LeaveMarket(){
		for (String item: groceries.keySet()){
			p.putInBag(item, groceries.get(item));
		}

	    log.add(new LoggedEvent("action LeaveMarket"));

	    
		if(gui!=null){
			gui.DoExitRestaurant();
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
		market.deleteCustomer(this);
		p.msgThisRoleDone(this);
		
	}
	
	private void LeaveSad(){
		if(gui!=null){
			gui.DoGoToExit();
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
		market.removeCustomer(this);
		//JUST ADDED
		if(gui!=null){
			gui.DoExitRestaurant();
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
		

		//System.err.println("Leaving market sad.");
		p.msgThisRoleDone(this);
	    log.add(new LoggedEvent("action LeaveSad"));
		sad = false;
		
		
		//HACK--------give yourself some food
	
	
		
	}
	//Utilities
	public Person getPerson(){
		return p;
	}
	public void setHost(MarketHost host2) {
		host = host2;
		
	}public void setGui(MarketCustomerGui g){
		gui = g;
		//this.gui = null;
	}
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setCashier(MarketCashier cashier){
		this.cashier = cashier;
	}
	
	public void setMarket(Market m){
		this.market = m;
		this.host = m.host;
		this.cashier = m.cashier;
	}
}
