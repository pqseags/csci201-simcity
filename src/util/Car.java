package util;

import interfaces.Person;

public class Car extends VehicleAgent{

	Person passenger;
	Loc currentLocation;
	Loc destination;
	String color;
	public enum DriveState {notInUse,inTransit,arrived};
	public DriveState driveState = DriveState.notInUse;
	cityGui.Vehicle carGui;
	
	public Car(){
		color = "black";
		carGui = new cityGui.Vehicle(0, 0, 10, 10);
	}
	
	public Car(String color){
		this.color = color;
		carGui = new cityGui.Vehicle(0, 0, 10, 10);
	}
	
	//public method to be called by a person
	public void drive(Loc a,Loc b){
		currentLocation = a;
		destination = b;
		doDrive(currentLocation,destination);
	}
	
	//private drive call that will handle animation and the notification"
	private void doDrive(Loc current, Loc dest){
		driveState = DriveState.inTransit;
		carGui.doDriveFromLocToLoc(current,dest);
		driveState = DriveState.arrived;
		//passenger.msgCarArrivedAtLoc(dest);
		driveState = DriveState.notInUse;
	}
	
}
