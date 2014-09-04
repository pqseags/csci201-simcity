package cityGui.test;

import java.util.ArrayList;
import java.util.List;

import util.Loc;

public class RoadLocs {

	public static List<Loc> locs = new ArrayList<Loc>();
	
	private int[] xs = {
			
	};
	
	private int[] ys = {
			
	};
	
	public RoadLocs(){
		
		for(int i= 0;i<xs.length;++i){
			locs.add(new Loc(xs[i],ys[i]));
		}
		
	}
	
	
}
