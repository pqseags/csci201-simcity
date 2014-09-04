package UnitTests.restaurantLindaUnitTests;

import interfaces.MarketDeliveryMan;
import interfaces.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cityGui.CityRestaurant;

import person.PersonAgent;

import market.Market;
import market.MarketInvoice;
import market.OrderItem;

import restaurant.Restaurant;
import restaurant.restaurantLinda.CashierRole;
import restaurant.restaurantLinda.Check;
import restaurant.restaurantLinda.CashierRole.MyBillState;
import util.CityMap;

import UnitTests.mock.MockPerson;
import UnitTests.mock.MarketMock.MockMarketDeliveryMan;
import UnitTests.mock.MarketMock.MockMarketHost;
import UnitTests.mock.restaurantLindaMock.*;

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
		Market market;
		MockRestaurantLinda restaurant;
		
        //these are instantiated for each test separately via the setUp() method.
		CashierRole cashier;
        MockWaiter waiter;
        MockCustomer customer;
        
        MockMarketHost marketHost;
        MockMarketDeliveryMan deliveryMan;
        
        MockPerson person;
        
        
        /**
         * This method is run before each test. You can use it to instantiate the class variables
         * for your agent and mocks, etc.
         */
        public void setUp() throws Exception{
                super.setUp();
                
                restaurant = new MockRestaurantLinda();
                cashier = new CashierRole("cashier", restaurant);                
                customer = new MockCustomer("customer");                
                waiter = new MockWaiter("waiter");
                market = new Market();
                marketHost = new MockMarketHost("MarketHost");
                deliveryMan = new MockMarketDeliveryMan("deliveryMan");
                person = new MockPerson("dummyPerson");
                
                cashier.setRestaurant(restaurant);
                cashier.setPerson(person);
                
        }
        
        /**
         * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
         */
        public void testOneCustomerNormalScenario(){                             
                //check preconditions              
                assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                                                + cashier.log.toString(), 0, cashier.log.size());
                
                assertEquals("CashierAgent should have an empty bills list before the PleaseComputebill is called. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 0);
                
                assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
                
                assertEquals("Cashier's should have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
                
                assertEquals("Cashier should have 0 cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 0);
                
                //step 1 of the test
                cashier.msgPleaseComputeBill(waiter, "Steak", customer);//send the message from a waiter

                //check postconditions for step 1 and preconditions for step 2
                assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                                + waiter.log.toString(), 0, waiter.log.size());
                
                assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
                
                CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer);
                
                assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
                
                assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
                
                assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
                			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
                
                cashier.msgTimerDone(cashier.getBills().get(0));
                
                assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
                			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is $16"));
                
                assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
                
                assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
                			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is $16"));
                
                assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
                			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is $16"));
                
                assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
                
                assertEquals("Cashier's should still have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
                
                
                
                
                //step 2 of the test
                Check bill = new Check("Steak", 16);
                cashier.msgHereIsPayment(customer, bill, 16);
                
                //check postconditions for step 2 / preconditions for step 3
               
                assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of $16 from customer " + customer.getName() + " for the bill " + bill));

                assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                                + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
                
                CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 16);
                assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
                
                
                
                //step 3
                //NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
                assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                        cashier.pickAndExecuteAnAction());
                
                assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
                			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
                
                //check postconditions for step 3 / preconditions for step 4
                assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe 0"));
                
                assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
                
                assertEquals("Cashier's debtor list should still be size 0. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
                
                assertEquals("Cashier should now have 16 in cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 16);
                
                assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                                cashier.pickAndExecuteAnAction());
                
        
        }//end one normal customer scenario
        
        /**
         *  This tests the cashier's handling when one customer only partially pays a bill. The customer does not return for another meal
         */
        public void testOneCustomerShort(){
        	cashier.setCash(0);
        	
        	//check preconditions              
            assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());
            
            assertEquals("CashierAgent should have an empty bills list before the PleaseComputebill is called. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 0);
            
            assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("Cashier should have 0 cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 0);
            
            //step 1 of the test
            cashier.msgPleaseComputeBill(waiter, "Steak", customer);//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());
            
            assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
            
            CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer);
            
            assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
            
            assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
            
            cashier.msgTimerDone(cashier.getBills().get(0));
            
            assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is $16"));
            
            assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
            			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is $16"));
            
            assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
            			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is $16"));
            
            assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should still have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            
            
            
            //step 2 of the test
            Check bill = new Check("Steak", 16);
            cashier.msgHereIsPayment(customer, bill, 15);
            
            //check postconditions for step 2 / preconditions for step 3
           
            assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of $15 from customer " + customer.getName() + " for the bill " + bill));

            assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                            + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
            
            CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 15);
            assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
            
            
            
            //step 3
            //NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                    cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
            			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
            
            //check postconditions for step 3 / preconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe " + 1));
            
            assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("Cashier's debtor list should now be size 1. Instead, the list is size " + cashier.getDebtors().size(), cashier.getDebtors().size(), 1);
            
            assertTrue("Cashier's debtor list should contain the customer. It doesn't.", cashier.getDebtors().containsKey(customer));
            
            assertTrue("The customer in the debtors list should owe 1. Instead, he owes " + cashier.getDebtors().get(customer), cashier.getDebtors().get(customer)==1);
            
            assertEquals("Cashier should now have 15 in cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 15);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                            cashier.pickAndExecuteAnAction());
        }
        
        /**
         * This tests the cashier's handling when a customer with an IOU comes back for another meal. The previous debt should be added to the new bill
         * Customer does not pay in full this time, either. 
         */
        public void testOneCustomerShortAgain(){
        	//check preconditions              
            assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());
            
            assertEquals("CashierAgent should have an empty bills list before the PleaseComputebill is called. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 0);
            
            assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            cashier.addDebtor(customer, 1);
            
            assertEquals("Cashier's debtor list should be size 1. Instead, the list is size " + cashier.getDebtors().size(), cashier.getDebtors().size(), 1);
            
            assertTrue("The customer in the debtors list should owe 100. Instead, he owes " + cashier.getDebtors().get(customer), cashier.getDebtors().get(customer)==1);
            
            assertEquals("Cashier should have 0 cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 0);
            
            //Check bill computation
            cashier.msgPleaseComputeBill(waiter, "Steak", customer);
            assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());
            
            assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
            
            CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer); 
            assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
            
            assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
            
            cashier.msgTimerDone(cashier.getBills().get(0));
            
            assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is $17"));
            
            //Notify waiter
            assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
            			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is $17"));
            
            assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
            			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is $17"));
            
            assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should now have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            

            //Check customer handling
            Check bill = new Check("Steak", 17);
            cashier.msgHereIsPayment(customer, bill, 15);
           
            assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of $15 from customer " + customer.getName() + " for the bill " + bill));

            assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                            + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
            
            CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 15);
            assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
            
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                    cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
            			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
            
            assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe " + 2));
            
            assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("Cashier's debtor list should now be size 1. Instead, the list is size " + cashier.getDebtors().size(), cashier.getDebtors().size(), 1);
            
            assertTrue("Cashier's debtor list should contain the customer. It doesn't.", cashier.getDebtors().containsKey(customer));
            
            assertTrue("The customer in the debtors list should owe 2. Instead, he owes " + cashier.getDebtors().get(customer), cashier.getDebtors().get(customer)==2);
            
            assertEquals("Cashier should now have 15 in cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 15);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                            cashier.pickAndExecuteAnAction());
        }
        
        /**
         * This tests the cashier's handling of a customer with an IOU but finally pays in full.
         */
        public void testOneCustomerShortPaysLater(){
        	//check preconditions              
            assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());
            
            assertEquals("CashierAgent should have an empty bills list before the PleaseComputebill is called. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 0);
            
            assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            cashier.addDebtor(customer, 1);
            
            assertEquals("Cashier's debtor list should be size 1. Instead, the list is size " + cashier.getDebtors().size(), cashier.getDebtors().size(), 1);
            
            assertTrue("The customer in the debtors list should owe 100. Instead, he owes " + cashier.getDebtors().get(customer), cashier.getDebtors().get(customer)==1);
            
            assertEquals("Cashier should have 0 cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 0);
            
            //Bill computation
            cashier.msgPleaseComputeBill(waiter, "Steak", customer);
            assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());
            
            assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
            
            CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer); 
            assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
            
            assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
            
            cashier.msgTimerDone(cashier.getBills().get(0));
            
            assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is $17"));
            
            //Notify waiter
            assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
            			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is $17"));
            
            assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
            			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is $17"));
            
            assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should now have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            

            //Customer handling
            Check bill = new Check("Steak", 17);
            cashier.msgHereIsPayment(customer, bill, 17);
           
            assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of $17 from customer " + customer.getName() + " for the bill " + bill));

            assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                            + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
            
            CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 17);
            assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
            

            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                    cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
            			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
            
            assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe 0"));
            
            assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("Cashier's debtor list should now be size 0. Instead, the list is size " + cashier.getDebtors().size(), cashier.getDebtors().size(), 0);
            
            assertEquals("Cashier should now have 17 in cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 17);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                            cashier.pickAndExecuteAnAction());
        }
        
        /**
         * This handles the simple customer and simple market scenario in succession. There is no interleaving, and the only thing shared between the scenarios is the money
         */
        public void testOneCustomerNormalScenarioWithOneMarketNormal(){
            
            //check preconditions              
            assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());
            
            assertEquals("CashierAgent should have an empty bills list before the PleaseComputebill is called. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 0);
            
            assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
       	 
       	 	cashier.setCash(40);
       	 	assertEquals("CashierAgent should have 40 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 40);
            
            //step 1 of the test
            cashier.msgPleaseComputeBill(waiter, "Steak", customer);//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());
            
            assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
            
            CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer);
            
            assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
            
            assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
            
            cashier.msgTimerDone(cashier.getBills().get(0));
            
            assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is $16"));
            
            assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
            			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is $16"));
            
            assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
            			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is $16"));
            
            assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should still have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);  
            
            
            //step 2 of the test
            Check bill = new Check("Steak", 16);
            cashier.msgHereIsPayment(customer, bill, 16);
            
            //check postconditions for step 2 / preconditions for step 3
           
            assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of $16 from customer " + customer.getName() + " for the bill " + bill));

            assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                            + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
            
            CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 16);
            assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
            
            
            
            //step 3
            //NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                    cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
            			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
            
            //check postconditions for step 3 / preconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe 0"));
            
            assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("Cashier's debtor list should still be size 0. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("CashierAgent should have 56 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 56);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                            cashier.pickAndExecuteAnAction());
       	 
       	 	
            //Checking preconditions to market scenario now...customer scenario should not have changed anything except cash
            assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
       	 
       	 	assertEquals("CashierAgent should have 56 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 56);
       	 
	       	 //Check message reception	       	 
	       	 List<OrderItem> order = new ArrayList<OrderItem>();
	       	 order.add(new OrderItem("Steak", 3));
	       	 MarketInvoice invoice = new MarketInvoice(order, market, restaurant, 24);
	       	 cashier.msgHereIsInvoice(deliveryMan, invoice);
	       	 
	       	String list = "{";
			for (OrderItem o: invoice.order){
				list+=o.quantityReceived+o.choice+", ";
			}
			list+="}";
	       	 
	       	 assertEquals("MockDeliveryMan should have an empty event log before the Cashier's scheduler is called. Instead, the deliveryMan's event log reads: "
	                    + deliveryMan.log.toString(), 0, deliveryMan.log.size());
	       	 
	       	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
	       	 
	       	 //Check actions
	       	 
	       	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
	       	 
	       	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
	        			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying market $24 for food shipment. Still owe 0. $32 left in cash."));
	       	       
	       	 assertTrue("DeliveryMan should have logged an event for receiving payment. However, the last logged event is "
	       			 + deliveryMan.log.getLastLoggedEvent(), deliveryMan.log.containsString("Received payment of 24 from cashier for order " + list));
	       	 
	       	 assertEquals("Cashier should no longer have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
	       	 
	       	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
	                    cashier.pickAndExecuteAnAction());
            
    
        }//end one normal customer scenario with one normal market
        
        /**
         * This tests the customer and market scenarios, with the market scenario occuring during bill computation
         * The market scenario should have priority
         */
