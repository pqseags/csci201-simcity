package person;


import house.House;
import house.InhabitantRole;
import interfaces.BaseRestaurantCustomer;
import interfaces.Occupation;
import interfaces.Person;
import interfaces.PlaceOfWork;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bank.BankCustomerRole;
import bank.BankTellerRole;
import market.Market;
import market.MarketCustomerRole;
import market.MarketDeliveryManRole;
import market.MarketEmployeeRole;
import restaurant.Restaurant;
import restaurant.restaurantLinda.CustomerRole;
import restaurant.restaurantLinda.OriginalWaiterRole;
import restaurant.restaurantLinda.ProducerConsumerWaiterRole;
import role.Role;
import util.Bank;
import util.Bus;
import util.BusAgent;
import util.BusStop;
import util.CarAgent;
import util.CityMap;
import util.Job;
import util.JobType;
import util.Loc;
import util.MarketMapLoc;
import util.BankMapLoc;
import util.OnRamp;
import util.Place;
import util.RestaurantMapLoc;
import util.Task;
import util.deposit;
import util.openAccount;
import util.rob;
import util.takeLoan;
import util.withdrawal;
import interfaces.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

import cityGui.CityComponent;
import cityGui.test.AStarTraversalPerson;
import astar.*;
import cityGui.test.PersonGui;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import public_Object.Food;
import role.Role;
import agent.Agent;

public class PersonAgent extends Agent implements Person {

	public PersonAgent(String name, CityMap city) {
		this.name = name;
		this.city = city;
		if(city!=null){
			this.personID = city.peopleCount;
			city.peopleCount++;
		}
		belongings = new Belongings();
		myJob = new Job();
		purse = new Purse();
		bankRole = new BankCustomerRole(name+"Bank",this);
		marketRole = new MarketCustomerRole(name+"Market",this);
		inhabitantRole = new InhabitantRole(name + "Home",this);

		restaurantYoccaRole = new restaurant.restaurantYocca.CustomerRole(name +"Restaurant", this);
		restaurantLindaRole = new restaurant.restaurantLinda.CustomerRole(name+"Restaurant", this);
		restaurantGabeRole = new restaurant.restaurantGabe.CustomerRole(name+"Restaurant", this);

		restaurantParkerRole = new restaurant.restaurantParker.CustomerRole(name+"Restaurant", this);

		restaurantSimonRole=new restaurant.restaurantSimon.CustomerRole(name+"Restaurant", this);


		Random random = new Random();
		hungerLevel = random.nextInt(10);
		
		//Parker Restaurant Testing
		//hungerLevel = 100;


		if (random.nextBoolean()){
			this.belongings.myFoods.add(new Food("Steak",5));
			this.belongings.myFoods.add(new Food("Chicken",15));
			this.belongings.myFoods.add(new Food("Pizza",15));
			this.belongings.myFoods.add(new Food("Salad",15));
		}


		purse.wallet = 50;


		if(name.equals("Drive1")){
			driveHack = DriveHack.drive1;
		}
		else if(name.equals("Drive2")){
			driveHack = DriveHack.drive2;
		}
		if(name.equals("Bus1")){
			busHack = BusHack.bus1;
		}
		else if (name.equals("Bus2")){
			busHack = BusHack.bus2;
		}

		//myCar.gui = new CarAgentGui();
	}



	//GETTERS
	public String getName(){
		return name;
	}


	//SETTERS

	public void setAStar(AStarTraversalPerson a){
		this.aStar = a;
	}

	public void setTime(int time){
		this.time = time;
	}

	//data
	public List<Role> roles = new ArrayList<Role>();
	public int time;
	public String name;
	public CityMap city;
	public int activeRoleCalls = 0;
	//Time time;

	public int hungerLevel = 100;
	public int tiredLevel = 100;
	public int personalAddress;
	public Purse purse;
	public Belongings belongings;
	public Map<String, Integer> shoppingList;
	public Job myJob;
	public Role activeRole = null;
	public Role nextRole = null;
	public boolean wantsToBuyCar = false;
	public BankCustomerRole bankRole;
	public MarketCustomerRole marketRole;
	public InhabitantRole inhabitantRole;

	public restaurant.restaurantYocca.CustomerRole restaurantYoccaRole;
	public restaurant.restaurantLinda.CustomerRole restaurantLindaRole;
	public restaurant.restaurantGabe.CustomerRole restaurantGabeRole;

	public restaurant.restaurantParker.CustomerRole restaurantParkerRole;


	public restaurant.restaurantSimon.CustomerRole restaurantSimonRole;
	

	public AStarTraversalPerson aStar;
	Position currentPosition = new Position(2,2);
	Position originalPosition = new Position(2,2);
	public PersonGui gui;
	int scale = 30;

	public int personID;
	public boolean robbedBank = false;

	//List<String> foodNames;
	public Semaphore atDestination = new Semaphore(0,true);
	public int MY_BANK = 0;
	public boolean wantsToRideBus = false;
	public Semaphore waitForBusToArrive = new Semaphore(0,true);
	private boolean onBus = false;
	public enum BusHack {bus1,bus2,NONE};
	public BusHack busHack = BusHack.NONE;

