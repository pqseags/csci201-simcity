package UnitTests.mock;

import role.Role;
import util.Bus;
import util.BusAgent;
import util.BusStop;
import util.OnRamp;
import house.House;
import interfaces.Person;

public class MockPerson extends Mock implements Person {

	public MockPerson(){
		super();
	}
	
	public MockPerson(String name){
		super(name);
	}
	
	@Override
	public void msgStateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBusAtStop(Bus b, BusStop stop) {
		// TODO Auto-generated method stub
		
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
	public void addToWallet(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeFromWallet(int amount) {
		// TODO Auto-generated method stub
		
	}

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
	public void addToAccount(int accNumber, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeFromAccount(int accNumber, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createAccount(int accountNumber, int amount, String name,
			String passWord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLoan(int accountNumber, int cash, int loanNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgThisRoleDone(Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneSleeping() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putInBag(String item, int amount) {
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
