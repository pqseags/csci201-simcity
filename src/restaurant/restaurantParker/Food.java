package restaurant.restaurantParker;

public class Food {
	
	
	String choice;
	int cookTime;
	public int amount;
	
	int amountOrdered;
	int marketIndex;


	Food(String choice, int cookTime, int amount){
		this.choice = choice;
		this.cookTime = cookTime;
		this. amount = amount;
		
		amountOrdered = 0;
		marketIndex = 0;
		
	}

}
