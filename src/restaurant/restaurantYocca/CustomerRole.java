package restaurant.restaurantYocca;

import restaurant.Restaurant;
import restaurant.restaurantLinda.CustomerRole.AgentEvent;
import restaurant.restaurantYocca.gui.CustomerGui;
//import restaurant.restaurantYocca.gui.RestaurantGui;
import interfaces.Person;
import interfaces.restaurantYocca.Cashier;
import interfaces.restaurantYocca.Customer;
import interfaces.restaurantYocca.Host;
import interfaces.restaurantYocca.Waiter;
import role.Role;
import agent.Agent;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int xTableLoc;
	private int yTableLoc;
	private int outOfChoice;
	private double amountOwed = 0;
	private boolean hasGoneToRestaurant = false;

	public RestaurantYocca restaurant = null;

	// agent correspondents
	private Waiter waiter;
	private Host host;
	private Cashier cashier = null;
	private double money;

	public boolean isAtWArea = false;

	private Bill customerCheck;

	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atWaitingArea = new Semaphore(0,true);

	//Table the Customer is occupying
	public Table t;
	private int cTableNum = 0;

	//Food choice
	private String choice;
	Menu menu = null;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, ReadyToOrderAgain, Ordered, Eating, WaitingForCheck, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, orderingFood, reOrderingFood, receivedFood, nowEating, doneEating, receivedCheck, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param animation  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(String name, Person person){
		super();
		this.name = name;
		double temp = 10 + (double)(Math.random() * 10);
		BigDecimal bd = new BigDecimal(temp);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.money = bd.doubleValue();
		p = person;
	}

	public CustomerRole(String name) {
		super();
		this.name = name;
		double temp = 10 + (double)(Math.random() * 10);
		BigDecimal bd = new BigDecimal(temp);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.money = bd.doubleValue();
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	public String getCustomerName() {
		return name;
	}

	public double getMoney() {
		return money;
	}

	public void setAmountOwed(double amount) {
		amountOwed = amount;
	}


	public void addAmountOwed(double amount) {
		amountOwed += amount;
	}

	public double getAmountOwed() {
		return amountOwed;
	}

	public void setTableLocation(int tableNum) {
		setCustomerTableNum(tableNum);
		xTableLoc = customerGui.getTableXLocation(cTableNum);
		yTableLoc = customerGui.getTableYLocation(cTableNum);
	}

	public int getCustomerTableNum() {
		return cTableNum;
	}

	private void setCustomerTableNum(int tableNum) {
		cTableNum = tableNum;
	}

	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		if (hasGoneToRestaurant == true && amountOwed > 0) {
			double temp = 20 + (double)(Math.random() * 5);
			BigDecimal bd = new BigDecimal(temp);
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.money += bd.doubleValue();
			//			customerGui.getGui().infoLabel.setText(
			//                "<html><pre> C: " + this.getName() + " ($" + this.getMoney() + ") </pre></html>");
		}
		hasGoneToRestaurant = true;
		p.msgStateChanged();
	}

	//newer SimCity message that role is at the restaurant
	public void msgAtRestaurant(Restaurant restaurantYocca){
		//System.out.println("Got the message that I'm here");
		//System.out.println(((RestaurantGabe)rg).host == null);
		this.restaurant = (RestaurantYocca) restaurantYocca;
		state = AgentState.DoingNothing;
		event = AgentEvent.gotHungry;
		p.msgStateChanged();
	}


	public void msgAtCashier() {
		customerGui.setArrived(true);
		atCashier.release();// = true;
		p.msgStateChanged();
	}

	public void msgFollowMeToTable(Menu m) {
		menu = m;
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		p.msgStateChanged();
	}


	public void msgWhatWouldYouLike() {
		event = AgentEvent.orderingFood;
		p.msgStateChanged();
	}

	public void msgWhatWouldYouLikeInstead() {
		event = AgentEvent.reOrderingFood;
		state = AgentState.ReadyToOrderAgain;
		p.msgStateChanged();
	}

	public void msgHereIsYourFood(String choice) {
		event = AgentEvent.receivedFood;
		customerGui.setFoodString(choice);
		p.msgStateChanged();
	}

	public void msgHereIsYourCheck(Bill c) {
		amountOwed += c.getPrice();
		customerCheck = new Bill(c);
		Do("Customer Check Price: " + customerCheck.getPrice());
		Do("Amount Customer Owes: " + amountOwed);
		event = AgentEvent.receivedCheck;
		p.msgStateChanged();
	}

	public void msgHereIsYourChange(double amount) {
		Do("Thank you for the change: " + amount);
		BigDecimal bd = new BigDecimal(money+amount);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		money = bd.doubleValue();
		event = AgentEvent.donePaying;
		Do("My money after transaction: " + money);
		Do("My amount owed after transaction: " + amountOwed);
		//		customerGui.getGui().infoLabel.setText(
		//                "<html><pre> C: " + this.getName() + " ($" + this.getMoney() + ") </pre></html>");
		p.msgStateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.seated;
		p.msgStateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		event = AgentEvent.doneLeaving;
		p.msgStateChanged();
	}

	public void msgAtWaitingArea() {//from animation
		customerGui.setArrived(true);
		atWaitingArea.release();// = true;
		this.isAtWArea = true;
		p.msgStateChanged();
	}	


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}

		if (customerGui.getX() == xTableLoc && customerGui.getY() == yTableLoc && state == AgentState.BeingSeated) {
			state = AgentState.ReadyToOrder;
			ReadyToOrder();
			return true;
		}

		if (state == AgentState.ReadyToOrder && event == AgentEvent.orderingFood) {
			state = AgentState.Ordered;
			Order();
			return true;
		} 

		if (state == AgentState.ReadyToOrderAgain && event == AgentEvent.reOrderingFood){
			state = AgentState.Ordered;
			ReOrder();
			return true;
		}

		if (state == AgentState.Ordered && event == AgentEvent.receivedFood){
			state = AgentState.WaitingForCheck;
			EatFood();
			return true;
		}

		if (state == AgentState.WaitingForCheck && event == AgentEvent.receivedCheck){
			state = AgentState.Paying;
			payCheck();
			return true;
		}

		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}


		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Customer: Going to Waiting Area");
		restaurant.host.msgIWantToEat(this);
		customerGui.DoGoToWaitingArea(restaurant.host.waitingCustomers.indexOf(this));
		customerGui.setArrived(false);
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}//send our instance, so he can respond to us
		int randNum = (int)(Math.random() * 2);
		if (randNum == 0 && restaurant.host.allTablesOccupied()) {
			Do("Will Leave");
			try {
				for (Customer c: restaurant.host.waitingCustomers) {
					Do("In for loop for waiting customers");
					Do("Getting Customer Name: " + c.getCustomerName());
					Do("Getting Name: " + this.name);
					if (c.getCustomerName() == this.name) {
						Do("Restaurant is full, I am going to leave.");
						restaurant.host.waitingCustomers.remove(c);
						state = AgentState.DoingNothing;
						event = AgentEvent.doneLeaving;
						customerGui.DoExitRestaurant();
					}
				}
			}
			catch (ConcurrentModificationException e) {
			}
		}

	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(1,xTableLoc,yTableLoc);//hack; only one table
	}

	private void ReadyToOrder() {
		timer.schedule(new TimerTask() {
			public void run() {
				int randNumChoice = (int)(Math.random() * 4);
				int randNum = (int)(Math.random() * 2);				
				if (money > menu.getCheapestPriceOnMenu() && money < 8.99) {
					Do("Picking cheapest item: " + menu.getCheapestItemOnMenu());
					choice = menu.getCheapestItemOnMenu();
					print("I am ready to order");
					waiter.msgImReadyToOrder(cTableNum);
					customerGui.setFoodString("?");
				}
				else if (money < menu.getCheapestPriceOnMenu()) {
					if (randNum == 0) {
						if (menu.menuList.get(randNumChoice).containsKey("St")) {
							Do("My choice: St");
							choice = "St";
						}
						if (menu.menuList.get(randNumChoice).containsKey("C")) {
							Do("My choice: C");
							choice = "C";
						}
						if (menu.menuList.get(randNumChoice).containsKey("Sa")) {
							Do("My choice: Sa");
							choice = "Sa";
						}
						if (menu.menuList.get(randNumChoice).containsKey("P")) {
							Do("My choice: P");
							choice = "P";
						}
						print("I am ready to order");
						waiter.msgImReadyToOrder(cTableNum);
						customerGui.setFoodString("?");
					}
					if (randNum == 1) {
						Do("Leaving.");
						waiter.msgDoneAndPaid(cTableNum);
						customerGui.setFoodString("");
						customerGui.DoExitRestaurant();
						state = AgentState.DoingNothing;
						event = AgentEvent.doneLeaving;
					}
				}
				else {
					if (menu.menuList.get(randNumChoice).containsKey("St")) {
						choice = "St";
					}
					if (menu.menuList.get(randNumChoice).containsKey("C")) {
						choice = "C";
					}
					if (menu.menuList.get(randNumChoice).containsKey("Sa")) {
						choice = "Sa";
					}
					if (menu.menuList.get(randNumChoice).containsKey("P")) {
						choice = "P";
					}
					print("I am ready to order");
					waiter.msgImReadyToOrder(cTableNum);
					customerGui.setFoodString("?");
				}
				p.msgStateChanged();
			}
		},
		2000);
	}

	private void ReOrder() {
		int randNumChoice = (int)(Math.random() * menu.menuList.size());
		if (money > menu.getCheapestPriceOnMenu() && money < 8.99 && choice == menu.getCheapestItemOnMenu()) {
			Do("Leaving.");
			waiter.msgDoneAndPaid(cTableNum);
			customerGui.setFoodString("");
			customerGui.DoExitRestaurant();
			state = AgentState.DoingNothing;
			event = AgentEvent.doneLeaving;
		}
		else if (menu.menuList.size() > 0) {
			if (menu.menuList.get(randNumChoice).containsKey("St")) {
				choice = "St";
			}
			else if (menu.menuList.get(randNumChoice).containsKey("C")) {
				choice = "C";
			}
			else if (menu.menuList.get(randNumChoice).containsKey("Sa")) {
				choice = "Sa";
			}
			else if (menu.menuList.get(randNumChoice).containsKey("P")) {
				choice = "P";
			}
			print("Here is my new order");
			customerGui.setFoodString(choice + "?");
			waiter.msgHereIsMyChoice(this,choice);
		}
		else {
			Do("Leaving.");
			waiter.msgDoneAndPaid(cTableNum);
			customerGui.setFoodString("");
			customerGui.DoExitRestaurant();
			state = AgentState.DoingNothing;
			event = AgentEvent.doneLeaving;
		}
	}

	private void Order() {
		Do("Here is my order");
		customerGui.setFoodString(choice + "?");
		waiter.msgHereIsMyChoice(this,choice);
	}

	private void EatFood() {
		Do("Eating Food");
		event = AgentEvent.nowEating;
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				Do("Done eating");
				customerGui.setFoodString("B?");
				event = AgentEvent.doneEating;
				waiter.msgImDone(cTableNum);
				p.msgDoneEating();
				p.msgStateChanged();
			}
		},
		2000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void payCheck() {
		customerGui.setFoodString("B");
		customerGui.DoGoToCashier();
		customerGui.setArrived(false);
		try {
			atCashier.acquire();
			p.msgStateChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.money >= amountOwed) {
			Do("Here is the my full payment for the check: " + customerCheck.getPrice());
			int temp = (int)amountOwed;
			temp += 1;
			if (this.money > temp) {
				customerCheck.setAmountPaid(temp);
				((Cashier) restaurant.cashier).msgPayment(customerCheck,(temp));
				money -= temp;
			}
			else {
				customerCheck.setAmountPaid(amountOwed);
				((Cashier) restaurant.cashier).msgPayment(customerCheck, amountOwed);
				money -= amountOwed;
			}
		}
		else {
			Do("Sorry, this is all I have left: " + money);
			customerCheck.setAmountPaid(money);
			((Cashier) restaurant.cashier).msgPayment(customerCheck, money);
			money = 0;
		}
		customerGui.setFoodString("");
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgDoneAndPaid(cTableNum);
		customerGui.DoExitRestaurant();

	}

	public void leaveRestaurant() {
		restaurant.leaveRestaurant(customerGui);
		restaurant = null;

		p.msgThisRoleDone(this);
		p.msgStateChanged();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	public void setHost(Host h) {
		host = h;
	}

	public void setCashier(Cashier c) {
		cashier = c;
	}

	@Override
	public boolean isAtWaitingArea() {
		return isAtWArea;
	}

	@Override
	public void setAtWaitingArea(boolean atArea) {
		isAtWArea = atArea;
	}
}

