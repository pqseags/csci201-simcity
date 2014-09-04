package restaurant.restaurantSimon;
import interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import market.Market;
import market.MarketInvoice;
import restaurant.Restaurant;
import restaurant.restaurantSimon.gui.CookGui;
import restaurant.restaurantSimon.interfaces.Cook;
import role.Role;
import UnitTests.mock.LoggedEvent;
import agent.Agent;
public class CookRole extends Role implements Cook{

	public enum OrderState{Pending,Cooking,Done,DoneAndCalled,Dilivered,canceled};
	class Order{


		WaiterRole w;
		String choice;
		int table;
		int pos=0;
		private OrderState s=OrderState.Pending;

		public Order(WaiterRole waiter, String ch, int t){
			w=waiter;
			choice=ch;
			table=t;
			//s=OrderState.Pending;
		}

	}
	public class FoodData{
		String type;
		double cookTime;
		public int amount=500;
		int low=2;
		int capacity=500;
		boolean ordered=false;
		public FoodData(String t, int ct){
			type=t;
			cookTime=ct;
		}
	}

	protected class Pair{
		String type;
		int quantity;
		Pair(String t, int q){
			type=t;
			quantity=q;
		}
	}

	private class ReOrderInfo{
		List<Pair> notEnough;
		Market market;
		ReOrderInfo(Market m,List<Pair> ne){
			market=m;
			notEnough=ne;
		}
	}
	public CookRole(RestaurantSimon r){
		inventory=new HashMap<>();
		inventory.put("Steak", new FoodData("Steak",10));
		inventory.put("Salad", new FoodData("Salad",4));
		inventory.put("Chicken", new FoodData("Chicken",7));
		inventory.put("Pizza", new FoodData("Chicken",7));

		restaurant=r;

		//hack check and add more
	}

