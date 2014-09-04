package util;

import java.util.concurrent.Semaphore;

import person.PersonAgent;
import cityGui.SimCityGui;
import cityGui.test.CarAgentGui;

public class CarAgent extends VehicleAgent{
	
	
	//Data
	
	public SimCityGui cityGui;
	
	public CarAgentGui gui;
	public PersonAgent passenger;
	
	public OnRamp currentLocation;
	public OnRamp destination;
	
	public boolean inUse = false;
	
	public Semaphore atDestination = new Semaphore(0,true);
	
	
	//Messages
	
	public void msgTakeMeTo(OnRamp from, OnRamp to){
		Do("Got the message to start moving");
		inUse = true;
		gui.clockwise = from.innerLane;
		this.currentLocation = from;
		this.destination = to;
		stateChanged();
		//System.out.println("Num permits "+stateChange.availablePermits());
		//startThread();
	}
	
	
	public void msgCarArrived(Loc loc){
		atDestination.release();
	}
	
	
	//Scheduler
	
	//@Override
	public boolean pickAndExecuteAnAction(){
		Do("Scheduler being called");
		if(inUse){
			DriveToDestination();
			return true;
		}
		
		return false;
	}
	
	
	//Actions
	
	public void DriveToDestination(){
		Do("Driving action");
		
		
		while(!gui.putCarOnRoad(currentLocation)){};
		passenger.msgCarOnRoad();
		//cityGui.city.movings.add(gui);
		
		doDriveToDestination();
		
		passenger.msgCarArrivedAtRamp(destination);
		
		//synchronized(cityGui.city.movings){
		cityGui.city.movings.remove(gui);
		//}
		
		inUse = false;
		
		//this.stopThread();
		
	}
	
	
	//Gui
	
	public void doDriveToDestination(){
		
		
		gui.goTo(destination.loc.x, destination.loc.y);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
