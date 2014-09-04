package util;

public class withdrawal extends Task{
	public int accountNumber;
	public String passWord;
	
	public withdrawal(){
		
	}
	
	public withdrawal(int amount,int accountNumber,String passWord){
		this.amount = amount;
		this.accountNumber = accountNumber;
		this.passWord = passWord;
	}
	
	
	public String getType(){
		return "withdrawal";
	}
	
}
