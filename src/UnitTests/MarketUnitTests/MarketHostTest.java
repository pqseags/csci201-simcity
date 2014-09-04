package UnitTests.MarketUnitTests;

import static org.junit.Assert.*;
import interfaces.MarketCustomer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketHostRole;
import market.Receipt;

import org.junit.Test;

import UnitTests.mock.MarketMock.MockMarketCashier;
import UnitTests.mock.MarketMock.MockMarketCustomer;
import UnitTests.mock.MarketMock.MockMarketEmployee;
import UnitTests.mock.MarketMock.MockMarketPerson;

public class MarketHostTest extends TestCase{

	Market market;
	MockMarketPerson person;
	MarketHostRole host;
	
	MockMarketCustomer customer;
	MockMarketEmployee employee;
	MockMarketCashier cashier;
	
	
	public void setUp() throws Exception{
		super.setUp();	
	
		person = new MockMarketPerson("P0");
		market = new Market();
		host = new MarketHostRole("Host", person, market);
		
		cashier = new MockMarketCashier("Ca1");
		customer = new MockMarketCustomer("C1");
		employee = new MockMarketEmployee("E1");
		
		customer.host = host;
		employee.host = host;
		market.cashier = cashier;
		
		
	}
	
	
	@Test
	public void testOne_NormalCustomerInteraction() {
		host.addEmployee(employee);
		market.inventory.put("Pizza", 100);
		
		//preconditions
		assertEquals("Host's event log should be empty.", 0, host.log.size());
		assertEquals("Host's employees list should be 1 long", 1, host.employees.size());
		assertEquals("Host's customers list should be empty.", 0, host.customers.size());
		assertEquals("Host's business orders list should be empty.", 0, host.businessOrders.size());
		assertFalse("Scheduler should return false", host.pickAndExecuteAnAction());
		assertEquals("Market pizza inventory should be 100", 100, (int)market.inventory.get("Pizza") );
		
		
		//Step 1:  msgCustomerWantsThis
		Map<String, Integer> orderList = new HashMap<String,Integer>();
		orderList.put("Pizza", new Integer(4));
		host.msgCustomerWantsThis(customer, orderList);
		assertTrue("Host should have receivedm message in log", host.log.containsString("got msgCustomerWantsThis"));
		assertEquals("Host should have one customer in customer list", 1, host.customers.size());
		
		//Step 2: call scheduler
		assertTrue("Scheduler should return true", host.pickAndExecuteAnAction());
		assertTrue("Host log should have recorded action ServeCustomer", host.log.containsString("action ServeCustomer"));
		assertTrue("Market inventory should have decreased", market.inventory.get("Pizza") < 100);
		assertTrue("Employee should ahve received a message", employee.log.containsString("got msgGetItemsForCustomer"));
		
		//Step 3:  msgCustomerLeaving
		Receipt receipt = new Receipt(null, 0, 0, null);
		Map<String, Integer> groceries = new HashMap<String, Integer>();
		host.msgCustomerLeaving(customer, receipt, groceries);
		assertTrue("host log should record getting message", host.log.containsString("got msgCustomerLeaving"));
		
		//step 4:  call scheduler
		assertTrue("Scheduler should return true", host.pickAndExecuteAnAction());
		assertTrue("Host log should have recorded action CheckCustomer", host.log.containsString("action CheckCustomer"));
		assertTrue("Customer should have logged receiving msgYouCanLeave", customer.log.containsString("got msgYouCanLeave"));
	}
	
	@Test
	public void testTwo_NoEmployeesCustomerInteraction() {
		market.inventory.put("Pizza", 100);
		
		//preconditions
		assertEquals("Host's event log should be empty.", 0, host.log.size());
		assertEquals("Host's employees list should be empty", 0, host.employees.size());
		assertEquals("Host's customers list should be empty.", 0, host.customers.size());
		assertEquals("Host's business orders list should be empty.", 0, host.businessOrders.size());
		assertFalse("Scheduler should return false", host.pickAndExecuteAnAction());
		assertEquals("Market pizza inventory should be 100", 100, (int)market.inventory.get("Pizza") );
		
		
		//Step 1:  msgCustomerWantsThis
		Map<String, Integer> orderList = new HashMap<String,Integer>();
		orderList.put("Pizza", new Integer(4));
		host.msgCustomerWantsThis(customer, orderList);
		assertTrue("Host should have receivedm message in log", host.log.containsString("got msgCustomerWantsThis"));
		assertEquals("Host should have one customer in customer list", 1, host.customers.size());
		
		//Step 2: call scheduler
		assertTrue("Scheduler should return true", host.pickAndExecuteAnAction());
		assertTrue("Host log should have recorded action ServeCustomer", host.log.containsString("action ServeCustomer"));
		assertTrue("Market inventory should have decreased", market.inventory.get("Pizza") < 100);
		assertTrue("Host should have logged that theres is no employees", host.log.containsString("no employees"));
		
	}

