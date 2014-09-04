package restaurant.restaurantLinda.gui;

import interfaces.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import person.PersonAgent;
import cityGui.CityRestaurantLinda;
import cityGui.CityRestaurantLindaCard;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import restaurant.restaurantLinda.gui.MyImage;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;

public abstract class GuiPerson implements Gui{
	
	public static final int WINDOWX = CityRestaurantLindaCard.CARD_WIDTH;		//Same as animation panel
    public static final int WINDOWY = CityRestaurantLindaCard.CARD_HEIGHT;
    public final int cellSize = CityRestaurantLinda.cellSize;
	
    protected int personSize=25;
    protected int xPos, yPos;
    protected int xDestination, yDestination;
    protected int xfinal, yfinal;
    
    AStarTraversal aStar;
    //Position previousPosition;
    Position currentPosition;
    Position diagonalPosition;
    List<Position> path;   
    
    Person pSprites;

	ImageIcon currentImage;
	protected int spriteCounter = 6;
	protected int changeSpriteCounter = 0;
	protected int spriteChangeSpeed = 12;
    
    enum movementType {undefined, upleft, upright, downleft, downright};
    
    int wait=0;
    int attempts=1;
    
    
    private List<MyImage> carriedItems = new ArrayList<MyImage>();
    private List<MyImage> stillItems = new ArrayList<MyImage>();
	private String bufferText;

    public boolean isPresent() {
        return true;
    }
        
