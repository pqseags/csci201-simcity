package interfaces;

import util.Task;

public interface BankTeller {
	
	
	public abstract void msgStateChanged();
	
	public abstract void msgIWantTo(Task t);
	
	public abstract void msgDoneAndLeaving();
	
}