	public Semaphore driveOver = new Semaphore(0,true);
	public List<OnRamp> onRamps = new ArrayList<OnRamp>();
	public enum DriveHack {drive1,drive2,NONE};
	public DriveHack driveHack = DriveHack.NONE;
	
	public int nextMarketToGoTo = 0;
	public int nextRestaurantToGoTo = 0;

	boolean waited = false;


	public int spriteChoice;
	public List<ImageIcon> upSprites = new ArrayList<ImageIcon>();
	public List<ImageIcon> downSprites = new ArrayList<ImageIcon>();
	public List<ImageIcon> leftSprites = new ArrayList<ImageIcon>();
	public List<ImageIcon> rightSprites = new ArrayList<ImageIcon>();

	public enum Personality
	{Normal, Wealthy, Deadbeat, Crook};
	private Personality personality;

	public void setBank(int num){
		MY_BANK = num;
	}

	public void setHouse(House h){
		this.belongings.myHouse = h;
		Random random = new Random();

		if (random.nextInt(3) == 0) {
			
			this.belongings.myHouse.room.inventory.get(0).quantity = 1;
		}
		else {
		
			this.belongings.myHouse.room.inventory.get(0).quantity = 0;
		}
		//Do("Having address set to "+this.belongings.myHouse.address.x+", "+this.belongings.myHouse.address.y);
	}

	public void setGui(PersonGui g){
		this.gui = g;
		//this.gui = null;
	}

	//I JUST MOVED THE JOB CLASS TO A PUBLIC UTIL CLASS SO THE CITY CAN ACCESS IT

	public class Belongings {

		public Belongings() {
			myLiving = new Property();
			myEstates = new ArrayList<Property>();
			myCars = new ArrayList<Car>();
			myFoods = new ArrayList<Food>();
			myAccounts = new ArrayList<BankAccount>();
			myHouse = null;//new House(new Loc(5,5));
			//System.out.println("\tAdress being overwritten");
		}

		public Property myLiving;
		public List<Property> myEstates;
		public List<Car> myCars;
		public List<Food> myFoods;
		public List<BankAccount> myAccounts;
		public House myHouse;
		public boolean bike = false;
	}

	public class BankAccount {

		public BankAccount(int accNumber,int amt,String name,String pw) {
			accountNumber = accNumber;
			amount = amt;
			custName = name;
			password = pw;
			myLoans = new ArrayList<Loan>();

		}

		public int amount;
		public int accountNumber;
		public String custName;
		public String password;
		public List<Loan> myLoans;

	}

	public class Loan {
		public int loanNumber;
		public int total;
		public int amountLeft;

		public void loan(int totalAmount) {
			total = totalAmount;
			amountLeft = total;
		}

		public void payoff(int payment) {
			amountLeft -= payment;
		}
	}

	public class Purse {
		public Map<String, Integer> bag = new HashMap<String,Integer>();
		public int wallet;
	}

	public class Property {
		public int address;
		public Person tenant;
		public int maintenanceLevel;

	}

	public class Car {
		public int licensePlateNumber;
	}

	public CarAgent myCar = new CarAgent();

	
	//Open-checking methods
	
	public boolean someRestaurantOpen(){
		if(city.map.get("Restaurant").isEmpty()){
			return false;
		}

		boolean someOpen = false;
		List<Restaurant> opens = new ArrayList<Restaurant>();

		for(Place p:city.map.get("Restaurant")){
			if(((RestaurantMapLoc)p).restaurant.isOpen()){
				opens.add(((RestaurantMapLoc)p).restaurant);
				someOpen = true;
			}
		}
		
		if(!someOpen){
			return false;
		}
		
		Random random = new Random();
		int chosenRestaurant = random.nextInt(opens.size());
		
		//nextMarketToGoTo = 0;
		for(Place p:city.map.get("Restaurant")){
			if(((RestaurantMapLoc)p).restaurant.equals(opens.get(chosenRestaurant))){
				nextRestaurantToGoTo = city.map.get("Restaurant").indexOf(p);
			}
		}	
		
		return true;

	}

	
	public boolean someMarketOpen(){
		boolean someOpen = false;
		List<Market> opens = new ArrayList<Market>();
		
		for(Place p:city.map.get("Market")){
			if(((MarketMapLoc)p).market.isOpen()){
				opens.add(((MarketMapLoc)p).market);
				someOpen = true;
			}
		}
		
		if(!someOpen){
			return false;
		}
		
		Random random = new Random();
		int chosenMarket = random.nextInt(opens.size());
		
		//nextMarketToGoTo = 0;
		for(Place p:city.map.get("Market")){
			if(((MarketMapLoc)p).market.equals(opens.get(chosenMarket))){
				nextMarketToGoTo = city.map.get("Market").indexOf(p);
			}
		}	
		
		return true;
		
		
	}
	
	public boolean myBankOpen(){
		if (city.map.get("Bank").isEmpty()){
			return false;
		}
		return ((BankMapLoc) city.map.get("Bank").get(this.MY_BANK)).bank.isOpen();
	}

	//msg
	

	public void msgYouWantToRideBus(boolean want){
		this.wantsToRideBus = want;
	}

	public void msgAtDestination(){
		atDestination.release();
	}

	public void msgCarOnRoad(){
		gui.waitingForCarToGetOnRoad = false;
		myCar.gui.moving = false;
	}

