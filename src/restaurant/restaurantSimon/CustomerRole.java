package restaurant.restaurantSimon;


import restaurant.Restaurant;
import restaurant.restaurantSimon.WaiterRole.Dish;
import restaurant.restaurantSimon.gui.CustomerGui;



import restaurant.restaurantSimon.interfaces.Customer;
import role.Role;
import agent.Agent;
import interfaces.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role  implements Customer {
	PersonAgent self;
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	private int TableNum;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private String choice;
	private double checkAmount=0;
	private Semaphore check = new Semaphore(0,true);
	private double myMoney=40;
	private List<Dish> menu=new ArrayList<Dish>();
	private boolean onlyChoice=false;
	
	public int waitingNum=6;//for the gui, so the customer is not in the animation at the very beginning

	Random rand=new Random();
	// agent correspondents
	private HostRole host;
	private WaiterRole waiter;
	private CashierRole cashier;
	RestaurantSimon restaurant=null;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Thinking,WaitingToOrder,ordered, Eating, DoneEating,Waiting, Paying,Leaving, InRestaurant, Deciding};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, beingSeated, seated, doneThinking,noMoney, takingOrder, gotFood, doneEating, donePaying,doneLeaving, full, leave, stay, notFull};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param animation  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(String name, HostRole h, CashierRole ca){
		super();
		this.name = name;
		host=h;
		cashier=ca;
	}
	public CustomerRole(String name, PersonAgent p){
		super();
		this.name = name;
		self=p;
//		host=h;
//		cashier=ca;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostRole host) {
		this.host = host;
	}

	public void setCashier(CashierRole ca){
		this.cashier=ca;
	}

	public String getCustomerName() {
		return name;
	}
	

	// Messages
	
	public void msgYourWaitingNumber(int num){
		waitingNum=num;
		customerGui.DoGoToWait(waitingNum);
		//Do("number is now" + num);
		self.msgStateChanged();
	}
	@Override
	public void HereIsYourChange(double total) {
		// TODO Auto-generated method stub
		Do("you can leave now");
		
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		// TODO Auto-generated method stub
		Do("you can leave now, but owe us!!");
	}
	

	public void msgNotFull(){
		event = AgentEvent.notFull;
		self.msgStateChanged();
	}

	public void msgFull(){
		event = AgentEvent.full;
		self.msgStateChanged();
	}

	public void msgPleaseReOrder(List<Dish> newMenu){
		menu=newMenu;
		event = AgentEvent.seated;
		self.msgStateChanged();
	}

	public void msgYourCheck(double amount){
		Do("I got the Check");
		checkAmount=amount;
		check.release();
		self.msgStateChanged();
	}

	public void gotHungry() {//from animation
		print("I'm hungry");
		checkAmount=0;
		event = AgentEvent.gotHungry;
		self.msgStateChanged();
	}

	public void msgFollowMeToTable(WaiterRole w, int tableNumber, List<Dish> m) {
		print("Received msgSitAtTable");
		waiter=w;
		menu=m;
		TableNum=tableNumber;
		event = AgentEvent.beingSeated;
		self.msgStateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		self.msgStateChanged();
	}

	public void msgWhatWouldYouLike(){
		event = AgentEvent.takingOrder;
		self.msgStateChanged();
	}

	public void msgHereIsYourFood(){
		Do("I got food");
		event=AgentEvent.gotFood;
		self.msgStateChanged();
	}

	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		self.msgStateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.InRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.InRestaurant && event == AgentEvent.full ){
			state = AgentState.Deciding;
			ifLeave();
			return true;
		}

		if (state == AgentState.InRestaurant && event == AgentEvent.notFull ){
			state = AgentState.WaitingInRestaurant;
			host.msgIStay(this);
			return true;
		}

		if (state == AgentState.Deciding && event == AgentEvent.leave ){
			state = AgentState.DoingNothing;
			host.msgILeave(this);
			leaveRestraunt();
			leftRestaurant();
			return true;
		}
		if (state == AgentState.Deciding && event == AgentEvent.stay ){

			host.msgIStay(this);
			state = AgentState.WaitingInRestaurant;
			return true;
		}

		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.beingSeated ){
			state = AgentState.BeingSeated;
			SitDown(TableNum);
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Thinking;
			ThinkingOrder();
			return true;
		}

		if (state == AgentState.Thinking && event == AgentEvent.doneThinking){
			state = AgentState.WaitingToOrder;
			ReadyToOder();
			Do("Waiting to order");
			return true;
		}

		//non nomative
		if (state == AgentState.Thinking && event == AgentEvent.noMoney){
			state = AgentState.Waiting;
			Do("Cannot Afford ANYTHING!!");
			return true;
		}


		if (state == AgentState.WaitingToOrder && event == AgentEvent.takingOrder){
			state = AgentState.ordered;
			Do("Ordered");
			customerGui.setWaitingForFood(choice);
			waiter.msgHereIsMyOrder(this,choice);
			return true;
		}

		if (state == AgentState.ordered && event == AgentEvent.seated){
			state = AgentState.BeingSeated;
			Do("Being seated");
			customerGui.setWaitingForFood(null);
			return true;
		}
		if (state == AgentState.ordered && event == AgentEvent.gotFood){
			state = AgentState.Eating;
			customerGui.setWaitingForFood(null);
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			waiting();
			state = AgentState.Waiting;

			return true;
		}
		if (state == AgentState.Waiting){
			goPaying();
			state = AgentState.Paying;
		
			return true;
		}

		if (state == AgentState.Paying && event == AgentEvent.donePaying){

			state = AgentState.Leaving;
			leaveRestraunt();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			leftRestaurant();
			return true;
		}
		return false;
	}

	// Actions

	private void ifLeave(){

		int leave=rand.nextInt(3);
		if(leave==1){
			Do("I will leave!!!");
			event = AgentEvent.leave;
			leaveRestraunt();

		}
		else{
			Do("I will stay");
			event = AgentEvent.stay;
		}
		self.msgStateChanged();
	}

	private void goPaying(){
		Do("go Paying");
		waiter.msgIAmLeaving(this);
		customerGui.DoWalkToCashier();
		double pay;
		if(checkAmount>myMoney){
			pay=myMoney;
		}else{
			pay=checkAmount;
		}
		cashier.msgHereIsMoney(this, pay);
		myMoney-=pay;
		Do("Payed: " + pay);
		event=AgentEvent.donePaying;

	}

	private void waiting(){
		try {
			check.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void ThinkingOrder() {
		Do("Thinking Order");
		if(name.equals("Flake")){
			Do("I am Flake!!!!!!!!!!!!");
			choice =menu.get(rand.nextInt(menu.size())).type;
			timer.schedule(new TimerTask() {
				
				public void run() {
					print("Done thinking");
					event = AgentEvent.doneThinking;
					//isHungry = false;
					self.msgStateChanged();
				}
			},
			500);//getHungerLevel() * 1000);//how long to wait before running task
		}
		else {

			if(myMoney>=5.99 && !onlyChoice){
				if(myMoney<8.99){
					choice="Salad";
					onlyChoice=true;
				}
				else if (myMoney<10.99){

					choice =menu.get(rand.nextInt(menu.size()-2)).type;
					if(name.equals("Salad")){
						choice="Salad";
					}
					else if(name.equals("Pizza")){
						choice="Pizza";
					}
				}
				else if (myMoney<15.99){
					choice =menu.get(rand.nextInt(menu.size()-1)).type;
					if(name.equals("Chicken")){
						choice="Chicken";
					}
					else if(name.equals("Salad")){
						choice="Salad";
					}
					else if(name.equals("Pizza")){
						choice="Pizza";
					}

				}
				else{
					choice =menu.get(rand.nextInt(menu.size())).type;
					if(name.equals("Steak")){
						choice="Steak";
					}
					else if(name.equals("Chicken")){
						choice="Chicken";
					}
					else if(name.equals("Salad")){
						choice="Salad";
					}
					else if(name.equals("Pizza")){
						choice="Pizza";
					}	
				}

				timer.schedule(new TimerTask() {
					
					public void run() {
						print("Done thinking" );
						event = AgentEvent.doneThinking;
						//isHungry = false;
						self.msgStateChanged();
					}
				},
				500);//getHungerLevel() * 1000);//how long to wait before running task
			}

			else{
				event = AgentEvent.noMoney;
				onlyChoice=false;
				self.msgStateChanged();
			}
		}
	}

	private void ReadyToOder(){
		Do("Ready to oder");
		waiter.msgImReadyToOrder(this);
	}
	private void SitDown(int tNumber) {
		Do("Being seated. Going to table");
		customerGui.DoGoToReadyPosition();
		waiter.msgCustomerReadyToBeSeated();
		customerGui.DoGoToSeat(TableNum);
	}


	private void EatFood() {
		Do("Eating Food");
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
				print("Done eating");
				event = AgentEvent.doneEating;
				//isHungry = false;
				self.msgStateChanged();
			}
		},
		500);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveRestraunt() {
		Do("Leaving.");
		customerGui.DoExitRestaurant();
		//waiter.msgIAmLeaving( this);
	}
	private void leftRestaurant(){
		self.msgThisRoleDone();
		restaurant.customerNum--;
		
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

	@Override
	public void msgAtRestaurant(Restaurant r) {
		restaurant=(RestaurantSimon)r;
		event = AgentEvent.gotHungry;
		self.msgStateChanged();
	}

	public void setPeople(HostRole h, CashierRole ca,RestaurantSimon res){
		host=h;
		cashier=ca;
		restaurant=res;
	}
	
public void Do(String message){
		
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);
		
	}
}

