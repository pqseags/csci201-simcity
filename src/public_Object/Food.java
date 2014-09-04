package public_Object;

public class Food {

	public String type;
	public int quantity;
	//int expirationDate;
	
	public Food(String t, int q){
		type=t;
		quantity=q;
	}
	
	public Food(Food f){
		type=f.type;//hack!!! does this do deep copy?
		quantity=f.quantity;
	}



}
