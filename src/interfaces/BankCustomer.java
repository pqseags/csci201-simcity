package interfaces;

import util.takeLoan;

public interface BankCustomer {

	
	public abstract String getName();
	
	public abstract void msgHowCanIHelpYou(BankTeller b);
	
	public abstract void msgAccountOpenedAnythingElse(int amount,int accountNumber,String passWord);

	public abstract void msgDepositCompletedAnythingElse(int amount);
	
	public abstract void msgHereIsWithdrawalAnythingElse(int amount);
	
	public abstract void msgLoanApprovedAnythingElse(int amount,int accountNumber,int loanNumber);
	
	public abstract void msgHereIsMoneyAnythingElse(int amount);
	
	public abstract void msgNoTellers();
	
	
	
}
