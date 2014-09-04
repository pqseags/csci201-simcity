package restaurant.restaurantParker;

import role.Role;
import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import market.MarketInvoice;
import restaurant.restaurantParker.DebtCustomer.DebtCustomerState;
import interfaces.MarketDeliveryMan;
import interfaces.Person;
import interfaces.restaurantParker.*;
import UnitTests.mock.*;


/**
 * Restaurant Cashier Agent
 */

public class CashierRole extends Role implements Cashier{
	
	public EventLog log = new EventLog();
	
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	public List<Payment> payments = Collections.synchronizedList(new ArrayList<Payment>());
	public List<DebtCustomer> debtCustomers = Collections.synchronizedList(new ArrayList<DebtCustomer>());
	public List<CashierMarketBill> marketBills = Collections.synchronizedList(new ArrayList<CashierMarketBill>());
	
	public int change =0;
	RestaurantParker restaurant;
	private String name;
	
	Menu menu = new Menu();
	



	public CashierRole(String name, RestaurantParker r) {
		super();
		this.name = name;
		this.restaurant = r;
		
	}


	public String getName() {
		return name;
	}



	// Messages
	public void msgComputeBill(String choice, Customer cust, Waiter wait){
		log.add(new LoggedEvent("Received msgComputeBill from " + wait.getName()));
		bills.add(new Bill(choice, cust, wait));
		p.msgStateChanged();
	}

	public void msgPayment(int bill, int payment, Customer cust){
		log.add(new LoggedEvent("Received msgPayment from " + cust.getName()));
		payments.add(new Payment(bill, payment, cust));
		p.msgStateChanged();
	}
	
	public void msgIHaveNoMoney(int debt, Customer cust){
		log.add(new LoggedEvent("Received msgIHaveNoMoney from " + cust.getName()));
		debtCustomers.add(new DebtCustomer(debt, cust));
		p.msgStateChanged();
	}
	
	public void msgHereIsMarketBill(int bill, Market market){
		log.add(new LoggedEvent("Received msgHereIsMarketBill from " + market.getName()));
		marketBills.add(new CashierMarketBill(bill, market));
		p.msgStateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
		if (!bills.isEmpty()){
			computeBill(bills.get(0));
			return true;
		}

		if (!payments.isEmpty()){
			computeChange(payments.get(0));
			return true;
		}
		
		synchronized (debtCustomers){
			for (DebtCustomer dc : debtCustomers){
				if (dc.state == DebtCustomerState.present){
					disciplineCustomer(dc);
					return true;
				}
			}
		}
	
		if (!marketBills.isEmpty()){
			payMarketBill(marketBills.get(0));
			return true;
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void computeBill(Bill bill){
		log.add(new LoggedEvent("Computing a bill for " + bill.customer.getName()));
		print("Computing a bill for " + bill.customer.getName() + " for his order of " + bill.choice);
		bills.remove(bill);
		
		int price = menu.menu.get(bill.choice);
		
		//check if customer has previous debt
		synchronized (debtCustomers){
			for (int i=0; i< debtCustomers.size(); i++) {
				if (debtCustomers.get(i).customer == bill.customer){
					print("He still owes debt from last time! Here's what he owes in total from tonight and before.");
					price = price + debtCustomers.get(i).debt;
				}
			}
		}
		
		
		
		
		print(bill.waiter.getName() + ", here is the bill that you requested.");
		bill.waiter.msgHereIsCheck(price, bill.customer);
		
	}

	private void computeChange(Payment payment){
		log.add(new LoggedEvent("Computing change."));
		change = payment.cash - payment.bill;
		print("Received $" + payment.cash + " cash, giving $" + change + " change to " + payment.customer.getName() );
		payment.customer.msgGiveChange(change);
		payments.remove(payment);
	}
	
	private void disciplineCustomer(DebtCustomer dc){
		log.add(new LoggedEvent("Disciplining " + dc.customer.getName()));
		print("You, sir, are a SCUMBAG! It's okay though, you can pay next time. I will remember your debt.");
		dc.state = DebtCustomerState.gone;
		dc.customer.msgGiveChange(1000);
	}
	
	private void payMarketBill(CashierMarketBill marketBill){
		print("I got a bill for $" + marketBill.bill + " from " + marketBill.market.getName() + ". Paying bill in full.");
		log.add(new LoggedEvent("I am paying a bill to " + marketBill.market.getName()));
		marketBill.market.msgBillPayment(marketBill.bill);
		marketBills.remove(marketBill);
	
	}


	@Override
	public void msgHereIsInvoice(MarketDeliveryMan deliveryMan,
			MarketInvoice order) {
		// TODO Auto-generated method stub
		
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


	public void changeShifts(Person p) {
		if (this.p!=null)
			this.p.msgThisRoleDone(this);
		
		this.p = p;
		this.name = p.getName();
		
	}
	
	// Utilities
	protected void print(String message){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_PARKER, name, message, restaurant.cityRestaurant.ID);
	}


}