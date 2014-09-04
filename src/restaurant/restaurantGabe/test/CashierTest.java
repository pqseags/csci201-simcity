package restaurant.restaurantGabe.test;

import java.util.ArrayList;

import market.MarketInvoice;
import UnitTests.mock.MarketMock.MockMarketDeliveryMan;
import restaurant.restaurantGabe.CashierRole;
import restaurant.restaurantGabe.CustomerRole;
//import restaurant.CashierAgent.cashierBillState;
//import restaurant.WaiterAgent.Bill;
import restaurant.restaurantGabe.test.mock.MockCustomer;
import restaurant.restaurantGabe.test.mock.MockMarket;
import restaurant.restaurantGabe.test.mock.MockWaiter;
import restaurant.restaurantGabe.util.Check;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockCustomer customer2;
	//MockMarket market1;
	//MockMarket market2;
	
	MockMarketDeliveryMan deliveryMan1;
	MockMarketDeliveryMan deliveryMan2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierRole("cashier");		
		customer = new MockCustomer("mockcustomer");
		customer2 = new MockCustomer("mockcustomer2");
		waiter = new MockWaiter("mockwaiter");
		//market1 = new MockMarket("market1");
		//market2 = new MockMarket("market2");
		
		deliveryMan1  = new MockMarketDeliveryMan("deliveryMan1");
		deliveryMan2 = new MockMarketDeliveryMan("deliveryMan2");
		
		
	}	
	
	
	/** This test will examine the case of a single market bill given to the cashier
	 * 
	 */
	public void testOneMarketBill(){
		
		//set the cashier up for this scenario
		//make sure the cashier isn't dealing with customer debts
		if(!cashier.debts.isEmpty()){
			cashier.debts = new ArrayList<CashierRole.Debt>();
		}
		
		//check preconditions
		assertTrue("Cashier shouldn't have any bills. He does.",cashier.marketBills.isEmpty());
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		
		//step 1 of the test - send the message
		cashier.setMoney(1000);
		cashier.msgHereIsInvoice(deliveryMan1, new MarketInvoice(null,null,null,100));
		//cashier.msgDeliveryBill(market1, 100);
		
		//check postconditions of 1 and pre of 2
//		assertTrue("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
//				+ market1.log.toString(), 0==market1.log.size());

		assertTrue("Cashier should have 1 bill in it. It doesn't.", cashier.marketBills.size()==1);
		assertTrue("Bill was not added to Cashier correctly",cashier.marketBills.get(0).i.total == 100);
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		//assertTrue("Cashier state doesn't have a free permit",cashier.stateChange.availablePermits()==2);
		
		//step 2 - call the scheduler
		assertTrue("Cashier scheduler should have returned true. It didn't",cashier.pickAndExecuteAnAction());
		
		
		//check post of 2
		//make sure the right action was called
//		assertTrue("Cashier log should indicate picking the pay bill action. Incorrecty reads: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Attempting to pay market1 "
//						+ "a bill of $100"));
//		assertTrue("Market log should read \"Recieved a payment of $100\". Incorrectly reads: "
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.getLastLoggedEvent().getMessage().equals("Recieved a payment of $100"));
//		
		//assertTrue("Cashier should have only $900. He doesn't",cashier.getMoney()==900);
		assertTrue("Cashier should have removed bill. He didn't",cashier.marketBills.isEmpty());
		
	
	}
	
	
	/** Will examine the case of two different markets fulfilling one cook order. The two markets will separately bill the cashier
	 * 
	 */
	public void testTwoMarketBills(){
		
		
		//set the cashier up for this scenario
		//make sure the cashier isn't dealing with customer debts
		if(!cashier.debts.isEmpty()){
			cashier.debts = new ArrayList<CashierRole.Debt>();
		}
		
		//check preconditions
		assertTrue("Cashier shouldn't have any bills. He does.",cashier.marketBills.isEmpty());
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		
		//step 1 of the test - send the message
		cashier.setMoney(1000);
		cashier.msgHereIsInvoice(deliveryMan1, new MarketInvoice(null,null,null,100));
		//cashier.msgDeliveryBill(market1, 100);
		
		
		//check postconditions of 1 and pre of 2
//		assertTrue("MockMarket1 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket1's event log reads: "
//				+ market1.log.toString(), 0==market1.log.size());
//		assertTrue("MockMarket2 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket2's event log reads: "
//				+ market2.log.toString(), 0==market2.log.size());

		assertTrue("Cashier should have 1 bill in it. It doesn't.", cashier.marketBills.size()==1);
		assertTrue("Bill was not added to Cashier correctly",cashier.marketBills.get(0).i.total == 100);
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		//assertTrue("Cashier state doesn't have a free permit",cashier.stateChange.availablePermits()==2);
		
		
		//step 2 - second marketBill comes in
		cashier.msgHereIsInvoice(deliveryMan2, new MarketInvoice(null,null,null,200));
		//cashier.msgDeliveryBill(market2, 200);
		
		
		//check post for 2, pre for 3
//		assertTrue("MockMarket1 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket1's event log reads: "
//				+ market1.log.toString(), 0==market1.log.size());
//		assertTrue("MockMarket2 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket2's event log reads: "
//				+ market2.log.toString(), 0==market2.log.size());

		assertTrue("Cashier should have 2 bills in it. It doesn't.", cashier.marketBills.size()==2);
		assertTrue("Bill was not added to Cashier correctly",cashier.marketBills.get(1).i.total == 200);
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		//assertTrue("Cashier state doesn't have 2 free permits",cashier.stateChange.availablePermits()==3);
		
		
		//step 3 - call the scheduler
		assertTrue("Cashier scheduler should have returned true. It didn't",cashier.pickAndExecuteAnAction());
		
		
		//check post of 3, pre of 4
		//make sure the right action was called
//		assertTrue("Cashier log should indicate picking the pay bill action. Incorrecty reads: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Attempting to pay market1 "
//						+ "a bill of $100"));
//		assertTrue("Market log should read \"Recieved a payment of $100\". Incorrectly reads: "
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.getLastLoggedEvent().getMessage().equals("Recieved a payment of $100"));
//		
		//assertTrue("Cashier should have only $900. He doesn't",cashier.getMoney()==900);
		assertTrue("Cashier should have removed bill. He didn't",cashier.marketBills.size()==1);
		//assertTrue("Second bill should be the only one left.",(cashier.marketBills.get(0).m==market2)&&(cashier.marketBills.get(0).amount==200));
		
		
		//step 4 - call the scheduler for the second bill
		assertTrue("Cashier scheduler should have returned true. It didn't",cashier.pickAndExecuteAnAction());
		
		
		//check post for 4
