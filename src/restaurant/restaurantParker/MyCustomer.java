package restaurant.restaurantParker;

public class MyCustomer {

	CustomerRole customer;
	int tableNumber;
	int lobbyPosition;
	String choice;
	int billTotal;
	enum MyCustomerState {passive, waiting, seated, readyToOrder, asked, ordered, orderedAndWaiting, refused, foodReady, served, wantCheck, askedCheck, waitingCheck, gotCheck, leaving};
	MyCustomerState state = MyCustomerState.passive;
	
	MyCustomer(int table, CustomerRole c, int p){
		customer = c;
		tableNumber = table;
		lobbyPosition = p;
	}
}
