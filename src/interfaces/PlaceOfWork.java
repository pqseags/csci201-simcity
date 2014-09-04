package interfaces;

import role.Role;
import util.JobType;
import util.Loc;

public interface PlaceOfWork {

	public abstract Role canIStartWorking(Person p,JobType type,Role r);
	
	public abstract boolean isOpen();
	
}
