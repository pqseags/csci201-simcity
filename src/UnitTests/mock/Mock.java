package UnitTests.mock;



/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class Mock {
	
	public EventLog log = new EventLog();
	
	private String name;
	
	public Mock(){
		
	}
	
	public String lastLog(){
		return this.log.getLastLoggedEvent().getMessage();
	}

	public Mock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
