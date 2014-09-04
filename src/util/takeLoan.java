package util;

public class takeLoan extends Task{
	String custName;
	public String passWord;
	public int accountNumber;
	
	public takeLoan(){
		
	}
	
	public takeLoan(int amount,int accountNumber, String passWord){
		this.amount = amount;
		this.custName = custName;
		this.accountNumber = accountNumber;
		this.passWord = passWord;
	}
	
	
	public String getType(){
		return "takeLoan";
	}
	
}