/*        public void testOneCustomerNormalScenarioWithOneMarketNormalInterLeaved(){
            
            //check preconditions              
            assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());
            
            assertEquals("CashierAgent should have an empty bills list before the PleaseComputebill is called. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 0);
            
            assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
       	 
       	 	cashier.setCash(40);
       	 	assertEquals("CashierAgent should have 40 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 40);
            
            //step 1 of the test
            cashier.msgPleaseComputeBill(waiter, "Steak", customer);//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());
            
            assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
            
            CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer);
            
            assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
            
            assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
            
            cashier.msgTimerDone(cashier.getBills().get(0));
            
            assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
            			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is 1599"));
            
            assertFalse("MockWaiter should not have received a message from the cook with the bill information, but he did.", waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is 1599"));
         
            
            //~~~~~~~~~~~~~~~~Entering market scenario~~~~~~~~~~~~~
            
          
            //Checking preconditions to market scenario now...customer scenario should not have changed anything except cash
            assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
       	 
       	 	assertEquals("CashierAgent should have 4000 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 4000);
       	 
	       	 //Check message reception
	       	 Map<String, Integer> order = new HashMap<String, Integer>();
	       	 order.put("Steak", 3);
	       	 
	       	 cashier.msgPleasePay(market1, order, 2400);
	       	 
	       	 assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
	                    + market1.log.toString(), 0, market1.log.size());
	       	 
	       	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
	       	 
	       	 //Check actions
	       	 
	       	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
	       	 
	       	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
	        			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 2400 for food shipment. Still owe 0. 1600 left in cash."));
	       	       
	       	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
	       			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 2400 from cashier " + cashier + " for order " + order));
	       	 
	       	 assertEquals("CashierAgent should have 1600 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 1600);
	       	
	       	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
	       	 
	       	 assertEquals("Cashier's 1 MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
	       	 
	       	 assertEquals("Cashier should still have 1 customer bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
	       	 
	       	 assertFalse("MockWaiter should not have received a message from the cook with the bill information, but he did.", waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is 1599"));
	         
	       	 
	       	 //~~~~~~~~~~~~~~End market scenario~~~~~~~~~~~~~~~~~~
            
            assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
            			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is 1599"));
            
            assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
            			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is 1599"));
            
            assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
            
            assertEquals("Cashier's should still have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);  
            
            
            //step 2 of the test
            Check bill = new Check("Steak", 1599);
            cashier.msgHereIsPayment(customer, bill, 1599);
            
            //check postconditions for step 2 / preconditions for step 3
           
            assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of 1599 from customer " + customer.getName() + " for the bill " + bill));

            assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                            + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
            
            CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 1599);
            assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
            
            
            
            //step 3
            //NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                    cashier.pickAndExecuteAnAction());
            
            assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
            			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
            
            //check postconditions for step 3 / preconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe 0"));
            
            assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("Cashier's debtor list should still be size 0. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
            
            assertEquals("CashierAgent should have 3199 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 3199);
               	 
	       	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
	                    cashier.pickAndExecuteAnAction());
            
    
        }//end one normal customer scenario with one normal market
 */       
        /**
         * Interleaving of the simple customer and delayed-market-payment scenario.
         * The market scenario runs first and ends with the cashier waiting for money
         * Customer scenario is used to refill the cashier's money completely
         * Cashier pays the market after the customer scenario has finished (because the customer gave him enough money)
         */
