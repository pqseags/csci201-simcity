package restaurant.restaurantGabe;

//import restaurant.restaurantGabe.OldCustomerAgent.AgentEvent;
import restaurant.restaurantGabe.gui.CookGui;
import restaurant.restaurantGabe.gui.CustomerGui;
//import restaurant.restaurantGabe.gui.RestaurantGui;
import restaurant.restaurantGabe.interfaces.Cook;
import restaurant.restaurantGabe.test.mock.EventLog;
//import restaurant.restaurantGabe.test.mock.LoggedEvent;
import restaurant.restaurantGabe.util.*;
import role.Role;
import agent.Agent;
import interfaces.BaseRestaurantCook;
import interfaces.Person;

import java.util.*;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;
import market.Market;
import market.MarketInvoice;

public class CookRole extends Role implements Cook{

	public EventLog logTest = new EventLog();
	
	//INITIALIZATION
	public CookRole(String name){
		this.name  = name;
		Foods.put("Pizza", new FoodItem("Pizza",7,100,2));
		Foods.put("Steak", new FoodItem("Steak",7,100,5));
		Foods.put("Chicken",new FoodItem("Chicken",5,100,2));
		Foods.put("Salad", new FoodItem("Salad",5,100,3));
		foods.add("Pizza");
		foods.add("Steak");
		foods.add("Chicken");
		foods.add("Salad");

	}

	public CookRole(String name,int zero){
		this.name  = name;
		Foods.put("Pizza", new FoodItem("Pizza",7,3,2));
		Foods.put("Steak", new FoodItem("Steak",7,6,5));
		Foods.put("Chicken",new FoodItem("Chicken",5,3,2));
		Foods.put("Salad", new FoodItem("Salad",5,4,3));
		foods.add("Pizza");
		foods.add("Steak");
		foods.add("Chicken");
		foods.add("Salad");

	}


	public void depleteInventory() {
		Foods.put("Pizza", new FoodItem("Pizza",7,0,2));
		Foods.put("Steak", new FoodItem("Steak",7,0,5));
		Foods.put("Chicken",new FoodItem("Chicken",5,0,2));
		Foods.put("Salad", new FoodItem("Salad",5,0,3));
	
		
		
	}
	
	//GETTERS
	public String getName(){
		return name;
	}

	//SETTERS
	public void addMarket(Market m){
		//Markets.add(m);
		//m.setCook(this);
	}

	public void setGui(CookGui c){
		this.gui = c;
		gui.DoGoToHome();
	}

	public void setRevolvingStand(RevolvingStand r){
		this.stand = r;
	}

	public void setRestaurant(RestaurantGabe rg){
		this.restaurant = rg;
	}


	//Hack
	/*public void setNoFoodsMarketInvSpeed(int inv,int speed){
		Foods.get("Salad").amount = 0;
		Foods.get("Pizza").amount = 0;
		Foods.get("Steak").amount = 0;
		Foods.get("Chicken").amount = 0;
		for(MarketRole m:Markets){
			m.setInvSpeed(inv,speed);
		}


		person.msgStateChanged();
	}

	public void setNoFoodsSpeed(int speed){
		Foods.get("Salad").amount = 0;
		Foods.get("Pizza").amount = 0;
		Foods.get("Steak").amount = 0;
		Foods.get("Chicken").amount = 0;
		for(MarketRole m:Markets){
			m.setSpeed(speed);
		}


		person.msgStateChanged();
	}
	 */
	public void setSalad(int amount){
		Foods.get("Salad").amount = amount;
	}



	//DATA

	//Person agent behind the role
	public PersonAgent person;

	CookGui gui;

	//restaurant
	RestaurantGabe restaurant;

	//List of markets and variable to keep track of the last market we ordered from
	int lastMarket = -1;
	//List<Market> Markets = new ArrayList<Market>();

	//will help with iterating
	private List<String> foods = new ArrayList<String>();

	String name;

	//stores all of the cooks orders
	private List<Order> Orders = Collections.synchronizedList(new ArrayList<Order>());

	//the revolving stand where some waiters deposit orders
	protected RevolvingStand stand;

	//stores cooktime information for restaurant's dishes
	public Map<String, FoodItem > Foods = Collections.synchronizedMap(new HashMap<String,FoodItem>());

	//currently being prepared item
	Order currentOrder;

