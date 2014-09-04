package UnitTests.MarketUnitTests;

import static org.junit.Assert.*;
import interfaces.MarketCustomer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketCashierRole;
import market.OrderItem;

import org.junit.Test;

import restaurant.Restaurant;
import UnitTests.mock.EventLog;
import UnitTests.mock.MarketMock.MockMarketCustomer;
import UnitTests.mock.MarketMock.MockMarketDeliveryMan;
import UnitTests.mock.MarketMock.MockMarketEmployee;
import UnitTests.mock.MarketMock.MockMarketPerson;
import UnitTests.mock.RestaurantMock.MockBaseRestaurantCashier;
import UnitTests.mock.RestaurantMock.MockBaseRestaurantCook;
import UnitTests.mock.RestaurantMock.MockRestaurant;

public class MarketCashierTest extends TestCase{
	

	Market market;
	MockMarketPerson person;
	MarketCashierRole cashier;
	
	MockMarketEmployee employee;
	MockMarketCustomer customer;
	MockMarketDeliveryMan deliveryMan;
	Restaurant rest;
	
	MockRestaurant restaurant;
	MockBaseRestaurantCook cook;
	MockBaseRestaurantCashier restaurantCashier;
	
	public void setUp() throws Exception{
		super.setUp();	
		market = new Market();
		person = new MockMarketPerson("P0");
		cashier = new MarketCashierRole("Cashier", person, market);
		
		employee = new MockMarketEmployee("Employee");
		customer = new MockMarketCustomer("Customer");
		deliveryMan = new MockMarketDeliveryMan("DeliveryMan");
		
		restaurant = new MockRestaurant();
		cook = new MockBaseRestaurantCook("Cook");
		restaurantCashier = new MockBaseRestaurantCashier("restaurantCashier");
		restaurant.cashier = restaurantCashier;
		restaurant.cook = cook;
		
	}
	
