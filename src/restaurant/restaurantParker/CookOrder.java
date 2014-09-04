package restaurant.restaurantParker;

public class CookOrder {
	
	String choice;
	int amountOrdered;
	int amountReceived;
	
	CookOrder(String choice, int amountOrdered, int amountReceived){
		this.choice = choice;
		this.amountOrdered = amountOrdered;
		this.amountReceived = amountReceived;
	}

}
