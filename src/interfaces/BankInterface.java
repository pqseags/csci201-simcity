package interfaces;

import util.deposit;
import util.openAccount;
import util.takeLoan;
import util.withdrawal;

public interface BankInterface {
	
	
	public abstract boolean addMeToQueue(BankCustomer b);
	
	public abstract boolean startTellerShift(BankTeller t);
	
	public abstract BankCustomer getCustomer();
	
	public abstract int addAccount(String custName,int amount,String passWord);
	
	public abstract boolean deposit(int amount,int accountNumber,String passWord);
	
	public abstract boolean withdraw(int amount,int accountNumber,String passWord);
	
	public abstract int giveLoan(int amount,int accountNumber,String passWord);
	
	public abstract int rob(int amount);
	
	
	
	
	
	
}
