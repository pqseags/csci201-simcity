package UnitTests.MarketUnitTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import interfaces.Person;
import junit.framework.TestCase;
import market.Market;
import market.MarketCustomerRole;
import market.MarketCustomerRole.RoleEvent;
import market.MarketCustomerRole.RoleState;
import market.Receipt;

import org.junit.Test;

import UnitTests.mock.MarketMock.*;

public class MarketCustomerTest extends TestCase{

	MockMarketPerson person;
	MarketCustomerRole customer;
	MockMarketHost host;
	MockMarketEmployee employee;
	MockMarketCashier cashier;
	Market market;
	
	
	
	
	public void setUp() throws Exception{
		super.setUp();	
		
		person = new MockMarketPerson("P0");
		person.wallet = 50;
		customer = new MarketCustomerRole("Cust", person);
		host = new MockMarketHost("Host");
		employee = new MockMarketEmployee("Employee");
		cashier = new MockMarketCashier("Cashier");
		market = new Market();
		
		customer.host = host;
		host.customer = customer;
		employee.customer = customer;
		cashier.customer = customer;
		
		market.host = host;
		market.cashier = cashier;
		
		
		
	}
	
	@Test
	public void testOne_NormalInteraction() {
	
		//preconditions
		assertEquals("Customer's event log should be empty.", 0, customer.log.size());
		assertEquals("Customer's shopping list should be empty.", 0, customer.shoppingList.size());
		assertEquals("Customer's name should be \"Cust\" ", "Cust", customer.getName());
		assertEquals("Customer's market property should be null", null, customer.market);
		assertFalse("Scheduler should return false", customer.pickAndExecuteAnAction());
		
		//Step 1 : Send msgAtMarket to customer
		customer.msgYouAreAtMarket(market);
		assertTrue("Customer's log should have received the message, but it doesnt.", customer.log.getLastLoggedEvent().toString().endsWith("got msgYouAreAtMarket"));
		assertEquals("Customers market property should be the instanciated market.", market, customer.market);
		assertEquals("Customers state should be RoleState.JustEnteredMarket", RoleState.JustEnteredMarket, customer.state);
		
		//Step 2:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("Customers state should be RoleState.Ordered, but it is in " + customer.state,  RoleState.Ordered, customer.state);
		assertTrue("Customer's log should say its implementing MakeOrder action, but it doesnt.", customer.log.getLastLoggedEvent().toString().endsWith("MakeOrder action"));
		assertTrue("Hosts log should say it received a message", host.log.getLastLoggedEvent().toString().endsWith("got msgCustomerWantsThis. total items: " + customer.shoppingList.size()));
		assertFalse("Scheduler should return false", customer.pickAndExecuteAnAction());
		
		//Step 3:  Send msgHereAreItems
		Map<String, Integer> groceries = new HashMap<String, Integer>();

		groceries.put("Pizza", new Integer(4));
		customer.msgHereAreItems(groceries);
		assertTrue("Customers log should have received the message", customer.log.getLastLoggedEvent().toString().endsWith("got msgHereAreItems total items: " + groceries.size()));
		assertEquals("Customers event should be RoleEvent.itemsArrived", RoleEvent.itemsArrived, customer.event);
		assertEquals("Customers grocery list should be the groceries in the message", groceries, customer.groceries);
		
		//Step 4:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customer's log should record GoPay action", customer.log.getLastLoggedEvent().toString().endsWith("action GoPay"));
		assertTrue("Cashier's log should say it got msgServiceCustomer", cashier.log.getLastLoggedEvent().toString().endsWith("got msgServiceCustomer for customer " + customer.getName() + " for order " + groceries.toString()));
		
		//Step 5:  Send msgHereIsTotal
		customer.msgHereIsTotal(40);
		assertTrue("Customers log should record msgHereIsTotal ", customer.log.getLastLoggedEvent().toString().endsWith("Got total for: $" + 40));
		assertEquals("Customer's bill property should equal 40", 40, customer.bill);
		assertEquals("Customers state should be RoleEvent.askedToPay", RoleEvent.askedToPay, customer.event);
		
		//Step 6: call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customers log should have MakePayment action", customer.log.getLastLoggedEvent().toString().endsWith("action MakePayment"));
		assertEquals("Customer should taken 40 from his wallet, leaving him with 10", 10, person.wallet);
		assertTrue("Cashier's log should have recorded msgCustomerPayment ", cashier.log.getLastLoggedEvent().toString().endsWith("got msgCustomerPayment"));
		
		//Step 7:  Send msgHereIsYourChange
		Receipt r = new Receipt(null, 0, 0, null);//TODO change if needed.
		customer.msgHereIsYourChange(r, 10);
		assertTrue("Customers log should have recorded getting message", customer.log.getLastLoggedEvent().toString().endsWith("got msgHereIsYourChange"));
		assertEquals("Customer should have gotten his receipt", r, customer.receipt);
		assertEquals("Person should have added change to his wallet", 20, person.wallet);
		assertEquals("Customers event should have changed", RoleEvent.paymentReceived, customer.event);
		
		//Step 8:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customer's log should have recorded action tryToLeave", customer.log.getLastLoggedEvent().toString().endsWith("action TryToLeave"));
		assertTrue("Hosts log should have got msgCustomerLeaving", host.log.getLastLoggedEvent().toString().endsWith("got msgCustomerLeaving name: " + customer.getName()));
		
		//Step 9: send msgYouCanLeave
		customer.msgYouCanLeave();
		assertTrue("Customers log should have recorded getting message", customer.log.getLastLoggedEvent().toString().endsWith("got msgYouCanLeave"));
		assertEquals("Customers event should have changed", RoleEvent.allowedToLeave, customer.event);
		
		//Step 10:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customers log should  record action LeaveMarket", customer.log.getLastLoggedEvent().toString().endsWith("action LeaveMarket"));
		assertTrue("PersonAgent log should record got msgRoleDone", person.log.getLastLoggedEvent().toString().endsWith("got msgThisRoleDone"));
		
		
	}

	
	@Test
	public void testTwo_DebtInteraction() {
	
		person.wallet = 20;
		
		//preconditions
		assertEquals("Customer's event log should be empty.", 0, customer.log.size());
		assertEquals("Customer's shopping list should be empty.", 0, customer.shoppingList.size());
		assertEquals("Customer's name should be \"Cust\" ", "Cust", customer.getName());
		assertEquals("Customer's market property should be null", null, customer.market);
		assertFalse("Scheduler should return false", customer.pickAndExecuteAnAction());
		
		//Step 1 : Send msgAtMarket to customer
		customer.msgYouAreAtMarket(market);
		assertTrue("Customer's log should have received the message, but it doesnt.", customer.log.containsString("got msgYouAreAtMarket"));
		assertEquals("Customers market property should be the instanciated market.", market, customer.market);
		assertEquals("Customers state should be RoleState.JustEnteredMarket", RoleState.JustEnteredMarket, customer.state);
		
		//Step 2:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("Customers state should be RoleState.Ordered",  RoleState.Ordered, customer.state);
		assertTrue("Customer's log should say its implementing MakeOrder action, but it doesnt.", customer.log.containsString("MakeOrder action"));
		assertTrue("Hosts log should say it received a message", host.log.getLastLoggedEvent().toString().endsWith("got msgCustomerWantsThis. total items: " + 0));//TODO if changed, change the number of things here.....
		assertFalse("Scheduler should return false", customer.pickAndExecuteAnAction());
		
		//Step 3:  Send msgHereAreItems
		Map<String, Integer> groceries = new HashMap<String, Integer>();

		groceries.put("Pizza", new Integer(4));
		customer.msgHereAreItems(groceries);
		assertTrue("Customers log should have received the message", customer.log.containsString("got msgHereAreItems"));
		assertEquals("Customers event should be RoleEvent.itemsArrived", RoleEvent.itemsArrived, customer.event);
		assertEquals("Customers grocery list should be the groceries in the message", groceries, customer.groceries);
		
		//Step 4:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customer's log should record GoPay action", customer.log.containsString("action GoPay"));
		assertTrue("Cashier's log should say it got msgServiceCustomer", cashier.log.containsString("got msgServiceCustomer"));
		
		//Step 5:  Send msgHereIsTotal
		customer.msgHereIsTotal(40);
		assertTrue("Customers log should record msgHereIsTotal ", customer.log.containsString("Got total for: $" + 40));
		assertEquals("Customer's bill property should equal 40", 40, customer.bill);
		assertEquals("Customers state should be RoleEvent.askedToPay", RoleEvent.askedToPay, customer.event);
		
		//Step 6: call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customers log should have MakePayment action", customer.log.containsString("action MakePayment"));
		assertEquals("Customer should all his money from his wallet, leaving him with $0",0, person.wallet);
		assertTrue("Cashier's log should have recorded msgCustomerPayment ", cashier.log.containsString("got msgCustomerPayment"));
		
		//Step 7:  Send msgHereIsYourChange
		Receipt r = new Receipt(null, 0, 0, null);//TODO change if needed.
		customer.msgYouOweMoney(r, 10);
		assertTrue("Customers log should have recorded getting message", customer.log.containsString("got msgYouOweMoney amount: $" + 10));
		assertEquals("Customer should have gotten his receipt", r, customer.receipt);
		assertEquals("Customers event should have changed", RoleEvent.paymentReceived, customer.event);
		
		//Step 8:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customer's log should have recorded action tryToLeave", customer.log.containsString("action TryToLeave"));
		assertTrue("Hosts log should have got msgCustomerLeaving", host.log.containsString("got msgCustomerLeaving name: " + customer.getName()));
		
		//Step 9: send msgYouCanLeave
		customer.msgYouCanLeave();
		assertTrue("Customers log should have recorded getting message", customer.log.containsString("got msgYouCanLeave"));
		assertEquals("Customers event should have changed", RoleEvent.allowedToLeave, customer.event);
		
		//Step 10:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customers log should  record action LeaveMarket", customer.log.containsString("action LeaveMarket"));
		assertTrue("PersonAgent log should record got msgRoleDone", person.log.containsString("got msgThisRoleDone"));
		
	}
	@Test
	public void testThree_NoStockInteraction() {

		//preconditions
		assertEquals("Customer's event log should be empty.", 0, customer.log.size());
		assertEquals("Customer's shopping list should be empty.", 0, customer.shoppingList.size());
		assertEquals("Customer's name should be \"Cust\" ", "Cust", customer.getName());
		assertEquals("Customer's market property should be null", null, customer.market);
		assertFalse("Scheduler should return false", customer.pickAndExecuteAnAction());

		//Step 1 : Send msgAtMarket to customer
		customer.msgYouAreAtMarket(market);
		assertTrue("Customer's log should have received the message, but it doesnt.", customer.log.containsString("got msgYouAreAtMarket"));
		assertEquals("Customers market property should be the instanciated market.", market, customer.market);
		assertEquals("Customers state should be RoleState.JustEnteredMarket", RoleState.JustEnteredMarket, customer.state);

		//Step 2:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("Customers state should be RoleState.Ordered",  RoleState.Ordered, customer.state);
		assertTrue("Customer's log should say its implementing MakeOrder action, but it doesnt.", customer.log.containsString("MakeOrder action"));
		assertTrue("Hosts log should say it received a message", host.log.containsString("got msgCustomerWantsThis"));
		assertFalse("Scheduler should return false", customer.pickAndExecuteAnAction());
		
		//Step 3:  call msgWeHaveNothing
		customer.msgWeHaveNothing();
		assertTrue("Customer should have logged gotten message", customer.log.containsString("got msgWeHaveNothing"));
		assertTrue("Customer's sad variable shoudl be true", customer.sad);
		
		//Step 4:  call scheduler
		assertTrue("Scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("Customers log should read action LeaveSad", customer.log.containsString("action LeaveSad"));
		
	}
}