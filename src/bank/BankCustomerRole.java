package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import bank.gui.BankCustomerGui;
import person.PersonAgent;
import interfaces.BankCustomer;
import interfaces.BankInterface;
import interfaces.BankTeller;
import interfaces.Person;
import role.Role;
import UnitTests.mock.bankMock.MockBankPerson;
import agent.Agent;
import util.*;

public class BankCustomerRole extends Role implements BankCustomer {

	
	//Constructor
	
	//quick and dirty
	public BankCustomerRole(String name,Person p){
		this.person = p;
		this.name = name;
		//System.out.println(getName());
	}
	
	
	//SETTERS
	
	public void setBank(Bank b){
		bank = b;
	}
	
	public void setPerson(Person p){
		person = p;
	}
	
	public void addTask(Task t){
		Tasks.add(t);
	}
	
	public void setGui(BankCustomerGui g) {
		bankCustomerGui = g;
	}
	
	//GETTERS
	
	public String getName(){
		return name;
	}
	
	
	
	//DATA
	
	public String passWord;
	
	public String name;
	
	public BankInterface bank;
	
	public Person person;
	
	
	public BankTeller teller;
	
	public enum CustState {init,inBank,inLine,goingToWindow,beingServed,leaving};
	public enum CustEvent {tellerReady,taskPending,left};
	
	public CustState state = CustState.init;
	public CustEvent event = null;
	
	public List<Task> Tasks = new ArrayList<Task>();
	public Semaphore atDestination = new Semaphore(0, true);
	
	Task pendingTask = null;
	
	public boolean noTellers = false;
	
	public BankCustomerGui bankCustomerGui;
	
	public int tellerWindowX = 0;
	public int tellerWindowY = 0;
	
	
	
	//MSG

	public void msgNoTellers() {
		
		if(state!=CustState.inLine && state!=CustState.inBank){
			//System.err.println("Cust has a teller when there are no tellers");
		}
		
		noTellers = true;
		
	}
	
	public void msgYouAreAtBank(Bank b){
		//System.out.println("YOU ARE AT BANK");
		this.bank = b;
		this.state = CustState.inBank;
	}
	
	public void msgHowCanIHelpYou(BankTeller t){
		Do("Just got asked how I can be helped.");
		
		teller = t;
		event = CustEvent.tellerReady;
		//state = CustState.beingServed;
		person.msgStateChanged();
		//stateChanged();
		
	}
	
	public void msgHowCanIHelpYou(BankTeller t, int x, int y){
		Do("Just got asked how I can be helped.");
		tellerWindowX = x;
		tellerWindowY = y;
		teller = t;
		event = CustEvent.tellerReady;
		//state = CustState.beingServed;
		person.msgStateChanged();
		//stateChanged();
		
	}
	
	public void msgDepositCompletedAnythingElse(int amount){
		Do("Told that my deposit was successful");
		int accNum = ((deposit) pendingTask).accountNumber;
		person.takeFromWallet(amount);
		person.addToAccount(accNum,amount);
		event = CustEvent.tellerReady;
		pendingTask = null;
		person.msgStateChanged();
	}
	
	public void msgHereIsWithdrawalAnythingElse(int amount){
		Do("Told that my withdrawal was successful");
		int accNum = ((withdrawal) pendingTask).accountNumber;
		person.addToWallet(amount);
		person.takeFromAccount(accNum,amount);
		event = CustEvent.tellerReady;
		pendingTask = null;
		person.msgStateChanged();
	}
	
	public void msgAccountOpenedAnythingElse(int amount, int accountNumber,String passWord){
		Do("I've got a new account with amount "+ amount);
		String name = ((openAccount) pendingTask).custName;
		person.takeFromWallet(amount);
		person.createAccount(accountNumber,amount,name,passWord);
		event = CustEvent.tellerReady;
		//this.passWord = passWord;
		//Do(passWord);
		pendingTask = null;
		person.msgStateChanged();
	}
	