	@Test
	public void testThree_NoInventoryCustomerInteraction() {
		host.addEmployee(employee);
		market.inventory.put("Pizza", 0);
		//preconditions
		assertEquals("Host's event log should be empty.", 0, host.log.size());
		assertEquals("Host's employees list should be 1 long", 1, host.employees.size());
		assertEquals("Host's customers list should be empty.", 0, host.customers.size());
		assertEquals("Host's business orders list should be empty.", 0, host.businessOrders.size());
		assertFalse("Scheduler should return false", host.pickAndExecuteAnAction());
		assertEquals("Market pizza inventory should be 0", 0, (int)market.inventory.get("Pizza") );
		
		
		//Step 1:  msgCustomerWantsThis
		Map<String, Integer> orderList = new HashMap<String,Integer>();
		orderList.put("Pizza", new Integer(4));
		host.msgCustomerWantsThis(customer, orderList);
		assertTrue("Host should have receivedm message in log", host.log.containsString("got msgCustomerWantsThis"));
		assertEquals("Host should have one customer in customer list", 1, host.customers.size());
		
		//Step 2: call scheduler
		assertTrue("Scheduler should return true", host.pickAndExecuteAnAction());
		assertTrue("Host log should have recorded action ServeCustomer", host.log.containsString("action ServeCustomer"));
		assertTrue("Host should have logged that theres no inventory", host.log.containsString("no inventory"));
		
	}
	
	@Test
	public void testFour_BadCustomerInteraction() {
		host.addEmployee(employee);
		market.inventory.put("Pizza", 100);
		//preconditions
		assertEquals("Host's event log should be empty.", 0, host.log.size());
		assertEquals("Host's employees list should be 1 long", 1, host.employees.size());
		assertEquals("Host's customers list should be empty.", 0, host.customers.size());
		assertEquals("Host's business orders list should be empty.", 0, host.businessOrders.size());
		assertFalse("Scheduler should return false", host.pickAndExecuteAnAction());
		assertEquals("Market pizza inventory should be 100", 100, (int)market.inventory.get("Pizza") );
		
		
		//Step 1:  msgCustomerWantsThis
		Map<String, Integer> orderList = new HashMap<String,Integer>();
		orderList.put("Pizza", new Integer(4));
		host.msgCustomerWantsThis(customer, orderList);
		assertTrue("Host should have receivedm message in log", host.log.containsString("got msgCustomerWantsThis"));
		assertEquals("Host should have one customer in customer list", 1, host.customers.size());
		
		//Step 2: call scheduler
		assertTrue("Scheduler should return true", host.pickAndExecuteAnAction());
		assertTrue("Host log should have recorded action ServeCustomer", host.log.containsString("action ServeCustomer"));
		assertTrue("Market inventory should have decreased", market.inventory.get("Pizza") < 100);
		assertTrue("Employee should ahve received a message", employee.log.containsString("got msgGetItemsForCustomer"));
		
		//Step 3:  msgCustomerLeaving
		Receipt receipt = new Receipt(null, 0, 0, null);
		Map<String, Integer> groceries = new HashMap<String, Integer>();
		groceries.put("Pizza", 5);
		host.msgCustomerLeaving(customer, receipt, groceries);
		assertTrue("host log should record getting message", host.log.containsString("got msgCustomerLeaving"));
		
		//step 4:  call scheduler
		assertTrue("Scheduler should return true", host.pickAndExecuteAnAction());
		assertTrue("Host log should have recorded action CheckCustomer", host.log.containsString("action CheckCustomer"));
		assertTrue("Host log should have recorded a bad customer", host.log.containsString("bad customer"));
		
	}
	
}
