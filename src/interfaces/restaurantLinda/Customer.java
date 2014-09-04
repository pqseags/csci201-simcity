package interfaces.restaurantLinda;

import interfaces.BaseRestaurantCustomer;
import restaurant.restaurantLinda.Check;
import restaurant.restaurantLinda.Menu;
import restaurant.restaurantLinda.gui.CustomerGui;

public interface Customer extends BaseRestaurantCustomer{
	
	public abstract void msgRestaurantFull();

	public abstract void msgFollowMe(Waiter w, Menu m);
	
	public abstract void msgWhatDoYouWant();
	
	public abstract void msgRedoOrder(Menu menu,String food);
	
	public abstract void msgHereIsFood(String food);
	
	public abstract void msgHereIsCheck(Check bill, Cashier cashier);
	
	public abstract void msgPaymentReceived(int owed);
	
	public abstract String getName();
	
	public abstract CustomerGui getGui();

}
