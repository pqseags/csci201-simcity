package house;

import java.util.Timer;
import java.util.TimerTask;

import UnitTests.mock.MockPerson;
import public_Object.Food;
import house.gui.InhabitantGui;
import interfaces.Inhabitant;
import interfaces.Person;
import role.Role;

public class InhabitantRole extends Role implements Inhabitant {

	//Constructor
	public InhabitantRole(){
	}
	
	public InhabitantRole(String name,Person p){
		this.self = p;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	
	//data
	String name;
	LivingUnit myRoom=new LivingUnit();
	public Person self;
	//enum InhabitantState {IDLE,HUNGRY,FOODREADY, TIRED,EXIT};
	//public InhabitantState s=InhabitantState.IDLE;
	public boolean wantEat=false;
	public boolean wantSleep=false;
	public boolean wantLeave=false;
	public boolean foodReady=false;
	InhabitantGui gui;

	String foodEating=null;
	Timer timer=new Timer();
	int sleepTime=1000;
	int cookingTime=1000;
	
	//msg
	
	public void msgTired(){
		//Do("I am tired");
		wantSleep=true;
		self.msgStateChanged();
	}
	
	public void msgGotHungry(){ //called by Person
		wantEat=true;
		self.msgStateChanged();
	}
	public void msgFoodReady(){ //called by Timer//may be able to merge all into one action
		foodReady=true;
		self.msgStateChanged();
	}
	public void msgLeaveHouse(){
		wantLeave=true;
		self.msgStateChanged();

	}

		
		

	

	//scheduler
	public boolean pickAndExecuteAnAction() {
		//Do("Deciding what to do");
		if(foodReady){
			PlateAndEat();
			return true;
		}
		if(wantEat){
			GetAndCook();
			return true;
		}
		if(wantSleep){
			Sleep();
			return true;
		}
		if(wantLeave){
			ExitHouse();
			return true;
		}
		ExitHouse();//hack!!! should receive message from person to exit
		//gui.DoIdle();	
		return false;
	}
	
	//action
	
	private void Sleep(){
		Do("Going to sleep");
		wantSleep=false;
		if(gui!=null)
		gui.DoSleep();
		try {
		    Thread.sleep(sleepTime);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		self.msgDoneSleeping();
		Do("I HAVE AWAKENED");
	}
	
	private void GetAndCook(){
		wantEat=false;
		if(gui!=null)
		gui.DoGoToFridge();
		//System.err.println("Picking");
		PickFood();
		if(gui!=null)
		gui.DoGoToGrill();
		if(gui!=null)
		gui.DoIdle();
		try {
		   
			Thread.sleep(cookingTime);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		foodReady=true;
		self.msgStateChanged();

		
		
	}
	private void PlateAndEat(){
		foodReady=false;
		if(gui!=null)
		gui.DoPlateAndEat();
		self.msgDoneEating();
	}
	private void ExitHouse(){
		wantLeave=false;
		if(gui!=null)//hack!!! change later
			gui.DoExit();
		if(myRoom!=null && myRoom.inhabitant!=null){
			
			myRoom.inhabitant=null;
		}
		self.msgThisRoleDone(this);
	}
	
	private void PickFood(){
		for(Food food : myRoom.inventory){
			if(food.quantity>0){
				food.quantity--;
				if(!(self instanceof MockPerson))
				 self.getHouse().controlPanel.foodEaten(food.type);
				foodEating=food.type;
				break;
			}
		}
	}
	//Animation
	
	
	//utilities
	public void setGui(InhabitantGui g){
		gui=g;
		
	}
	
	
}
