package cityGui.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cityGui.CityComponent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Node;
import astar.Position;

public class AStarTraversalVehicle extends AStarTraversal{

	public static int scale = 30;
	
	public AStarTraversalVehicle(Semaphore[][] grid) {
		super(grid);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Node> expandFunc(Node n) {
		//System.out.println("This expand being called");
		AStarNode node = (AStarNode) n;
		//loop computes the positions you can get to from node
		List<Node> expandedNodes = new ArrayList<Node>();
		List<Position> path = node.getPath();
		Position pos = node.getPosition();
		int x = pos.getX();
		int y = pos.getY();
		//this next pair of loops will create all the possible moves
		//from pos.
		for(int i = -1; i <= 1; i++) {//increment for x direction
		    for (int j = -1; j <= 1; j++) {//increment for y direction
			//create the potential next position
			int nextX=x+i;
			int nextY=y+j;
			
			
			
			if(!inSameLane(scale*x,scale*y,scale*nextX,scale*nextY)){
				continue;
			}
			
			if(!acceptableAngleChange(scale*x,scale*y,scale*nextX,scale*nextY)){
				continue;
			}
			
//			//get rid of diagonal moves
			if(i*j!=0){
				continue;
			}
			
			//make sure next point is on the grid
			if ((nextX+1>grid.length || nextY+1>grid[0].length) ||
			      (nextX<0 || nextY<0)) continue;
			Position next = new Position(nextX,nextY);
			//System.out.println("considering"+next);
			
			//COMMENTING OUT THIS LINE
			//if (inPath(next,path) || (!next.open(grid) && !CityComponent.onSidewalk(nextX, nextY)) ) continue;
			
			if (inPath(next,path)) continue;
			//printCurrentList();
			//System.out.println("available"+next);
			AStarNode nodeTemp = new AStarNode(next);
	
			//update distance travelled
			nodeTemp.setDistTravelled(
	                        node.getDistTravelled()+pos.distance(next));
			//update approximate total distance to destination
			//note that we are computing the straight-line
			//heuristic on the fly right here from next to endingState
			nodeTemp.setApproxTotalDist(
				nodeTemp.getDistTravelled() + next.distance((Position)endingState));	
			//update internal path
			nodeTemp.updatePath(path);
			expandedNodes.add(nodeTemp);//could have just added
						    //them directly to nodelist 
		    }
		}
		
		return expandedNodes;
    }//end expandFunc
	
	public static boolean walkable(int xChunk,int yChunk){
		int x = scale*xChunk;
		int y = scale*yChunk;
		
		if(CityComponent.onSidewalk(x, y)){
			return true;
		}
		
		//crosswalk
		if((x>=280 && x<=320)&&(y>=80 && y<=160)){
			return true;
		}
		if((x>=280 && x<=320)&&(y>=440 && y<=520)){
			return true;
		}
		if((x>=80 && x<=160)&&(y>=280 && y<=320)){
			return true;
		}
		if((x>=440 && x<=520)&&(y>=280 && y<=320)){
			return true;
		}
		return false;
		
	}
	
	public boolean inRoad(int xChunk, int yChunk){
		int x = scale*xChunk;
		int y = scale*yChunk;
		
		//crosswalk
		if((x>=280 && x<=320)&&(y>=80 && y<=160)){
			return false;
		}
		if((x>=280 && x<=320)&&(y>=440 && y<=520)){
			return false;
		}
		if((x>=80 && x<=160)&&(y>=280 && y<=320)){
			return false;
		}
		if((x>=440 && x<=520)&&(y>=280 && y<=320)){
			return false;
		}
		
		
		//initial check
		if(x<80 || x>520 || y<80 || y>520){
			return false;
		}
		
		if((x>80 && x<520) && (y>80 && y<160)){
			return true;
		}
		if((x>80 && x<160) && (y>80 && y<520)){
			return true;
		}
		if((x>80 && x<520) && (y>440 && y<520)){
			return true;
		}
		if((x>440 && x<520) && (y>440 && y<520)){
			return true;
		}
		return false;
		
	}
	
	public static boolean inClockwiseLane(int x,int y){
		
		if(!((x>=120 && x<=480)&&(y>=120 && y<=480))){
			return false;
		}
		
		if((x>120 && x<=440)&&(y>=140 && y<=440)){
			return false;
		}
		
		return true;
	}
	
	public static boolean inCounterClockwiseLane(int x,int y){
		
		if(!((x>=80 && x<=520)&&(y>=80 && y<=520))){
			return false;
		}
			
		if((x>=120 && x<=480)&&(y>=120 && y<=480)){
			return false;
		}
		
		return true;
	}
	
	
	//tells if the two spots are in the same lane. Needed for expandFunc
	public static boolean inSameLane(int startX,int startY,int endX,int endY){
		
		if(inClockwiseLane(startX,startY) && inClockwiseLane(endX,endY)){
			return true;
		}
		
		if(inCounterClockwiseLane(startX,startY) && inCounterClockwiseLane(endX,endY)){
			return true;
		}
		
		return false;
		
		
	}
	
//	//calculates the angle change between two points in the (x,y) plane...
//	//will make expandFund much easier to write
//	public static double deltaTheta(int startX,int startY,int endX,int endY){
//		
//		int x1 = startX - 300;
//		int x2 = endX - 300;
//		int y1 = startY - 300;
//		int y2 = endY - 300;
//		
//		double a = Math.pow(Math.pow(x1-x2,2)+Math.pow(y1-y2, 2), .5);
//		double b = Math.pow(Math.pow(x1,2)+Math.pow(y1, 2), .5);
//		double c = Math.pow(Math.pow(x2,2)+Math.pow(y2, 2), .5);
//		
//		
//		return Math.acos((b*b + c*c - a*a)/(2*b*c));
//		
//		
//		
//	}
	
	public static boolean acceptableAngleChange(int startX,int startY,int endX,int endY){
		
		int x1 = startX - 300;
		int x2 = endX - 300;
		int y1 = startY - 300;
		int y2 = endY - 300;
		
		if(inClockwiseLane(startX,startY)){
			//System.out.println("CLockwise");
			if(x2*y1 - y2*x1<0){
				//System.out.println("HERE: ("+ x1+", "+y1+")"+" ("+x2+", "+y2+")");
				return true;
			}
			else{	
				return false;
			}
		}
		
		else if(inCounterClockwiseLane(startX,startY)){
			if(x2*y1 - y2*x1>0){
				return true;
			}
			else{	
				return false;
			}
		}
		
		//System.err.println("Car is neither in clockwise nor counterclockwise lane - PROBLEM");
		return false;
		
		
	}


	
	
}
