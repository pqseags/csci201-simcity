package UnitTests.restaurantLindaUnitTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.Market;
import market.MarketInvoice;
import market.OrderItem;

import restaurant.ProducerConsumerMonitor;
import restaurant.restaurantLinda.CookRole;
import restaurant.restaurantLinda.RestaurantLinda;
import restaurant.restaurantLinda.RestaurantOrder;
import restaurant.restaurantLinda.CookRole.Food;
import UnitTests.mock.MockPerson;
import UnitTests.mock.MarketMock.MockMarket;
import UnitTests.mock.MarketMock.MockMarketDeliveryMan;
import UnitTests.mock.MarketMock.MockMarketHost;
import UnitTests.mock.restaurantLindaMock.MockCashier;
import UnitTests.mock.restaurantLindaMock.MockCook;
import UnitTests.mock.restaurantLindaMock.MockCustomer;
import UnitTests.mock.restaurantLindaMock.MockHost;
import UnitTests.mock.restaurantLindaMock.MockWaiter;
import junit.framework.TestCase;

public class CookTest extends TestCase{

	CookRole cook;
	MockCashier cashier;
	MockCustomer customer;
	MockMarketHost marketHost;
	MockWaiter waiter;
	
	MockPerson person;
	
	RestaurantLinda restaurant;
	MockMarket market;
	
	MockMarketDeliveryMan deliveryMan;
	
	ProducerConsumerMonitor<RestaurantOrder> orderMonitor;
	
	public void setUp() throws Exception{
        super.setUp();
        
        restaurant = new RestaurantLinda();
        market = new MockMarket();
        
        person = new MockPerson();
        cashier = new MockCashier("cashier");
        orderMonitor = new ProducerConsumerMonitor<RestaurantOrder>();
        cook = new CookRole("cook", orderMonitor, restaurant);
        cook.p = person;
        customer = new MockCustomer("customer");
        waiter = new MockWaiter("waiter");
        
        marketHost = new MockMarketHost("marketHost");
        deliveryMan = new MockMarketDeliveryMan("deliveryMan");
        
        market.host = marketHost;
        
        restaurant.markets = new ArrayList<Market>();
        restaurant.markets.add(market);
	}

	public void testNormalStandCheck(){
		cook.checkInventory = false;

		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("Cook's orders should be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertEquals("Cashier should not have an log. Instead it is size " + cashier.log.size(), cashier.log.size(), 0);
		assertEquals("Waiter should have an empty event log at the beginning. Instead, the size is " + waiter.log.size(), waiter.log.size(), 0);
		assertTrue("The order stand should be empty. It isn't.", orderMonitor.isEmpty());
		assertTrue("Cook's checkOrderStand variable should be true", cook.checkOrderStand);

		RestaurantOrder order = new RestaurantOrder(waiter, 1, "Steak");
		
		orderMonitor.insert(order);
		
		assertFalse("The order stand should no longer be empty. It isn't.", orderMonitor.isEmpty());
		
		//Pick up the order stand
		assertTrue("Cook's pick and execute an action should return true. It didn't.", cook.pickAndExecuteAnAction());
		
		assertTrue("Cook should have logged a message about checking the stand. Instead, the last logged message is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Checking order stand"));
		assertTrue("Cook should have logegd a message about finding a new order on the stand. Instead, the last logged message is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Found a new order"));
		
		assertEquals("Cook's order list should now be size 1. Instead, it is size " + cook.orders.size(), 1, cook.orders.size());
		
		CookRole.Order cookOrder = cook.orders.get(0);
		assertEquals("Cook's waiter in the cook's order should be correct. Instead, the waiter listed in the order is " + cookOrder.w.getName(), cookOrder.w, waiter);
		assertEquals("The order in the cook's list should be for table 1, instead, the talbe is " + cookOrder.table, cookOrder.table, 1);
		
		
		
		//See if cook processes the order
		assertTrue("Cook's pick and execute an action should now return true. It didn't.", cook.pickAndExecuteAnAction());
		