	public void msgCarArrivedAtRamp(OnRamp destination){
		//blah
		//stateChanged();
		Do("Getting out of car");
		driveOver.release();
		gui.setLoc(destination.sidewalkLoc);
		gui.xDestination = gui.rectangle.x;
		gui.yDestination = gui.rectangle.y;
		Do("My Loc: "+destination.sidewalkLoc.x+", "+destination.sidewalkLoc.y);
		stateChanged();
	}


	public void msgBusAtStop(BusAgent b,BusStop stop){
		//blah
		//stateChanged();
		//Do("IN THIS MESSAGE "+gui.x+" "+gui.y+" "+stop.sidewalkLoc.x+" "+stop.sidewalkLoc.y);
		if(stop.sidewalkLoc.x == gui.xDestination && stop.sidewalkLoc.y==gui.yDestination){


			gui.onBus();
			onBus = true;
			Do("Getting on bus");

			//b.getOnBus(this);
		}
		else{
			Do("Getting OFF bus");
			onBus = false;
			waitForBusToArrive.release();
			wantsToRideBus = false;


			//Do("SHIT I JUST WOKE UP");
			gui.setLoc(stop.sidewalkLoc);
			gui.offBus();
			//waitForBusToArrive.release();
			//b.getOffBuss(this);
		}

	}

	public void msgBusAtStop(Bus b,BusStop stop){
		//blah
		//stateChanged();
		//Do("IN THIS MESSAGE "+gui.x+" "+gui.y+" "+stop.sidewalkLoc.x+" "+stop.sidewalkLoc.y);
		if(stop.sidewalkLoc.x == gui.xDestination && stop.sidewalkLoc.y==gui.yDestination){


			gui.onBus();
			onBus = true;
			Do("Getting on bus");

			//b.getOnBus(this);
		}
		else{
			Do("Getting OFF bus");
			onBus = false;
			waitForBusToArrive.release();
			wantsToRideBus = false;


			//Do("SHIT I JUST WOKE UP");
			gui.setLoc(stop.sidewalkLoc);
			gui.offBus();
			//waitForBusToArrive.release();
			//b.getOffBuss(this);
		}

	}


	public void msgDoneEating(){
		hungerLevel=0;
		stateChanged();
	}
	public void msgDoneSleeping(){
		//AlertLog.getInstance().logInfo(AlertTag.PERSON, getName(), "I AM DONE SLEEPINGGGGG",null);
		tiredLevel=0;
		stateChanged();
	}
	//Scheduler
	public boolean pickAndExecuteAnAction() {


		//		//DON'T DELETE THIS. IF YOU'RE TRYING TO FIX A MERGE CONFLICT,
		//		//JUST COMMENT THIS OUT
		//		if(onRamps.size()>=2){
		//			
		//				
		//			doDrive(onRamps.get(0),onRamps.get(1));
		//			return true;
		//		}

		if(driveHack == DriveHack.drive1){
			doDrive(onRamps.get(0),onRamps.get(1));
			driveHack = DriveHack.NONE;
			return true;
		}
		if(driveHack == DriveHack.drive2){
			doDrive(onRamps.get(2),onRamps.get(3));
			driveHack = DriveHack.NONE;
			return true;
		}
		if(busHack == BusHack.bus1){
			doRideBus(true);
			busHack = BusHack.NONE;
			return true;
		}
		if(busHack == BusHack.bus2){
			doRideBus(false);
			busHack = BusHack.NONE;
			return true;
		}

		//Do("Deciding what to do - "+ time);
		//Do("Role: "+activeRole);

		/*if(name.equals("p1")){
			if(myJob.placeOfWork==null && !city.map.get("Bank").isEmpty()){
				setJob(((BankMapLoc) city.map.get("Bank").get(0)).bank,JobType.BankTeller,0,100);
			}
		}*/

		//		if(name.equals("p1")&&time==6){
		//			try {
		//				Thread.sleep(300);
		//			} catch (InterruptedException e) {
		//				
		//				e.printStackTrace();
		//			}
		//		}
		//		if(name.equals("p3")&&time<6){
		//			return false;
		//		}
		//Do("Deciding what to do ");
		if(onBus){
			return false;
		}

		if (activeRole != null) {
			//Do("will do role stuff");
			activeRoleCalls++;

			//This takes care of getting off work
			if(activeRole == myJob.jobRole && !timeInJobShift()){
				if(myJob.jobRole instanceof BankTellerRole){
					if(((BankTellerRole) myJob.jobRole).canLeave()){
						Do("It's bank quitting time.");
						//activeRole.p = null;
						//((BankTellerRole) myJob.jobRole).msgLeaveBank();
						activeRole = null;
						return true;
					}
				}
				if(((Occupation) myJob.jobRole).canLeave()){

					Do("It's quitting time.");

					activeRole = null;
					return true;
				}
			}
			try{
				return activeRole.pickAndExecuteAnAction();
			}
			catch(NullPointerException e){
				return true;
			}
		}
		if(time == myJob.shiftStart-1){
			return false;
		}

		if(name.equals("BankRobber") && !robbedBank){
			goToBank();
		}


		//Do("Deciding what to do");
		//TODO FIX THIS MAXTIME ISSUE
		if(myJob.placeOfWork!=null && timeInJobShift() && timeInJobShift((time+1)%50) && timeInJobShift((time+2)%50)
				&&timeInJobShift((time+3)%50)&&timeInJobShift((time+4)%50)&&timeInJobShift((time+5)%50)
				){
			goToWork();
			return true;
		}

		if(wantsToRideBus){
			Do("will ride the bus");
			doRideBus(true);
		}

		//FOR NOW - TODO - GET THIS TO WORK
		/*
		if (purse.wallet > 500 && wantsToBuyCar) {
			buyCar();
		}*/
		if (hungerLevel > 6) {
			getFood();
			return true;
		}
		if(!robbedBank && myBankOpen()){
			if(!city.map.get("Bank").isEmpty() && belongings.myAccounts.size()==0){
				goToBank();
				return true;
			}

			if(!city.map.get("Bank").isEmpty() && ((purse.wallet <= 10 || purse.wallet >= 1000) && !wantsToBuyCar)) {
				goToBank();
				return true;
			}
		}


		if (!city.map.get("Market").isEmpty() && foodsLow() && someMarketOpen()) {
			goToMarket();
			return true;
		}

		if (tiredLevel > 14 && belongings.myHouse!=null) {
			getSleep();
			return true;
		}

		if (!belongings.myEstates.isEmpty()) {
			for (Property property: belongings.myEstates) {
				if (property.maintenanceLevel > 168) {
					goDoMaintenance(property);
					return true;
				}
			}
			return true;
		}

		if (belongings.myLiving.maintenanceLevel > 168) {
			goDoMaintenance(belongings.myLiving);
			return true;
		}

		//FOR NOW - TODO GET THIS TO WORK
		/*
		if (getNetWorth() > 500) {
			buyCar();
			return true;
		}*/

		return false;
	}

