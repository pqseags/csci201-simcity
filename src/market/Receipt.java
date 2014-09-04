package market;

import interfaces.MarketCashier;

import java.util.Map;

public class Receipt {
	 Map<String, Integer> order;
	 int total;
	 int payment;
	 MarketCashier cashier;
	 
	 public Receipt(Map<String,Integer> order, int total, int payment, MarketCashier cashier){
		 this.order = order;
		 this.total = total;
		 this.payment = payment;
		 this.cashier = cashier;
	 }
	    
}