		assertTrue("Cook should have logged a message about trying to cook steak. Instead, the last logged message is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Try cooking of Steak"));
		
		
		
	}
	
	public void testNormalDontCheckStand(){
		cook.checkInventory = false;
		cook.checkOrderStand = false;
		
		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("Cook's orders should be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertEquals("Cashier should have an log. Instead it is size " + cashier.log.size(), cashier.log.size(), 0);
		assertEquals("Waiter should still have an empty event log at the beginning. Instead, the size is " + waiter.log.size(), waiter.log.size(), 0);
		assertTrue("The order stand should still be empty. It isn't.", orderMonitor.isEmpty());
		
		//Don't check stand
		assertFalse("Cook's scheduler should return false. It didn't.", cook.pickAndExecuteAnAction());
		
		assertEquals("Cook's log should still be size 0. Instead, it is size " + cook.log.size(), cook.log.size(), 0); 
		assertEquals("Cook's orders should still be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertEquals("Cashier should not have an log. Instead it is size " + cashier.log.size(), cashier.log.size(), 0);
		assertEquals("Waiter should have an empty event log at the beginning. Instead, the size is " + waiter.log.size(), waiter.log.size(), 0);
		assertTrue("The order stand should be empty. It isn't.", orderMonitor.isEmpty());


	}
	
	public void testEmptyStand(){
		cook.checkInventory = false;
		cook.checkOrderStand = true;
		
		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("Cook's orders should be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertEquals("Cashier should have an log. Instead it is size " + cashier.log.size(), cashier.log.size(), 0);
		assertEquals("Waiter should have an empty event log at the beginning. Instead, the size is " + waiter.log.size(), waiter.log.size(), 0);
		assertTrue("The order stand should be empty. It isn't.", orderMonitor.isEmpty());
		
		//check an empty order stand
		assertTrue("Cook's scheduler should return true. It didn't.", cook.pickAndExecuteAnAction());
		
		assertEquals("Cook's log should now be size 2. Instead it is size " + cook.log.size(), cook.log.size(), 2);
		assertTrue("Cook should have logged an event about checking the orderStand, but didn't. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Checking order stand"));
		assertTrue("Cook should not have found any orders on the order stand, and so should have logged as much. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("No new orders"));
		assertFalse("Cook's checkOrderStand variable should now be false", cook.checkOrderStand);
		
		assertTrue("The order stand should still be empty. It isn't.", orderMonitor.isEmpty());

	}
	
	public void testNoSendOpening(){
		cook.checkOrderStand = false;
		
		//Original inventory, should not need to order
		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("MarketHost should have an empty log. Instead it is size " + marketHost.log.size(), 0, marketHost.log.size());
		assertTrue("Cook's scheduler should return true. It doesn't.", cook.pickAndExecuteAnAction());
		
		assertEquals("Cook should have a log of size 2. Instead it is size " + cook.log.size(), 2, cook.log.size());
		assertTrue("Cook should have logged an event about checking the inventory. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Need "));
		assertTrue("Cook's second log statement should say that no foods were low. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("No foods are low"));
		assertEquals("MarketHost should have an empty log. Instead it is size " + marketHost.log.size(), 0, marketHost.log.size());
		
		assertFalse("Cook's inventoryCheck boolean should now be false. It isn't.", cook.checkInventory);
		
		assertFalse("Cook's scheduler should return false. It doesn't.", cook.pickAndExecuteAnAction());

	}
	
	public void testSendingOrder(){
		cook.checkOrderStand = false;
		
		cook.foodMap.put("Steak", cook.new Food("Steak", 5000, 20, 100, 20));
		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("Cook's orders should be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertEquals("MarketHost should have an empty log. Instead it is size " + marketHost.log.size(), 0, marketHost.log.size());
		assertTrue("Cook's scheduler should return true. It doesn't.", cook.pickAndExecuteAnAction());
		
		assertEquals("Cook should have a log of size 2. Instead it is size " + cook.log.size(), 2, cook.log.size());
		assertTrue("Cook should have logged an event about checking the inventory. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Need 80 Steak, "));
		assertTrue("Cook's second log statement should say that no foods were low. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("ordering more food"));
		
		Map<String, Integer> order = new HashMap<String, Integer>();
		order.put("Steak", 80);
		
		assertEquals("MarketHost should now have 1 event in the log. Instead, it is size " + marketHost.log.size(), 1, marketHost.log.size());
		assertTrue("MarketHost should have logged a message about receiving the order. Instead, the last logged event is " + marketHost.log.getLastLoggedEvent(), marketHost.log.containsString("Received new order from restaurant for order: " + order.toString()));
		
		assertEquals("Cook's orders should still be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		
		assertFalse("Cook's scheduler should return false. It doesn't.", cook.pickAndExecuteAnAction());
	}
	
	public void testReceivingFoodDelivery(){
		
		cook.foodMap.get("Steak").quantity = 50;
		cook.checkInventory = false;
		cook.checkOrderStand = false;
		
		assertEquals("Cook should have an empty log. Instead it is size " + cook.log.size(), cook.log.size(), 0);
		assertEquals("MarketHost should have an empty log. Instead it is size " + marketHost.log.size(), 0, marketHost.log.size());
		assertEquals("Cook's orders should still be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertFalse("Cook's scheduler should return false. It doesn't.", cook.pickAndExecuteAnAction());
		
		List<OrderItem> order = new ArrayList<OrderItem>();
		order.add(new OrderItem("Steak", 80, 50));
		MarketInvoice invoice = new MarketInvoice(order, market, restaurant, 600);
		
		cook.msgHereIsDelivery(invoice);
		
		assertEquals("Cook's log should now be size 1. Instead it is size " + cook.log.size(), 1, cook.log.size(), 1);
		assertTrue("Cook should have logged a message about the shipment. Instead, the last logged event is " + cook.log.getLastLoggedEvent(), cook.log.containsString("Received 50Steaks, "));
		
		assertEquals("Cook's orders should still be size 0. Instead it is size " + cook.orders.size(), cook.orders.size(), 0);
		assertFalse("Cook's scheduler should still return false. It doesn't.", cook.pickAndExecuteAnAction());
	}
}