	//Actions
	private void goToWork() {
		//parker market testing changes
		//		try {
		//			Thread.sleep((int)(1000+ 1000*Math.random()));
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		Do("I am going to work as a "+myJob.jobType + " role: " + myJob.jobRole+" shift: "+myJob.shiftStart+" "+myJob.shiftEnd);
		//AlertLog.getInstance().logError(AlertTag.PERSON, this.name, "I am going to work as a "+myJob.jobType + " role: " + myJob.jobRole+" shift: "+myJob.shiftStart+" "+myJob.shiftEnd);
		//HACK

		if(myJob.placeOfWork==null){
			myJob.shiftStart+=1;
			myJob.shiftEnd+=1;
			return;
		}


		doGoToWork();
		Do("Got to work");
		Role tempJobRole = myJob.placeOfWork.canIStartWorking(this, myJob.jobType, myJob.jobRole);
		//System.out.println(myJob.jobType+" "+myJob.jobRole+" "+tempJobRole);
		//THIS IS JUST A TEMPORARY FIX, IF SOMEONE DOESN'T GET TO WORK,
		//WE JUST MOVE THEIR SHIFT BACK BY ONE TIME STEP
		if(tempJobRole==null){
			myJob.shiftStart+=1;
			myJob.shiftEnd+=1;
			Do("Didn't get to start working");
			return;
		}

		Do("Starting to work");
		myJob.jobRole = tempJobRole;
		activeRole = tempJobRole;
		//Do(""+ activeRole);
		//System.out.flush();
	}

