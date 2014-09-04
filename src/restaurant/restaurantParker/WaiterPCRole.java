package restaurant.restaurantParker;

import restaurant.restaurantParker.MyCustomer.MyCustomerState;
import person.PersonAgent;


public class WaiterPCRole extends WaiterRole{

	RevolvingStand<RevolvingStandOrder> revolvingStand;
	
	public WaiterPCRole(String name, PersonAgent p) {
		super(name);
		this.p = p;
		
	}

	//everything else in WaiterAgent class
	
	@Override
	void placeOrder(MyCustomer mc) {
		RevolvingStandOrder order = new RevolvingStandOrder( mc.choice, mc.tableNumber, mc.customer, this);
		DoPlaceOrder(mc);
		
		mc.state = MyCustomerState.orderedAndWaiting;
		
		print("Adding the order to the revolving stand");
		
		//add it to the stand
		revolvingStand.insert(order);
	}
	
	public void setRevolvingStand(RevolvingStand<RevolvingStandOrder> stand){
		revolvingStand = stand;
	}

	@Override
	public boolean canLeave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	


}
