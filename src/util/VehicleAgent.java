package util;

import interfaces.Person;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;

public class VehicleAgent extends Agent {

	//Data
	
	public List<Person> passengers  = Collections.synchronizedList(new ArrayList<Person>());
	//public Rectangle safeRegion;
	
	
	
	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
