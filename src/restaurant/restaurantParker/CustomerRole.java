package restaurant.restaurantParker;

import restaurant.Restaurant;
import role.Role;
import restaurant.restaurantParker.gui.CustomerGui;
import restaurant.restaurantParker.WaiterRole;
//import restaurant.restaurantParker.gui.RestaurantGui;
import person.PersonAgent;
import agent.Agent;
import interfaces.restaurantParker.*;

import java.util.*;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role implements Customer{
	
	
	//HACK variables
	private boolean scam;
	private boolean hacked;
	private boolean returning;
	
	
	private String name;
	private int cash;
	private int bill;
	private int change;
	private String choice;
	private boolean room;
	private Menu menu;
	private int hungerLevel = 12;  // determines length of meal
	private int ponderTime = 5000;
	Timer timer = new Timer();
	
	
	private CustomerGui customerGui;
	private int lobbyPosition;
	
	RestaurantParker restaurant;

	//lab3
	private int destinationTable;
	
	
	// agent correspondents
	private WaiterRole waiter;
	private HostRole host;
	private CashierRole cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, GoingToLobby, WaitingInRestaurant, TalkedHost, GotToLobbySeat, BeingSeated, Seated, Pondering, Decided, Ordering, ReOrdering, Eating, DoneEating, WantCheck, Paying, Paid, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, atLobby, atLobbySeat, toldRoom, followHost, seated, choiceMade, asked, refused, served, doneEating, gotBill, atCashier, gotChange, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(String name, PersonAgent p){
		super();
		this.name = name;
		this.p = p;
		
		returning = false;
		scam = false;
		hacked = false;
		String doot = "";
		
		Random rand = new Random();
		cash = rand.nextInt(50);
		
		
		
		
		//NAME HACKS
		if ((this.name.equals("Steak")) || (this.name.equals("Chicken")) || (this.name.equals("Pizza")) || (this.name.equals("Salad"))){
			hacked = true;
			doot = "*HACKED*";
		}
		
		if (this.name.equals("Poor")){
			doot = "*HACKED*";
			cash = 5;
		}
		
		if (this.name.equals("SemiPoor")){
			doot = "*HACKED*";
			cash = 14;
		}
		
		if (this.name.equals("Scam")){
			doot = "*HACKED*";
			cash = 50;
			scam = true;
		}
		
		//print("Created a new customer with $" + cash + ". " + doot);
	
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setWaiter(WaiterRole waiter) {
		this.waiter = waiter;
	}

	public void setHost(HostRole host){
		this.host = host;
	}
	
	public void setCashier(CashierRole cashier){
		this.cashier = cashier;
	}
	
	public String getCustomerName() {
		return name;
	}
	
	public WaiterRole getWaiter(){
		return waiter;
	}
	// Messages

	public void msgAtRestaurant(Restaurant r) {//from animation
		//System.err.println("CustomerRole.msgAtRestaurant");
		this.restaurant = (RestaurantParker)r;
		event = AgentEvent.gotHungry;
		p.msgStateChanged();
	}

	public void msgIsThereRoom(boolean room, int position){
		this.room = room;
		this.lobbyPosition = position;
		event = AgentEvent.toldRoom;
		p.msgStateChanged();
	}
	
	public void msgSitAtTable(int tableNumber, Menu m, WaiterRole w) {
		print("Received msgSitAtTable");
		this.menu = m;
		this.waiter = w;
		event = AgentEvent.followHost;
		destinationTable = tableNumber;
		p.msgStateChanged();
	}
	
	public void msgWhatWouldYouLike(){
		event = AgentEvent.asked;
		p.msgStateChanged();
		
	}
	
	public void msgOutOfFood(Menu m){
		menu = m;
		event = AgentEvent.refused;
		p.msgStateChanged();
	}
	
	public void msgHereIsYourFood(String choice){
		event = AgentEvent.served;
		p.msgStateChanged();
	}
	
	public void msgHereIsBill(int check){
		bill = check;
		event = AgentEvent.gotBill;
		p.msgStateChanged();
	}
	
	public void msgGiveChange(int change){
		this.change =  change;
		event = AgentEvent.gotChange;
		p.msgStateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		p.msgStateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier(){
		event = AgentEvent.atCashier;
		p.msgStateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
//		System.err.println("got msgAnimationFinsihedLeavingREstarunt");
//		System.err.println(state);
		event = AgentEvent.doneLeaving;
		p.msgStateChanged();
	}
	
	public void msgAnimationFinishedGoToLobby(){
		event = AgentEvent.atLobby;
		p.msgStateChanged();
	}
	
	public void msgAnimationFinishedGoToLobbySeat(){
		print("Got to my Lobby Seat");
		event = AgentEvent.atLobbySeat;
		p.msgStateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		//HACK TO MAKE HIM WAIT FOR STAFF!!!!
		if (host.p == null){
			return true;
		}
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.GoingToLobby;
			goToLobby();
			return true;
		}
		
		if (state == AgentState.GoingToLobby && event == AgentEvent.atLobby ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.toldRoom ){
			state = AgentState.TalkedHost;
			ReplyHost();
			return true;
		}
		
		if (state ==AgentState.TalkedHost && event == AgentEvent.atLobbySeat){
			state = AgentState.GotToLobbySeat;
			ReallyReplyHost();
			return true;
		}
		
		if (state == AgentState.GotToLobbySeat && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			
			state = AgentState.Pondering;
			PonderMenu();
			return true;
		}
		
		
		if (state == AgentState.Pondering && event == AgentEvent.choiceMade){
			
			state = AgentState.Decided;
			CallWaiter();
			return true;
		}
		
		if (state == AgentState.Decided && event == AgentEvent.asked){
			
			state = AgentState.Ordering;
			orderFood();
			return true;
		}
		
		if ((state == AgentState.Ordering || state == AgentState.ReOrdering) && event == AgentEvent.refused){
			state = AgentState.ReOrdering;
			event = AgentEvent.none;
			reOrderFood();
			return true;
		}
	
		if ((state == AgentState.Ordering|| state == AgentState.ReOrdering) && event == AgentEvent.served){
	
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
	
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WantCheck;
			askForCheck();
			return true;
		}
		
		if (state == AgentState.WantCheck && event == AgentEvent.gotBill){
			state = AgentState.Paying;
			PayBill();
			return true;
		}
		
		if (state == AgentState.Paying && event == AgentEvent.atCashier){
			state = AgentState.Paid;
			SettleUp();
			return true;
		}
		
		if (state == AgentState.Paid && event == AgentEvent.gotChange){
			state = AgentState.Leaving;
			LeaveCashier();
			return true;
		}
		if ((state == AgentState.Leaving || state == AgentState.DoingNothing)&& event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			
			personLeave();
			return true;
		}
		return false;
	}

	// Actions

	private void goToLobby(){
		Do("Going to restaurant");
		customerGui.DoGoToLobby();
	}
	
	private void goToRestaurant() {
		
	
		//get some more cash
		if (returning){
			Random rand = new Random();
			int moreCash = rand.nextInt(50);
			cash = cash + moreCash;
		}

		print("I'm hungry, and I have $" + cash + " in my pocket.");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	
	private void ReplyHost(){
		if (room){
			print("Great, thanks.");
			//host.msgIWillStay(this);
			customerGui.DoGoToLobbySeat(lobbyPosition);
		}
		else{
			//decide whether to stay or not
			Random rand = new Random();
			int choiceInt = rand.nextInt(2);
			if (choiceInt == 1){
				print("That's okay, add me to the waitlist please!");
				//host.msgIWillStay(this);
				customerGui.DoGoToLobbySeat(lobbyPosition);

			}
			else if (choiceInt == 0){
				print("I don't have time to wait, I'm leaving.");
				customerGui.DoExitRestaurant();
				state = AgentState.DoingNothing;
				host.msgIWontStay(this);
			}
			
			else {  //should never happen
				System.err.println("BAAAAAD");
			}
			
		}
	}
	
	private void ReallyReplyHost(){
		host.msgIWillStay(this);
	}
	
	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(destinationTable);
	}

	private void PonderMenu(){
		Do("Looking at Menu");
		
		//TIMER
		timer.schedule(new TimerTask() {
			
			public void run() {
				
				List<String> availableMenu
				= new ArrayList<String>();
				
				for(String food : menu.keys){
					if (menu.menu.get(food) <= cash){
						availableMenu.add(food);
					}
				}
				
				//actually make a choice
				int menuSize = availableMenu.size();
				print("I can choose from " + menuSize + " items on the menu.");
				if (menuSize == 0){
					print("I am too poor to order anything!");
					leaveTable();
					return;
				}
				
				Random rand = new Random();
				int choiceInt = rand.nextInt(menuSize);
				choice = availableMenu.get(choiceInt);
				event = AgentEvent.choiceMade;
				print("Made a choice");
				p.msgStateChanged();
			}
		},
		ponderTime);//how long to wait before running task
		
	}
	
	private void CallWaiter(){
		DoCallWaiter();
		waiter.msgImReadyToOrder(this);
		
	}
	private void DoCallWaiter(){
		Do("I want to Order");
		customerGui.readyToOrder();
	}
	
	private void orderFood(){
		
		if (hacked){
			choice = name;
		}
		
		DoOrderFood(choice);
		waiter.msgHereIsMyChoice(choice, this);
	}
	
	private void DoOrderFood(String choice){
		print ("I would like a " + choice);
		customerGui.Ordered(choice);
		
	}
	
	private void reOrderFood(){
		
		List<String> availableMenu = new ArrayList<String>();
		for(String food : menu.keys){
			if (menu.menu.get(food) <= cash){
				availableMenu.add(food);
			}
		}
		
		int menuSize = availableMenu.size();
		print("I can choose from " + menuSize + " items on the menu.");
		if (menuSize == 0){
			print("Well then, I'll be off!");
			leaveTable();
			return;
		}
		
		Random rand = new Random();
		int choiceInt = rand.nextInt(menuSize);
		choice = availableMenu.get(choiceInt);
		print("Okay, then I would like a " + choice);
		customerGui.Ordered(choice);
		waiter.msgHereIsMyChoice(choice, this);
	}

	private void EatFood() {
		Do("Eating Food");
		customerGui.gotFood();
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			
			public void run() {
				print("Done eating a " + choice);
				event = AgentEvent.doneEating;
				//isHungry = false;
				p.msgStateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}

	private void askForCheck(){
		Do("Telling " + waiter.getName() + " that I am ready for my check.");
		customerGui.wantCheck();
		waiter.msgImDone(choice, this);
	}
	
	private void PayBill(){
		print("Time to pay for my meal.");
		Do("Telling " + waiter.getName() + " that I am paying and leaving.");
		waiter.msgLeavingTable(this);
		
		customerGui.DoGoToCashier();
	}

	private void SettleUp(){
		
		returning = true;
		Random rand = new Random();
		int randomInt = rand.nextInt(10);
		if ((scam) || (randomInt == 0)){
			cash = 0;
		}
		
		if (bill<=cash){	   //normative scenario
			int payment = cash;
			cashier.msgPayment(bill, payment, this);
			print("Paying $" + payment);
			cash = cash - payment;
			bill = 0;
		}
		
		else{	//this guy is a jip!
			print("Uhoh, I must have lost my money!");
			cashier.msgIHaveNoMoney(bill, this);
		}
	}
	
	private void LeaveCashier(){
		state = AgentState.DoingNothing;
		if (!(change == 1000)){
			print("I got $" + change + " in change. Now I can finally leave the restaurant!");
			cash = cash + change;
			customerGui.DoExitRestaurant();
			//personLeave();
		}
		
		else {
			print("Phew! Got off easy this time!");
			customerGui.DoExitRestaurant();
			//personLeave();
		}
	}
	
	private void leaveTable() {
		DoLeaveTable();
		returning = true;
		state = AgentState.DoingNothing;
		waiter.msgLeavingTable(this);
		
	}
	
	private void DoLeaveTable(){
		Do("Telling " + waiter.getName() + " that I am leaving.");
		customerGui.DoExitRestaurant();
		//personLeave();
	}

	
	private void personLeave(){
		System.err.println("CustomerROle.personLeave");
		p.msgThisRoleDone(this);
		p.msgDoneEating();
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

	protected void print(String message){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_PARKER, name, message, restaurant.cityRestaurant.ID);
	}
}

