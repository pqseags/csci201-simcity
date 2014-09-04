package UnitTests.MarketUnitTests;

import static org.junit.Assert.*;
import interfaces.MarketCustomer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketEmployeeRole;
import market.MarketInvoice;
import market.OrderItem;

import org.junit.Test;

import cityGui.CityRestaurant;
import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import UnitTests.mock.MarketMock.MockMarketCashier;
import UnitTests.mock.MarketMock.MockMarketCustomer;
import UnitTests.mock.MarketMock.MockMarketDeliveryMan;
import UnitTests.mock.MarketMock.MockMarketPerson;
import UnitTests.mock.RestaurantMock.MockRestaurant;

public class MarketEmployeeTest extends TestCase{

	MockMarketPerson person;
	MarketEmployeeRole employee;
	
	MockMarketCustomer customer;
	MockMarketCashier cashier;
	MockMarketDeliveryMan deliveryMan;
	
	ProducerConsumerMonitor<MarketInvoice> monitor;
	
	MockRestaurant restaurant;
	Market market;
	
	public void setUp() throws Exception{
		super.setUp();	
	
		person = new MockMarketPerson("P0");
		employee = new MarketEmployeeRole("Emp", person);
		
		customer = new MockMarketCustomer("Customer");
		cashier = new MockMarketCashier("Cashier");
		deliveryMan = new MockMarketDeliveryMan("DeliveryMan");
		
		monitor = new ProducerConsumerMonitor<MarketInvoice>();
		
		employee.setMonitor(monitor);	
		employee.cashier = cashier;
		
		restaurant = new MockRestaurant();
		market = new Market();
		market.cashier = cashier;
		employee.setMarket(market);		
	}
	
	
	@Test
	public void testOne_CustomerOrder() {
		//preconditions
		assertEquals("Employees customerOrders list should be empty", 0 , employee.customerOrders.size() );
		assertEquals("Employees businessOrders list should be empty", 0 ,  employee.businessOrders.size());
		assertEquals("Employee's name should be \"Emp\" ", "Emp", employee.getName());
		assertTrue("Dock monitor should be empty", monitor.isEmpty());
		assertFalse("Scheduler should return false", employee.pickAndExecuteAnAction());
		
		//Step 1:  send msgGetItemsForCustomer
		Map<String, Integer> orderList = new HashMap<String, Integer>();
		orderList.put("Pizza", new Integer(4));
		employee.msgGetItemsForCustomer(customer, orderList);
		assertTrue("Employees log should have recorded getting message, but has: " + employee.log.getLastLoggedEvent(), employee.log.containsString("got msgGetItemsForCustomer from " + customer.getName()));
		assertEquals("Employees customerOrders list should have 1", 1 , employee.customerOrders.size() );
		
		//Step 2:  call scheduler
		assertTrue("Scheduler should return true", employee.pickAndExecuteAnAction());
		assertTrue("Employees log should have recorded action CollectItems", employee.log.containsString("action CollectItems"));
		assertTrue("Employees log should have recorded collecting item", employee.log.containsString("collecting item"));
		
		//Step 3:  call scheduler again
		assertTrue("Scheduler should return true", employee.pickAndExecuteAnAction());
		assertTrue("Employees log should have recorded action GiveItemsToCustomer", employee.log.containsString("action GiveItemsToCustomer"));
		assertEquals("Employees customerOrders list should have 0", 0 , employee.customerOrders.size() );
		assertTrue("Customer's log should have recorded msgHereAreItems total items: 1 but " + customer.log.getLastLoggedEvent(), customer.log.containsString("got msgHereAreItems total items: " + 1));
		
		assertTrue("Dock monitor should still be empty", monitor.isEmpty());
		
		assertFalse("Employee's scheduler should return false", employee.pickAndExecuteAnAction());
		
	}

	@Test
	public void testTwo_BusinessOrder() {
		
		//preconditions
		assertEquals("Employees customerOrders list should be empty", 0 , employee.customerOrders.size() );
		assertEquals("Employees businessOrders list should be empty", 0 ,  employee.businessOrders.size());
		assertEquals("Employee's name should be \"Emp\" ", "Emp", employee.getName());
		assertTrue("Dock monitor should be empty", monitor.isEmpty());
		assertFalse("Scheduler should return false", employee.pickAndExecuteAnAction());

		//Step 1:  send msgGetThis
		List<OrderItem> order = new ArrayList<OrderItem>();
		order.add(new OrderItem("Steak", 5));
		
		String list = "{";
		for(OrderItem o: order){
			list+=o.quantityReceived + o.choice + ", ";
		}
		list+="}";
		
		employee.msgGetThis(order, restaurant);
		assertTrue("Employee's log should have recorded got msgGetThis with an order size of " + order.size(), employee.log.containsString("got msgGetThis with an order size of " + order.size()));
		assertEquals("Employees businessOrders list should have 1", 1 , employee.businessOrders.size() );
		assertTrue("Dock monitor should still be empty", monitor.isEmpty());
	
		//Step 2:  run scheduler
		assertTrue("Scheduler should return true", employee.pickAndExecuteAnAction());
		assertTrue("Employee's log should have recorded action", employee.log.containsString("action GetBusinessOrder"));
		assertTrue("Cashier should have recorded getting message", cashier.log.containsString("got msgCalculateInvoice from employee " + employee.getName() + " for order " + list + " for restaurant " + restaurant.toString()));
		assertTrue("Dock monitor should still be empty", monitor.isEmpty());
		
		//Step 3:  send msgGiveInvoice
		employee.msgGiveInvoice(order, restaurant, 10);
		assertTrue("Employee's log should have recorded got msgGiveInvoice with order size: " + order.size() + " for a total of: $" + 10, employee.log.containsString("got msgGiveInvoice with order size: " + order.size() + " for a total of: $" + 10));
		assertTrue("Dock monitor should still be empty", monitor.isEmpty());
		
		//Step 4: employee should send invoice to dock
		assertTrue("Employee's scheduler should return true", employee.pickAndExecuteAnAction());
		assertTrue("Employee's log should have recorded 'action PlaceOrderOnDock'. Instead, the last logged event is " + employee.log.getClass(), employee.log.containsString("action PlaceOrderOnDock"));
		
		assertFalse("Dock monitor should no longer be empty.", monitor.isEmpty());
		
		MarketInvoice invoice = monitor.remove();
		
		assertTrue("The invoice from the dock monitor should contain the correct order", invoice.order.equals(order));
		assertTrue("The invoice from the dock monitor should contain the correct restaurant", invoice.restaurant== restaurant);
		assertTrue("The invoice from the dock monitor should contain the market", invoice.market==market);
		assertTrue("The invoice from the dock monitor should have the correct total", invoice.total==10);
		
		assertFalse("Employee's scheduler should return false", employee.pickAndExecuteAnAction());
		
		
	}
	
}
