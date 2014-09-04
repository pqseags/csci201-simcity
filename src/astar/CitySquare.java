package astar;

import java.util.concurrent.Semaphore;


//This class will be used as the locations in the city grid 
public class CitySquare {
	
	//probably going to be used only for vehicles, since those can't bump into
	//eachother on the road
	public Semaphore gridSem;
	
	//This will let other code know if the crosswalk is being used by traffic
	//or pedestrians
	public GridOwner who;
	

}
