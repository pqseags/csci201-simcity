package role;

import interfaces.Person;
import UnitTests.mock.EventLog;
import agent.Agent;

public abstract class Role extends Agent{
	
	public EventLog log = new EventLog();
	public Person p;
	
	public abstract boolean pickAndExecuteAnAction();
	
	public synchronized void startThread() {
        
		//Don't do anything! This is a role.
		System.out.println("Warning: Role thread is being started.");
		super.startThread();
		
    }
	
	public void StateChanged(){
		super.stateChanged();
	}
	
	public Person getPerson(){
		return p;
	}
}
