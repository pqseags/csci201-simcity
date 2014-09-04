package market;

public class OrderItem {
	
	public int quantityOrdered;
	public int quantityReceived;
	public String choice;
	

	public OrderItem( String choice, int quantity){
		this.choice = choice;
		this.quantityOrdered = quantity;
		quantityReceived = 0;
	}
	
	//Overloaded constructor for unit tests
	public OrderItem( String choice, int quantity, int received){
		this.choice = choice;
		this.quantityOrdered = quantity;
		quantityReceived = received;
	}
}
