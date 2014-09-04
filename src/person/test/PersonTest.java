package person.test;

import house.InhabitantRole;

import java.util.ArrayList;
import java.util.List;

import market.Market;
import market.MarketCustomerRole;
import person.PersonAgent;
import person.PersonAgent.BankAccount;
import person.PersonAgent.Belongings;
import person.PersonAgent.Property;
import public_Object.Food;
import role.Role;
import util.Bank;
import util.BankMapLoc;
import util.CityMap;
import util.MarketMapLoc;
import util.Place;
import bank.BankCustomerRole;
import bank.BankTellerRole;
import bank.test.mock.MockTeller;
import junit.framework.TestCase;

public class PersonTest extends TestCase {

	CityMap city = new CityMap();
	String name = "Andrew";

	PersonAgent andrew = new PersonAgent(name,city);
	PersonAgent dude = new PersonAgent("Dude",city);
	
	public void setUp() throws Exception{
		super.setUp();	
		andrew = new PersonAgent(name,city);		
	}	

	public void testPersonGoToJob() {
		Role job = new BankTellerRole(andrew.name,andrew);
		andrew.myJob.jobRole = job;
		andrew.roles.add(job);
		andrew.myJob.shiftStart = 8;
		andrew.myJob.shiftEnd = 16;
		andrew.time = 12;
		andrew.purse.wallet = 50;
		andrew.hungerLevel = 0;
			
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertEquals("Roles doesn't contain 1 thing in it", andrew.roles.size(),1);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain BankTellerRole", andrew.roles.get(0) instanceof BankTellerRole);	
		assertTrue("myJobs doesn't contain BankTellerRole", andrew.myJob.jobRole instanceof BankTellerRole);	
		assertTrue("ActiveRole doesn't contain BankTellerRole", andrew.activeRole instanceof BankTellerRole);

	}
	
	public void testPersonGoToBank() {
		//Creates bank
		ArrayList<Place> banks = new ArrayList<Place>();
		Bank bank = new Bank();
		BankMapLoc bankMapLoc = new BankMapLoc(bank);
		banks.add(bankMapLoc);
		andrew.city.map.put("Bank", banks);
		
		//Sets preconditions
		BankAccount account = andrew.new BankAccount(0,0,andrew.name,"Password");
		andrew.belongings.myAccounts.add(account);
		andrew.purse.wallet = 300;
		andrew.activeRole = null;
		andrew.nextRole = null;
	
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain BankCustomerRole", andrew.roles.get(0) instanceof BankCustomerRole);
	
		andrew.purse.wallet = 300;
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles has more than 1 thing in it when it should still have 1", andrew.roles.size(), 1);
		assertTrue("Active Role doesn't contain BankCustomerRole", andrew.roles.get(0) instanceof BankCustomerRole);
		
		andrew.pickAndExecuteAnAction();
		
		assertTrue("Active Role isn't called", andrew.activeRoleCalls > 0);
		assertEquals("BankCustomer doesn't have 1 account", andrew.belongings.myAccounts.size(),1);
		//assertTrue("BankCustomer doesn't have money in account after depositing", andrew.belongings.myAccounts.get(0).amount > 0);
	}
	
	public void testPersonGoToMarket() {
		
		//Creates market
		ArrayList<Place> markets = new ArrayList<Place>();
		Market market = new Market();
		MarketMapLoc marketMapLoc = new MarketMapLoc(market);
		markets.add(marketMapLoc);
		andrew.city.map.put("Market", markets);
		
		//Sets preconditions
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.clear();
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain MarketCustomerRole", andrew.roles.get(0) instanceof MarketCustomerRole);
		
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.clear();
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles has more than 1 thing in it when it should still contain 1", andrew.roles.size(),1);
	}
	
	public void testPersonGoGetFoodForHome() {
		
		//Creates market
		ArrayList<Place> markets = new ArrayList<Place>();
		Market market = new Market();
		MarketMapLoc marketMapLoc = new MarketMapLoc(market);
		markets.add(marketMapLoc);
		andrew.city.map.put("Market", markets);
		
		//Sets preconditions
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.clear();
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain MarketCustomerRole", andrew.roles.get(0) instanceof MarketCustomerRole);
	}
	
	public void testPersonGoEatFoodAtHome() {
		
		//Sets preconditions
		andrew.hungerLevel = 7;
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.add(new Food("Pizza",3));
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it, it has " + andrew.roles.size() + " instead", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain InhabitantRole", andrew.roles.get(0) instanceof InhabitantRole);
	}
	
	public void testPersonGoEatFoodAtRestaurant() {

		//Sets preconditions
		andrew.hungerLevel = 7;
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.clear();
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		//assertEquals("Roles doesn't have 1 thing in it, it has " + andrew.roles.size() + " instead", andrew.roles.size(),1);
		//assertTrue("Roles doesn't contain InhabitantRole", andrew.roles.get(0) instanceof InhabitantRole);
	}
	
	public void testPersonGoToSleep() {
		
		//Set preconditions
		andrew.tiredLevel = 15;
		andrew.hungerLevel = 0;
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.add(new Food("Pizza",4));
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it, it has " + andrew.roles.size() + " instead", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain InhabitantRole", andrew.roles.get(0) instanceof InhabitantRole);
	}
	
	public void testPersonBuyCarEnoughMoneyInWallet() {
//		//Set preconditions
//		andrew.tiredLevel = 0;
//		andrew.hungerLevel = 0;
//		andrew.purse.wallet = 1000;
//		andrew.belongings.myFoods.add(new Food("Pizza",4));
//		andrew.activeRole = null;
//		andrew.nextRole = null;
//		
//		assertEquals("Roles isn't empty", andrew.roles.size(),0);
				
	}
	
	public void testPersonGoDoMaintenanceOnHome() {
		//Set preconditions
		andrew.tiredLevel = 0;
		andrew.hungerLevel = 0;
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.add(new Food("Pizza",4));
		andrew.belongings.myLiving = andrew.new Property();
		andrew.belongings.myLiving.maintenanceLevel = 170;
		andrew.activeRole = null;
		andrew.nextRole = null;
		
		assertTrue("Home's maintenance level isn't greater than 168", andrew.belongings.myLiving.maintenanceLevel > 168);
		assertEquals("Roles isn't empty", andrew.roles.size(),0);
		
		andrew.pickAndExecuteAnAction();
		
		assertEquals("Roles doesn't have 1 thing in it, it has " + andrew.roles.size() + " instead", andrew.roles.size(),1);
		assertTrue("Roles doesn't contain InhabitantRole", andrew.roles.get(0) instanceof InhabitantRole);
		assertEquals("Home's maintenance level doesn't equal 0", andrew.belongings.myLiving.maintenanceLevel, 0);
	}
	
	public void testPersonGoDoMaintenanceOnEstates() {
		//Set preconditions
		andrew.tiredLevel = 0;
		andrew.hungerLevel = 0;
		andrew.purse.wallet = 50;
		andrew.belongings.myFoods.add(new Food("Pizza",4));
		andrew.activeRole = null;
		andrew.nextRole = null;
	}
	
	
	
}
