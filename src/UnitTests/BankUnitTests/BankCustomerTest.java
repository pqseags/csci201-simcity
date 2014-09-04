package UnitTests.BankUnitTests;

import util.Bank;
import util.deposit;
import util.openAccount;
import util.rob;
import util.takeLoan;
import util.withdrawal;
import UnitTests.mock.bankMock.MockBankPerson;
import UnitTests.mock.bankMock.MockBankTeller;
import bank.BankCustomerRole;
import bank.BankTellerRole;
import junit.framework.*;

public class BankCustomerTest extends TestCase{
	
	//Unit test for the BankCustomerRole
	
	BankCustomerRole customer;
	MockBankTeller teller;
	Bank bank;
	
	MockBankPerson person;
	
	
	public void setUp() throws Exception {
		super.setUp();
		
		person = new MockBankPerson("p0");
		customer = new BankCustomerRole("c0",person);
		customer.setPerson(person);
		teller = new MockBankTeller("t0");
		bank = new Bank();
		bank.startTellerShift(teller);
		
		
		
	}
	
	
	/** This test will examine the case of a single customer with no tasks
	 * 
	 */
	public void testNothingToDo(){
		
		//step 0 - make sure the customer doesn't do anything yet
		assertFalse("Customer shouldn't be doing anything",customer.pickAndExecuteAnAction());
		
		//step 1 - put the customer in the bank
		customer.msgYouAreAtBank(bank);
		
		//check post of 1 and pre of 2
		assertTrue("Customer should be in bank.",customer.state == BankCustomerRole.CustState.inBank);
		assertTrue("Customer should have bank pointer", customer.bank != null);
		assertTrue("Person should still have an empty event log",person.log.isEmpty());
		//System.err.println("2");
		
		//step 2 - call the customer scheduler
		//System.err.println("Pick and execute(not in line yet");
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		//System.out.println("3");
		
		//check post of 2 and pre of 3
		assertTrue("Bank should have customer in line.",bank.bankCustomers.get(0) == customer);
		assertTrue("Customer should know he's waiting in line",customer.state==BankCustomerRole.CustState.inLine);
		//System.out.println("4");
		
		
		//step 3 - send him the message that he's being helped
		customer.msgHowCanIHelpYou(teller);	
		//System.out.println("5");
		
		//check post of 3 and pre of 4
		assertTrue("Customer event should know he's being waited on",customer.event == BankCustomerRole.CustEvent.tellerReady);
		assertTrue("Cusomter should know who the teller who's helping him",customer.teller != null);
		//System.out.println(person.log.getLastLoggedEvent());
		assertTrue("Person that holds customer should have a new permit",person.log.getLastLoggedEvent().getMessage().equals("Just got a new permit"));
		//System.out.println("6");
		
		//step 4 - call the person scheduler
		customer.atDestination.release();
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		//System.out.println("7");
		
		//check post of 4 and pre of 5
		assertTrue("Customer should know he's being helped",customer.state == BankCustomerRole.CustState.beingServed);
		assertTrue("Teller should not have been asked for anything yet.",teller.log.isEmpty());
		//System.out.println("8");
		
		//step 5 - call the scheduler
		//System.err.println("about to enter pick and execute to and the customer should know that they are done.");
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		//System.err.println("9");
		
		//check post of 5 and pre of 6
		assertTrue("Customer should have told teller he's leaving",teller.log.getLastLoggedEvent().getMessage().equals("My customer just left"));
		assertTrue("Customer should know he's leaving",customer.state == BankCustomerRole.CustState.leaving);
		assertTrue("Person should have been told that the role finished",person.log.getLastLoggedEvent().getMessage().equals("My BankCustomerRole just finished"));
		//System.err.println("10");

		
		
		assertTrue(true);
		
	}
	
