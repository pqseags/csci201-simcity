package util;

public class BankMapLoc extends Place{
	public BankMapLoc(Bank b){
		this.bank = b;
	}
	public Bank bank;
	public int bankTopLeftX;
	public int bankTopLeftY;
}
