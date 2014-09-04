package restaurant.restaurantYocca;

import interfaces.restaurantYocca.Waiter;

public class Order {
	
	public Order () {
	}
	
	OrderState state;
	
	Order(Waiter w, String ch, Table t, OrderState s){
		waiter = w;
		choice = ch;
		table = t;
		table.tableNumber = t.getTableNumber();
		state = s;
	}	
	
	private Table table;
	private String choice;
	public Waiter waiter = null;
	public boolean cooking = false;
	
	public enum OrderState{pending,cooking,done,finished}
	
	public void setChoice(String c) {
		choice = c;
	}
	
	public String getChoice() {
		return choice;
	}
	
	public void setTable(Table t) {
		table = t;
	}
	
	public Table getTable() {
		return table;
	}
	
	public boolean isBeingCooked() {
		return cooking;
	}
	
	public void setBeingCooked(boolean t) {
		cooking = t;
	}
	
	public void setWaiter(Waiter w) {
		waiter = w;
	}
}