//		assertTrue("Cashier log should indicate picking the pay bill action. Incorrecty reads: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Attempting to pay market2 "
//						+ "a bill of $200"));
//		assertTrue("Market log should read \"Recieved a payment of $200\". Incorrectly reads: "
//				+ market2.log.getLastLoggedEvent().toString(), market2.log.getLastLoggedEvent().getMessage().equals("Recieved a payment of $200"));
//		
		//assertTrue("Cashier should have only $700. He doesn't",cashier.getMoney()==700);
		assertTrue("Cashier should have removed bill. He didn't",cashier.marketBills.isEmpty());
				
		
		
	}
	
//	/**Will test when the cashier can't pay for the market order
//	 * 
//	 */
//	public void testCantPayMarketBill(){
//
//		//set the cashier up for this scenario
//		//make sure the cashier isn't dealing with customer debts
//		if(!cashier.debts.isEmpty()){
//			cashier.debts = new ArrayList<CashierRole.Debt>();
//		}
//		
//		//check preconditions
//		assertTrue("Cashier shouldn't have any bills. He does.",cashier.marketBills.isEmpty());
//		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
//		
//		//step 1 of the test - send the message
//		cashier.setMoney(95);
//		cashier.msgDeliveryBill(market1, 100);
//		
//		//check postconditions of 1 and pre of 2
////		assertTrue("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
////				+ market1.log.toString(), 0==market1.log.size());
//
//		assertTrue("Cashier should have 1 bill in it. It doesn't.", cashier.marketBills.size()==1);
//		assertTrue("Bill was not added to Cashier correctly",cashier.marketBills.get(0).amount == 100);
//		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
//		//assertTrue("Cashier state doesn't have a free permit",cashier.stateChange.availablePermits()==2);
//		
//		//step 2 - call the scheduler
//		assertTrue("Cashier scheduler should have returned true. It didn't",cashier.pickAndExecuteAnAction());
//		
//		
//		//check post of 2
//		//make sure the right action was called
//		assertTrue("Cashier log should indicate picking the pay bill action. Incorrecty reads: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Attempting to pay market1 "
//						+ "a bill of $100"));
////		assertTrue("Market log should read \"Cashier's debt will be added to the next bill\". Incorrectly reads: "
////				+ market1.log.getLastLoggedEvent().toString(), market1.log.getLastLoggedEvent().getMessage().equals("Cashier's debt will be added to next bill"));
////		
//		assertTrue("Cashier should still have $95. He doesn't",cashier.getMoney()==95);
//		assertTrue("Cashier should have removed bill. He didn't",cashier.marketBills.isEmpty());
//		
//	
//	}
	
	
	/**Will test a normal one customer, one waiter, cashier interaction
	 * 
	 */
	public void testNormalSingleBill(){
		
		//set the cashier up for this scenario
		//wipe all of the cashier's data - shouldn't be any
		if(!cashier.debts.isEmpty()){
			cashier.debts = new ArrayList<CashierRole.Debt>();
		}
		if(!cashier.marketBills.isEmpty()){
			cashier.marketBills = new ArrayList<CashierRole.MarketBill>();
		}
		
		
		//check preconditions
		assertTrue("Cashier shouldn't have any cust bills. He does.",cashier.debts.isEmpty());
		assertTrue("Cashier shouldn't have any market bills. He does.",cashier.marketBills.isEmpty());
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		
		
		
		//step 1 - send message to compute bill
		cashier.msgComputeBill(waiter, customer, "Steak");
		
		//check post for 1 and pre for 2
		assertTrue("Cashier debt list should contain a debt.It doesn't.",!cashier.debts.isEmpty());
		assertTrue("Cashier's first debt should be for waiter and cust and steak.",(cashier.debts.get(0).w==waiter)&&
				(cashier.debts.get(0).c == customer) && (cashier.debts.get(0).s==CashierRole.DebtState.uncomputed));
		
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		assertTrue("Waiter should have an empty log. He doesn't",waiter.log.isEmpty());
		assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
		//assertTrue("Cashier state doesn't have a free permit",cashier.stateChange.availablePermits()==2);
		
		
		//step 2 - call the scheduler to compute the bill
		assertTrue("Cashier scheduler should have returned true.",cashier.pickAndExecuteAnAction());
		
		//check post for 2 and pre for 3
		assertTrue("Debt list should still have a debt in it.",!cashier.debts.isEmpty());
		assertTrue("Cashier log should indicate computing bill. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Computing bill for waiter mockwaiter and customer mockcustomer"));
		assertTrue("Debt amount for Steak should be 15.",cashier.debts.get(0).amount==15);
		assertTrue("Debt should be in the unpaid state.",cashier.debts.get(0).s==CashierRole.DebtState.unpaid);
		
		assertTrue("Waiter log should read \"Recieved a check for customer customer for $15\". Incorrectly reads: "
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.getLastLoggedEvent().getMessage().equals("Recieved a check for customer mockcustomer for $15"));
		//assertTrue("Cashier state should have 2 permit left",cashier.stateChange.availablePermits()==2);
		
		
		//step 3 - send message that customer is paying
		cashier.msgPayment(new Check(customer,15), 20);
		
		//check post for 3 and pre for 4
		assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
		assertTrue("Debt should have a change field of 5.",cashier.debts.get(0).change==5);
		assertTrue("Debt should be in the needsChange state.",cashier.debts.get(0).s==CashierRole.DebtState.needsChange);
		//assertTrue("Cashier state should have a new permit",cashier.stateChange.availablePermits()==3);
		

		//step 4 - call the scheduler to give change
		assertTrue("Cashier scheduler should have returned true.",cashier.pickAndExecuteAnAction());
		
		//check post for 4
		assertTrue("Cashier log should indicate giving change. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Giving mockcustomer change"));
		assertTrue("Cashier should be finished with the debt.",cashier.debts.get(0).s==CashierRole.DebtState.paid);
		assertTrue("Customer log should show the change",customer.log.getLastLoggedEvent().getMessage().equals("Being given $5 in change"));
		
		
	}
	
	/**Will test the one customer, one waiter, cashier interaction when cust can't pay
	 * 
	 */
	public void testCantPayOneBill(){
		
		//set up for this scenario
		//wipe all of the cashier's data - shouldn't be any
		if(!cashier.debts.isEmpty()){
			cashier.debts = new ArrayList<CashierRole.Debt>();
		}
		if(!cashier.marketBills.isEmpty()){
			cashier.marketBills = new ArrayList<CashierRole.MarketBill>();
		}
		
		
		//check preconditions
		assertTrue("Cashier shouldn't have any cust bills. He does.",cashier.debts.isEmpty());
		assertTrue("Cashier shouldn't have any market bills. He does.",cashier.marketBills.isEmpty());
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		
		
		
		//step 1 - send message to compute bill
		cashier.msgComputeBill(waiter, customer, "Steak");
		
		//check post for 1 and pre for 2
		assertTrue("Cashier debt list should contain a debt.It doesn't.",!cashier.debts.isEmpty());
		assertTrue("Cashier's first debt should be for waiter and cust and steak.",(cashier.debts.get(0).w==waiter)&&
				(cashier.debts.get(0).c == customer) && (cashier.debts.get(0).s==CashierRole.DebtState.uncomputed));
		
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		assertTrue("Waiter should have an empty log. He doesn't",waiter.log.isEmpty());
		assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
		//assertTrue("Cashier state doesn't have a free permit",cashier.stateChange.availablePermits()==2);
		
		
		//step 2 - call the scheduler to compute the bill
		assertTrue("Cashier scheduler should have returned true.",cashier.pickAndExecuteAnAction());
		
		//check post for 2 and pre for 3
		assertTrue("Debt list should still have a debt in it.",!cashier.debts.isEmpty());
		assertTrue("Cashier log should indicate computing bill. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Computing bill for waiter mockwaiter and customer mockcustomer"));
		assertTrue("Debt amount for Steak should be 15.",cashier.debts.get(0).amount==15);
		assertTrue("Debt should be in the unpaid state.",cashier.debts.get(0).s==CashierRole.DebtState.unpaid);
		
		assertTrue("Waiter log should read \"Recieved a check for customer customer for $15\". Incorrectly reads: "
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.getLastLoggedEvent().getMessage().equals("Recieved a check for customer mockcustomer for $15"));
		//assertTrue("Cashier state should have 2 permit left",cashier.stateChange.availablePermits()==2);
		
		
		//step 3 - send message that customer is paying
		cashier.msgICantPay(new Check(customer,15), 0);
		
		//check post for 3 and pre for 4
		assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
		assertTrue("Debt should have a change field of 0.",cashier.debts.get(0).change==0);
		assertTrue("Debt should be in the cantPay state.",cashier.debts.get(0).s==CashierRole.DebtState.cantPay);
		//assertTrue("Cashier state should have a new permit",cashier.stateChange.availablePermits()==3);
		

		//step 4 - call the scheduler to let go
		assertTrue("Cashier scheduler should have returned true.",cashier.pickAndExecuteAnAction());
		
		//check post for 4
		assertTrue("Cashier log should indicate letting go. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Letting mockcustomer pay next time"));
		assertTrue("Cashier should debt in delinquent state.",cashier.debts.get(0).s==CashierRole.DebtState.delinquent);
		assertTrue("Customer log should show the change",customer.log.getLastLoggedEvent().getMessage().equals("I'll pay next time"));
		
		
		//step 5 - customer comes back to restaurant, waiter asks for new bill
		cashier.msgComputeBill(waiter, customer, "Steak");
		
		//check post for 5 and pre for 6
		assertTrue("Cashier debt list should still contain one debt.It doesn't.",cashier.debts.size()==1);
		//assertTrue("Cashier state should have another free permit",cashier.stateChange.availablePermits()==4);
		
		//step 6 - call the scheduler to compute the bill
		assertTrue("Cashier scheduler should return true",cashier.pickAndExecuteAnAction());
		
		//check post for 6 and pre for 7
		assertTrue("Customer debt should be 30 now",cashier.debts.get(0).amount==30);
		assertTrue("Cashier log should indicate computing bill. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals(
						"Computing bill for waiter mockwaiter and customer mockcustomer"));
		assertTrue("Debt should be in the unpaid state.",cashier.debts.get(0).s==CashierRole.DebtState.unpaid);
		assertTrue("Waiter log should read \"Recieved a check for customer customer for $30\". Incorrectly reads: "
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.getLastLoggedEvent().getMessage().equals(
						"Recieved a check for customer mockcustomer for $30"));
		
		
		//step 7 send the message that customer will pay this time
		cashier.msgPayment(new Check(customer,30), 35);
		
		//check post for 7 and pre for 8
		assertTrue("Debt should have a change field of 5.",cashier.debts.get(0).change==5);
		assertTrue("Debt should be in the needsChange state.",cashier.debts.get(0).s==CashierRole.DebtState.needsChange);
		//assertTrue("Cashier state should have a new permit",cashier.stateChange.availablePermits()==5);
		
		
		//step 8 call the scheduler to give change
		assertTrue("Cashier scheduler should have returned true",cashier.pickAndExecuteAnAction());
		
		//check post for 8 and pre for 9
		assertTrue("Cashier log should indicate giving change. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Giving mockcustomer change"));
		assertTrue("Cashier should be finished with the debt.",cashier.debts.get(0).s==CashierRole.DebtState.paid);
		assertTrue("Customer log should show the change",customer.log.getLastLoggedEvent().getMessage().equals("Being given $5 in change"));
		
		
		//step 9 customer comes back one more time, should have a clean slate
		cashier.msgComputeBill(waiter, customer, "Steak");
		cashier.pickAndExecuteAnAction();
		
		//check post for 9
		assertTrue("Customer bill should only be 15",cashier.debts.get(0).amount==15);
		
		
	}
	
	
	/**This test will examine a single customer bill interleaved with a market bill - both normative
	 * 
	 */
	public void testOneCustBillOneMarketBill(){
		//set up for this scenario
		//wipe all of the cashier's data - shouldn't be any
		if(!cashier.debts.isEmpty()){
			cashier.debts = new ArrayList<CashierRole.Debt>();
		}
		if(!cashier.marketBills.isEmpty()){
			cashier.marketBills = new ArrayList<CashierRole.MarketBill>();
		}
		
		
		//check preconditions
		assertTrue("Cashier shouldn't have any cust bills. He does.",cashier.debts.isEmpty());
		assertTrue("Cashier shouldn't have any market bills. He does.",cashier.marketBills.isEmpty());
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		
		
		//step 1 - send message to compute bill
		cashier.msgComputeBill(waiter, customer, "Pizza");
		
		//check post of 1 and pre of 2
		assertTrue("Cashiser should have a bill in the list",!cashier.debts.isEmpty());
		assertTrue("Cashier's first debt should be for $12",cashier.debts.get(0).amount==12);
		assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
		assertTrue("Waiter should have an empty log. He doesn't",waiter.log.isEmpty());
		assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
		//assertTrue("Cashier state should have another free permit",cashier.stateChange.availablePermits()==2);
		
		
		//step 2 - send message that a market bill is owed
		cashier.setMoney(1000);
		cashier.msgHereIsInvoice(deliveryMan1, new MarketInvoice(null,null,null,100));
		//cashier.msgDeliveryBill(market1, 100);
		
		//check post of 2 and pre of 3
//		assertTrue("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
//				+ market1.log.toString(), 0==market1.log.size());

		assertTrue("Cashier marketBills should have 1 bill in it. It doesn't.", cashier.marketBills.size()==1);
		assertTrue("Bill was not added to Cashier correctly",cashier.marketBills.get(0).i.total == 100);
		assertTrue("Cashier still should have an empty log. He doesn't",cashier.log.isEmpty());
		//assertTrue("Cashier should have another free permit",cashier.stateChange.availablePermits()==3);
		
		
		//step 3 - call scheduler to (hopefully) first handle customer bill
		assertTrue(cashier.pickAndExecuteAnAction());
		
		//check post of 3 and pre of 4
		assertTrue("Cashier log should indicate computing bill. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals(
						"Computing bill for waiter mockwaiter and customer mockcustomer"));
		assertTrue("Debt amount for pizza should be 12.",cashier.debts.get(0).amount==12);
		assertTrue("Debt should be in the unpaid state.",cashier.debts.get(0).s==CashierRole.DebtState.unpaid);
		
		assertTrue("Waiter log should read \"Recieved a check for customer customer for $12\". Incorrectly reads: "
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.getLastLoggedEvent().getMessage().equals("Recieved a check for customer mockcustomer for $12"));
		
		
		//step 4 - send message that customer is paying
		cashier.msgPayment(new Check(customer,12), 20);
		
		//check post of 4 and pre of 5
		assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
		assertTrue("Debt should have a change field of 8.",cashier.debts.get(0).change==8);
		assertTrue("Debt should be in the needsChange state.",cashier.debts.get(0).s==CashierRole.DebtState.needsChange);
		//assertTrue("Cashier state should have a new permit",cashier.stateChange.availablePermits()==4);
		
		
		//step 5 - call scheduler to (hopefully) give change
		assertTrue("Scheduler should have returned true",cashier.pickAndExecuteAnAction());
		
		//check post of 5 and pre of 6
		assertTrue("Cashier log should indicate giving change. Incorrecty reads: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Giving mockcustomer change"));
		assertTrue("Cashier should be finished with the debt.",cashier.debts.get(0).s==CashierRole.DebtState.paid);
		assertTrue("Customer log should show the change",customer.log.getLastLoggedEvent().getMessage().equals("Being given $8 in change"));
		//assertTrue("market1's log should still be empty",market1.log.isEmpty());
		
		
		//step 6 - call scheduler to pay market bill
		assertTrue("Cashier scheduler should have returned true",cashier.pickAndExecuteAnAction());
		
		//check post of 6