	@Test
	public void testOne_NormalCustomerInteraction() {
		//preconditions
		assertEquals("Cashier's event log should be empty.", 0, cashier.log.size());
		assertEquals("Cashier's customer list should be empty.", 0, cashier.customers.size());
		assertEquals("Cashier's order list should be empty.", 0, cashier.orders.size());
		
		//Step 1 : send msgServiceCustomer
		Map<String, Integer> groceries = new HashMap<String, Integer>();
		groceries.put("Chicken", 2);
		cashier.msgServiceCustomer(customer,  groceries);
		assertTrue("Cashier's log should have received the message" + cashier.log.toString(), cashier.log.containsString("got msgServiceCustomer name: " + customer.getName()));
		assertEquals("Cashier's customer list should be 1", 1, cashier.customers.size());
		
		//Step 2 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action but has: " + cashier.log.getLastLoggedEvent(), cashier.log.containsString("action ComputeTotal"));
		try {
			Thread.sleep(groceries.size() + 610);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("Cashier's log should have received message 'done computing bill' but reads : " + cashier.log.getLastLoggedEvent(), cashier.log.containsString("done computing bill"));

		//Pre Step 3
		assertEquals("Customers log should be empty but its actual size is: " + customer.log.size(), customer.log.size(), 0);
		
		//Step 3 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action AskCustomerToPay but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("action AskCustomerToPay"));
		assertTrue("Customer's log should have record of 'Got total for $4' but has: " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().endsWith("Got total for: $4"));

		//Post Step 3
		assertEquals("Customers log should be size 1, but its actual size is: " + customer.log.size(), customer.log.size(), 1);
		
		//Step 4
		cashier.msgCustomerPayment(customer, 4);
		assertTrue("Cashier's log should have received the message 'got msgCustomerPayment for: $4', but got " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("got msgCustomerPayment for: $4"));
		
		//Step 5
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action AcceptPayment but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent(2).toString().endsWith("action AcceptPayment"));
		assertTrue("Cashier's log should have record of 'I owe the customer change: $' but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("I owe the customer change: $0"));
		assertTrue("Customer's log should have record of 'Got chage of: $0' but has: " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().endsWith("Got chage of: $0"));
		
		//Step 6
		assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
	}
	
	public void testTwo_NormalDeliveryManInteraction() {
		//preconditions
		OrderItem tempOrder = new OrderItem("Steak", 5);
		tempOrder.quantityReceived = 5;
		ArrayList<OrderItem> listItems = new ArrayList<OrderItem>();
		listItems.add(tempOrder);
		assertEquals("Cashier's event log should be empty.", 0, cashier.log.size());
		assertEquals("Cashier's customer list should be empty.", 0, cashier.customers.size());
		assertEquals("Cashier's order list should be empty.", 0, cashier.orders.size());
		assertEquals("Employees's log should be empty, but is size" + employee.log.size(), employee.log.size(), 0);
		
		//Step 1 : send msgCalculateInvoice
		Map<String, Integer> groceries = new HashMap<String, Integer>();
		cashier.msgCalculateInvoice(employee, listItems, rest);
		assertTrue("Cashier's log should have received the message" + cashier.log.toString(), cashier.log.getLastLoggedEvent().toString().endsWith("got msgCalculateInvoice"));
		assertEquals("Cashier's order list should be 1", 1, cashier.orders.size());
		
		//Step 2 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of 'action ComputeBusinessPayment', but has: " + cashier.log.getLastLoggedEvent(2), cashier.log.containsString("action ComputeBusinessPayment"));
		assertTrue("Employees's log should have 'got msgGiveInvoice for $10' but has : " + employee.log.getLastLoggedEvent(), employee.log.getLastLoggedEvent().toString().endsWith("got msgGiveInvoice for $10"));
		assertEquals("Employee should hae a log size of 1, but it is: " + employee.log.size(), 1, employee.log.size());
		assertEquals("Cashier should have no more BusinessPayments to be computed(orders == 0)", 0, cashier.orders.size());
		
		//Step 3 : call scheduler
		assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
		
		//Step 4 : message the cashier a business payment.
		assertEquals("Cashier should have an empty businessPayment list but has a size of: " + cashier.businessPayments.size(), 0, cashier.businessPayments.size());
		cashier.msgHereIsBusinessPayment(10);
		assertEquals("Cashier should have one businessPayment in the list but has a size of: " + cashier.businessPayments.size(), 1, cashier.businessPayments.size());

		//Step 5 : call scheduler
		assertEquals("Cashier's should hae a log size of 3, but it is: " + cashier.log.size(), 3, cashier.log.size());
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		
		//Post Step 5
		assertTrue("Cashier's log should have record of 'action ProcessBusinessPayment', but has: " + cashier.log.getLastLoggedEvent(2), cashier.log.containsString("action ProcessBusinessPayment"));
		assertEquals("Cashier's should hae a log size of 5, but it is: " + cashier.log.size(), 5, cashier.log.size());

		//Step 6 : call scheduler
		assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
	}
	
	public void testThree_CustomerShortMoneyInteraction() {
		//preconditions
		assertEquals("Cashier's event log should be empty.", 0, cashier.log.size());
		assertEquals("Cashier's customer list should be empty.", 0, cashier.customers.size());
		assertEquals("Cashier's order list should be empty.", 0, cashier.orders.size());
		
		//Step 1 : send msgServiceCustomer
		Map<String, Integer> groceries = new HashMap<String, Integer>();
		groceries.put("Chicken", 2);
		cashier.msgServiceCustomer(customer,  groceries);
		assertTrue("Cashier's log should have received the message" + cashier.log.toString(), cashier.log.containsString("got msgServiceCustomer name: " + customer.getName()));
		assertEquals("Cashier's customer list should be 1", 1, cashier.customers.size());
		
		//Step 2 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action but has: " + cashier.log.getLastLoggedEvent(), cashier.log.containsString("action ComputeTotal"));
		try {
			Thread.sleep(groceries.size() + 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("Cashier's log should have received message 'done computing bill' but reads : " + cashier.log.getLastLoggedEvent(), cashier.log.containsString("done computing bill"));

		//Pre Step 3
		assertEquals("Customers log should be empty but its actual size is: " + customer.log.size(), customer.log.size(), 0);
		
		//Step 3 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action AskCustomerToPay but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("action AskCustomerToPay"));
		assertTrue("Customer's log should have record of 'Got total for $4' but has: " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().endsWith("Got total for: $4"));

		//Post Step 3
		assertEquals("Customers log should be size 1, but its actual size is: " + customer.log.size(), customer.log.size(), 1);
		
		//Step 4
		cashier.msgCustomerPayment(customer, 2);
		assertTrue("Cashier's log should have received the message 'got msgCustomerPayment for: $2', but got " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("got msgCustomerPayment for: $2"));
		
		//Step 5
		assertEquals("cashier should have nobody in their debtorsList, but there are " + cashier.debtorsList.size(), 0, cashier.debtorsList.size());
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action AcceptPayment but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent(2).toString().endsWith("action AcceptPayment"));
		assertTrue("Cashier's log should have record of 'The customer still owes me $2 and is now on my debtors list' but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("The customer still owes me $2 and is now on my debtors list"));
		assertTrue("Customer's log should have record of 'I still owe this market $2' but has: " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().endsWith("I still owe this market $2"));
		
		//Step 5 postconditions
		assertEquals("cashier should have one person in their debtorsList, but there are " + cashier.debtorsList.size(), 1, cashier.debtorsList.size());
		assertEquals("the amount stored in debtorsList shoud be $2, but it has $" + (int)cashier.debtorsList.get(customer.getPerson()), 2, (int)cashier.debtorsList.get(customer.getPerson()));
		
		//Step 6
		assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
	}
	
	public void testFour_CustomerTooMuchMoneyInteraction() {
		//preconditions
		assertEquals("Cashier's event log should be empty.", 0, cashier.log.size());
		assertEquals("Cashier's customer list should be empty.", 0, cashier.customers.size());
		assertEquals("Cashier's order list should be empty.", 0, cashier.orders.size());
		
		//Step 1 : send msgServiceCustomer
		Map<String, Integer> groceries = new HashMap<String, Integer>();
		groceries.put("Chicken", 2);
		cashier.msgServiceCustomer(customer,  groceries);
		assertTrue("Cashier's log should have received the message" + cashier.log.toString(), cashier.log.containsString("got msgServiceCustomer name: " + customer.getName()));
		assertEquals("Cashier's customer list should be 1", 1, cashier.customers.size());
		
		//Step 2 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action but has: " + cashier.log.getLastLoggedEvent(), cashier.log.containsString("action ComputeTotal"));
		try {
			Thread.sleep(groceries.size() + 610);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("Cashier's log should have received message 'done computing bill' but reads : " + cashier.log.getLastLoggedEvent(), cashier.log.containsString("done computing bill"));

		//Pre Step 3
		assertEquals("Customers log should be empty but its actual size is: " + customer.log.size(), customer.log.size(), 0);
		
		//Step 3 : call scheduler
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action AskCustomerToPay but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("action AskCustomerToPay"));
		assertTrue("Customer's log should have record of 'Got total for $4' but has: " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().endsWith("Got total for: $4"));

		//Post Step 3
		assertEquals("Customers log should be size 1, but its actual size is: " + customer.log.size(), customer.log.size(), 1);
		
		//Step 4
		cashier.msgCustomerPayment(customer, 100);
		assertTrue("Cashier's log should have received the message 'got msgCustomerPayment for: $100', but got " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("got msgCustomerPayment for: $100"));
		
		//Step 5
		assertTrue("Scheduler should return true", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's log should have record of action AcceptPayment but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent(2).toString().endsWith("action AcceptPayment"));
		assertTrue("Cashier's log should have record of 'I owe the customer change: $' but has: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().endsWith("I owe the customer change: $96"));
		assertTrue("Customer's log should have record of 'Got chage of: $96' but has: " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().endsWith("Got chage of: $96"));
		
		//Step 6
		assertFalse("Scheduler should return false", cashier.pickAndExecuteAnAction());
	}

}