	/** This test will examine the case of a single customer with several tasks
	 * 
	 */
	public void testOneCustomerManyTasks(){
		
		//step 0 - make sure the customer doesn't do anything yet and has no tasks
		assertFalse("Customer shouldn't be doing anything",customer.pickAndExecuteAnAction());
		assertTrue("Customer shouldn't have tasks to do.",customer.Tasks.isEmpty());
		
		//SET-UP/first half of step 1 put some tasks in the customer
		customer.addTask(new deposit(100,0,"passWord"));
		customer.addTask(new openAccount(150,"c0"));
		customer.addTask(new withdrawal(100,0,"passWord"));
		customer.addTask(new rob(500));
		customer.addTask(new takeLoan(100, 0, "passWord"));
		
		//step 1 - put the customer in the bank
		customer.msgYouAreAtBank(bank);
		
		//check post of 1 and pre of 2
		assertTrue("Customer should have 3 tasks to do",customer.Tasks.size()==5);
		assertTrue("Customer should be in bank.",customer.state == BankCustomerRole.CustState.inBank);
		assertTrue("Customer should have bank pointer", customer.bank != null);
		assertTrue("Person should still have an empty event log",person.log.isEmpty());
		
		//step 2 - call the customer scheduler
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		
		//check post of 2 and pre of 3
		assertTrue("Bank should have customer in line.",bank.bankCustomers.get(0) == customer);
		assertTrue("Customer should know he's waiting in line",customer.state==BankCustomerRole.CustState.inLine);
		
		
		//step 3 - send him the message that he's being helped
		customer.msgHowCanIHelpYou(teller);	
		
		//check post of 3 and pre of 4
		assertTrue("Customer event should know he's being waited on",customer.event == BankCustomerRole.CustEvent.tellerReady);
		assertTrue("Cusomter should know who the teller who's helping him",customer.teller != null);
		//System.out.println(person.log.getLastLoggedEvent());
		assertTrue("Person that holds customer should have a new permit",person.log.getLastLoggedEvent().getMessage().equals("Just got a new permit"));
		
		//step 4 - call the person scheduler
		customer.atDestination.release();
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		
		//check post of 4 and pre of 5
		assertTrue("Customer should know he's being helped",customer.state == BankCustomerRole.CustState.beingServed);
		assertTrue("Teller should not have been asked for anything yet.",teller.log.isEmpty());
		
		//step 5 - call the scheduler
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		
		//check post of 5 and pre of 6
		assertTrue("Teller should have a new task",teller.log.getLastLoggedEvent().getMessage().equals("Just got a new task: deposit"));
		assertTrue("Customer should know that his task is pending",customer.event == BankCustomerRole.CustEvent.taskPending);
		assertTrue("Customer should have taken the task off of his tasks list",customer.Tasks.size()==4);
		
		//step 6 - tell the customer the first task is done, do another
		customer.msgDepositCompletedAnythingElse(100);
		
		//check post of 6 and pre of 7
		assertTrue("Customer should know that the teller is ready for th next task",customer.event == BankCustomerRole.CustEvent.tellerReady);
		assertTrue("Person should have taken from their wallet after deposit",person.log.getLastLoggedEvent(3).getMessage().equals("Took from wallet"));
		assertTrue("Person should've added to their account",person.log.getLastLoggedEvent(2).getMessage().equals("Added to account"));
		assertTrue("Person should have a new permit",person.log.getLastLoggedEvent().getMessage().equals("Just got a new permit"));
		
		//step 7 - call the person scheduler to do the net task
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		
		//check post of 7 and pre of 8
		assertTrue("Teller should have a new task",teller.log.getLastLoggedEvent().getMessage().equals("Just got a new task: openAccount"));
		assertTrue("Customer should know that his task is pending",customer.event == BankCustomerRole.CustEvent.taskPending);
		assertTrue("Customer should have taken the task off of his tasks list",customer.Tasks.size()==3);
		
		//step 8 - tell the customer the first task is done, do another
		customer.msgAccountOpenedAnythingElse(100,0,"passWord");
		
		//check post of 8 and pre of 9
		assertTrue("Customer should know that the teller is ready for th next task",customer.event == BankCustomerRole.CustEvent.tellerReady);
		assertTrue("Person should have taken from their wallet after deposit",person.log.getLastLoggedEvent(3).getMessage().equals("Took from wallet"));
		assertTrue("Person should've created an account",person.log.getLastLoggedEvent(2).getMessage().equals("Created a new account"));
		assertTrue("Person should have a new permit",person.log.getLastLoggedEvent().getMessage().equals("Just got a new permit"));

		//step 9 - call the person scheduler to do the net task
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		
		//check post of 9 and pre of 10
		assertTrue("Teller should have a new task",teller.log.getLastLoggedEvent().getMessage().equals("Just got a new task: withdrawal"));
		assertTrue("Customer should know that his task is pending",customer.event == BankCustomerRole.CustEvent.taskPending);
		assertTrue("Customer should have taken the task off of his tasks list",customer.Tasks.size()==2);

		//step 10 - tell the customer the first task is done, do another
		customer.msgHereIsWithdrawalAnythingElse(100);
		
		//check post of 10 and pre of 11
		assertTrue("Customer should know that the teller is ready for th next task",customer.event == BankCustomerRole.CustEvent.tellerReady);
		assertTrue("Person should have taken from their wallet after deposit",person.log.getLastLoggedEvent(3).getMessage().equals("Added to wallet"));
		assertTrue("Person should've added to their account",person.log.getLastLoggedEvent(2).getMessage().equals("Took from account"));
		assertTrue("Person should have a new permit",person.log.getLastLoggedEvent().getMessage().equals("Just got a new permit"));

		//step 11 - call the person scheduler to do the net task
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());

		//check post of 11 and pre of 12
		assertTrue("Teller should have a new task",teller.log.getLastLoggedEvent().getMessage().equals("Just got a new task: rob"));
		assertTrue("Customer should know that his task is pending",customer.event == BankCustomerRole.CustEvent.taskPending);
		assertTrue("Customer should have taken the task off of his tasks list",customer.Tasks.size()==1);

		//step 12 - tell the customer the first task is done, do another
		customer.msgHereIsMoneyAnythingElse(500);

		//check post of 12 and pre of 13
		assertTrue("Customer should know that the teller is ready for th next task",customer.event == BankCustomerRole.CustEvent.tellerReady);
		assertTrue("Person should have taken from their wallet after deposit",person.log.getLastLoggedEvent(2).getMessage().equals("Added to wallet"));
		//assertTrue("Person should've added to their account",person.log.getLastLoggedEvent(2).getMessage().equals("Took from account"));
		assertTrue("Person should have a new permit",person.log.getLastLoggedEvent().getMessage().equals("Just got a new permit"));
		assertEquals("Customers taskList should have been emptied", 0, customer.Tasks.size());
		
		//step 13 - call the scheduler
		assertTrue("Customer should've acted",customer.pickAndExecuteAnAction());
		
		
		//check post of 13
		assertTrue("Customer should have told teller he's leaving",teller.log.getLastLoggedEvent().getMessage().equals("My customer just left"));
		assertTrue("Customer should know he's leaving",customer.state == BankCustomerRole.CustState.leaving);
		assertTrue("Person should have been told that the role finished",person.log.getLastLoggedEvent().getMessage().equals("My BankCustomerRole just finished"));

	}

	
}
