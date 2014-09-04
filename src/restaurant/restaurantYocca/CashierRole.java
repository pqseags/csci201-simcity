package restaurant.restaurantYocca;

import agent.Agent;
import restaurant.restaurantYocca.CustomerRole.AgentEvent;
import restaurant.restaurantYocca.CustomerRole.AgentState;
//import restaurant.restaurantYocca.gui.AnimationPanel;
import restaurant.restaurantYocca.gui.CustomerGui;
import restaurant.restaurantYocca.gui.WaiterGui;
import interfaces.BaseRestaurantCashier;
import interfaces.MarketDeliveryMan;
import interfaces.Person;
import interfaces.restaurantYocca.Cashier;
import interfaces.restaurantYocca.Customer;
import interfaces.restaurantYocca.Waiter;
import role.Role;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Semaphore;

import market.MarketInvoice;

/**
 * Restaurant Cook Agent
 */

public class CashierRole extends Role implements Cashier {

	public List<Bill> checkList
	= Collections.synchronizedList(new ArrayList<Bill>());
	public List<Bill> marketBills = Collections.synchronizedList(new ArrayList<Bill>());

	Timer timer = new Timer();
	
	private Waiter waiter;
	
	private RestaurantYocca restaurant;
	
	public String name;
	
//	private AnimationPanel animationPanel = null;
	
	private double restaurantMoney = 0;

	public CashierRole(String name, RestaurantYocca restaurantYocca) {
		super();
		this.name = name;
		restaurant = restaurantYocca;
	}

	public CashierRole(String name) {
		super();
		this.name = name;	
	}

	public String getName() {
		return name;
	}

	
	// Messages
	
	public void msgComputeCheck(String choice, Customer cust, Waiter w) {
		Bill c = new Bill(choice, cust, w);
		checkList.add(c);
		p.msgStateChanged();
	}
	
	public void msgPayment(Bill c, double money) {
		restaurantMoney += money;
		synchronized(checkList) {
		for (Bill checks: checkList) {
			if (checks.customer.getCustomerTableNum() == c.customer.getCustomerTableNum()) {
				checks.setAmountPaid(money);
				checks.setAmountToPay(c.getAmountToPay() - money);
				checks.setPaid(true);
				checks.setGivenChange(false);
			}
		}
		}
		p.msgStateChanged();
	}
	
	public void msgHereIsMarketBill(Bill b) {
		marketBills.add(b);
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	*/
	public boolean pickAndExecuteAnAction() {
		synchronized(checkList) {
		try{
		for (Bill c: checkList) {
			if (!c.isComputing()) {
				computeCheck(c);
				c.setComputing(true);
			}
			if (c.isComputing() && c.hasPaid() && !c.hasGivenChange()) {
				Do("Going to give change");
				giveChange(c);
				c.setGivenChange(true);
			}
		}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		}
		synchronized(marketBills) {
		try {
		for (Bill b: marketBills) {
			payMarketBill(b);
		}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		return false;
		}
	}
	
	// Actions
	
	private void computeCheck(Bill c) {
		Do("Computed Check");
		c.waiter.msgCheckComputed(c);
	}
	
	private void giveChange(Bill c) {
		if (c.getAmountPaid() >= c.customer.getAmountOwed()) {
			Do("Thank you for paying in full. Have a good day!");
			BigDecimal bd = new BigDecimal(c.getAmountPaid() - c.customer.getAmountOwed());
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			double temp = bd.doubleValue();
			c.customer.setAmountOwed(0);
			c.customer.msgHereIsYourChange(temp);
			BigDecimal bd2 = new BigDecimal(restaurantMoney -= (temp));
			bd2 = bd2.setScale(2,BigDecimal.ROUND_HALF_UP);
			restaurantMoney = bd2.doubleValue();
			Do("Restaurant money after transaction: " + restaurantMoney);
			//animationPanel.setMoneyString(Double.toString(restaurantMoney));
			checkList.remove(c);
		}
		else {
			Do("Pay me next time you come back");
			BigDecimal bd = new BigDecimal(c.customer.getAmountOwed() - c.getAmountPaid());
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			double temp = bd.doubleValue();
			c.customer.setAmountOwed(temp);
			c.customer.msgHereIsYourChange(0);
			Do("Restaurant money after transaction: " + restaurantMoney);
			//animationPanel.setMoneyString(Double.toString(restaurantMoney));
			checkList.remove(c);
		}
	}
	
	public void payMarketBill(Bill b) {
		if (b.getAmountToPay() <= restaurantMoney) {
			b.market.cashier.msgHereIsBusinessPayment((int) b.getAmountToPay());
			Do("Here is the full payment for the market order");
			BigDecimal bd = new BigDecimal(restaurantMoney - b.getAmountToPay());
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			restaurantMoney = bd.doubleValue();
			marketBills.remove(b);
		}
		else if (b.getAmountToPay() > restaurantMoney && restaurantMoney > 0) {
			b.market.cashier.msgHereIsBusinessPayment((int) restaurantMoney);
			Do("Here is partial payment for the market order");
			BigDecimal bd = new BigDecimal(b.getAmountToPay() - restaurantMoney);
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			b.setAmountToPay(bd.doubleValue());
			restaurantMoney = 0;
		}
	}
	
	public void setRestaurantMoney(double a) {
		restaurantMoney = a;
	}
	
	public double getRestaurantMoney() {
		return restaurantMoney;
	}
	
	public void setWaiter(Waiter w) {
		waiter = w;
	}
	
//	public void setAnimationPanel(AnimationPanel a) {
//		animationPanel = a;
//	}

	public void changeShifts(Person p) {
		if (this.p!=null){
			this.p.msgThisRoleDone(this);
		}
		this.p = p;
		this.name = p.getName();
	}

	@Override
	public void msgHereIsInvoice(MarketDeliveryMan deliveryMan,
			MarketInvoice order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		return this.p!=null;
	}

	@Override
	public boolean canLeave() {
		return false;
	}

	public boolean YouAreDoneWithShift(){
		
		if(true){
			Do("Being kicked off the job now");
			p.msgThisRoleDone(this);
			this.p = null;
			//market.DefaultName(this);
		}
		return true;
	}
}

