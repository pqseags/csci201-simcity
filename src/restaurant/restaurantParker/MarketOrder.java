package restaurant.restaurantParker;


public class MarketOrder {

	String choice;
	int quantity;
	
	enum MarketOrderState {Ordered, Locating, Ready, Shipped};
	MarketOrderState state = MarketOrderState.Ordered;
	
	
	MarketOrder(String choice, int quantity){
		this.choice = choice;
		this.quantity = quantity;
	}
	
}
