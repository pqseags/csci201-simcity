package UnitTests.BusUnitTests;

import interfaces.Person;

import java.util.ArrayList;
import java.util.List;

import util.Bank;
import util.Bus;
import util.BusStop;
import util.Loc;
import util.deposit;
import util.openAccount;
import util.withdrawal;
import UnitTests.mock.bankMock.MockBankPerson;
import UnitTests.mock.bankMock.MockBankTeller;
import UnitTests.mock.busMock.MockBusPerson;
import bank.BankCustomerRole;
import bank.BankTellerRole;
import junit.framework.*;

public class BusTest extends TestCase{
	
	//Unit test for the BankCustomerRole
	
	
	List<Person> people1 = new ArrayList<Person>();
	List<Person> people2 = new ArrayList<Person>();
	BusStop stop0;
	BusStop stop1;
	BusStop stop2;
	List<BusStop> stops = new ArrayList<BusStop>();
	
	Bus bus;
	
	
	public void setUp() throws Exception {
		super.setUp();
		
		for(int i = 0;i<10;++i){
			people1.add(new MockBusPerson());
			people2.add(new MockBusPerson());
		}
	
		stop0 = new BusStop(new Loc(0,0),new ArrayList<Person>(0));
		stop1  = new BusStop(new Loc(1,0),new ArrayList<Person>(people1));
		stop2 = new BusStop(new Loc(2,0),new ArrayList<Person>(people2));
		stops.add(stop0);
		stops.add(stop1);
		stops.add(stop2);
		
		bus = new Bus();
		bus.setBusStops(stops);
		bus.setCurrentStop(0);
		bus.randomOffset = 0;
		
		
	}
	
	
	/** This test will examine the only normative scenario - a bus goes to the stop,
	 * picks people up, goes to the next stop, and repeats.
	 * 
	 * 5/10 people at stop1 get on. Then 3 of those get off at stop2. Also,
	 * 5/10 people at stop2 get on.
	 */
	public void testBusScenario(){
		
		
		
		//verify the preconditions of the whole scenario
		assertTrue("Bus should be at stop zero",bus.currentStop==0);
		assertTrue("Bus shouldn't have any passengers",bus.passengers.isEmpty());
		assertTrue("Bus should be at stop",bus.atStop);
		
		
		//step 1 - tell the bus it's time to go to the next stop
		bus.updateTime(3);
		
		//check post of 1 and pre of 2
		//assertTrue("Bus should have incremented stop",bus.currentStop == 1);
		assertTrue("Bus should not be at stop any more",!bus.atStop);
		
		
		//step 2 - tell the bus he's arrived
		bus.updateBus();
		
		//check post of 2 and pre of 3
		assertTrue("Bus should have 10 passengers now",bus.passengers.size()==10);
		assertTrue("Bus should have incremented stop",bus.currentStop == 1);
		assertTrue("Bus should be at stop",bus.atStop);
		
		//step 3 - tell the bus it's time to move again
		bus.updateTime(6);
		
		//check post of 3 and pre of 4
		assertTrue("Bus should not be at stop any more",!bus.atStop);
		
		//step 4 - tell the bus he's arrived
		bus.updateBus();
		
		//check post of 2 and pre of 3
		assertTrue("Bus should have 10 passengers now",bus.passengers.size()==10);
		for(int i = 0;i<10;++i){
			assertTrue("Second round of passengers should be in the bus",bus.passengers.contains(people2.get(i)));
			assertTrue("First round of passengers should not be in the bus",!bus.passengers.contains(people1.get(i)));
			
		}
		assertTrue("Bus should have incremented stop",bus.currentStop == 2);
		assertTrue("Bus should be at stop",bus.atStop);
		
		
		//NEW DESIGNS - NEEDED NEW TESTS
		
		/*assertTrue("Bus should have 5 passengers",bus.passengers.size()==5);
		assertTrue("Stop1 should only have 5 people left",stop1.peopleWaiting.size()==5);
		for(int i = 0;i<5;++i){
			assertTrue("First 5 of people1 should be on bus",bus.passengers.contains(people1.get(i)));
			assertTrue("First 5 of people1 should not be at stop1",!stop1.peopleWaiting.contains(people1.get(i)));
			assertTrue("Second 5 people should not be on bus",!bus.passengers.contains(people1.get(5+i)));
			assertTrue("Second 5 of people1 should be at stop1",stop1.peopleWaiting.contains(people1.get(5+i)));

		}
		for(int i = 0;i<10;++i){
			assertTrue("Stop2 people should not be on bus",!bus.passengers.contains(stop2.peopleWaiting.get(i)));
			assertTrue("Stop2 people should be untouched",
					((MockBusPerson) stop2.peopleWaiting.get(i)).log.isEmpty());
		}
		
		//step 3 - tell bus to move again
		bus.actionPerformed(null);
		
		//check post of 3 and pre of 4
		assertTrue("Bus should have incremented stop",bus.currentStop == 2);
		assertTrue("Bus should still have 5 passengers",bus.passengers.size()==5);
		assertTrue("Stop 1 should still have 5 passengers",stop1.peopleWaiting.size()==5);
		assertTrue("Stop 2 should still have 10 passengers",stop2.peopleWaiting.size()==10);
		for(int i = 0;i<5;++i){
			assertTrue("First 5 of people 1 should have been notified",
					((MockBusPerson) bus.passengers.get(i)).lastLog().equals(
							"Told that the bus just arrived at stop with location"
							+ " (2,0)"));
			assertTrue("Second 5 should have the same log as before",
					((MockBusPerson) people1.get(i+5)).lastLog().equals("Told that "
							+ "the bus just arrived at stop with "
							+ "location (1,0)"));
		}
		for(int i = 0;i<10;++i){
			assertTrue("Stop2 people should have gotten the bus's arrival message",
					((MockBusPerson) people2.get(i)).lastLog().equals("Told that "
							+ "the bus just arrived at stop with "
							+ "location (2,0)"));
		}
		
		//step 4 - add/subtract some people2 people to/from the bus
		for(int i = 0;i<5;++i){
			bus.getOnBus(people2.get(i));
		}
		for(int i = 0;i<3;++i){
			bus.getOffBuss(people1.get(i));
		}
		
		//check post of 4 and pre of 5
		assertTrue("Bus should have 7 passengers",bus.passengers.size()==7);
		assertTrue("Stop1 should only have 5 people left",stop1.peopleWaiting.size()==5);
		assertTrue("Stop2 should only have 5 people left",stop2.peopleWaiting.size()==5);
		for(int i = 0;i<5;++i){
			assertTrue("First 5 of people2 should be on bus",bus.passengers.contains(people2.get(i)));
			assertTrue("First 5 of people2 should not be at stop2",!stop2.peopleWaiting.contains(people2.get(i)));
			assertTrue("Second 5 people2 should not be on bus",!bus.passengers.contains(people2.get(5+i)));
			assertTrue("Second 5 of people2 should be at stop2",stop2.peopleWaiting.contains(people2.get(i+5)));

		}
		for(int i = 0;i<5;++i){
			assertTrue("Second five people of people1 should not be on bus",!bus.passengers.contains(people1.get(i+5)));
		}
		for(int i = 0;i<3;++i){
			assertTrue("First3 people of people1 should not be on bus",!bus.passengers.contains(people1.get(i)));
			assertTrue("First3 people of people1 should not be at stop1",!stop1.peopleWaiting.contains(people1.get(i)));
		}
		for(int i = 3;i<5;++i){
			assertTrue("Last 2 of first 5 of people1 should be on bus",bus.passengers.contains(people1.get(i)));
			assertTrue("Last 2 of first 5 of people1 should not be at stop1",!stop1.peopleWaiting.contains(people1.get(i)));
			assertTrue("Last 2 of first 5 of people1 should not be at stop2",!stop2.peopleWaiting.contains(people2.get(i)));

		}*/

		
		/*
		//verify the preconditions of the whole scenario
		assertTrue("Bus should be at stop zero",bus.currentStop==0);
		assertTrue("Bus shouldn't have any passengers",bus.passengers.isEmpty());
		
		
		//step 1 - tell the bus it's time to go to the next stop
		bus.actionPerformed(null);
		
		//check post of 1 and pre of 2
		assertTrue("Bus should have incremented stop",bus.currentStop == 1);
		assertTrue("Bus should still have empty passenger list",bus.passengers.isEmpty());
		assertTrue("Stop 1 should still have 10 passengers",stop1.peopleWaiting.size()==10);
		assertTrue("Stop 2 should still have 10 passengers",stop2.peopleWaiting.size()==10);
		for(int i = 0;i<10;++i){
			assertTrue("Stop1 people should have gotten the bus's arrival message",
					((MockBusPerson) people1.get(i)).lastLog().equals("Told that "
							+ "the bus just arrived at stop with "
							+ "location (1,0)"));
			assertTrue("Stop2 people should be untouched",
					((MockBusPerson) stop2.peopleWaiting.get(i)).log.isEmpty());
		}
		
		
		//step 2 - make some people get on the bus
		for(int i = 0;i<5;++i){
			bus.getOnBus(people1.get(i));
		}
		
		//check post of 2 and pre of 3
		assertTrue("Bus should have 5 passengers",bus.passengers.size()==5);
		assertTrue("Stop1 should only have 5 people left",stop1.peopleWaiting.size()==5);
		for(int i = 0;i<5;++i){
			assertTrue("First 5 of people1 should be on bus",bus.passengers.contains(people1.get(i)));
			assertTrue("First 5 of people1 should not be at stop1",!stop1.peopleWaiting.contains(people1.get(i)));
			assertTrue("Second 5 people should not be on bus",!bus.passengers.contains(people1.get(5+i)));
			assertTrue("Second 5 of people1 should be at stop1",stop1.peopleWaiting.contains(people1.get(5+i)));

		}
		for(int i = 0;i<10;++i){
			assertTrue("Stop2 people should not be on bus",!bus.passengers.contains(stop2.peopleWaiting.get(i)));
			assertTrue("Stop2 people should be untouched",
					((MockBusPerson) stop2.peopleWaiting.get(i)).log.isEmpty());
		}
		
		//step 3 - tell bus to move again
		bus.actionPerformed(null);
		
		//check post of 3 and pre of 4
		assertTrue("Bus should have incremented stop",bus.currentStop == 2);
		assertTrue("Bus should still have 5 passengers",bus.passengers.size()==5);
		assertTrue("Stop 1 should still have 5 passengers",stop1.peopleWaiting.size()==5);
		assertTrue("Stop 2 should still have 10 passengers",stop2.peopleWaiting.size()==10);
		for(int i = 0;i<5;++i){
			assertTrue("First 5 of people 1 should have been notified",
					((MockBusPerson) bus.passengers.get(i)).lastLog().equals(
							"Told that the bus just arrived at stop with location"
							+ " (2,0)"));
			assertTrue("Second 5 should have the same log as before",
					((MockBusPerson) people1.get(i+5)).lastLog().equals("Told that "
							+ "the bus just arrived at stop with "
							+ "location (1,0)"));
		}
		for(int i = 0;i<10;++i){
			assertTrue("Stop2 people should have gotten the bus's arrival message",
					((MockBusPerson) people2.get(i)).lastLog().equals("Told that "
							+ "the bus just arrived at stop with "
							+ "location (2,0)"));
		}
		
		//step 4 - add/subtract some people2 people to/from the bus
		for(int i = 0;i<5;++i){
			bus.getOnBus(people2.get(i));
		}
		for(int i = 0;i<3;++i){
			bus.getOffBuss(people1.get(i));
		}
		
		//check post of 4 and pre of 5
		assertTrue("Bus should have 7 passengers",bus.passengers.size()==7);
		assertTrue("Stop1 should only have 5 people left",stop1.peopleWaiting.size()==5);
		assertTrue("Stop2 should only have 5 people left",stop2.peopleWaiting.size()==5);
		for(int i = 0;i<5;++i){
			assertTrue("First 5 of people2 should be on bus",bus.passengers.contains(people2.get(i)));
			assertTrue("First 5 of people2 should not be at stop2",!stop2.peopleWaiting.contains(people2.get(i)));
			assertTrue("Second 5 people2 should not be on bus",!bus.passengers.contains(people2.get(5+i)));
			assertTrue("Second 5 of people2 should be at stop2",stop2.peopleWaiting.contains(people2.get(i+5)));

		}
		for(int i = 0;i<5;++i){
			assertTrue("Second five people of people1 should not be on bus",!bus.passengers.contains(people1.get(i+5)));
		}
		for(int i = 0;i<3;++i){
			assertTrue("First3 people of people1 should not be on bus",!bus.passengers.contains(people1.get(i)));
			assertTrue("First3 people of people1 should not be at stop1",!stop1.peopleWaiting.contains(people1.get(i)));
		}
		for(int i = 3;i<5;++i){
			assertTrue("Last 2 of first 5 of people1 should be on bus",bus.passengers.contains(people1.get(i)));
			assertTrue("Last 2 of first 5 of people1 should not be at stop1",!stop1.peopleWaiting.contains(people1.get(i)));
			assertTrue("Last 2 of first 5 of people1 should not be at stop2",!stop2.peopleWaiting.contains(people2.get(i)));

		}
		
		
		
		
		
		*/
		
		//
		
		
		
		
	}
	
}