	private enum FoodState {normal,low,requested,pending};
	public class FoodItem{
		public FoodItem(String name,int cooktime,int amount,int low){
			this.name = name;
			this.cooktime = cooktime;
			this.amount = amount;
			this.low = low;
			stockSize = 15;
			s = FoodState.normal;
			timesOrdered = 0;
		}
		String name;
		int cooktime;
		public int amount;
		int low;
		public FoodState s;
		int stockSize;
		int timesOrdered;
	}



	//MESSAGES

	public boolean YouAreDoneWithShift(){

		if(true){
			DoMessage("Being kicked off the job now");
			person.msgThisRoleDone(this);
			this.person = null;
			//market.DefaultName(this);
		}
		return true;
	}

	public void msgStateChanged(){
		person.msgStateChanged();
	}

	public void msgGotFood(String choice){
		gui.DoRemoveFood(choice);
	}

	@Override
	public void msgHereIsDelivery(MarketInvoice order) {
		// TODO Check more carefully if all food was gotten 
		//in appropriate quantities.

		//synchronized(Foods){
		FoodItem foodInQuestion = Foods.get(order.order.get(0).choice);
		DoMessage("Order just came in.");

		synchronized(Foods){
			for(int i = 0;i<foods.size();++i){
				Foods.get(order.order.get(i).choice).amount+=order.order.get(i).quantityReceived;
				Foods.get(order.order.get(i).choice).s = FoodState.normal;
			}
		}

		person.msgStateChanged();

	}




	//Message from market that food is being delivered
	public void msgFoodDelivery(List<String> foods,List<Integer> nums){

		synchronized(Foods){
			FoodItem foodInQuestion = Foods.get(foods.get(0));
			DoMessage("Order just came in.");

			synchronized(Foods){
				for(int i = 0;i<foods.size();++i){
					Foods.get(foods.get(i)).amount += nums.get(i);
					Foods.get(foods.get(i)).s = FoodState.normal;
				}
			}

			//Do("but now I have " +foodInQuestion.amount);
			person.msgStateChanged();
		}
	}
	
	
	@Override
	public void msgCannotFulfillOrder(Market m,
			Map<String, Integer> unfulfillable) {
		// TODO Auto-generated method stub
		
		if(unfulfillable==null || unfulfillable.isEmpty()){
			
			for(String f:this.foods){
				if(/*Foods.get(f).s==FoodState.pending ||*/ Foods.get(f).s==FoodState.requested){
					if(unfulfillable.containsKey(f)){
						continue;
					}
				}
				Foods.get(f).s = FoodState.pending;
			}
			
		}
		
		for(String f:this.foods){
			//Do(f+ " is in state " + Foods.get(f).s);
			if(Foods.get(f).s == FoodState.requested){
				Foods.get(f).s = FoodState.low;
				DoMessage("Going to rerequest "+Foods.get(f).name);
			}
		}
		person.msgStateChanged();
		
		
	}

	//Market tells cook which of the foods ordered he can supply
	public void msgICanFulFill(List<String> availableFoods){

		if(availableFoods!=null){

			for(String f:availableFoods){
				//Do(f);
				Foods.get(f).s = FoodState.pending;
			}


		}
		for(String f:this.foods){
			//Do(f+ " is in state " + Foods.get(f).s);
			if(Foods.get(f).s == FoodState.requested){
				Foods.get(f).s = FoodState.low;
				DoMessage("Going to rerequest "+Foods.get(f).name);
			}
		}
		person.msgStateChanged();
	}

	//cook is given a new order
	public void msgHereIsAnOrder(WaiterRole w,Order o){
		DoMessage("Got the order");
		logTest.add(new restaurant.restaurantGabe.test.mock.LoggedEvent("Got an order from waiter "+w.getName()+" for "+o.choice));
		o.s = OrderState.requested;
		Orders.add(o);
		person.msgStateChanged();
	}

	//food has finished cooking
	public void msgFoodIsDone(Order o){
		o.s = OrderState.cooked;
		person.msgStateChanged();
	}


	//SCHEDULER
	public boolean pickAndExecuteAnAction(){

		//if there are any low foods, order them
		//Change this to just numbers
		/*for(String s:foods){
			if(Foods.get(s).s == FoodState.low){
				OrderMoreFood();
				return true;
			}
		}*/

		Order tryOrder = stand.remove();
		if(tryOrder!=null){
			DoMessage("Picking an order off the stand.");
			Orders.add(tryOrder);
		}


		//if Order has a cooked order
		synchronized(Orders){
			for(Order o:Orders){
				if(o.s==OrderState.cooked){
					//Do("Food is done!");
					//GiveOrder()
					GiveOrder(o);
					return true;
				}
			}
		}

		//if Order as a requested order
		synchronized(Orders){
			for(Order o:Orders){
				if(o.s == OrderState.requested){
					//CookOrder()
					TryToCookOrder(o);
					return true;
				}
			}
		}
		synchronized(foods){
			for(String s:foods){
				if(Foods.get(s).amount < Foods.get(s).low && !(Foods.get(s).s==FoodState.requested || Foods.get(s).s==FoodState.pending) && !(Foods.get(s).timesOrdered>restaurant.markets.size())){
					OrderMoreFood();
					return true;
				}
			}
		}


		return false;

	}

