package util;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;
import interfaces.BankInterface;
import interfaces.Person;
import interfaces.PlaceOfWork;
import bank.BankTellerRole;
import role.Role;
import market.*;

public class Job {
	
	public Job(){
		
	}
	
	public Job(Role role,int shiftStart,int shiftEnd,PlaceOfWork placeOfWork,Person person,JobType jt){
		this.jobRole = role;
		//this.location = location;
		this.shiftStart = shiftStart;
		this.shiftEnd = shiftEnd;
		this.placeOfWork = placeOfWork;
		this.jobType = jt;
		if(jobType == JobType.BankTeller){
			((BankTellerRole) jobRole).setPerson(person);
			((BankTellerRole) jobRole).setBank((Bank) placeOfWork);
		}
		if(jobType == JobType.MarketEmployee){
			((MarketEmployeeRole) jobRole).setPerson((PersonAgent) person);
			((MarketEmployeeRole) jobRole).setMarket((Market) placeOfWork);
		}
		
	}
	
	public Role jobRole;
	public PlaceOfWork placeOfWork;
	//public int location;
	public int shiftStart;
	public int shiftEnd;
	public JobType jobType;
		
		
		
}