/*        public void testOneCustomerNormalScenarioWithDelayPaymentOneRefill(){
        //Check preconditions
       	 assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                    + cashier.log.toString(), 0, cashier.log.size());
       	 
       	 assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
        
         assertEquals("Cashier's should have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
       	 
       	 assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
       	 
       	 cashier.setCash(1);
       	 assertEquals("CashierAgent should have 1 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 1);
       	 
       	 //Check message reception
       	 Map<String, Integer> order = new HashMap<String, Integer>();
       	 order.put("Steak", 3);
       	 
       	 cashier.msgPleasePay(market1, order, 1400);
       	 
       	 assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                    + market1.log.toString(), 0, market1.log.size());
       	 
       	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
       	 
       	 //Check actions       	 
       	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
       	 
       	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
        			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 1 for food shipment. Still owe 1399. 0 left in cash."));
       	       
       	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
       			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 1 from cashier " + cashier + " for order " + order));
       	 
       	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
       	 
       	 assertEquals("Cashier's 1 MyBill should have a status of partiallyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.partiallyPaid);
       	 
       	 //Get ready for next through the customer scenario
       	 
       	 //Checking preconditions (market should scenario should not have changed them except the cash)         
         assertEquals("Cashier should have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
         
         assertEquals("Cashier's should have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
         
         assertEquals("Cashier should have 0 cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 0);
         
         //Check bill computation
         cashier.msgPleaseComputeBill(waiter, "Steak", customer);
         assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                         + waiter.log.toString(), 0, waiter.log.size());
         
         assertEquals("Cashier should have 1 bill in it. Instead, it is size " + cashier.getBills().size(), cashier.getBills().size(), 1);
         
         
         CashierRole.Bill b = cashier.new Bill(waiter, "Steak", customer);         
         assertTrue("Cashier's 1 bill should be the correct bill. It isn't", cashier.getBills().get(0).equals(b));
         
         assertTrue("Cashier's scheduler should have returned true (started computed bill), but didn't.", cashier.pickAndExecuteAnAction());
         
         assertTrue("Cashier should have an event saying he's getting ready to compute the new bill. However, the last logged event is " 
         			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Computing bill from waiter " + waiter.getName() + " for customer " + customer.getName() + " who ordered Steak"));
         
         cashier.msgTimerDone(cashier.getBills().get(0));
         
         assertTrue("Cashier should have have received a message from the timer that he's finished computing the bill. However, the last logged event is " 
         			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Finished computing bill for waiter " + waiter.getName() + " for customer " + customer.getName() + ". For the order Steak, the total is 1599"));
         
         //Check waiter notification
         assertTrue("Cashier's scheduler should have reacted true from the timer call. It didn't.", cashier.pickAndExecuteAnAction());
         
         assertTrue("Cashier should have logged an event saying he has notified the waiter. However, the last logged event is: "
         			 + cashier.log.getLastLoggedEvent(), cashier.log.containsString("Notifying waiter of finished bill for customer " + customer.getName() + " who ordered Steak. Total is 1599"));
         
         assertTrue("MockWaiter should have received a message from the cook with the bill information, but he didn't. Instead, the last logged event is "
         			+ waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received message from cashier with the newly computed bill for customer " + customer.getName() + " who ordered Steak. Total is 1599"));
         
         assertEquals("Cashier should still have an empty customer list. Instead, it is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0); 
         
         assertEquals("Cashier's should still have an empty debtors list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);

         //Check customer handling
         Check bill = new Check("Steak", 1599);
         cashier.msgHereIsPayment(customer, bill, 1599);
                 
         assertTrue("Cashier should have logged \"Received HereIsPayment\" but didn't. His log reads instead: "
                         + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment of 1599 from customer " + customer.getName() + " for the bill " + bill));

         assertEquals("Cashier should have added the customer to their customer list but didn't. Instead, the list is size " 
                         + cashier.getCustomers().size(), cashier.getCustomers().size(), 1);
         
         CashierRole.MyCustomer mc = cashier.new MyCustomer(customer, bill, 1599);
         assertTrue("Cashier's newly-added MyCustomer either has the wrong customer, check, or bill.", cashier.getCustomers().get(0).equals(mc));                
         

         assertTrue("Cashier's scheduler should have returned true (needs to react to customer's HereIsPayment, but didn't.",
                                 cashier.pickAndExecuteAnAction());
         
         assertTrue("Cashier should have logged \"Processing payment from customer\" but didn't. His log reads instead: "
         			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Processing payment from customer"));
         
         assertTrue("MockCustomer should have logged an event for receiving \"PaymentReceived\" with the correct balance, but his last event logged reads instead: "
                         + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from cashier and owe 0"));
         
         assertEquals("Cashier should have removed the customer from their customer list. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
         
         assertEquals("Cashier's debtor list should still be size 0. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
         
         assertEquals("Cashier should now have 1599 in cash. Instead, he has " + cashier.getCash(), cashier.getCash(), 1599);
       	 
       	 //Test whether we finish paying
       	 assertTrue("Cashier's scheduler should have returned true (finish the bill), but didn't.", cashier.pickAndExecuteAnAction());
       	 
       	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
         			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 1399 for food shipment. Still owe 0. 200 left in cash."));
       	 
       	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
       			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 1399 from cashier " + cashier + " for order " + order));
       	 
       	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
       	 
       	 assertEquals("Cashier's 1 MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
       	 
       	 assertEquals("Cashier should have a customer list of size 0. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
        
         assertEquals("Cashier's debtor list should still be size 0. Instead, the list is size " + cashier.getCustomers().size(), cashier.getCustomers().size(), 0);
         
         assertEquals("CashierAgent should have 200 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 200);
       	 
       	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                    cashier.pickAndExecuteAnAction());
       	 
       }
 */      
        /**
         * This tests the cashier and market very simply: one market, and the cashier is able to pay immediately
         */
 /*       public void testOneMarketNormalScenario(){
        	 //Check preconditions
        	 assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                     + cashier.log.toString(), 0, cashier.log.size());
        	 
        	 assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
        	 
        	 cashier.setCash(4000);
        	 assertEquals("CashierAgent should have 4000 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 4000);
        	 
        	 //Check message reception
        	 Map<String, Integer> order = new HashMap<String, Integer>();
        	 order.put("Steak", 3);
        	 
        	 cashier.msgPleasePay(market1, order, 2400);
        	 
        	 assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                     + market1.log.toString(), 0, market1.log.size());
        	 
        	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 //Check actions
        	 
        	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
        	 
        	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
         			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 2400 for food shipment. Still owe 0. 1600 left in cash."));
        	       
        	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
        			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 2400 from cashier " + cashier + " for order " + order));
        	 
        	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 assertEquals("Cashier's 1 MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
        	 
        	 assertEquals("CashierAgent should have 1600 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 1600);
        	 
        	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                     cashier.pickAndExecuteAnAction());
        	 
        }
  */      
        /**
         * Still only one market, but the cashier is unable to pay immediately. Cashier gets money (somehow) and then proceeds to pay
         */
 /*       public void testOneMarketDelayPaymentOneRefill(){
        	//Check preconditions
        	 assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                     + cashier.log.toString(), 0, cashier.log.size());
        	 
        	 assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
        	 
        	 assertEquals("CashierAgent should have 0 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 0);
        	 
        	 //Check message reception
        	 Map<String, Integer> order = new HashMap<String, Integer>();
        	 order.put("Steak", 3);
        	 
        	 cashier.msgPleasePay(market1, order, 2400);
        	 
        	 assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                     + market1.log.toString(), 0, market1.log.size());
        	 
        	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 //Check actions       	 
        	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
        	 
        	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
         			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 0 for food shipment. Still owe 2400. 0 left in cash."));
        	       
        	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
        			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 0 from cashier " + cashier + " for order " + order));
        	 
        	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 assertEquals("Cashier's 1 MyBill should have a status of partiallyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.partiallyPaid);
        	 
        	 //Get ready for next payment
        	 cashier.setCash(2400);
        	 assertEquals("CashierAgent should have 2400 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 2400);
        	 
        	 //Test whether we finish paying
        	 assertTrue("Cashier's scheduler should have returned true (finish the bill), but didn't.", cashier.pickAndExecuteAnAction());
        	 
        	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
          			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 2400 for food shipment. Still owe 0. 0 left in cash."));
        	 
        	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
        			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 2400 from cashier " + cashier + " for order " + order));
        	 
        	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 assertEquals("Cashier's 1 MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
        	 
        	 assertEquals("CashierAgent should have 0 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 0);
        	 
        	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                     cashier.pickAndExecuteAnAction());
        	 
        }
*/        
        /**
         * Still only one market, but the cashier is unable to pay immediately. Cashier gets money (somehow), but still does not have enough
         * Cashier should wait until they do get enough (after the second money refill) before attempting to pay
         */
 /*       public void testOneMarketDelayPaymentTwoRefills(){
        	//Check preconditions
        	 assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                     + cashier.log.toString(), 0, cashier.log.size());
        	 
        	 assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
        	 
        	 assertEquals("CashierAgent should have 0 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 0);
        	 
        	 //Check message reception
        	 Map<String, Integer> order = new HashMap<String, Integer>();
        	 order.put("Steak", 3);
        	 
        	 cashier.msgPleasePay(market1, order, 2400);
        	 
        	 assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                     + market1.log.toString(), 0, market1.log.size());
        	 
        	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 //Check actions       	 
        	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
        	 
        	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
         			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 0 for food shipment. Still owe 2400. 0 left in cash."));
        	       
        	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
        			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 0 from cashier " + cashier + " for order " + order));
        	 
        	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 assertEquals("Cashier's 1 MyBill should have a status of partiallyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.partiallyPaid);
        	 
        	 //Get ready for next payment
        	 cashier.setCash(1600);
        	 assertEquals("CashierAgent should have 1600 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 1600);
        	 
        	 assertFalse("Cashier's scheduler should have returned false (not enough cash to trigger payment), but didn't.",
                     cashier.pickAndExecuteAnAction());
        	 
        	 cashier.setCash(2400);
        	 assertEquals("CashierAgent should have 2400 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 2400);
        	 
        	 //Test whether we finish paying
        	 assertTrue("Cashier's scheduler should have returned true (finish the bill), but didn't.", cashier.pickAndExecuteAnAction());
        	 
        	 assertTrue("Cashier should have an event saying he's paying the bill to market. However, the last logged event is " 
          			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 2400 for food shipment. Still owe 0. 0 left in cash."));
        	 
        	 assertTrue("Market should have logged an event for receiving payment. However, the last logged event is "
        			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 2400 from cashier " + cashier + " for order " + order));
        	 
        	 assertEquals("Cashier should still have the 1 MyBill. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
        	 
        	 assertEquals("Cashier's 1 MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
        	 
        	 assertEquals("CashierAgent should have 0 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 0);
        	 
        	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                     cashier.pickAndExecuteAnAction());
        	 
        }
  */      
        /**
         * Cashier handles two markets at once. (Two requests for payment arrive at the same time)
         * Situation might occur if one market was unable to fulfill the cook's initial request, but cashier doesn't know or care about the cook's side of the scenario
         * Situation can also occur simply because a previous request just took so long processing.
         */
