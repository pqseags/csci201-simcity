package util;

public class openAccount extends Task{
	public String custName;
	
	public openAccount(){
		
	}
	
	
	public openAccount(int amount, String custName){
		this.amount = amount;
		this.custName = custName;
	}
	
	public String getType(){
		return "openAccount";
	}
	
}
