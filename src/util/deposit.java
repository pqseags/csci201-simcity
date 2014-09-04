package util;

public class deposit extends Task {
	public int accountNumber;
	public String passWord;
	
	public deposit(){
		
	}
	
	public deposit(int amount,int accountNumber,String passWord){
		this.amount = amount;
		this.accountNumber = accountNumber;
		this.passWord = passWord;
	}
	
	public String getType(){
		return "deposit";
	}
}
