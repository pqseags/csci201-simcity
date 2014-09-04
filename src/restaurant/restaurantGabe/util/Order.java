package restaurant.restaurantGabe.util;

import java.util.Timer;

import restaurant.restaurantGabe.CustomerRole;
import restaurant.restaurantGabe.WaiterRole;


//The enum is in its own file - couldn't think of a better way
//enum OrderState {ordered,requested,cooked,served};


public class Order {
	public Order(CustomerRole c,String choice,OrderState s,WaiterRole w){
		this.c = c;
		this.choice = choice;
		this.s = s;
		this.w = w;
	}
	public CustomerRole c;
	public WaiterRole w;
	public String choice;
	public OrderState s;
	public Timer cooktimer;
}
