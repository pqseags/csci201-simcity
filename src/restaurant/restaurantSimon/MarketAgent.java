//package restaurant;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.Semaphore;
//
//import restaurant.CookAgent.Pair;
//import restaurant.interfaces.Market;
//import agent.Agent;
//
//public class MarketAgent extends Agent implements Market {
//
//
//
//	public enum OrderState{Pending,Delivered};
//	class Order{
//
//
//		CookAgent cook;
//		List <Pair> pairList;
//		private OrderState s=OrderState.Pending;
//
//		public Order(CookAgent co, List<Pair> pl){
//			cook=co;
//			pairList=pl;
//		}
//
//	}
//	private class FoodData{
//		String type;
//		double dileverTime;
//		double price;
//		int number=20;
//		public FoodData(String t, int dt,double pr){
//			type=t;
//			dileverTime=dt;
//			price=pr;
//			
//		}
//	}
//
//	public MarketAgent(String n){
//		name=n;
//		inventory=new HashMap<>();
//		inventory.put("Steak", new FoodData("Steak",7, 10));
//		inventory.put("Salad", new FoodData("Salad",5, 2));
//		inventory.put("Chicken", new FoodData("Chicken",6, 8));
//		inventory.put("Pizza", new FoodData("Pizza",5, 5));
//
//
//		//hack check and add more
//	}
//
//	//data
//	private String name;
//	private CashierAgent cashier;
//	private List<Order> orders=Collections.synchronizedList(new ArrayList<Order>());
//	Timer timer=new Timer();
//	private Map <String , FoodData> inventory;
//	private int dileveryTime=1;
//	private double owe=0;
//
//
//
//	//hack to establish relationship 
//	public void setCashier(CashierAgent ca){
//		cashier=ca;
//		
//	}
//	//msg
//	
//
//	public void msgHereIsMoney(double total){
//		owe-=total;
//		stateChanged();
//	}
//
//	public void msgHereIsAnOrder(CookAgent co, List<Pair> pl){
//		orders.add(new Order(co,pl));
//		print("I got the order");
//		stateChanged();
//	}
//
//	public void foodDone(Order o){//called by timer
//		o.s = OrderState.Delivered;
//		stateChanged();
//	}
//
//
//
//
//
//	//scheduler
//
//	@Override
//	protected boolean pickAndExecuteAnAction() {//hack!! add more in between
//		// TODO Auto-generated method stub
//		synchronized(orders){
//		for(Order order : orders){
//			if(order.s==OrderState.Pending){
//				sendOrder(order);
//
//				return true;
//			}
//		}
//		}
//
//		return false;
//	}
//
//
//	//actions
//
//	private void sendOrder( Order o){
//		final Order o2=o;
//		boolean send=false;
//		List<Pair> shortOf=new ArrayList<Pair>();
//		List<Pair> satisfied=new ArrayList<Pair>();
//		
//		for(Pair p : o.pairList){
//			if((inventory.get(p.type)).number<p.quantity){
//				if((inventory.get(p.type)).number!=0){
//					send=true;
//					
//				}
//				Do("Not enough "+ p.type + ", sending what I have");
//				shortOf.add(o.cook.new Pair(p.type,p.quantity-(inventory.get(p.type)).number));
//				satisfied.add(o.cook.new Pair(p.type,(inventory.get(p.type)).number));
//				owe+=(((inventory.get(p.type)).number)*(inventory.get(p.type).price));
//				(inventory.get(p.type)).number=0;
//				
//				
//			}
//			else{
//				satisfied.add(o.cook.new Pair(p.type,p.quantity));
//				owe+=((p.quantity)*(inventory.get(p.type).price));
//				inventory.get(p.type).number-=p.quantity;
//				print("Sending"+ p.type);
//				send=true;
//				
//			}
//		}
//		if(!shortOf.isEmpty()){
//			o.cook.msgOutOf(this, shortOf);
//		}
//
//
//		if(send){
//			cashier.msgCheckFromMarket(this, owe);
//			final List<Pair> s=satisfied;
//			timer.schedule(new TimerTask() {
//				public void run() {
//					print("Done delivering ");
//					
//					Do("new Food delivered from market");
//					o2.cook.msgOrderDelivered(name,s);
//					stateChanged();
//
//				}
//			},
//			(long)(1000*dileveryTime));//getHungerLevel() * 1000);//how long to wait before running task
//			o.s=OrderState.Delivered;
//		}
//
//	DoSending(o);//animation
//	}
//
//
//	
//
//
//
//private void PlateIt(Order o){
//
//}
//
//private void DoSending(Order o){
//	/*
//	 * guiCook.cookIt(o.choice);
//	 * cooking.acquire();//this put cook agent to sleep until cooking.release called in animation
//	 * 
//	 */
//}
//public String getName(){
//	return name;
//}
//
//
//public String toString() {
//	return "market " + getName();
//}
//
//}
