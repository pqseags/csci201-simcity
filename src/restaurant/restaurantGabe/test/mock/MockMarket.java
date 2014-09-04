package restaurant.restaurantGabe.test.mock;

//import restaurant.restaurantGabe.interfaces.Market;

public class MockMarket extends Mock /*implements interfaces.Market*/{
	
	public EventLog log = new EventLog();
	
	@Override
	public String getName(){
		return super.getName();
	}
	
	
	public MockMarket(String name) {
		super(name);

	}
	
	public void msgHereIsPayment(int amount){
		
		log.add(new LoggedEvent("Recieved a payment of $"+amount));
		
	}
	
	public void msgCantPay(){
		
		log.add(new LoggedEvent("Cashier's debt will be added to next bill"));
		
	}

}
