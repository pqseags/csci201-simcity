package restaurant.restaurantSimon;

import restaurant.restaurantLinda.ProducerConsumerWaiterRole;
import restaurant.restaurantSimon.CashierRole;
import restaurant.restaurantSimon.CustomerRole;
import restaurant.restaurantSimon.CookRole;
import restaurant.restaurantSimon.HostRole;
import restaurant.Restaurant;
import restaurant.restaurantSimon.WaiterRole;




import restaurant.restaurantSimon.gui.CookGui;
import restaurant.restaurantSimon.gui.CustomerGui;
import restaurant.restaurantSimon.gui.HostGui;
//import restaurant.restaurantSimon.gui.ListPanel;
//import restaurant.restaurantSimon.gui.RestaurantGui;
import restaurant.restaurantSimon.gui.WaiterGui;
//import restaurant.restaurantSimon.gui.WaiterListPanel;
import role.Role;
import util.JobType;
import interfaces.BaseRestaurantCustomer;
import interfaces.Person;
import interfaces.restaurantLinda.Waiter;

import javax.swing.*;

import cityGui.CityRestaurantSimon;
import cityGui.CityRestaurantSimonCard;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantSimon extends Restaurant {
	//Host, cook, waiters and customers
	private HostRole host = new HostRole("No Host");
	public CookRole cook = new CookRole(this);
	private CashierRole cashier=new CashierRole("No Cashier",this);

	public CityRestaurantSimon cityRestaurant;
	public CityRestaurantSimonCard animation=null;
	public int customerNum=0;
	private CookGui cookGui=new CookGui(cook,animation);

	boolean p=false;
	
	public boolean isOpen = true;


	public Vector<CustomerRole> customers = new Vector<CustomerRole>();
	private Vector<WaiterRole> waiters = new Vector<WaiterRole>();


	public RestaurantSimon(){
		cook.setGui(cookGui);
	}

	public RestaurantSimon(CityRestaurantSimon cr) {
		this.cityRestaurant=cr;
		cook.setGui(cookGui);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */

	/**
	 * Adds a customer or waiter to the appropriate list
	 *
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	//	public void addPerson(String type, String name, Boolean ifhungry) {
	//
	//		if(!p){
	//		if (type.equals("Customers")) {
	//			CustomerAgent c = new CustomerAgent(name);	
	//			CustomerGui g = new CustomerGui(c, gui);
	//			if(ifhungry)
	//			{
	//				g.setHungry();
	//			}
	//
	//
	//			gui.animationPanel.addGui(g);// dw
	//			c.setHost(host);
	//			c.setCashier(cashier);
	//			c.setGui(g);
	//			customers.add(c);
	//			c.startThread();
	//		}
	//		else if(type.equals("Waiters"))
	//		{
	//			WaiterAgent w=new WaiterAgent(name);
	//			WaiterGui g = new WaiterGui(w,gui);
	//			gui.animationPanel.addGui(g);
	//			w.setGui(g);
	//			w.setHost(host);
	//			w.goToHost(host);
	//			w.setCook(cook);
	//			w.setCashier(cashier);
	//			waiters.add(w);
	//			w.startThread();
	//
	//		}
	//		}
	//	}


	//	public void Pause(){
	//		if(p){
	//			p=false;
	//			WaiterListPanel.p=false;
	//		}
	//		else{
	//			p=true;
	//			WaiterListPanel.p=true;
	//		}
	//		host.pause();
	//		cashier.pause();
	//		cook.pause();
	//		cook.getGui().pause();
	//		m1.pause();
	//		m2.pause();
	//		m3.pause();
	//		////hack!!!! also need to pause host and cook agent& gui here
	//		for(WaiterAgent waiter: waiters){
	//			waiter.pause();
	//			waiter.getGui().pause();
	//		}
	//		for(CustomerAgent customer : customers){
	//			customer.pause();
	//			customer.getGui().pause();
	//		}
	//
	//	}

	@Override
	public Role canIStartWorking(Person p, JobType type, Role r) {

		if (type == JobType.RestaurantHost){
			host.changeShifts(p);
			return (Role)host;
		}
		else if (type == JobType.RestaurantSimonWaiter1){
			for(WaiterRole w: waiters){
				if(w.self==p){
					w.goToHost(host);
					return w;
				}
			}
			WaiterRole tw=new WaiterRole(p, p.getName(), host, cook, cashier,this);
			waiters.add(tw);
			waiterComingtoWork(tw);
			return tw;
		}
		//		else if (type == JobType.RestaurantWaiter2){
		//			((WaiterRole)r).setRestaurant(this);
		//			((ProducerConsumerWaiterRole)r).setMonitor(orderMonitor);
		//			waiterComingToWork((Waiter) r);
		//			return r;
		//		}
		else if (type == JobType.RestaurantCook){
			cook.changeShifts(p);
			//System.err.println(animation==null);
			animation.addGui(cookGui);
			return  (Role)cook;
		}
		else if (type == JobType.RestaurantCashier){
			cashier.changeShifts(p);
			return cashier;
		}

		//System.out.println("Unrecognized job type: " + type);
		return null;
	}

	public void waiterComingtoWork(WaiterRole w){
		WaiterGui wg=new WaiterGui(w,animation);
		animation.addGui(wg);
		w.setGui(wg);
		w.goToHost(host);		
	}
	@Override
	public void customerEntering(BaseRestaurantCustomer c) {
		customerNum++;
		((CustomerRole)c).setPeople(host, cashier,this);
		CustomerGui cg = new CustomerGui(((CustomerRole)c), animation);
		((CustomerRole)c).setGui(cg);
		cg.setHungry();
		animation.addGui(cg);
		customers.add(((CustomerRole)c));
		
	}
	public void setAnimationPanel(CityRestaurantSimonCard crc){
		animation=crc;
		
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}
		
	
}
