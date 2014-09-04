package restaurant.restaurantParker;

public class LobbyCustomer {

	int seat;
	CustomerRole customer;
	enum CustomerState { passive, entered, staying, leaving};
	CustomerState state = CustomerState.passive;
	
	LobbyCustomer(CustomerRole cust){
		customer = cust;
		seat = -1;
	}
}
