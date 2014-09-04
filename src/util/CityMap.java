package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityMap {
	
	
	public Map<String,ArrayList<Place>> map = new HashMap<String,ArrayList<Place>>();
	
	public List<BusStop> fStops = new ArrayList<BusStop>();
	public List<BusStop> bStops = new ArrayList<BusStop>();
	
	public int peopleCount = 0;
}