	public void msgLoanApprovedAnythingElse(int cash, int accountNumber, int loanNumber){
		Do("Loan approved for $"+ cash);
		person.addToWallet(cash);
		person.addLoan(accountNumber,cash,loanNumber);
		event = CustEvent.tellerReady;
		pendingTask = null;
		person.msgStateChanged();
	}
	
	public void msgHereIsMoneyAnythingElse(int cash){
		person.addToWallet(cash);
		event = CustEvent.tellerReady;
		pendingTask = null;
		if (!(person instanceof MockBankPerson))
			((PersonAgent) person).robbedBank = true;
		//Do("I have " +Tasks.size() + " tasks");
		Tasks.clear();
		person.msgStateChanged();
	}
	
	public void msgAtDestination() {
		atDestination.release();
		person.msgStateChanged();
	}
	
	
	
	//SCHED
	
	public boolean pickAndExecuteAnAction(){
		//Do("My state is: "+ state);
		//if you're inBank, get in line
		
		if(noTellers){
			System.out.println("In here");
			leaveBank();
		}
		
		if(state == CustState.inBank){
			System.out.flush();
			getInLine();
			return true;
		}
		
		//if you're being served & the event is that teller is read, do next task
		if(state == CustState.inLine && event == CustEvent.tellerReady) {
			goToWindow();
			return true;
		}
		
		if(state == CustState.beingServed && event == CustEvent.tellerReady){
			NextTask();
			return true;
		}
		
		return false;
		
	}
	
	//ACT
	
	private void getInLine(){
		if(!person.getName().equals("BankRobber")){
			Do("Getting in line!");
		}
		if(bank.addMeToQueue(this)){
			//Do("BEFORE");
			doGetInLine();
			state = CustState.inLine;
			//Do("After");
			//Thread.dumpStack();
			System.out.flush();
		}
		else{
			Do("Not allowed in line, no tellers working");
			Tasks.clear();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leaveBank();
		}
		
		//Do(""+state);
	}
	
	private void goToWindow() {
		Do("Going to the teller's window");
		
		if(bankCustomerGui!=null){
			bankCustomerGui.DoGoToTellerWindow(tellerWindowX, tellerWindowY);
		}
		else{
			atDestination.release();
		}
		state = CustState.goingToWindow;
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = CustState.beingServed;
	}
	
	private void NextTask(){
		System.out.println(Tasks.size());
		//Do("Telling teller the next task");
		
		//Task t = Tasks.get(0);
		if(Tasks.isEmpty()){
			Do("Finished what I needed done. I'm leaving");
			state = CustState.leaving;
			
			if(bankCustomerGui!=null && !(person instanceof MockBankPerson)){
				bankCustomerGui.DoExitBank();
			}
			else{
				atDestination.release();
			}
			
			teller.msgDoneAndLeaving();
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			((Bank)bank).exitBank(this);
			event = CustEvent.left;
			person.msgThisRoleDone(this);
			//System.err.println("I should be out of the bank");
			return;
		}
		
		
		Task t = Tasks.get(0);
		
		Do(t.getType());
		
		Task tCopy = t.copyTask();
		//Task tCopy = t.clone();
		
		teller.msgIWantTo(tCopy);
		
		event = CustEvent.taskPending;
		pendingTask = t;
		Tasks.remove(0);
		
		
		
	}
	
	protected void leaveBank(){
		state = CustState.leaving;
		
		if(bankCustomerGui!=null){
			bankCustomerGui.DoExitBank();
		}
		else{
			atDestination.release();
		}
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		((Bank)bank).exitBank(this);
		event = CustEvent.left;
		person.msgThisRoleDone(this);
		//System.err.println("I should be out of the bank");
		return;
	}
	
	
	
	
	//GUI
	
	private void doGetInLine(){
		
	}
	
	private void doGoToWindow() {
		
	}



	
	
	
}