//		assertTrue("Cashier log should indicate picking the pay bill action. Incorrecty reads: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Attempting to pay market1 "
//						+ "a bill of $100"));
//		assertTrue("Market log should read \"Recieved a payment of $100\". Incorrectly reads: "
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.getLastLoggedEvent().getMessage().equals("Recieved a payment of $100"));
		
		//assertTrue("Cashier should have only $900. He doesn't",cashier.getMoney()==900);
		assertTrue("Cashier should have removed bill. He didn't",cashier.marketBills.isEmpty());
		
		
	}
	
	/**This test will examine a two customer scenario interleaved with a market bill - one cust
	 * will be normative, other non-normative
	 */
	public void testTwoCustBillOneMarketBill(){
		//set up for this scenario
				//wipe all of the cashier's data - shouldn't be any
				if(!cashier.debts.isEmpty()){
					cashier.debts = new ArrayList<CashierRole.Debt>();
				}
				if(!cashier.marketBills.isEmpty()){
					cashier.marketBills = new ArrayList<CashierRole.MarketBill>();
				}
				
				
				//check preconditions
				assertTrue("Cashier shouldn't have any cust bills. He does.",cashier.debts.isEmpty());
				assertTrue("Cashier shouldn't have any market bills. He does.",cashier.marketBills.isEmpty());
				assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
				
				
				//step 1 - send message to compute bill
				cashier.msgComputeBill(waiter, customer, "Chicken");
				
				//check post of 1 and pre of 2
				assertTrue("Cashiser should have a bill in the list",!cashier.debts.isEmpty());
				assertTrue("Cashier's first debt should be for $10",cashier.debts.get(0).amount==10);
				assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
				assertTrue("Waiter should have an empty log. He doesn't",waiter.log.isEmpty());
				assertTrue("Customer1 should have an empty log. He doesn't",customer.log.isEmpty());
				assertTrue("Customer2 should have an empty log. He doesn't",customer2.log.isEmpty());
				//assertTrue("Cashier state should have another free permit",cashier.stateChange.availablePermits()==2);
				
				
				//step 2 - send message that a market bill is owed
				cashier.setMoney(1000);
				cashier.msgHereIsInvoice(deliveryMan1, new MarketInvoice(null,null,null,100));
				//cashier.msgDeliveryBill(market1, 100);
				
				//check post of 2 and pre of 3
//				assertTrue("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
//						+ market1.log.toString(), 0==market1.log.size());

				assertTrue("Cashier marketBills should have 1 bill in it. It doesn't.", cashier.marketBills.size()==1);
				assertTrue("Bill was not added to Cashier correctly",cashier.marketBills.get(0).i.total == 100);
				assertTrue("Cashier still should have an empty log. He doesn't",cashier.log.isEmpty());
				//assertTrue("Cashier should have another free permit",cashier.stateChange.availablePermits()==3);
				
				
				//step 3 - send message to compute a second customer bill
				cashier.msgComputeBill(waiter, customer2, "Salad");
				
				//check post of 3 and pre of 4
				assertTrue("Cashiser should have a bill in the list",!cashier.debts.isEmpty());
				assertTrue("Cashier's second debt should be for $5",cashier.debts.get(1).amount==5);
				assertTrue("Cashier should have an empty log. He doesn't",cashier.log.isEmpty());
				assertTrue("Waiter should have an empty log. He doesn't",waiter.log.isEmpty());
				assertTrue("Customer1 should have an empty log. He doesn't",customer.log.isEmpty());
				assertTrue("Customer2 should have an empty log. He doesn't",customer2.log.isEmpty());
				//assertTrue("Cashier state should have another free permit",cashier.stateChange.availablePermits()==4);
				assertTrue("Cashier debt list should have 2 debts, doesn't",cashier.debts.size()==2);
				
				
				//step 4 - call scheduler to (hopefully) first compute customer1 bill
				assertTrue(cashier.pickAndExecuteAnAction());
				
				//check post of 4 and pre of 5
				assertTrue("Cashier log should indicate computing bill. Incorrecty reads: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals(
								"Computing bill for waiter mockwaiter and customer mockcustomer"));
				assertTrue("Debt1 amount for chicken should be 10.",cashier.debts.get(0).amount==10);
				assertTrue("Debt1 should be in the unpaid state.",cashier.debts.get(0).s==CashierRole.DebtState.unpaid);
				
				assertTrue("Waiter log should read \"Recieved a check for customer mockcustomer for $10\". Incorrectly reads: "
						+ waiter.log.getLastLoggedEvent().toString(), waiter.log.getLastLoggedEvent().getMessage().equals("Recieved a check for customer mockcustomer for $10"));
				
				
				//step 5 - send message that customer1 is paying
				cashier.msgPayment(new Check(customer,10), 20);
				
				//check post of 5 and pre of 6
				assertTrue("Customer should have an empty log. He doesn't",customer.log.isEmpty());
				assertTrue("Debt1 should have a change field of 10.",cashier.debts.get(0).change==10);
				assertTrue("Debt1 should be in the needsChange state.",cashier.debts.get(0).s==CashierRole.DebtState.needsChange);
				//assertTrue("Cashier state should have a new permit",cashier.stateChange.availablePermits()==5);
				
				
				//step 6 - call scheduler to (hopefully) compute second bill
				assertTrue("Scheduler should have returned true",cashier.pickAndExecuteAnAction());
				
				//check post of 6 and pre of 7
				assertTrue("Cashier log should indicate computing bill2. Incorrecty reads: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals(
								"Computing bill for waiter mockwaiter and customer mockcustomer2"));
				assertTrue("Debt2 amount for chicken should be 5.",cashier.debts.get(1).amount==5);
				assertTrue("Debt2 should be in the unpaid state.",cashier.debts.get(1).s==CashierRole.DebtState.unpaid);
				
				assertTrue("Waiter log should read \"Recieved a check for customer mockcustomer2 for $5\". Incorrectly reads: "
						+ waiter.log.getLastLoggedEvent().toString(), waiter.log.getLastLoggedEvent().getMessage().equals("Recieved a check for customer mockcustomer2 for $5"));
				assertTrue("Customer1 log should still be empty",customer.log.isEmpty());
				
				
				//step 7 - call scheduler to (hopefully) give change
				assertTrue("Scheduler should have returned true",cashier.pickAndExecuteAnAction());
				
				//check post of 7 and pre of 8
				assertTrue("Cashier log should indicate giving change. Incorrecty reads: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Giving mockcustomer change"));
				assertTrue("Cashier should be finished with the debt.",cashier.debts.get(0).s==CashierRole.DebtState.paid);
				assertTrue("Customer log should show the change",customer.log.getLastLoggedEvent().getMessage().equals("Being given $10 in change"));
				//assertTrue("market1's log should still be empty",market1.log.isEmpty());
				
				
				//step 8 - call scheduler to pay market bill
				assertTrue("Cashier scheduler should have returned true",cashier.pickAndExecuteAnAction());
				
				//check post of 8 and pre of 9
