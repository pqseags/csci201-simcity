package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cityGui.CityMarket;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import market.gui.MarketPanel;
import restaurant.ProducerConsumerMonitor;
import role.Role;
import util.JobType;
import interfaces.MarketCashier;
import interfaces.MarketCustomer;
import interfaces.MarketDeliveryMan;
import interfaces.MarketEmployee;
import interfaces.MarketHost;
import interfaces.Person;
import interfaces.PlaceOfWork;
import person.*;

public class Market implements PlaceOfWork{
	
	int cash = 0;

	public MarketHost host;
	public MarketCashier cashier;
	public List<MarketEmployee> employees = new ArrayList<MarketEmployee>();
	public List<MarketDeliveryMan> deliveryMen = new ArrayList<MarketDeliveryMan>();
	int money;
	public CityMarket gui;
	
	public MarketPanel panel;
	
	public Map<String, Integer> inventory = new HashMap<String, Integer>();
	
	ProducerConsumerMonitor<MarketInvoice> deliveryDockMonitor = new ProducerConsumerMonitor<MarketInvoice>();
	
	public boolean isOpen = true;
	
	
	//CONSTRUCTOR
	public Market(){
		this.host = new MarketHostRole("DefaultUnmannedHost",null,this);
		this.cashier = new MarketCashierRole("DefaultUnmannedCashier",null, this);
		
		host.setMarket(this);
		cashier.setMarket(this);
		
		inventory.put("Steak", 1000);
		inventory.put("Chicken", 1000);
		inventory.put("Pizza", 1000);
		inventory.put("Salad", 1000);
		inventory.put("Car", 150);
	}
	
	public boolean employeeLeaving(MarketEmployee m){
		
		
		if(((MarketHostRole) host).employeeLeaving(m)){
			return true;
		}
		
		//System.err.println("For some reason the employee wasn't removed, or wasn't in the list");
		return false;
		
		
	}
	
	public boolean employeeLeft(MarketEmployee m){
		panel.animation.removeGui(((MarketEmployeeRole) m).getGui());
		this.employees.remove(m);
		return true;
	}
	
	
	public boolean CanIStartWorking(MarketEmployee m){
		System.out.println("Deciding whether to let employee work");
		if(host.NewEmployee(m)){
			employees.add(m);
			return true;
		}
		//System.err.println("Market Employee wasn't allowed to work");
		return false;
		
	}
	
	public MarketHost CanIBeHost(Person person){
		if(((MarketHostRole) host).p==null || host.YouAreDoneWithShift()){
			((MarketHostRole) host).name = person.getName()+"MarketHost";
			((MarketHostRole) host).p = person;
			return host;
		}
		//System.err.println("New host wasn't allowded to take over");
		return null;
	}
	
	public MarketCashier CanIBeCashier(Person person){
		if(((MarketCashierRole) cashier).p==null || cashier.YouAreDoneWithShift()){
			((MarketCashierRole) cashier).name = person.getName()+ "MarketCashier";
			((MarketCashierRole) cashier).p = person;
			return cashier;
		}
		//System.err.println("New cashier wasn't allowed to take over");
		return null;
	}


	@Override
	public Role canIStartWorking(Person p,JobType jobType,Role m) {
		// TODO Auto-generated method stub
		if(jobType == JobType.MarketEmployee){
			
			if(host.NewEmployee((MarketEmployee) m)){
				System.out.println("Adding a new employee "+p.getName());
				employees.add((MarketEmployee) m);
				((MarketEmployeeRole) m).inEmployeeList = true;
				panel.addEmployee((MarketEmployeeRole) m);
				((MarketEmployeeRole)m).setMonitor(deliveryDockMonitor);
				return m;
			}
			//System.err.println("Market Employee wasn't allowed to work");
		}
		
		else if(jobType == JobType.MarketHost){
			return (Role) CanIBeHost(p);
		}
		
		else if(jobType == JobType.MarketCashier){
			return (Role) CanIBeCashier(p);
		}
		
		else if(jobType == JobType.MarketDeliveryMan){
			
				deliveryMen.add((MarketDeliveryMan) m);
				/*for (MarketEmployee e : employees){
					e.addDeliveryMan((MarketDeliveryMan)m);
				}*/
				((MarketDeliveryManRole)m).setMonitor(deliveryDockMonitor);
				return m;
			
		}
		
		return null;
	}
	
	public void newCustomer(MarketCustomer cust){
		panel.addCustomer((MarketCustomerRole) cust);
	}
	
	public void removeCustomer(MarketCustomer cust){
		//send the cust to the panel to remove it!
		if(panel!=null){
			panel.removeCustomer();
		}
	}
	
	public void deleteCustomer(MarketCustomer cust){
		if (panel!= null){
			panel.deleteCustomer((MarketCustomerRole)cust);
		}
	}
	
	public void setMarketPanel(MarketPanel p){
		panel = p;
	}
	
	public void updateInventory(){
		if(panel!=null){
			panel.updateInventory();
		}
	}
	
	public void DefaultName(Role r){
		if(r instanceof MarketHost){
			((MarketHostRole) r).name = "DefaultUnmannedHost";
		}
		if(r instanceof MarketCashier){
			((MarketCashierRole) r).name = "DefaultUnmannedCashier";
		}
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return isOpen;
	}
	

	
}
