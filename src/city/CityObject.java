package city;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.Timer;

import cityGui.CityRestaurantSimon;
import cityGui.SimCityGui;
import person.PersonAgent;
import restaurant.Restaurant;
import role.Role;
import market.Market;
import market.MarketCashierRole;
import market.MarketEmployeeRole;
import market.MarketHostRole;
import bank.BankCustomerRole;
import bank.BankTellerRole;
import util.Bank;
import util.BankMapLoc;
import util.Bus;
import util.BusAgent;
import util.BusStop;
import util.CityMap;
import util.Job;
import util.JobType;
import util.MarketMapLoc;
import util.Place;
import util.RestaurantMapLoc;

public class CityObject implements ActionListener{

	
	
	//TTHIS WILL BE THE MAIN CITY OBJECT WHICH WILL HOLD THE CITY 
	//AND EVERYTHING IN IT.
	
	//IT WILL ALSO CONNECT AGENTS/ROLES/BUILDINGS VIA THE 
	//APPROPRIATE POINTER-SETTER METHODS
	
	
	//========================A City Will Need:====================================
	
	//======MAP======
	
	public CityMap cityMap = new CityMap();
	
	public final int numBanks = 0;
	public final int numMarkets = 0;
	public final int numRestaurants = 0;
	public int MAXTIME = 100;
	
	List<BankMapLoc> banks = new ArrayList<BankMapLoc>();
	List<MarketMapLoc> markets = new ArrayList<MarketMapLoc>();
	List<RestaurantMapLoc> restaurants = new ArrayList<RestaurantMapLoc>();
	
	//public List<BusStop> fStops = new ArrayList<BusStop>();
	//public List<BusStop> bStops = new ArrayList<BusStop>();
	public BusAgent fBus;
	public BusAgent bBus;
	
	int currentTime = 0;
	static int TIMER_DELAY = 3000;
	
	SimCityGui mainCity;
	
	
	public List<PersonAgent> people = new ArrayList<PersonAgent>();
	public final int NUM_PEOPLE = 2;
	
	
	public CityObject(SimCityGui s){
		
		mainCity = s;
		
		Timer timer = new Timer(TIMER_DELAY, this);
		timer.start();
		
		cityMap.map.put("Bank", new ArrayList<Place>());
		cityMap.map.put("Market", new ArrayList<Place>());
		cityMap.map.put("Restaurant", new ArrayList<Place>());
		cityMap.map.put("House", new ArrayList<Place>());
		//cityMap.map.put("BusStop", new ArrayList<Place>());
		
		
		
		
		
		//CREATE THE OBJECTS, ADD THEM TO THE MAP
		
		
		//======BUILDINGS======
		
		//==Banks==
		
		for(int i = 0;i<numBanks;++i){
			Bank b = new Bank();
			BankMapLoc bMap = new BankMapLoc(b);
			cityMap.map.get("Bank").add(bMap);
			banks.add(bMap);
		}
		
		
		//==Restaurants==
		
//		for(int i = 0;i<numRestaurants;++i){
//			Restaurant r = new Restaurant();
//			RestaurantMapLoc rMap = new RestaurantMapLoc(r);
//			cityMap.map.get("Restaurant").add(rMap);
//			restaurants.add(rMap);
//		}
		
		//==Markets==
		
		for(int i = 0;i<numMarkets;i++){
			Market m = new Market();
			//m.host = new MarketHostRole(null,null);
			MarketMapLoc mMap = new MarketMapLoc(m);
			cityMap.map.get("Market").add(mMap);
			markets.add(mMap);
		}
		
		
		
		//==Houses==
		
		
		//======PEOPLE======
		
		//==People
			//+ Will need to be instantiated with different jobs/housing situations
			//+ Instantiate with map - they'll have almost all the pointers they'll need
		
		
		/*
		
		for(int i = 0;i<NUM_PEOPLE;++i){
			PersonAgent p = new PersonAgent("p"+i,cityMap);
			p.startThread();
			people.add(p);
			
		}
		//people.get(0).myJob = new Job(new BankTellerRole("p0Teller"),0,1,10,cityMap.map.get("Bank").get(0),people.get(0));
		people.get(0).setJob(banks.get(0).bank, JobType.BankTeller,1,4);
		people.get(1).setJob(banks.get(0).bank, JobType.BankTeller,4,10);
		//System.out.println(people.get(1).myJob.shiftStart);
		
		PersonAgent p3 = new PersonAgent("p3",cityMap);
		//p3.myJob = new Job(new BankTellerRole("p3Teller"),0,0,1,cityMap.map.get("Bank").get(0),p3);
		p3.setJob(banks.get(0).bank, JobType.BankTeller, 0, 1);
		p3.startThread();
		people.add(p3);
		
		PersonAgent p4 = new PersonAgent("p4",cityMap);
		p4.setJob(markets.get(0).market, JobType.MarketHost, 0, 6);
		p4.startThread();
		people.add(p4);
		
		PersonAgent p5 = new PersonAgent("p5",cityMap);
		p5.setJob(markets.get(0).market,JobType.MarketEmployee,0,6);
		p5.startThread();
		people.add(p5);
		
		PersonAgent p6 = new PersonAgent("p6",cityMap);
		p6.setJob(markets.get(0).market, JobType.MarketCashier, 0, 6);
		p6.startThread();
		people.add(p6);
		
		*/
		
		
		
		/*testPerson p0 = new testPerson("p0",cityMap);
		p0.startThread();
		
		p0.msgGoToMarket();*/
		
		
		//======TRANSPORTATION======
		
		//==Buses==
		
	}
		

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		updateTime();
		
	}
	
	
	public void updateTime(){
		System.out.println("CITY: Updating time to "+(currentTime+1)%MAXTIME);
		currentTime = (currentTime+1)%MAXTIME;
		for(PersonAgent p:people){
			p.setTime(currentTime);
			p.msgStateChanged();
		}
		if(fBus!=null){
			//fBus.updateTime(currentTime);
		}
		if(bBus!=null){
		//	bBus.updateTime(currentTime);
		}
		//System.out.println(cityMap.map.get("Bank").size());
		
		
	}
	
	
	
	
	/*public static void main(String [] args){
		
		//CityObject city = new CityObject();
		
		
		return;
	}*/
	
	
	
}
