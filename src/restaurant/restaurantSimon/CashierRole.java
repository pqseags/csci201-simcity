package restaurant.restaurantSimon;

import interfaces.MarketDeliveryMan;
import interfaces.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;















import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import market.MarketInvoice;
import restaurant.restaurantSimon.interfaces.Cashier;
import restaurant.restaurantSimon.interfaces.Customer;
import restaurant.restaurantSimon.interfaces.Market;
import restaurant.restaurantSimon.interfaces.Waiter;
import role.Role;

public class CashierRole extends Role implements Cashier{

	public enum CheckState{Pending,Done,Sent,Collected};
	public class Check{  //to give waiters for customers


		public Waiter w;
		String choice;
		public Customer customer;
		public double amount=0;
		public CheckState s=CheckState.Pending;

		public Check(Waiter waiter, String ch, Customer cus){
			w=waiter;
			choice=ch;
			customer=cus;

		}

	}
	public enum BillState{Pending,Payed,CannotAfford};
	public class Bill{ //for market
		public double total;
		public Market market;
		public BillState s=BillState.Pending;
		public Bill(double t, Market m){
			total=t;
			market=m;

		}

	}


	public CashierRole(){
		menu=new HashMap<>();
		menu.put("Steak",15.99);
		menu.put("Salad",5.99);
		menu.put("Chicken", 10.99);
		menu.put("Pizza", 8.99);


	}
	public CashierRole(String n, RestaurantSimon r){
		name=n;
		menu=new HashMap<>();
		menu.put("Steak",15.99);
		menu.put("Salad",5.99);
		menu.put("Chicken", 10.99);
		menu.put("Pizza", 8.99);
		restaurant=r;


	}

	//data
	Person self;
	String name;
	public double accountBalance=20;
	public List<Check> checks= Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills=Collections.synchronizedList(new ArrayList<Bill>());
	Timer timer=new Timer();
	private Map <String , Double> menu;
	int marketCounter=0;
	Semaphore cooking= new Semaphore(1,true);
	RestaurantSimon restaurant=null;



	//msg


	public void msgCheckFromMarket(Market market, double total){
		Do("Got check from "+market);
		bills.add(new Bill(total, market));
		self.msgStateChanged();
	}


	public void msgHereIsMoney(Customer cus, double amount){
		synchronized(checks){
			for(Check check :checks){
				if (check.customer==cus){
					check.amount-=amount;
					accountBalance+=amount;
					check.s=CheckState.Collected;
				}
			}
		}
		synchronized(bills){
			for(Bill bill : bills){
				if(bill.s==BillState.CannotAfford){
					bill.s=BillState.Pending;
					Do("collected money from " +cus+ ", now try to pay again");
				}
			}
		}
		self.msgStateChanged();
		}

	public void msgHereIsARequest(Waiter waiter, String ch, Customer cus){
		boolean neW=true;
		synchronized(checks){
			for(Check check : checks){
				if(check.customer==cus){
					check.w=waiter;
					check.choice=ch;
					check.s=CheckState.Pending;
					print("I got the request for check");
					neW=false;
					self.msgStateChanged();
				}
			}
		}
		if(neW){
			checks.add(new Check(waiter,ch,cus));
			print("I got the request for check");
			self.msgStateChanged();
		}
	}

	public void checkSent(Check c){//called by timer
		c.s = CheckState.Done;
		self.msgStateChanged();
	}


	public void msgGiveMeCheck(WaiterRole w){
		synchronized(checks){
			for(Check check : checks){
				if(check.w==w){
					check.s=CheckState.Sent;
				}
			}
		}
		self.msgStateChanged();

	}


	//scheduler

	@Override
	public boolean pickAndExecuteAnAction() {//hack!! add more in bewteen
		synchronized(checks){
			for(Check check : checks){
				if(check.s==CheckState.Done){
					sendCheck(check);
					return true;
				}
			}
		}
		synchronized(checks){
			for(Check check : checks){
				if(check.s==CheckState.Pending){
					computeCheck(check);
					return true;
				}
			}
		}
		synchronized(bills){
			for(Bill bill : bills){
				if(bill.s==BillState.Pending){
					payBill(bill);
					return true;
				}

			}
		}



		return false;
	}


	//actions

	private void payBill(Bill b){
		if(accountBalance>=b.total){
			accountBalance-=b.total;
			b.market.msgHereIsMoney(b.total);
			b.s=BillState.Payed;
			Do("bill payed to "+ b.market);
		}
		else {
			Do("cannot afford check, will pay"+ b.market+ " later");
			b.s=BillState.CannotAfford;
		}

	}


	private void computeCheck( Check ch){
		Set<String> keys=menu.keySet();
		for(String key : keys){
			if (key==ch.choice){
				ch.amount+=menu.get(key);
			}
		}
		ch.s=CheckState.Done;
	}
	private void sendCheck(Check check){
		check.w.msgCheckIsReady(check.customer, check.amount);
		check.s=CheckState.Sent;
	}



	public void changeShifts(Person p){
		if (this.self!=null)
			this.self.msgThisRoleDone(this);
		
		this.self = p;
		this.name = p.getName()+" Cashier";
	}
	//utilities

	public String getName(){
		return name;
	}
	public String toString() {
		return "waiter " + getName();
	}
	@Override
	public void msgHereIsInvoice(MarketDeliveryMan deliveryMan,
			MarketInvoice order) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return self!=null;
	}
	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return restaurant.customers.size()==0;
	}
public void Do(String message){
		
		if (restaurant.cityRestaurant!=null)
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_SIMON, name, message, restaurant.cityRestaurant.ID);
		
	}
}
