//package util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import bank.BankCustomerRole;
//import bank.BankTellerRole;
//import bank.Person;
//
//public class TestingPanel {
//
//
//	
//
//	
//	public TestingPanel(){
//		//c0.startThread();
//	}
//	
//	public static void main(String[]  args){
//		Bank bank = new Bank();
//		
//		//BankCustomerRole c0 = new BankCustomerRole("Cust0");
//		BankTellerRole t0 = new BankTellerRole("Teller0");
//		
//		List<BankCustomerRole> custs = new ArrayList<BankCustomerRole>();
//		List<BankTellerRole> tellers = new ArrayList<BankTellerRole>();
//		
//		BankCustomerRole c;
//		BankTellerRole t;
//		for(int i = 0;i<2;++i){
//			c = new BankCustomerRole("Cust"+i);
//			c.setBank(bank);
//			c.setPerson(new Person());
//			c.addTask(new openAccount(1000,c.getName()));
//			//String passWord = c.passWord;
//			//System.out.println(passWord + "Hi");
//			
//			
//			c.startThread();
//			
//			/*t = new BankTellerRole("Teller"+i);
//			t.setBank(bank);
//			t.setPerson(new Person());
//			t.startThread();*/
//			
//			custs.add(c);
//			//tellers.add(t);
//		}
//		
//		
//		//c0.setBank(bank);
//		//c0.setPerson(new Person());
//		t0.setBank(bank);
//		
//		
//		//c0.startThread();
//		t0.startThread();
//	}
//	
//}
