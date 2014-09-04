package restaurant.restaurantLinda;

public class Check {
	private String order;
	private int total;
	
	public Check(String choice, int cost){
		order=choice;
		total=cost;
	}
	
	public boolean equals(Object anObject){
		if (anObject instanceof Check){
			Check c = (Check) anObject;
			return c.order.equals(order) && c.total==total;
		}
		return false;
	}
	
	public int getTotal(){
		return total;
	}
	
	public String toString(){
		return "{Order=" + order + ", Total=" + total+"}";
	}
}
