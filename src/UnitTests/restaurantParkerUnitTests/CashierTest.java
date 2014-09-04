//package UnitTests.restaurantParkerUnitTests;
//
////automatically generated
//import static org.junit.Assert.*;
//import org.junit.Test;
////^^^^^^^^^^^^^^^^^^^^^^^
//
//import UnitTests.mock.restaurantParkerMock.*;
//
//import restaurant.restaurantParker.CashierRole;
//import restaurant.restaurantParker.*;
//import junit.framework.*;
//
//
//public class CashierTest extends TestCase{
//	
//	
//	CashierRole cashier;
//	MockWaiter waiter;
//	MockCustomer customer1;
//	MockCustomer customer2;
//	MockMarket market1;
//	MockMarket market2;
//	RestaurantParker restaurant;
//	
//	
//	public void setUp() throws Exception{
//		super.setUp();	
//		restaurant = new RestaurantParker();
//		cashier = new CashierRole("cashier", restaurant);		
//		customer1 = new MockCustomer("mockCustomer1");	
//		customer2 = new MockCustomer("mockCustomer2");
//		waiter = new MockWaiter("mockWaiter");
//		market1 = new MockMarket("mockMarket1");
//		market2 = new MockMarket("mockMarket2");
//		
//		
//		customer1.cashier = cashier;
//		customer2.cashier = cashier;
//		waiter.cashier = cashier;
//		market1.cashier = cashier;
//		market2.cashier = cashier;
//		
//	}	
//
//	
//	@Test
//	public void test1OneMarketPayment() {
//		
//		//preconditions
//		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",0, cashier.marketBills.size());	
//		assertEquals("Cashier's event log should be empty, but it is not.", 0, cashier.log.size());
//		
//		//Step 1:  send bill to cashier from market
//		cashier.msgHereIsMarketBill(40, market1);
//		assertEquals("Cashier should have logged \"Received msgHereIsMarketBill from mockMarket1\". Instead it reads: " + cashier.log.toString(),
//					true, cashier.log.containsString("Received msgHereIsMarketBill from " + market1.getName()));
//		assertEquals("Market1's event log should be empty, but it is not.", 0, market1.log.size());
//		assertEquals("Cashier should have 1 bill in marketBills, but it does not.", 1, cashier.marketBills.size());
//		
//		//Step 2:  call the scheduler and make sure it returns true
//		assertTrue("Scheduler should return true, but it didn't",  cashier.pickAndExecuteAnAction());
//		assertTrue("Cashier's event log should have paying event but doesn't", cashier.log.containsString("I am paying a bill to " + market1.getName()));
//		assertEquals("Market should have received message and have event in eventLog", true, market1.log.containsString("Received payment."));
//		assertTrue("Cashier's marketBills list should be empty, but isn't", cashier.marketBills.size() ==0 );
//		
//		//Step 3:  call the scheduler and make sure it returns false
//		assertFalse("Cashier's scheduler should return false, but it didn't.", cashier.pickAndExecuteAnAction());
//		
//	}
//	
//	public void test2TwoMarketPayments(){
//		
//		//preconditions
//		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",0, cashier.marketBills.size());	
//		assertEquals("Cashier's event log should be empty, but it is not.", 0, cashier.log.size());
//		
//		//Step 1:  send first bill to cashier from market1
//				cashier.msgHereIsMarketBill(30, market1);
//				assertEquals("Cashier should have logged \"Received msgHereIsMarketBill from mockMarket1\". Instead it reads: " + cashier.log.toString(),
//							true, cashier.log.containsString("Received msgHereIsMarketBill from " + market1.getName()));
//				assertEquals("Market1's event log should be empty, but it is not.", 0, market1.log.size());
//				assertEquals("Cashier should have 1 bill in marketBills, but it does not.", 1, cashier.marketBills.size());
//	
//		//Step 2:  send second bill to cashier from market2
//				cashier.msgHereIsMarketBill(20, market2);
//				assertEquals("Cashier should have logged \"Received msgHereIsMarketBill from mockMarket2\". Instead it reads: " + cashier.log.toString(),
//							true, cashier.log.containsString("Received msgHereIsMarketBill from " + market2.getName()));
//				assertEquals("Market2's event log should be empty, but it is not.", 0, market2.log.size());
//				assertEquals("Cashier should have 2 bill in marketBills, but it does not.", 2, cashier.marketBills.size());
//	
//		//Step 3:  call the scheduler and make sure it returns true
//				assertTrue("Scheduler should return true, but it didn't",  cashier.pickAndExecuteAnAction());
//				assertTrue("Cashier's event log should have paying event but doesn't", cashier.log.containsString("I am paying a bill to " + market1.getName()));
//				assertEquals("Market should have received message and have event in eventLog", true, market1.log.containsString("Received payment."));
//				assertTrue("Cashier's marketBills list should have only 1 bill in it, but doesn't.", cashier.marketBills.size() ==1 );
//				
//		//Step 4:  call the scheduler again and make sure it returns true
//				assertTrue("Cashier's scheduler should return true, but it didn't.", cashier.pickAndExecuteAnAction());
//				assertTrue("Cashier's event log should have paying event but doesn't", cashier.log.containsString("I am paying a bill to " + market2.getName()));
//				assertEquals("Market should have received message and have event in eventLog", true, market2.log.containsString("Received payment."));
//				assertTrue("Cashier's marketBills list should have 0 bills in it, but doesn't.", cashier.marketBills.size() ==0 );
//				
//		//Step 5:  call the scheduler again and make sure it returns false
//				assertFalse("Cashier's scheduler should return false, but it didn't.", cashier.pickAndExecuteAnAction());
//	}
//	
//	
//	public void test3OneCustomerPays(){
//		//preconditions
//				assertEquals("Cashier should have 0 bills in it. It doesn't.",0, cashier.bills.size());
//				assertEquals("Cashier should have 0 payments. It doesn't.", 0, cashier.payments.size());
//				assertEquals("Cashier's event log should be empty, but it is not.", 0, cashier.log.size());
//				
//		//Step 1:  send message from waiter to cashier
//				cashier.msgComputeBill("Steak", customer1, waiter);
//				assertTrue("Cashier should have logged \"Received msgComputeBill from mockWaiter\". Instead it reads: " + cashier.log.toString(),
//						cashier.log.containsString("Received msgComputeBill from " + waiter.getName()));
//				assertEquals("Waiter's event log should be empty, but it is not.", 0, waiter.log.size());
//				assertEquals("Cashier's bills list should have one bill", 1, cashier.bills.size());
//		
//		//Step 2:  run the scheduler and check to see it returns true
//				assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//				assertTrue("Cashier's log should have event for calculating bill for Customer", cashier.log.containsString("Computing a bill for " + customer1.getName()));
//				assertTrue("Waiter's log should have shown it has received a check of 40 for mockCustomer1. Instead it reads: " + waiter.log.toString(), waiter.log.containsString("I was given a check of 40 for " + customer1.getName()));
//				assertEquals("Waiter's check should be 40", 40, waiter.check);
//				assertEquals("Cashier should no longer have any bills", 0, cashier.bills.size());
//				
//		//Step 3:  run scheduler, check for false
//				assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
//				
//		//Step 4:  send payment message from customer to cashier
//				cashier.msgPayment(40, 47, customer1);
//				assertTrue("Cashier should have logged \"Received msgPayment from mockCustomer1\". Instead it reads: " + cashier.log.toString(),
//						cashier.log.containsString("Received msgPayment from " + customer1.getName()));
//				assertEquals("Customer should have an empty log.", 0, customer1.log.size());
//				assertEquals("Cashier should have one payment in its list.", 1, cashier.payments.size());
//		
//		//Step 5:  run scheduler, check for true
//				assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
//				assertTrue("Cashier's log should have \"Computing change.\"", cashier.log.containsString("Computing change."));
//				assertEquals("Change computed by cashier should equal 7", 7, cashier.change);
//				assertTrue("Customer's log should say that he received change of 7", customer1.log.containsString("Received change of $7"));
//				assertEquals("Cashier should now have 0 payments", 0, cashier.payments.size());
//		
//		//Step 6:  run scheduler, check for false
//				assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());			
//	}
//	
//	public void test4OneCustomerOneMarket(){
//		//preconditions
//			assertEquals("Cashier should have 0 bills in it. It doesn't.",0, cashier.bills.size());
//			assertEquals("Cashier should have 0 payments. It doesn't.", 0, cashier.payments.size());
//			assertEquals("Cashier should have 0 marketBills", 0, cashier.marketBills.size());
//			assertEquals("Cashier's event log should be empty, but it is not.", 0, cashier.log.size());
//			
//		//Step 1:  send message from waiter to cashier
//			cashier.msgComputeBill("Steak", customer1, waiter);
//			assertTrue("Cashier should have logged \"Received msgComputeBill from mockWaiter\". Instead it reads: " + cashier.log.toString(),
//				cashier.log.containsString("Received msgComputeBill from " + waiter.getName()));
//			assertEquals("Waiter's event log should be empty, but it is not.", 0, waiter.log.size());
//			assertEquals("Cashier's bills list should have one bill", 1, cashier.bills.size());
//			
//		//Step 2:  send bill to cashier from market
//			cashier.msgHereIsMarketBill(40, market1);
//			assertEquals("Cashier should have logged \"Received msgHereIsMarketBill from mockMarket1\". Instead it reads: " + cashier.log.toString(),
//						true, cashier.log.containsString("Received msgHereIsMarketBill from " + market1.getName()));
//			assertEquals("Market1's event log should be empty, but it is not.", 0, market1.log.size());
//			assertEquals("Cashier should have 1 bill in marketBills, but it does not.", 1, cashier.marketBills.size());
//			
//		//Step 3:  run scheduler, check for true, check for bill computation
//			assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should have event for calculating bill for Customer", cashier.log.containsString("Computing a bill for " + customer1.getName()));
//			assertTrue("Waiter's log should have shown it has received a check of 40 for mockCustomer1. Instead it reads: " + waiter.log.toString(), waiter.log.containsString("I was given a check of 40 for " + customer1.getName()));
//			assertEquals("Waiter's check should be 40", 40, waiter.check);
//			assertEquals("Cashier should no longer have any bills", 0, cashier.bills.size());
//			
//		//Step 4:  run scheduler, check for true, check for marketBill payment
//			assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's event log should have paying event but doesn't", cashier.log.containsString("I am paying a bill to " + market1.getName()));
//			assertEquals("Market should have received message and have event in eventLog", true, market1.log.containsString("Received payment."));
//			assertTrue("Cashier's marketBills list should be empty, but isn't", cashier.marketBills.size() ==0 );
//			
//		//Step 5:  run scheduler, check for false
//			assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
//			
//		//Step 6:  message cashier from customer
//			cashier.msgPayment(40, 47, customer1);
//			assertTrue("Cashier should have logged \"Received msgPayment from mockCustomer1\". Instead it reads: " + cashier.log.toString(),
//					cashier.log.containsString("Received msgPayment from " + customer1.getName()));
//			assertEquals("Customer should have an empty log.", 0, customer1.log.size());
//			assertEquals("Cashier should have one payment in its list.", 1, cashier.payments.size());
//	
//		//Step 7:  run scheduler, check for true
//			assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should have \"Computing change.\"", cashier.log.containsString("Computing change."));
//			assertEquals("Change computed by cashier should equal 7", 7, cashier.change);
//			assertTrue("Customer's log should say that he received change of 7", customer1.log.containsString("Received change of $7"));
//			assertEquals("Cashier should now have 0 payments", 0, cashier.payments.size());
//	
//		//Step 8:  run scheduler, check for false
//			assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());			
//	}
//	
//	public void test5OneCustomerCantPay(){
//		//preconditions
//			assertEquals("Cashier should have 0 bills in it. It doesn't.",0, cashier.bills.size());
//			assertEquals("Cashier should have 0 payments. It doesn't.", 0, cashier.payments.size());
//			assertEquals("Cashier's event log should be empty, but it is not.", 0, cashier.log.size());
//		
//		//Step 1:  send message from waiter to cashier
//			cashier.msgComputeBill("Steak", customer1, waiter);
//			assertTrue("Cashier should have logged \"Received msgComputeBill from mockWaiter1\". Instead it reads: " + cashier.log.toString(),
//				cashier.log.containsString("Received msgComputeBill from " + waiter.getName()));
//			assertEquals("Waiter's event log should be empty, but it is not.", 0, waiter.log.size());
//			assertEquals("Cashier's bills list should have one bill", 1, cashier.bills.size());
//
//		//Step 2:  run the scheduler and check to see it returns true
//			assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should have event for calculating bill for Customer", cashier.log.containsString("Computing a bill for " + customer1.getName()));
//			assertTrue("Waiter's log should have shown it has received a check of 40 for mockCustomer1. Instead it reads: " + waiter.log.toString(), waiter.log.containsString("I was given a check of 40 for " + customer1.getName()));
//			assertEquals("Waiter's check should be 40", 40, waiter.check);
//			assertEquals("Cashier should no longer have any bills", 0, cashier.bills.size());
//		
//		//Step 3:  run scheduler, check for false
//			assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
//			
//		//Step 4:  send IHaveNoMoney message from customer to cashier
//			cashier.msgIHaveNoMoney(40, customer1);
//			assertTrue("Cashier should have logged \"Received msgIHaveNoMoney from mockCustomer1\". Instead it reads: " + cashier.log.toString(),
//					cashier.log.containsString("Received msgIHaveNoMoney from " + customer1.getName()));
//			assertEquals("Customer should have an empty log.", 0, customer1.log.size());
//			assertEquals("Cashier should have one debtCustomer in its list.", 1, cashier.debtCustomers.size());
//	
//		//Step 5:  run scheduler, check for true
//			assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should say it it disciplining mockCustomer1", cashier.log.containsString("Disciplining " + customer1.getName()));
//			assertTrue("Customer's log should say it received change of 1000 (a notification that he can pay later.", customer1.log.containsString("Received change of $1000"));
//			assertEquals("Cashier should still have one debtCustomer", 1, cashier.debtCustomers.size());
//			
//		//Step 6:  run scheduler, check for false
//			assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());			
//			
//	}
//	
//	public void test6OneCustomerPaysOneMarketOneCustomerCantPay(){
//		//preconditions
//			assertEquals("Cashier should have 0 bills in it. It doesn't.",0, cashier.bills.size());
//			assertEquals("Cashier should have 0 payments. It doesn't.", 0, cashier.payments.size());
//			assertEquals("Cashier should have 0 marketBills", 0, cashier.marketBills.size());
//			assertEquals("Cashier should ahve 0 debtCustomers", 0, cashier.debtCustomers.size());
//			assertEquals("Cashier's event log should be empty, but it is not.", 0, cashier.log.size());
//		
//		//Step 1:  send two messages from waiter to customer
//			cashier.msgComputeBill("Steak", customer1, waiter);
//			cashier.msgComputeBill("Pizza", customer2, waiter);
//			assertTrue("Cashier should have logged \"Received msgComputeBill from mockWaiter1\". Instead it reads: " + cashier.log.toString(),
//					cashier.log.containsString("Received msgComputeBill from " + waiter.getName()));
//			assertEquals("Waiter's event log should be empty, but it is not.", 0, waiter.log.size());
//			assertEquals("Cashier's bills list should have one bill", 2, cashier.bills.size());
//			
//		//Step 2:  run scheduler, check for true, check for compute first bill
//			assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should have event for calculating bill for Customer1", cashier.log.containsString("Computing a bill for " + customer1.getName()));
//			assertTrue("Waiter's log should have shown it has received a check of 40 for mockCustomer1. Instead it reads: " + waiter.log.toString(), waiter.log.containsString("I was given a check of 40 for " + customer1.getName()));
//			assertEquals("Waiter's check should be 40", 40, waiter.check);
//			assertEquals("Cashier should now have 1 bills", 1, cashier.bills.size());
//			
//		//Step 3: run scheduler, check for true, check for compute second bill
//			assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should have event for calculating bill for Customer2", cashier.log.containsString("Computing a bill for " + customer2.getName()));
//			assertTrue("Waiter's log should have shown it has received a check of 20 for mockCustomer2. Instead it reads: " + waiter.log.toString(), waiter.log.containsString("I was given a check of 20 for " + customer2.getName()));
//			assertEquals("Waiter's check should be 20", 20, waiter.check);
//			assertEquals("Cashier should now have 0 bills", 0, cashier.bills.size());
//			
//		//Step 4: send bill to cashier from market
//			cashier.msgHereIsMarketBill(40, market1);
//			assertEquals("Cashier should have logged \"Received msgHereIsMarketBill from mockMarket1\". Instead it reads: " + cashier.log.toString(),
//						true, cashier.log.containsString("Received msgHereIsMarketBill from " + market1.getName()));
//			assertEquals("Market1's event log should be empty, but it is not.", 0, market1.log.size());
//			assertEquals("Cashier should have 1 bill in marketBills, but it does not.", 1, cashier.marketBills.size());
//			
//		//Step 5:  run scheduler, check true, check for market payment
//			assertTrue("Cashier's scheduler should return true but it didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's event log should have paying event but doesn't", cashier.log.containsString("I am paying a bill to " + market1.getName()));
//			assertEquals("Market should have received message and have event in eventLog", true, market1.log.containsString("Received payment."));
//			assertTrue("Cashier's marketBills list should be empty, but isn't", cashier.marketBills.size() ==0 );
//			
//		//Step 6:  run scheduler, check false
//			assertFalse("Cashier's scheduler should return false.", cashier.pickAndExecuteAnAction());
//			
//		//Step 7:  send 2 messages from 2 different customers for payment/cant pay
//			cashier.msgPayment(40, 47, customer1);
//			cashier.msgIHaveNoMoney(30, customer2);
//			
//			assertTrue("Cashier should have logged \"Received msgIHaveNoMoney from mockCustomer2\". Instead it reads: " + cashier.log.toString(),
//					cashier.log.containsString("Received msgIHaveNoMoney from " + customer2.getName()));
//			assertEquals("Customer should have an empty log.", 0, customer2.log.size());
//			assertEquals("Cashier should have one debtCustomer in its list.", 1, cashier.debtCustomers.size());
//			
//			assertTrue("Cashier should have logged \"Received msgPayment from mockCustomer1\". Instead it reads: " + cashier.log.toString(),
//					cashier.log.containsString("Received msgPayment from " + customer1.getName()));
//			assertEquals("Customer should have an empty log.", 0, customer1.log.size());
//			assertEquals("Cashier should have one payment in its list.", 1, cashier.payments.size());
//			
//		//Step 8:  run scheduler, check true, check he does payment c1
//			assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should have \"Computing change.\"", cashier.log.containsString("Computing change."));
//			assertEquals("Change computed by cashier should equal 7", 7, cashier.change);
//			assertTrue("Customer's log should say that he received change of 7", customer1.log.containsString("Received change of $7"));
//			assertEquals("Cashier should now have 0 payments", 0, cashier.payments.size());
//			
//		//Step 9:  run scheduler, check true, check he does disciplining c2
//			assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's log should say it it disciplining mockCustomer2", cashier.log.containsString("Disciplining " + customer2.getName()));
//			assertTrue("Customer's log should say it received change of 1000 (a notification that he can pay later.", customer2.log.containsString("Received change of $1000"));
//			assertEquals("Cashier should still have one debtCustomer", 1, cashier.debtCustomers.size());
//			
//		//Step 10:  run scheduler, check for false
//			assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());			
//	}
//
//}