	//data
	Person self;
	private String name="cook";
	private CookGui cookGui=null;
	private boolean openOrder=true;
	private List<ReOrderInfo> reOrders=Collections.synchronizedList(new ArrayList<ReOrderInfo>());
	List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());

	private List<Order> orders=Collections.synchronizedList(new ArrayList<Order>());
	Timer timer=new Timer();
	public Map <String , FoodData> inventory;

	int marketCounter=0;

	RestaurantSimon restaurant;

	//hack to establish relationship with market
	public void msgAddMarket(Market m){
		markets.add(m);

	}


	//hack to connect gui and agent
	public void setGui(CookGui gui){
		cookGui=gui;
	}

	//msg



	public void msgOrderDelivered(String market, List<Pair> satisfied){
		for(Pair p : satisfied){
			inventory.get(p.type).amount+=p.quantity;
			Do("received "+inventory.get(p.type).type+" of "+ p.quantity+" from "+market);
			inventory.get(p.type).ordered=false;
		}
		stateChanged();
	}

	public void msgOutOf(Market market, List<Pair> notEnough){

		reOrders.add(new ReOrderInfo(market,notEnough));
		stateChanged();

	}

	public void msgHereIsAnOrder(WaiterRole waiter, String ch, int table){

		orders.add(new Order(waiter,ch,table));
		print("I got the order");
		stateChanged();
	}



	public void msgGiveMeFood(WaiterRole w){
		synchronized(orders){
			for(Order order : orders){
				if(order.w==w){
					order.s=OrderState.Dilivered;
					cookGui.offSetPlate(order.pos);
					stateChanged();
				}
			}
		}



	}


	//scheduler

	@Override
	public boolean pickAndExecuteAnAction() {//hack!! add more in bewteen

		if(openOrder){
			orderFoodThatIsLow();
			openOrder=false;
			return true;
		}
		synchronized(orders){
			for(Order order : orders){
				if(order.s==OrderState.Done){
					PlateOrder(order);

					return true;
				}
			}
		}
		synchronized(orders){
			int orderNum=0;
			for(Order order : orders){
				if(order.s==OrderState.Cooking){
					orderNum++;
				}
				if(order.s==OrderState.Pending){
					tryToCookOrder(order,orderNum);

					return true;
				}
			}
		}

		if(reOrders.size()!=0){	
			reOrder();
			return true;
		}
		cookGui.DoGoToDesk();

		return false;
	}


	//actions
	private void reOrder(){
		Market m=PickMarket();
		while(m==reOrders.get(0).market){
			m=PickMarket();
		}
		//TODO
		//Map<String,Integer> shoppingList = new HashMap<String, Integer>();	
		
		//		for(int i=0;reOrders.get(0).notEnough)
		//		
		//		shoppingList.put("",1 );
		//		m.host.msgBusinessWantsThis(restaurant, shoppingList)
		//		
		//m.msgHereIsAnOrder(this,reOrders.get(0).notEnough);
		reOrders.remove(0);
	}

	private void tryToCookOrder( Order o,int p){
		o.s=OrderState.Cooking;
		o.pos=p;
		if((inventory.get(o.choice)).amount==0){
			Do("Out of "+ o.choice);
			o.w.msgOutOf(o.choice,o.table);
			o.s=OrderState.canceled;
			Do("Short of "+ o.choice);
			orderFoodThatIsLow();
		}
		else{
			if((inventory.get(o.choice)).amount<=(inventory.get(o.choice)).low){
				orderFoodThatIsLow();
			}

			inventory.get(o.choice).amount--;
			print("Cooking"+ o.choice);
			//hack!!!! what it should be
			DoCooking(o);//animation
			//o.s=OrderState.Cooking;
			final Order o2=o;

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
					print("Done Cooking");
					o2.s=OrderState.Done;
					stateChanged();

				}
			},
			(long)(500*(inventory.get(o.choice).cookTime)));//getHungerLevel() * 1000);//how long to wait before running task



		}
	}

	private void PlateOrder(Order o){
		o.s=OrderState.DoneAndCalled;
		cookGui.DoGoToGrill(o.pos, null);
		cookGui.setWithFood(o.choice);
		cookGui.DoPlateIt(o.pos,o.choice);
		o.w.msgOrderIsReady(o.table, o.choice);

	}

	private void orderFoodThatIsLow(){
		List<Pair> goods= new ArrayList<Pair>();
		Set<String> keys=inventory.keySet();
		for(String key : keys){
			if(inventory.get(key).amount<=inventory.get(key).low && !inventory.get(key).ordered){
				goods.add(new Pair(key,inventory.get(key).capacity-inventory.get(key).amount));
				Do("Ordering "+ inventory.get(key).type +" of "+(inventory.get(key).capacity-inventory.get(key).amount) );
				inventory.get(key).ordered=true;
			}
		}
		if(!goods.isEmpty()){
			Market m=PickMarket();
			//m.msgHereIsAnOrder(this, goods);
			
			
			
			//TODO
			//m.host.msgBusinessWantsThis(restaurant, shoppingList);
		}

	}

	private void DoCooking(Order o){

		cookGui.DoGoToFridge(o.choice);
		cookGui.DoGoToGrill(o.pos,o.choice);
		//cooking.acquire();//this put cook agent to sleep until cooking.release called in animation


	}

	public void changeShifts(Person p){
		if (this.self!=null){
			cookGui.DoExit();
			this.self.msgThisRoleDone(this);
		}
		
		
		this.self = p;
		this.name = p.getName()+" Cook";
	}
	
	
	//utilities
	public CookGui getGui(){
		return cookGui;
	}

	private Market PickMarket(){
		Market m=markets.get(marketCounter);

		marketCounter=(marketCounter+1)%(markets.size());
		return m;
	}

	public String getName(){
		return name;
	}
	public String toString() {
		return "cook " + getName();
	}


	@Override
	public void msgHereIsDelivery(MarketInvoice order) {
		
		
	}


	@Override
	public void msgCannotFulfillOrder(Market m,
			Map<String, Integer> unfulfillable) {
		
		
	}


	@Override
	public boolean isPresent() {
		
		return self!=null;
	}


	@Override
	public boolean canLeave() {
	
		return restaurant.customers.size()==0;
	}


	public void decreaseInventory(String choice){
		inventory.get(choice).amount--;
		
		DoInfo(choice + " is now at " + inventory.get(choice).amount);
		
		if (p!=null)
			p.msgStateChanged();
	}
	
	public void increaseInventory(String choice){
		inventory.get(choice).amount++;
		
		DoInfo(choice + " is now at " + inventory.get(choice).amount);
		
		if (p!=null)
			p.msgStateChanged();
	}


	@Override
	public void depleteInventory() {

		inventory.get("Steak").amount=0;
		inventory.get("Salad").amount=0;
		inventory.get("Chicken").amount=0;
		inventory.get("Pizza").amount=0;
		

	}
	
	public void DoInfo(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));
	}
	
	public void DoMessage(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);
		log.add(new LoggedEvent(message));		
	}
	
	public void DoDebug(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);	
	}
	
	public void DoError(String message){
		//super.Do(message);
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logError(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);	
	}


public void Do(String message){
		
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);
		
	}

}
