package UnitTests.restaurantLindaUnitTests;

import restaurant.ProducerConsumerMonitor;
import restaurant.restaurantLinda.CashierRole;
import restaurant.restaurantLinda.ProducerConsumerWaiterRole;
import restaurant.restaurantLinda.RestaurantLinda;
import restaurant.restaurantLinda.RestaurantOrder;
import restaurant.restaurantLinda.WaiterRole;
import UnitTests.mock.MockPerson;
import UnitTests.mock.RestaurantMock.MockRestaurant;
import UnitTests.mock.restaurantLindaMock.MockCashier;
import UnitTests.mock.restaurantLindaMock.MockCook;
import UnitTests.mock.restaurantLindaMock.MockCustomer;
import UnitTests.mock.restaurantLindaMock.MockHost;
import UnitTests.mock.restaurantLindaMock.MockWaiter;
import junit.framework.TestCase;

public class WaiterTest extends TestCase{
	
	ProducerConsumerWaiterRole waiter;
	MockCashier cashier;
	MockCustomer customer;
	MockCook cook;
	MockHost host;
	
	MockPerson person;
	
	RestaurantLinda restaurant;
	
	ProducerConsumerMonitor<RestaurantOrder> orderMonitor;

	
	public void setUp() throws Exception{
        super.setUp();
        
        person = new MockPerson();
        
        orderMonitor = new ProducerConsumerMonitor<RestaurantOrder>();
        
        waiter = new ProducerConsumerWaiterRole("waiter", person);
        customer = new MockCustomer("customer");
        cashier = new MockCashier("cashier");
        cook = new MockCook("cook");
        host = new MockHost("host");
        
        restaurant = new RestaurantLinda();
        restaurant.cook = cook;
        restaurant.cashier = cashier;  
        
        waiter.setMonitor(orderMonitor);
        waiter.setRestaurant(restaurant);
	}
	
	public void testOrderStand(){
		
		assertEquals("Waiter should have an empty event log at the beginning. Instead, the size is " + waiter.log.size(), waiter.log.size(), 0);
		assertEquals("Waiter should have an empty customers list. Instead it is size " + waiter.customers.size(), waiter.customers.size(), 0);
		assertEquals("Cashier should have an log. Instead it is size " + cashier.log.size(), cashier.log.size(), 0);
		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("Customer should have an empty log. Instead it is size " + customer.log.size(), customer.log.size(), 0);
		assertEquals("Host should have an empty log. Instead it is " + host.log.size(), host.log.size(), 0);
		assertTrue("The order stand should be empty. It isn't.", orderMonitor.isEmpty());
		
		assertFalse("Waiter's scheduler should return false, but didn't.", waiter.pickAndExecuteAnAction());
		
		//Waiter gets ready to seat customer
		waiter.msgPleaseServeCustomer(customer, 1);
		assertEquals("Waiter should now have 1 message in the log. Instead, the log is size " + waiter.log.size(), waiter.log.size(), 1);
		assertTrue("Waiter should have received a request to serve the customer. Instead, the last logged event is " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("Received request to set customer "+customer.getName()+" at table 1"));
		
		assertTrue("Waiter's scheduler should return true, but didn't.", waiter.pickAndExecuteAnAction());
		
		assertTrue("Waiter should have a message about walking to the door. Instead the last logged event is " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("Walking to door to fetch customer " + customer.getName()));
		
		assertEquals("Customer's log should now be size 1. Instead, it is size " + customer.log.size(), customer.log.size(), 1);
		assertTrue("Customer should have received a followMe, but the last logged event is " + customer.log.getLastLoggedEvent(), customer.log.containsString("Received followMe from waiter " + waiter.getName() + ", and received a menu"));
		
		assertFalse("Waiter's scheduler should return false, but didn't.", waiter.pickAndExecuteAnAction());
		
		assertTrue("The order stand should still be empty. It isn't.", orderMonitor.isEmpty());
		
		
		//Waiter should take customer's order
		waiter.msgReadyToOrder(customer);
		
		assertTrue("Waiter's scheduler should return false, but didn't.", waiter.pickAndExecuteAnAction());
		assertTrue("Waiter should have logged a message to take customer's order. Instead, the last logged event is " + waiter.log.getLastLoggedEvent(), waiter.log.containsString(customer.getName() + ", may I take your order?"));
		
		assertTrue("Customer should have received a message asking what they wanted. Instead, the last logged event is " + customer.log.getLastLoggedEvent(), customer.log.containsString("Was asked what I wanted"));	
		
		assertFalse("Waiter's scheduler should return true, but didn't.", waiter.pickAndExecuteAnAction());
		
		assertTrue("The order stand should still be empty. It isn't.", orderMonitor.isEmpty());
		
		
		//Waiter should send order to stand
		waiter.msgHereIsChoice(customer, "Steak");
		
		assertTrue("Waiter's scheduler should return true, but didn't.", waiter.pickAndExecuteAnAction());
		assertTrue("Waiter should have logged a message saying they're sending the order to the cook. Instead, the last logged event is " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("Manually sending " + customer.getName() + "'s order to cook"));
		
		assertFalse("The order stand should no longer be empty. It isn't.", orderMonitor.isEmpty());
		
		RestaurantOrder order = orderMonitor.remove();
		
		assertEquals("The waiter from the order in the stand should match this waiter. It isn't.", order.w.getName(), waiter.getName());
		assertEquals("The table listed in the order be 1. Instead, it is " + order.table, order.table, 1);
		assertEquals("The choice listed in the order should be Steak. Instead it is " + order.choice, order.choice, "Steak");
		
		assertTrue("Order stand should be empty again. It isn't", orderMonitor.isEmpty());
		
		assertFalse("Waiter's scheduler should now return false.", waiter.pickAndExecuteAnAction());
		
		
	}
}
