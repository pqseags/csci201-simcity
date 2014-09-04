package util;

import interfaces.BankCustomer;
import interfaces.BankInterface;
import interfaces.BankTeller;
import interfaces.Person;
import interfaces.PlaceOfWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cityGui.CityBank;
import cityGui.CityBankCard;
import cityGui.CityHouseCard;
import role.Role;
import UnitTests.mock.bankMock.MockBankPerson;
import bank.BankCustomerRole;
import bank.BankTellerRole;
import bank.gui.BankControlPanel;
import bank.gui.BankCustomerGui;
import bank.gui.BankGui;
import bank.gui.BankTellerGui;

public class Bank implements BankInterface, PlaceOfWork{
	

	
	//stores all the people in the queue
	public CityBankCard animation;
	
	public List<BankCustomer> bankCustomers = Collections.synchronizedList(new ArrayList<BankCustomer>());
	public List<BankCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<BankCustomer>());
	
	public List<BankTeller> myTellers = new ArrayList<BankTeller>();
	public List<BankTeller> currentTellers = new ArrayList<BankTeller>();
	public List<BankAccount> accounts = new ArrayList<BankAccount>();
	public BankGui bankGui = new BankGui(this);
	
	public List<TellerSpot> tellerSpots = new ArrayList<TellerSpot>();
	public boolean isOpen = true;
	
	public CityBank cityBankGui;
	
	int totalAmount = 1000000000;
	
	public Bank() {
		bankGui.bankControlPanel = new BankControlPanel( animation, this);
		tellerSpots.add(new TellerSpot(100 + 300/2-12, 75));//x value of the counter + center of counter - half width
		tellerSpots.add(new TellerSpot(100 + 300/2-12, 445));//x value of the counter + center of counter - half width
		tellerSpots.add(new TellerSpot(100 + 300, 175));//x value of the counter + counterWidth
		tellerSpots.add(new TellerSpot(100 + 300, 325));//x value of the counter + counterWidth
	}
	
	public class BankAccount{
		public BankAccount(int amount,int accountNumber,String custName,String passWord){
			this.amount = amount;
			this.accountNumber = accountNumber;
			this.custName = custName;
			this.passWord = passWord;
		}
		public int amount;
		public int accountNumber;
		public String custName;
		public String passWord;
		public List<loan> myLoans = new ArrayList<loan>();
	}
	
	public class loan{

		public loan(int totalAmount,int loanNumber) {
			total = totalAmount;
			amountLeft = total;
			this.loanNumber = loanNumber;
		}
		public int total;
		int amountLeft;//to be paid off.
		public int loanNumber;

		public void payOff(int payment) {
			amountLeft -= payment;
		}
	}
	
	public class TellerSpot {
		public TellerSpot(int x, int y) {
			xPos = x;
			yPos = y;
		}
		BankTeller currentTeller;
		int xPos;
		int yPos;
	}
	
	public boolean startTellerShift(BankTeller t){
		currentTellers.add(t);
		if (t instanceof BankTellerRole) {
			((BankTellerRole)t).startedWorking=true;
			BankTellerGui g = new BankTellerGui(((BankTellerRole)t));
			if (!tellerSpots.isEmpty())
				for (TellerSpot TS : tellerSpots) {
					if (TS.currentTeller==null) {
						g.initialSpot(TS.xPos, TS.yPos);
						TS.currentTeller = t;
						break;
					}
				}
			((BankTellerRole)t).setGui(g);
			bankGui.bankPanel.tellerPanel.addListButton(((BankTellerRole)t).getName());
			animation.addGui(g);
		}
		//bankGui.bankAnimationPanel.addGui(g);
		//t.msgStateChanged();
		return true;
	}
	
	public void finishTellerShift(BankTeller t) {
		for (TellerSpot TS : tellerSpots) {
			if (TS.currentTeller==t) {
				TS.currentTeller = null;
			}
		}
		currentTellers.remove(t);
		bankGui.bankPanel.tellerPanel.removeAll();
//		for (BankTeller BT : currentTellers) {
//			bankGui.bankPanel.tellerPanel.addListButton(((BankTellerRole)BT).getName());
//		}
		if(currentTellers.isEmpty()){
			tellCustomersNoTellers();
		}
	}
	
	public void tellCustomersNoTellers(){
		synchronized(bankCustomers){
			for(int i  =0;i<bankCustomers.size();++i){
				bankCustomers.get(0).msgNoTellers();
				bankCustomers.remove(0);
			}
		}
	}
	
	public void exitBank(BankCustomer c) {
		//bankCustomers.remove(c);
		//bankGui.bankPanel.customerPanel.removeAll();
//		for (BankCustomer BC : bankCustomers) {
//			bankGui.bankPanel.customerPanel.addListButton(((BankCustomerRole)BC).getName());
//		}
	}
	
	public BankCustomer getCustomer(){
		
		synchronized(bankCustomers){
			if(!bankCustomers.isEmpty()){
				BankCustomer b = bankCustomers.get(0);
				System.out.println("");
				bankCustomers.remove(0);
				return b;
			}
		}
		return null;
	}
	
	public boolean addMeToQueue(BankCustomer c){
		
		if(currentTellers.isEmpty()){
			System.out.println("Cust not allowed in line at empty bank");
			return false;
		}
		System.out.println("Requested to be put in line now.");
		bankCustomers.add(c);
		if (c instanceof BankCustomerRole) {
			if ( !(((BankCustomerRole) c).p instanceof MockBankPerson) ) {
			BankCustomerGui g = new BankCustomerGui(((BankCustomerRole)c));
			((BankCustomerRole)c).setGui(g);
			bankGui.bankPanel.customerPanel.addListButton(((BankCustomerRole)c).getName());
			//bankGui.bankAnimationPanel.addGui(g);
			if (animation!=null) {
				animation.addGui(g);
			}
			}
		}
		//System.out.println("Size of the queue is "+bankCustomers.size());
		for(BankTeller t:currentTellers){
			//System.out.println("Teller messaged");
			t.msgStateChanged();
		}
		return true;
	}
	
	
	//BANKING FUNCTIONS
	
	public synchronized int addAccount(String name,int amount,String passWord){
		int accountNumber = accounts.size();
		accounts.add(new BankAccount(amount,accountNumber,name,passWord));
		bankGui.bankPanel.accountPanel.addListButton("" + accountNumber);
		return accountNumber;
	}
	
	public synchronized boolean deposit(int amount,int accountNumber,String passWord){
		if(accountNumber>=accounts.size()){
			return false;
		}
		BankAccount ba = accounts.get(accountNumber);
		if(passWord.equals(ba.passWord)){
			accounts.get(accountNumber).amount += amount;
			return true;
		}
		return false;
	}
	
	public synchronized boolean withdraw(int amount,int accountNumber,String passWord){
		if(accountNumber>=accounts.size()){
			return false;
		}
		BankAccount ba = accounts.get(accountNumber);
		if(passWord.equals(ba.passWord) && ba.amount>=amount){
			accounts.get(accountNumber).amount -= amount;
			return true;
		}
		return false;
	}
	
	public synchronized int giveLoan(int amount,int accountNumber,String passWord){
		if(accountNumber>=accounts.size()){
			return -1;
		}
		BankAccount ba = accounts.get(accountNumber);
		int l = ba.myLoans.size();
		ba.myLoans.add(new loan(amount,l));
		return l;
		
	}
	
	public synchronized int rob(int amount){
		if(totalAmount>=amount){
			totalAmount -= amount;
			return amount;
		}
		
		int a = totalAmount;
		totalAmount = 0;
		return a;
		
	}


	@Override
	public Role canIStartWorking(Person p,JobType jobType,Role r) {
		// TODO Auto-generated method stub
		if(jobType == JobType.BankTeller){
			if(startTellerShift((BankTeller) r)){
				return r;
			}
			return null;
		}
		System.out.println("A non-teller is trying to work at the bank!!");
		return null;
	}


	public void setAnimationPanel(CityBankCard chc){
		animation=chc;
		
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	
	
	
}