	private void goToBank() {

		if(city.map.get("Bank").isEmpty()){
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		Bank b = ((BankMapLoc) city.map.get("Bank").get(MY_BANK)).bank;
		Loc loc = city.map.get("Bank").get(MY_BANK).loc;


		activeRole = bankRole;

		if(name.equals("BankRobber") && !robbedBank){
			Do("Going to go rob the bank");
			bankRole.Tasks.add(new rob(1000));
			tempDoGoToCityLoc(loc);

			bankRole.msgYouAreAtBank(b);
			activeRole = bankRole;
			//robbedBank = true;
			return;

		}

		//open account
		if(belongings.myAccounts.isEmpty()){
			Do("Going to bank to open new account");
			bankRole.Tasks.add(new openAccount((int) Math.floor(purse.wallet*.5),name));
			//Do("Before");

			tempDoGoToCityLoc(loc);
			//doGoToBuilding(loc);


			//Do("After");
			bankRole.msgYouAreAtBank(b);
			activeRole = bankRole;
			return;
		}

		//deposit
		if (purse.wallet >= 100) {
			Do("I am going to the bank to deposit $" + (purse.wallet-50));
			bankRole.Tasks.add(new deposit((purse.wallet-50),belongings.myAccounts.get(0).accountNumber,belongings.myAccounts.get(0).password));
		}

		//withdrawal
		if (purse.wallet <= 10 && getMoneyInBank() >= 50)  {
			Do("I am going to the bank to withdraw $" + (60 - purse.wallet));
			bankRole.Tasks.add(new withdrawal((70 - purse.wallet),belongings.myAccounts.get(0).accountNumber,belongings.myAccounts.get(0).password));
		}

		//loan
		if (purse.wallet <= 10 && getMoneyInBank() < 50) {
			Do("I am going to the bank to withdraw $" + (getMoneyInBank()) + " and to loan $" + (50 - getMoneyInBank()));
			bankRole.Tasks.add(new withdrawal(getMoneyInBank(),belongings.myAccounts.get(0).accountNumber,belongings.myAccounts.get(0).password));
			bankRole.Tasks.add(new takeLoan(50 - getMoneyInBank(),belongings.myAccounts.get(0).accountNumber,belongings.myAccounts.get(0).password));
		}

		tempDoGoToCityLoc(loc);
		bankRole.msgYouAreAtBank(b);
		activeRole = bankRole;
	}

	private void goToMarket() {

		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		AlertLog.getInstance().logInfo(AlertTag.MARKET, this.getName(), "I am going to the market to buy food for home", "hi!");
		Do("I am going to the market to buy food for home");
		//doGoToMarket();
		//MarketCustomerRole marketRole = null;
		int marketChoice = (int) Math.floor(city.map.get("Market").size()*Math.random());
		marketChoice = nextMarketToGoTo;
		Market m = ((MarketMapLoc) city.map.get("Market").get(marketChoice)).market;
		Loc loc = city.map.get("Market").get(marketChoice).loc;


		tempDoGoToCityLoc(loc);


		//TODO UNCOMMENT THIS
		//doGoToBuilding(loc);



		//ShoppingList shoppingList = makeShoppingList();


		for(Food f:this.belongings.myFoods){
			if(f.quantity<=10){
				marketRole.addToShoppingList(f.type, 10);
				//Do("f.type");
			}
		}

		//marketRole.setMarket(m);

		//HACK--------if no host or cashier in restaurant then give yourself some food and leave!
		/*if (!m.host.isPresent() || !m.cashier.isPresent()){
			Do("NO ONE AT THE MARKET");
			for(Food f:this.belongings.myFoods){
					f.quantity+=5;
					return;
			}
		}*/

		marketRole.msgYouAreAtMarket(m);
		m.newCustomer(marketRole);
		activeRole = marketRole;
	}





	private void getFood() {
		if (belongings.myHouse!=null && belongings.myHouse.room.inventory.get(0).quantity>0) {
			Do("I am going to eat at home");
			doGoHome();
			activeRole = inhabitantRole;
			belongings.myHouse.msgImHome(inhabitantRole);
			inhabitantRole.msgGotHungry();
			return;
		}
		else {
			//Do("I am going to eat at a restaurant");
			goToRestaurant();
		}
	}
	private void getSleep() {


		Do("I am going home to sleep ");
		//Do("I am going home to sleep "+ "Dest: "+belongings.myHouse.address.x+belongings.myHouse.address.y);
		//Do(this.gui.rectangle.x + " "+this.gui.rectangle.y + " and "+this.gui.xDestination+ " "+gui.yDestination);
		//Do("Going home to "+this.belongings.myHouse.address.x+", "+this.belongings.myHouse.address.y);
		doGoHome();
		activeRole = inhabitantRole;
		belongings.myHouse.msgImHome(inhabitantRole);
		inhabitantRole.msgTired();

	}

	private void goToRestaurant() {
		if(city.map.get("Restaurant").isEmpty()){
			hungerLevel = 0;
			return;
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
		//hack
		if(!someRestaurantOpen()){
			hungerLevel =0;
		}
		
		Random random = new Random();
		int rand = random.nextInt(city.map.get("Restaurant").size());
		rand = nextRestaurantToGoTo;
		Restaurant b = ((RestaurantMapLoc) city.map.get("Restaurant").get(rand)).restaurant;

		if (!b.isOpen())
			return;

		Loc loc = city.map.get("Restaurant").get(rand).loc;

		tempDoGoToCityLoc(loc);
		hungerLevel =0;

		if (b instanceof restaurant.restaurantGabe.RestaurantGabe){
			b.customerEntering(restaurantGabeRole);
			restaurantGabeRole.msgAtRestaurant(b);
			activeRole = restaurantGabeRole;
			AlertLog.getInstance().logInfo(AlertTag.PERSON, name, "Going to Gabe Restaurant", name);
		}
		else if (b instanceof restaurant.restaurantLinda.RestaurantLinda){
			b.customerEntering(restaurantLindaRole);
			restaurantLindaRole.msgAtRestaurant(b);
			activeRole = restaurantLindaRole;
			AlertLog.getInstance().logInfo(AlertTag.PERSON, name, "Going to Linda Restaurant, name", name);
		}
		else if (b instanceof restaurant.restaurantYocca.RestaurantYocca){
			b.customerEntering(restaurantYoccaRole);
			restaurantYoccaRole.msgAtRestaurant(b);
			activeRole = restaurantYoccaRole;
			AlertLog.getInstance().logInfo(AlertTag.PERSON, name, "Going to Yocca Restaurant", name);
		}

		else if (b instanceof restaurant.restaurantParker.RestaurantParker){
			b.customerEntering(restaurantParkerRole);
			//restaurantParkerRole.msgAtRestaurant(b);
			activeRole = restaurantParkerRole;
			AlertLog.getInstance().logInfo(AlertTag.PERSON, name, "Going to Parker Restaurant", name);
		}

		else if (b instanceof restaurant.restaurantSimon.RestaurantSimon){
			b.customerEntering(restaurantSimonRole);
			restaurantSimonRole.msgAtRestaurant(b);
			activeRole = restaurantSimonRole;
			AlertLog.getInstance().logInfo(AlertTag.PERSON, name, "Going to Simon Restaurant", name);

		}
		else{
			AlertLog.getInstance().logError(AlertTag.PERSON, name, "Could not find appropriate customer role", name);
		}
	}

	private void buyCar() {
		//MarketCustomerRole marketRole = null;
		if (purse.wallet < 500) {
			Do("I am going to get money from the bank and then I'm going to buy a car");

			wantsToBuyCar = true;

			doGoToBuilding(city.map.get("Bank").get(MY_BANK).loc);
			bankRole.Tasks.add(new withdrawal(500, belongings.myAccounts.get(0).accountNumber, belongings.myAccounts.get(0).password));
			bankRole.msgYouAreAtBank(((BankMapLoc) city.map.get("Bank").get(MY_BANK)).bank);
			activeRole = bankRole;
		}
		else {
			Market m = ((MarketMapLoc) city.map.get("Market").get(0)).market;
			Do("I am going to buy a car from the market");


			marketRole.msgYouAreAtMarket(m);
			activeRole = marketRole;
		}
	}

	private void goDoMaintenance(Property p) {
		doGoHome();
		activeRole = inhabitantRole;
		p.maintenanceLevel = 0;
	}

	private void rideBus(){
		doRideBus();
		//this.wantsToRideBus = false;
	}

	//ANIMATION

	private void tempDoGoToCityLoc(Loc loc) {
		// TODO Auto-generated method stub
		//System.out.println("CALLING THE TEMP MARKET MOVE");

		//System.out.println("\t" + this.personID + "astaasdfasdfasdfasgjkgdfhoagahghiogar");

		Loc gridLoc = CityComponent.findNearestGridLoc(new Point(loc.x,loc.y));
		//Do("Nearest gri loc to "+loc.x+", "+loc.y+" is "+gridLoc.x+", "+gridLoc.y );


		if(gui!=null){
			guiMoveFromCurrentPostionTo(new Position(gridLoc.x,gridLoc.y));

			this.gui.doGoToBuilding(loc);
		}


	}

	public void doGoHome(){
		//Do("My address: "+belongings.myHouse.address.x+" "+belongings.myHouse.address.y);
		if(belongings.myHouse==null){
			tempDoGoToCityLoc(new Loc(5,5));
		}
		else{
			tempDoGoToCityLoc(belongings.myHouse.address);
		}
	}


	private void doGoToBuilding(Loc loc){


		if(this.gui!=null){
			gui.doGoToBuilding(loc);
		}
		else{
			atDestination.release();
		}

		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void doRideBus(boolean fStop){
		Do("\tGoing to bus Stop");
		//		gui.doGoToBus(city.fStops.get(0).sidewalkLoc);
		//		try {
		//			atDestination.acquire();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		city.fStops.get(0).waitForBus(this);
		//		try {
		//			waitForBusToArrive.acquire();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		Do("\t\t\t\tNow");
		//		//onBus = true;
		if(fStop){
			gui.onTheMove = true;
			gui.waitingForBus = true;
			tempDoGoToCityLoc(city.fStops.get(0).sidewalkLoc);
			city.fStops.get(0).waitForBus(this);
			try {
				waitForBusToArrive.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			gui.onTheMove = true;
			gui.waitingForBus = true;
			tempDoGoToCityLoc(city.bStops.get(0).sidewalkLoc);
			city.bStops.get(0).waitForBus(this);
			try {
				waitForBusToArrive.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void doRideBus(){
		Do("\tGoing to bus Stop");
		//		gui.doGoToBus(city.fStops.get(0).sidewalkLoc);
		//		try {
		//			atDestination.acquire();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		city.fStops.get(0).waitForBus(this);
		//		try {
		//			waitForBusToArrive.acquire();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		Do("\t\t\t\tNow");
		//		//onBus = true;

		gui.onTheMove = true;
		gui.waitingForBus = true;
		tempDoGoToCityLoc(city.fStops.get(0).sidewalkLoc);
		city.fStops.get(0).waitForBus(this);
		try {
			waitForBusToArrive.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doDrive(OnRamp from,OnRamp to){

		Do("Taking a drive");
		//		if(!waited){
		//			try {
		//				Thread.sleep(9000);
		//			} catch (InterruptedException e1) {
		//				// TODO Auto-generated catch block
		//				e1.printStackTrace();
		//			}
		//			waited = true;
		//		}
		//stopThread();

		gui.waitingForCarToGetOnRoad = true;

		tempDoGoToCityLoc(from.sidewalkLoc);

		myCar.msgTakeMeTo(from, to);

		try {
			driveOver.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do("I HAVE AWOKEN");



	}

	private void doGoToWork(){
		Loc location = findPlaceOfWork(myJob.placeOfWork);
		tempDoGoToCityLoc(location);
	}

	public Loc findPlaceOfWork(PlaceOfWork workPlace){

		if(workPlace == null){
			//System.err.println("Someone's trying to go to a null work location");
			return null;
		}

		if(workPlace instanceof Bank){
			for(Place bMap:city.map.get("Bank")){
				if(((BankMapLoc) bMap).bank==workPlace){
					return bMap.loc;
				}
			}
		}
		if(workPlace instanceof Market){
			for(Place mMap:city.map.get("Market")){
				if(((MarketMapLoc) mMap).market==workPlace){
					return mMap.loc;
				}
			}
		}
		if(workPlace instanceof Restaurant){
			for(Place rMap:city.map.get("Restaurant")){
				if(((RestaurantMapLoc) rMap).restaurant==workPlace){
					return rMap.loc;
				}
			}
		}
		return null;

	}


	//Utilities

	//I'm thinking this should include the actual Role rather than having the person make it....
	public void setJob(PlaceOfWork placeOfWork,JobType jobType,int start,int end){
		//Do("\t\t\t IN SET JOB "+jobType);
		Role jobRole = null;
		if(jobType.equals(JobType.MarketHost) || jobType.equals(JobType.MarketCashier)
				|| jobType.equals(JobType.RestaurantHost) || jobType.equals(JobType.RestaurantCashier) || jobType.equals(JobType.RestaurantCook)){
			jobRole = null;
			//myJob = new Job(null,start,end,placeOfWork,this,jobType);
			//return;
		}
		else if(jobType==JobType.BankTeller){
			jobRole = new BankTellerRole(name+"Teller",this);
			//myJob = new Job(jobRole,start,end,placeOfWork,this,jobType);
			//return;
		}
		else if(jobType==JobType.MarketEmployee){
			jobRole = new MarketEmployeeRole(name+"MarketEmployee",this);
			//myJob = new Job(jobRole,start,end,placeOfWork,this,jobType);
		}
		else if (jobType==JobType.MarketDeliveryMan){
			jobRole = new MarketDeliveryManRole(name+"MarketDeliveryMan", this, (Market)placeOfWork);
		}
		else if (jobType==JobType.RestaurantYoccaWaiter1){
			//jobRole = new OriginalWaiterRole(name+"normalWaiter",this);
			jobRole = new restaurant.restaurantYocca.ProducerConsumerWaiterRole(name+"pcWaiter", this);
		}
		else if (jobType==JobType.RestaurantYoccaWaiter2){
			jobRole = new restaurant.restaurantYocca.OriginalWaiterRole(name+"RestaurantWaiter",this);
		}
		else if (jobType==JobType.RestaurantGabeWaiter1){
			//Do("\t\t\tIN HERE");
			//jobRole = new OriginalWaiterRole(name+"normalWaiter",this);
			jobRole = new restaurant.restaurantGabe.StandWaiterRole(name+"pcWaiter", this);
		}
		else if (jobType==JobType.RestaurantGabeWaiter2){
			jobRole = new restaurant.restaurantGabe.TalkingWaiterRole(name+"RestaurantWaiter",this);
		}
		else if (jobType==JobType.RestaurantLindaWaiter1){
			//jobRole = new OriginalWaiterRole(name+"normalWaiter",this);
			jobRole = new restaurant.restaurantLinda.ProducerConsumerWaiterRole(name+"pcWaiter", this);
		}
		else if (jobType==JobType.RestaurantLindaWaiter2){
			jobRole = new restaurant.restaurantLinda.OriginalWaiterRole(name+"RestaurantWaiter",this);
		}
		else if (jobType==JobType.RestaurantParkerWaiter1){
			//jobRole = new OriginalWaiterRole(name+"normalWaiter",this);
			jobRole = new restaurant.restaurantParker.WaiterPCRole(name+"pcWaiter", this);
		}
		else if (jobType==JobType.RestaurantParkerWaiter2){
			jobRole = new restaurant.restaurantParker.WaiterOriginalRole(name+"RestaurantWaiter",this);
		}
		myJob = new Job(jobRole,start,end,placeOfWork,this,jobType);

	}

	public void msgStateChanged() {
		//this.pickAndExecuteAnAction();
		//Do("Got the stateChanged message");
		this.stateChanged();
	}

	public void putInBag(String item,int amount){
		this.purse.bag.put(item,amount);
		addFoodToInventory(item,amount);
	}

	public void addToWallet(int amount) {
		this.purse.wallet += amount;
	}

	public void takeFromWallet(int amount) {
		this.purse.wallet -= amount;
	}

	public int getWalletAmount(){
		return purse.wallet;
	}

	public void addFoodToBag(String type, int quantity){
		if (purse.bag.containsKey(type))
			purse.bag.put(type, purse.bag.get(type)+quantity);
		else
			purse.bag.put(type, purse.bag.get(type));

		//temporarily make it so that bagged food goes strait into inventory
		addFoodToInventory(type,quantity);

	}

	public void addFoodToInventory(String type, int quantity){
		Do("Adding "+quantity+" "+type+"s to inventory.");
		for(Food f:this.belongings.myFoods){
			if(f.type.equals(type)){
				f.quantity+=quantity;
			}
		}
	}

	public int getMoneyInBank() {
		int totalMoney = 0;
		for (BankAccount account: belongings.myAccounts) {
			totalMoney += account.amount;
		}
		return totalMoney;
	}

	public int getNetWorth() {
		int totalMoney = 0;
		for (BankAccount account: belongings.myAccounts) {
			totalMoney += account.amount;
			for (Loan loan: account.myLoans) {
				totalMoney -= loan.amountLeft;
			}
		}
		totalMoney += purse.wallet;
		return totalMoney;
	}

	public void msgThisRoleDone() {
		activeRole = null;
	}



	//Bank Utilities

	public void addToAccount(int accNum,int amount){
		for (BankAccount account: belongings.myAccounts) {
			if (account.accountNumber == accNum) {
				account.amount += amount;
			}
		}
	}

	public void takeFromAccount(int accNum,int amount){
		for (BankAccount account: belongings.myAccounts) {
			if (account.accountNumber == accNum) {
				account.amount -= amount;
			}
		}
	}

	public void createAccount(int accountNumber,int amount,String name,String passWord){
		BankAccount account = new BankAccount(accountNumber,amount,name,passWord);
		belongings.myAccounts.add(account);
	}

	public void addLoan(int accountNumber,int cash, int loanNumber){
		Loan loan = new Loan();
		loan.amountLeft = cash;
		loan.total = cash;
		loan.loanNumber = loanNumber;
		for (BankAccount account: belongings.myAccounts) {
			if (account.accountNumber == accountNumber) {
				account.myLoans.add(loan);
			}
		}
	}

	@Override
	public void msgThisRoleDone(Role role) {
		// TODO Auto-generated method stub
		this.activeRole = null;
	}

	public boolean foodsLow(){

		for(Food f:this.belongings.myFoods){
			if(f.quantity<10){
				return true;
			}
		}
		return false;

	}

	public boolean timeInJobShift(){

		return timeInJobShift(time);

		/*if(myJob.shiftEnd>=myJob.shiftStart){
			if(time>=myJob.shiftStart && time<=myJob.shiftEnd){
				return true;
			}
			return false;
		}
		if(myJob.shiftEnd<myJob.shiftStart){
			if(time<myJob.shiftStart && time>myJob.shiftEnd){
				return false;
			}
			return true;
		}
		return false;*/
	}

	public boolean timeInJobShift(int time){
		if(myJob.shiftEnd>=myJob.shiftStart){
			if(time>=myJob.shiftStart && time<=myJob.shiftEnd){
				return true;
			}
			return false;
		}
		if(myJob.shiftEnd<myJob.shiftStart){
			if(time<myJob.shiftStart && time>myJob.shiftEnd){
				return false;
			}
			return true;
		}
		return false;
	}


	//hack for restaurant stuff?
	public void setActiveRole(String role){
		/*if (role.equals("RestaurantCustomer"))
			activeRole = restaurantRole;*/
	}



	//this is just a subroutine for waiter moves. It's not an "Action"
	//itself, it is called by Actions.
	void guiMoveFromCurrentPostionTo(Position to){
		//System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());

		//to = new Position(2,5);
		Loc l = CityComponent.findNearestGridLoc(new Point(gui.rectangle.x,gui.rectangle.y));
		currentPosition = new Position(l.x,l.y);
		//System.out.println("("+currentPosition.getX()+","+currentPosition.getY()+")");
		//System.out.println("("+to.getX()+","+to.getY()+")");



		//Do("CALLING PERSON MOVE");

		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
		List<Position> path = aStarNode.getPath();
		// Do("Got here, path calculated");
		Boolean firstStep   = true;
		Boolean gotPermit   = true;

		for (Position tmpPath: path) {
			//The first node in the path is the current node. So skip it.
			if (firstStep) {
				firstStep   = false;
				continue;
			}

			//Try and get lock for the next step.
			int attempts    = 1;
			gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());

			//Did not get lock. Lets make n attempts.
			while (!gotPermit && attempts < 3) {
				//System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

				//Wait for 1sec and try again to get lock.
				try { Thread.sleep(1000); }
				catch (Exception e){}

				gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
				attempts ++;
			}

			//Did not get lock after trying n attempts. So recalculating path.
			if (!gotPermit) {
				//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
				guiMoveFromCurrentPostionTo(to);
				break;
			}

			//Got the required lock. Lets move.
			//System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
			currentPosition.release(aStar.getGrid());
			currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
			//System.out.println(gui==null);
			gui.move(currentPosition.getX(), currentPosition.getY());
		}
		/*
        boolean pathTaken = false;
        while (!pathTaken) {
            pathTaken = true;
            //print("A* search from " + currentPosition + "to "+to);
            AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
            if (a == null) {//generally won't happen. A* will run out of space first.
                System.out.println("no path found. What should we do?");
                break; //dw for now
            }
            //dw coming. Get the table position for table 4 from the gui
            //now we have a path. We should try to move there
            List<Position> ps = a.getPath();
            Do("Moving to position " + to + " via " + ps);
            for (int i=1; i<ps.size();i++){//i=0 is where we are
                //we will try to move to each position from where we are.
                //this should work unless someone has moved into our way
                //during our calculation. This could easily happen. If it
                //does we need to recompute another A* on the fly.
                Position next = ps.get(i);
                if (next.moveInto(aStar.getGrid())){
                    //tell the layout gui
                    guiWaiter.move(next.getX(),next.getY());
                    currentPosition.release(aStar.getGrid());
                    currentPosition = next;
                }
                else {
                    System.out.println("going to break out path-moving");
                    pathTaken = false;
                    break;
                }
            }
        }
		 */
	}



	@Override
	public void setTiredLevel(int i) {
		this.tiredLevel = i;
	}



	@Override
	public House getHouse() {
		return belongings.myHouse;
	}
















}
