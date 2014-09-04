package restaurant.restaurantParker;

import interfaces.restaurantParker.Waiter;


public class MyWaiter {
	
	WaiterRole waiter;
	
	int numberCustomers = 0;
	
	enum MyWaiterState {working, wantBreak, onBreak, leavingBreak};
	MyWaiterState state = MyWaiterState.working;
	
	
	
	MyWaiter(){
	
	}
	
	MyWaiter(WaiterRole r){
		waiter = r;
		state = MyWaiterState.working;
	}
}
