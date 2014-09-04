package interfaces;

import java.util.Map;

import person.PersonAgent;
import market.Receipt;

public interface MarketCustomer {

	public void msgHereAreItems(Map<String, Integer> groceries);
	public void msgHereIsTotal(int total);
	public void msgHereIsYourChange(Receipt receipt, int change);
	public void msgYouOweMoney(Receipt receipt, int debt);
	public void msgYouCanLeave();
	public void msgOutOfStock(Map<String, Integer> unfullfillable);
	public void msgWeHaveNothing();
	public abstract String getName();
	public abstract void msgNoEmployees();
	
	public Person getPerson();
}
