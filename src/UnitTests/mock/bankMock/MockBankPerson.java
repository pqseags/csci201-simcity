package UnitTests.mock.bankMock;

import house.House;
import interfaces.Person;
import role.Role;
import util.Bus;
import util.BusAgent;
import util.BusStop;
import util.Loc;
import util.OnRamp;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

public class MockBankPerson extends Mock implements Person {	
	
	public MockBankPerson(String name) {
		super(name);

	}

	public void msgStateChanged() {
		log.add(new LoggedEvent("Just got a new permit"));
		//System.out.println(log.getLastLoggedEvent());
	}

	public void addToWallet(int amount) {
		log.add(new LoggedEvent("Added to wallet"));
	}

	public void takeFromWallet(int amount) {
		log.add(new LoggedEvent("Took from wallet"));
	}

	public void addToAccount(int accNumber, int amount) {
		log.add(new LoggedEvent("Added to account"));
	}

	public void takeFromAccount(int accNumber, int amount) {
		log.add(new LoggedEvent("Took from account"));
	}

	public void createAccount(int accountNumber, int amount, String name,
			String passWord) {
		
		log.add(new LoggedEvent("Created a new account"));
		
	}

	public void addLoan(int accountNumber, int cash, int loanNumber) {
		
		log.add(new LoggedEvent("I have a new loan for $" + cash));
		
	}

	public void msgThisRoleDone(Role role) {
		log.add(new LoggedEvent("My BankCustomerRole just finished"));
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		
	}

	public void msgBusAtStop(BusStop stop) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void msgCarArrivedAtLoc(Loc destination) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public int getWalletAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addFoodToBag(String type, int quantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putInBag(String item, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBusAtStop(Bus b, BusStop stop) {
		// TODO Auto-generated method stub
		
	}

	public void msgSetTiredLevel(int level) {
		// TODO Auto-generated method stub
		
	}
	
	public void msgDoneSleeping() {
		
	}

	@Override
	public void msgBusAtStop(BusAgent b, BusStop stop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCarArrivedAtRamp(OnRamp destination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTiredLevel(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public House getHouse() {
		// TODO Auto-generated method stub
		return null;
	}

}
