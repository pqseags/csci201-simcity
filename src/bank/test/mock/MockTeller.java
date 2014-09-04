package bank.test.mock;

import interfaces.BankTeller;
import util.Task;
import UnitTests.mock.EventLog;
import UnitTests.mock.LoggedEvent;

public class MockTeller implements BankTeller {

	public EventLog log = new EventLog();
	
	
	String name;
	public MockTeller(String name){
		this.name = name;
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