//				assertTrue("Cashier log should indicate picking the pay bill action. Incorrecty reads: " 
//						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Attempting to pay market1 "
//								+ "a bill of $100"));
//				assertTrue("Market log should read \"Recieved a payment of $100\". Incorrectly reads: "
//						+ market1.log.getLastLoggedEvent().toString(), market1.log.getLastLoggedEvent().getMessage().equals("Recieved a payment of $100"));
//				
				//assertTrue("Cashier should have only $900. He doesn't",cashier.getMoney()==900);
				assertTrue("Cashier should have removed bill. He didn't",cashier.marketBills.isEmpty());
				assertTrue("Customer2 log should still be empty",customer2.log.isEmpty());
				assertTrue("Cashier debtlist should have size 2",cashier.debts.size()==2);
				
				
				//step 9 - send a message that customer 2 can't pay
				cashier.msgICantPay(new Check(customer2,5), 0);
				
				//check post of 9 and pre of 10
				assertTrue("Customer2 should have an empty log. He doesn't",customer2.log.isEmpty());
				assertTrue("Debt2 should have a change field of 0.",cashier.debts.get(1).change==0);
				assertTrue("Debt2 should be in the cantPay state.",cashier.debts.get(1).s==CashierRole.DebtState.cantPay);
				//assertTrue("Cashier state should have a new permit",cashier.stateChange.availablePermits()==6);
				
				
				//step 10
				assertTrue("Cashier scheduler should return true.",cashier.pickAndExecuteAnAction());
				
				//check post of 10 and pre of 11
				assertTrue("Cashier log should indicate letting go. Incorrecty reads: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.getLastLoggedEvent().getMessage().equals("Letting mockcustomer2 pay next time"));
				assertTrue("Cashier should debt in delinquent state.",cashier.debts.get(1).s==CashierRole.DebtState.delinquent);
				assertTrue("Customer log should show the change",customer2.log.getLastLoggedEvent().getMessage().equals("I'll pay next time"));
				
				
				
				
				
	}
	
	
	
	/*public void testPayment(){
		
		Check c = new Check(customer,10);
		
		CashierAgent.Debt d = new CashierAgent.Debt();
		d.c = c.c;
		d.s = CashierAgent.DebtState.unpaid;
		
		cashier.debts.add(d);
		
		cashier.msgPayment(c, 10);
		
		CashierAgent.Debt b = cashier.findDebt(c.c);
		
		
		assertEquals("Debt stat was not set to needsChange",b.s,CashierAgent.DebtState.needsChange);
		
		assertEquals("findDebt method must not be finding right debt",d,b);
		
		
	}*/
	
	/*public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		Bill bill = new Bill(cashier, customer, 2, 7.98);
		cashier.HereIsBill(bill);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//step 2 of the test
		cashier.ReadyToPay(customer, bill);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.customerApproached);
		
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received ReadyToPay"));

		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
				+ cashier.bills.get(0).bill.netCost, cashier.bills.get(0).bill.netCost == 7.98);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).bill.customer == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
	
			
		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
		
		
		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.bills.get(0).changeDue, cashier.bills.get(0).changeDue == 0);
		
		
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.done);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario */
	
	
}
