package UnitTests.mock.bankMock;

import interfaces.BankTeller;
import util.Task;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

public class MockBankTeller extends Mock implements BankTeller{

	
	
	String name;
	public MockBankTeller(String name){
		this.name = name;
	}
	
	public MockBankTeller(){
		
	}
	
	@Override
	public void msgIWantTo(Task t) {
		log.add(new LoggedEvent("Just got a new task: "+ t));
		
	}

	@Override
	public void msgDoneAndLeaving() {
		log.add(new LoggedEvent("My customer just left"));
		
	}

	@Override
	public void msgStateChanged() {
		// TODO Auto-generated method stub
		
	}

}