/*        public void testTwoMarketsNormalScenario(){
        	//Check preconditions
       	 assertEquals("CashierAgent should have an empty event log before the Cashier's PleaseComputeBill is called. Instead, the Cashier's event log reads: "
                    + cashier.log.toString(), 0, cashier.log.size());
       	 
       	 assertEquals("CashierAgent should have an empty MyBills list before the PleasePayBill is called. Instead, the Cashier's mybills list is size " + cashier.getMyBills().size(), cashier.getMyBills().size(), 0);
       	 
       	 cashier.setCash(4000);
       	 assertEquals("CashierAgent should have 4000 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 4000);
       	 
       	 //Check message reception
       	 Map<String, Integer> order1 = new HashMap<String, Integer>();
       	 order1.put("Steak", 3);
       	 
       	 cashier.msgPleasePay(market1, order1, 2400);
       	 
       	 assertEquals("MockMarket1 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                    + market1.log.toString(), 0, market1.log.size());
       	 
       	 assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                + market2.log.toString(), 0, market2.log.size());
       	 
       	 assertEquals("Cashier should have 1 MyBill in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 1);
       	 
       	 
       	 Map<String, Integer> order2 = new HashMap<String, Integer>();
      	 order2.put("Chicken", 2);
      	 order2.put("Salad", 1);
      	 
      	 cashier.msgPleasePay(market2, order2, 1400);
      	 
      	 assertEquals("MockMarket should still have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                   + market1.log.toString(), 0, market1.log.size());
      	 
      	 assertEquals("Cashier should have 2 MyBills in it. Instead, it has " + cashier.getMyBills().size(), cashier.getMyBills().size(), 2);
      	    	 
       	 
       	 //Check actions for market1
       	 assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
       	 
       	 assertTrue("Cashier should have an event saying he's paying the bill to market1. However, the last logged event is " 
        			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market1 +  " 2400 for food shipment. Still owe 0. 1600 left in cash."));
       	       
       	 assertTrue("Market1 should have logged an event for receiving payment. However, the last logged event is "
       			 + market1.log.getLastLoggedEvent(), market1.log.containsString("Received payment of 2400 from cashier " + cashier + " for order " + order1));
       	 
       	 assertEquals("MockMarket2 should still have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                + market2.log.toString(), 0, market2.log.size());
       	 
       	 assertEquals("Cashier should still have the 2 MyBills. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 2);
       	 
       	 assertEquals("Cashier's 1st MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
       	 
       	 assertEquals("Cashier's 2nd MyBill should still have a status of newlyReceived. Instead, the status is " + cashier.getMyBills().get(1).status, cashier.getMyBills().get(1).status,MyBillState.newlyReceived);
      	 
       	 
       	 assertEquals("MockMarket1 should have only 1 event in the event log before the Cashier's scheduler is called again. Instead, the MockMarket's event log reads: "
                + market1.log.toString(), 1, market1.log.size());
       	 
       	 
       	 //Check actions for market2
       	 assertTrue("Cashier's scheduler should have returned true (second market is still waiting), but didn't.", cashier.pickAndExecuteAnAction());
       	 
       	 assertTrue("Cashier should have an event saying he's paying the bill to market2. However, the last logged event is " 
    			+ cashier.log.getLastLoggedEvent(), cashier.log.containsString("Paying " + market2 +  " 1400 for food shipment. Still owe 0. 200 left in cash."));
   	       
       	 assertEquals("MockMarket1 should still have only 1 event in the event log after the Cashier's scheduler is called again. Instead, the MockMarket's event log reads: "
                + market1.log.toString(), 1, market1.log.size());
       	 
       	 assertTrue("Market2 should have logged an event for receiving payment. However, the last logged event is "
	   			 + market2.log.getLastLoggedEvent(), market2.log.containsString("Received payment of 1400 from cashier " + cashier + " for order " + order2));
	   	 
	   	 assertEquals("Cashier should still have the 2 MyBills. Instead, the size of the MyBills is " + cashier.getMyBills().size(), cashier.getMyBills().size(), 2);
	   	 
	   	 assertEquals("Cashier's 1st MyBill should still have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(0).status, cashier.getMyBills().get(0).status,MyBillState.fullyPaid);
	   	 
	   	 assertEquals("Cashier's 2nd MyBill should have a status of fullyPaid. Instead, the status is " + cashier.getMyBills().get(1).status, cashier.getMyBills().get(1).status,MyBillState.fullyPaid);
	   	 
	   	assertEquals("CashierAgent should have 200 in cash. Instead, it has " + cashier.getCash(), cashier.getCash(), 200);
	   	 	 
       	 assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.",
                    cashier.pickAndExecuteAnAction());
       	 
       }
    */    
}