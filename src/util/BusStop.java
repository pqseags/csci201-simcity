package util;

import interfaces.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusStop extends Place{
	
	public BusStop(){
		
	}
	
	public BusStop(Loc loc){
		this.location = loc;
	}
	
	public BusStop(Loc location,List<Person> peopleWaiting){

		this.location = location;
		this.peopleWaiting = peopleWaiting;
	}
	
	public Loc location = new Loc(-1000,-1000);
	public Loc sidewalkLoc =  new Loc(0,0);
	public List<Person> peopleWaiting = Collections.synchronizedList(new ArrayList<Person>());
	
	
	public void waitForBus(Person p){
		System.out.println("Waiting at the bus Stop");
		peopleWaiting.add(p);
	}
	
	
	
	

}
