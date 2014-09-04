package UnitTests.mock.bankMock;

import interfaces.BankCustomer;
import interfaces.BankTeller;
import UnitTests.mock.LoggedEvent;
import UnitTests.mock.Mock;

public class MockBankCustomer extends Mock implements BankCustomer{

	String name;
	public String passWord;
	
	public MockBankCustomer(String string) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	@Override
	public void msgHowCanIHelpYou(BankTeller b) {
		
		log.add(new LoggedEvent("Being helped"));
		
	}

	@Override
	public void msgAccountOpenedAnythingElse(int amount, int accountNumber,
			String passWord) {
		this.passWord = passWord;
		log.add(new LoggedEvent("My openAccount was completed"));
		
	}

	@Override
	public void msgDepositCompletedAnythingElse(int amount) {
		
		log.add(new LoggedEvent("My deposit was completed"));
		
	}

	@Override
	public void msgHereIsWithdrawalAnythingElse(int amount) {
		
		log.add(new LoggedEvent("My withdrawal was completed"));
		
	}

	@Override
	public void msgLoanApprovedAnythingElse(int amount, int accountNumber,
			int loanNumber) {
		
		log.add(new LoggedEvent("My loan was granted"));
		
		
	}

	@Override
	public void msgHereIsMoneyAnythingElse(int amount) {
		
		log.add(new LoggedEvent("My robbery was granted!"));
		
		
	}
	
	@Override
	public void msgNoTellers() {
		
	}

}
