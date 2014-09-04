package house;
import house.gui.InhabitantGui;
import interfaces.Person;

import java.util.*;

import public_Object.Food;

public class LivingUnit {
	//data
	public InhabitantRole inhabitant=null;
	public List<Food> inventory=new ArrayList<Food>(); //should we have many kinds of food or only one at the very beginning?
	
	public LivingUnit(){
		inventory.add(new Food("Steak",15));
		
	}
	
	
	
	//msg
	
	
	
	public void msgLeaveHome(){ //called by Person
		inhabitant=null;
	}
	public void msgFoodIn(List<Food> foods){
		for(Food bought : foods){

			boolean added=false;
			for(Food food : inventory){
				if(bought.type.equals(food.type)){
					food.quantity+=bought.quantity;
					added=true;
				}
			}
			if(!added){
				inventory.add(new Food(bought));
			}

		}
	}
	
	


}