    //Returns a boolean stating whether or not the gui has traveled to the 'final destination'
    public boolean moveAndCheckDestination() {
    	
    	if (wait>0){
    		wait--;
    		return false;
    	}
    	
    	//if (destination == goal.customer){
    	//	CalculatePath(new Position(customer.getPosition().x/personSize+1,customer.getPosition().y/personSize+1)); 
    	//}
    	
    	if (xPos < xDestination) {
			xPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.pSprites).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.pSprites).rightSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (xPos > xDestination) {
			xPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.pSprites).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.pSprites).leftSprites.size());
				changeSpriteCounter++;
			}			
		}
		if (yPos < yDestination) {
			yPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.pSprites).downSprites.get(changeSpriteCounter % ((PersonAgent)this.pSprites).downSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (yPos > yDestination) {
			yPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.pSprites).upSprites.get(changeSpriteCounter % ((PersonAgent)this.pSprites).upSprites.size());
				changeSpriteCounter++;
			}
		}
        
        
                
        if (xfinal==xPos && yfinal ==yPos)
        {  
        	xDestination = xfinal;
    		yDestination = yfinal;
    		return true;
        }
        else if (xPos == xDestination && yPos == yDestination){
        	//System.out.println("finished2");
        	/*if (previousPosition!=currentPosition && previousPosition!=null){
        		previousPosition.release(aStar.getGrid());
        		previousPosition = currentPosition;
        	}*/

        	//1 means we reached our destination
        	if (path!=null && path.size()>1)
        		MoveToNextPosition();
        	else if (path!=null){
        		path=null;
        		//AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, "GUIPERSON", "Try to change destination");
        		if (xDestination!=xfinal || yDestination!=yfinal){
        			xDestination=xfinal;
        			yDestination=yfinal;
        		}
        	}
        }
        
        return false;
    }
    
    void CalculatePath(Position to){
    	//No point in wasting resources recalculating a path if the destination is the same
    	if (path!=null)
    		if (path.get(path.size()-1).equals(to))
    			return;
    	
    	path = null;
    	
    	/*if (currentPosition!=null)
    		currentPosition.release(aStar.getGrid());*/
    	/*boolean wasOpen=true;
    	try{
    		wasOpen = to.open(aStar.getGrid());
    	}
    	catch (ArrayIndexOutOfBoundsException e){
    		AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, "GuiPerson", "Array Out of Bounds Exception when trying check position" + to);
    		e.printStackTrace();
    	}
    	if(!wasOpen)
    		to.release(aStar.getGrid());*/
    	
    	//System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());
    	
    	System.gc();
    	
    	AStarNode aStarNode;
    	
    	//if (previousPosition==null)
    		aStarNode = (AStarNode)aStar.generalSearch(new Position(xPos/cellSize, yPos/cellSize), to);		
    	/*else
    		aStarNode = (AStarNode)aStar.generalSearch(previousPosition, to);*/
    	path = aStarNode.getPath();
    	
    	//System.out.println("path = " + path);
    	
    	/*if(!wasOpen)
    		to.moveInto(aStar.getGrid());
    	
    	currentPosition = path.get(0);
    	currentPosition.moveInto(aStar.getGrid());

    	if (xPos/cellSize < currentPosition.getX())
	    	xDestination = currentPosition.getX()*cellSize;
    	else if (xPos/cellSize == currentPosition.getX() && (xPos+personSize)/cellSize == currentPosition.getX())
    		xDestination = xPos;
    	else
    		xDestination = (currentPosition.getX()+1)*cellSize - personSize;
    		
    	
    	if (yPos/cellSize < currentPosition.getY())
    		yDestination = currentPosition.getY()*cellSize;
    	else if (yPos/cellSize == currentPosition.getY() && (yPos+personSize)/cellSize == currentPosition.getY())
    		yDestination = yPos;
    	else
    		yDestination = (currentPosition.getY()+1)*cellSize - personSize;
    	*/
    	
    }
    
    void MoveToNextPosition(){
	    //Try and get lock for the next step.
	    //int attempts    = 1;
	    //boolean gotPermit   = new Position(path.get(1).getX(), path.get(1).getY()).moveInto(aStar.getGrid());
    	boolean gotPermit= true;

	    //Did not get lock. Lets make n attempts.
	    /*while (!gotPermit && attempts < 3) {
			System.out.println("[Gaut] " + agent.getName() + " got NO permit for " + path.get(1).toString() + " on attempt " + attempts);
	
			//Wait for 1sec and try again to get lock.
			try { Thread.sleep(1000); }
			catch (Exception e){}
	
			gotPermit   = new Position(path.get(1).getX(), path.get(1).getY()).moveInto(aStar.getGrid());
			attempts ++;
	    }*/
	    
	    if (!gotPermit && attempts<3){
	    	//System.err.println("got NO permit for " + path.get(1).toString() + " on attempt " + attempts);
	    	AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, "GuiPerson", "Got no permit for " + path.get(1).toString() + " on attempt " + attempts, null);
	    	Random rand = new Random();
	    	
	    	wait=rand.nextInt(10) + 10;
	    	attempts++;
	    	return;
	    }

	    //Did not get lock after trying n attempts. So recalculating path.            
	    if (!gotPermit) {
			//System.out.println("[Gaut] " + agent.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
	    	AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, "GuiPerson", "Recalculating path", null);
			attempts=1;
			CalculatePath(path.get(path.size()-1));
			return;
	    }

	    //Got the required lock. Lets move.
	    //System.out.println("[Gaut] " + agent.getName() + " got permit for " + path.get(0).toString());
	    attempts=1;
	    currentPosition = path.get(1);
	    //currentPosition.moveInto(aStar.getGrid());

	    path.remove(0);
	    
	    
	    if (xPos/cellSize < currentPosition.getX())
	    	xDestination = currentPosition.getX()*cellSize;
    	else if (xPos/cellSize == currentPosition.getX() && (xPos+personSize)/cellSize == currentPosition.getX())
    		xDestination = xPos;
    	else
    		xDestination = (currentPosition.getX()+1)*cellSize - personSize;
    		
    	
    	if (yPos/cellSize < currentPosition.getY())
    		yDestination = currentPosition.getY()*cellSize;
    	else if (yPos/cellSize == currentPosition.getY() && (yPos+personSize)/cellSize == currentPosition.getY())
    		yDestination = yPos;
    	else
    		yDestination = (currentPosition.getY()+1)*cellSize - personSize;
    	
    	//System.out.println("Position: " + xPos + " " + yPos + "     Destination: "  + xDestination + " " + yDestination);
    }

}