package cityGui.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cityGui.CityComponent;
import astar.*;

public class AStarTraversalPerson extends AStarTraversal{

	public AStarTraversalPerson(Semaphore[][] grid) {
		super(grid);
		// TODO Auto-generated constructor stub
	}
	
	public static int scale = 30;
	
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
			
			//System.out.println("TRY: "+nextX+", "+nextY+" is "+walkable(nextX,nextY));
			
			if(!walkable(nextX,nextY)){
				continue;
			}
			
			
			//get rid of diagonal moves
			if(i*j!=0){
				continue;
			}
			
			//make sure next point is on the grid
			if ((nextX+1>grid.length || nextY+1>grid[0].length) ||
			      (nextX<0 || nextY<0)) continue;
			Position next = new Position(nextX,nextY);
			//System.out.println("considering"+next);
			if (inPath(next,path) /*|| (!next.open(grid) && !CityComponent.onSidewalk(nextX, nextY))*/ ) continue;
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

}
