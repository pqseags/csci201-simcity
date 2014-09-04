package util;

public class OnRamp {

	
	public OnRamp(Loc loc,Loc sidewalkLoc, boolean vertical,boolean innerLane){
		this.loc = loc;
		this.sidewalkLoc = sidewalkLoc;
		this.vertical = vertical;
		this.innerLane = innerLane;
	}
	
	public Loc loc;
	public Loc sidewalkLoc;
	public boolean vertical;
	public boolean innerLane;
	
}