	//ACTIONS

	//Will look at food amount. If not out, start cook timer and leave order
	private void TryToCookOrder(final Order o){
		//Print message
		FoodItem food = Foods.get(o.choice);
		if(food.amount==0){
			o.w.msgOutOfFood(o);
			DoMessage("We're out of "+food.name);
			Orders.remove(o);
			return;
		}
		food.amount--;
		//Move check to scheduler
		/*if(food.s == FoodState.normal && food.amount<food.low){
			food.s = FoodState.low;
			Do("Low on " + food.name);
		}*/

		DoMessage("Cooking some "+ o.choice);
		DoCookOrder(o.choice);

		//change order state
		o.s = OrderState.cooking;

		//get cooktime
		int t = Foods.get(o.choice).cooktime;

		//start cooktimer
		o.cooktimer = new Timer();
		o.cooktimer.schedule(new TimerTask(){
			public void run() {
				msgFoodIsDone(o);
			}
		},
		t*1000);

	}

	//notifies waiter that order is ready
	private void GiveOrder(Order o){
		DoGiveOrder(o);
		o.s = OrderState.available;
		o.w.msgOrderIsReady(o);
	}

	//Orders more food from the markets
	private void OrderMoreFood(){

		List<String> need = new ArrayList<String>();
		List<Integer> num = new ArrayList<Integer>();
		Map<String,Integer> order = new HashMap<String,Integer>();
		synchronized(Foods){
			for(String f:foods){
				FoodItem currentFood = Foods.get(f);

				if(currentFood.amount< currentFood.low && currentFood.s!=FoodState.pending &&  currentFood.s!=FoodState.requested){
					if(currentFood.timesOrdered==restaurant.markets.size()){
						DoMessage("Tried all markets, none have "+currentFood.name+ " so we're permanantly out.");
						currentFood.timesOrdered++;
						continue;
					}

					need.add(f);
					num.add(currentFood.stockSize - currentFood.amount);
					order.put(f, currentFood.stockSize - currentFood.amount);
					currentFood.s = FoodState.requested;
					currentFood.timesOrdered++;
					DoMessage("Ordering more " + f);
				}
			}
		}
		//
		lastMarket = (1+lastMarket)%restaurant.markets.size();
		//Markets.get(lastMarket).msgHereIsMyOrder(need,num);



		restaurant.markets.get(lastMarket).host.msgBusinessWantsThis(restaurant, order);

	}

	//GUI

	private void DoCookOrder(String choice){
		gui.DoCookOrder(choice);
	}

	private void DoGiveOrder(Order o){
		//Do("Here's that order of "+o.choice+ " for "+o.c.getName());
		gui.DoPlateOrder(o.choice);
	}

	
	public void DoInfo(String message){
		//super.Do(message);
		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		if(restaurant==null){
			return;
		}
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));		
	}
	
	public void DoDebug(String message){
		//super.Do(message);
		AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_GABE, name, message, restaurant.cityRestaurantGabe.ID);
		//log.add(new LoggedEvent(message));		
	}


	public void decreaseInventory(String choice){
		Foods.get(choice).amount--;
		DoDebug(choice + " now has " + Foods.get(choice).amount);
		
		if (p!=null)
			p.msgStateChanged();
	}
	
	public void increaseInventory(String choice){
		Foods.get(choice).amount++;
		DoDebug(choice + " now has " + Foods.get(choice).amount);
		
		if (p!=null)
			p.msgStateChanged();
	}


	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return person!=null;
	}

//	@Override
//	public boolean canLeave() {
//		// TODO Auto-generated method stub
//		//if (restaurant.numCustomers==0) {
//		restaurant.leaveRestaurant(this);
//		return true;
//		//	}
//		//	return false;
//	}
	
	@Override
	public boolean canLeave() {
		return false;
	}
	
	public void changeShifts(Person p) {
		if (this.person!=null){
			this.person.msgThisRoleDone(this);
		}	
		this.person = (PersonAgent) p;
		this.name = p.getName();
	}
}
