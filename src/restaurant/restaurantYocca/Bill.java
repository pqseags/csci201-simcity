package restaurant.restaurantYocca;

import market.Market;
import interfaces.restaurantYocca.Customer;
import interfaces.restaurantYocca.Waiter;

public class Bill {
	private String choice;
	private double price;
	private double amountPaid = 0;
	private double amountToPay;
	private boolean paid = false;
	private boolean computing = false;
	private boolean givenChange = false;
	public Customer customer = null;
	public Waiter waiter = null;
	public Market market = null;
	
	public Bill(String choice, Customer c, Waiter w) {
		customer = c;
		waiter = w;
		if (choice == "St") {
			price = 15.99;
		}
		if (choice == "C") {
			price = 10.99;
		}
		if (choice == "Sa") {
			price = 5.99;
		}
		if (choice == "P") {
			price = 8.99;
		}
		amountToPay = price;
	}
	
	public Bill(String choice, double amount, Market m) {
		market = m;
		if (choice == "St") {		
			price = 15.99 * amount;
		}
		if (choice == "C") {
			price = 10.99 * amount;
		}
		if (choice == "Sa") {
			price = 5.99 * amount;
		}
		if (choice == "P") {
			price = 8.99 * amount;
		}
		amountToPay = price;
	}
	
	Bill(Bill c) {
		this.customer = c.customer;
		this.waiter = c.waiter;
		this.price = c.price;
		this.amountToPay = c.amountToPay;
		this.amountPaid = c.amountPaid;
	}
	
	public void setPaid(boolean p) {
		paid = p;
	}
	
	public boolean hasPaid() {
		return isPaid();
	}
	
	public void setComputing(boolean b) {
		computing = b;
	}
	
	public boolean isComputing() {
		return computing;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getAmountToPay() {
		return amountToPay;
	}
	
	public void setAmountToPay(double amount) {
		amountToPay = amount;
	}
	
	public double getAmountPaid() {
		return amountPaid;
	}
	
	public void setAmountPaid(double amount) {
		amountPaid = amount;
	}

	public boolean hasGivenChange() {
		return givenChange;
	}
	
	public void setGivenChange(boolean b) {
		givenChange = b;
	}

	public boolean isPaid() {
		return paid;
	}
}
