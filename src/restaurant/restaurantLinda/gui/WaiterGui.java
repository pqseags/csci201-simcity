package restaurant.restaurantLinda.gui;

import restaurant.restaurantLinda.WaiterRole;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;

import person.PersonAgent;
import cityGui.CityRestaurantLindaCard;
import astar.*;

public class WaiterGui extends GuiPerson {	
	private WaiterRole agent = null;

    private int personSize=25;
    private Map<Integer,Point> tableMap;
    private List<MyImage> platedFoods;
    private goal destination=goal.none;
    private enum goal{none,customer,customerTable, table,cook,plating, leaving};
    private CustomerGui customer;
    
    Position homePosition;
    
    int wait=0;
    int attempts=1;
    
    
    private List<MyImage> carriedItems = Collections.synchronizedList(new ArrayList<MyImage>());
    private List<MyImage> stillItems = Collections.synchronizedList(new ArrayList<MyImage>());
	private String bufferText;
	
	public boolean isPresent = true;
	

    public WaiterGui(WaiterRole agent, int position, AStarTraversal aStar) {
        this.agent = agent;
        this.aStar = aStar;
        
        this.pSprites = agent.p;
		currentImage = ((PersonAgent)this.agent.p).downSprites.get(0);
		
        int limit = (WINDOWX-300)/cellSize;
        if (position>=limit){
        	position%=limit;
        }
        
        homePosition = new Position(1+position, WINDOWY/cellSize-1);
        
        xPos = yPos = 0;
        
        xPos = xDestination = xfinal = homePosition.getX()*cellSize;
        yPos = yDestination = yfinal = homePosition.getY()*cellSize;
        System.out.println("homePosition = " + homePosition);
        //previousPosition = currentPosition = new Position(xPos/cellSize, yPos/cellSize);
        //currentPosition.moveInto(aStar.getGrid());
        //CalculatePath(homePosition);
        
    }
    
    public void DoGoToCustomer(CustomerGui customer) {
    	bufferText = null;
    	destination=goal.customer;
    	this.customer = customer;
    	xfinal = customer.getDestination().x + personSize;
    	yfinal = customer.getDestination().y + personSize;
    	CalculatePath(new Position(xfinal/cellSize, yfinal/cellSize)); 
    }

    public void DoSeatCustomer(CustomerGui customer, int table) {
    	bufferText = "Follow Me";
        destination=goal.customerTable;
        this.customer = customer;
        xfinal = tableMap.get(table).x + personSize;
        yfinal = tableMap.get(table).y - personSize;
        CalculatePath(new Position(xfinal/cellSize, yfinal/cellSize));
        //customer.DoGoToSeat(tableMap.get(table).x,tableMap.get(table).y);
    }
    
    public void DoGoToTable(int table){
    	destination= goal.table;
    	 xfinal = tableMap.get(table).x + personSize;
         yfinal = tableMap.get(table).y - personSize;
    	CalculatePath(new Position(xfinal/cellSize, yfinal/cellSize));
    }
    
    public void DoGoToCook(){
    	destination = goal.cook;
    	xfinal = WINDOWX-150-personSize;
    	yfinal = (int) (WINDOWY*0.5);
    	CalculatePath(new Position(xfinal/cellSize, yfinal/cellSize));
    }
    
    public void DoGoToPlatingArea(String food){
    	destination = goal.plating;
    	
    	synchronized(platedFoods){
    		for(MyImage plate: platedFoods){
    			if (plate.type.equals(food)){
    				xfinal = plate.x-personSize;
    				yfinal = plate.y;
    				platedFoods.remove(plate);
    				stillItems.add(plate);
    				break;
    			}
    		}
    	}  	
    	CalculatePath(new Position(xfinal/cellSize, yfinal/cellSize));
    }
    
    public void DoServeFood(String food,int table){
    	synchronized(stillItems){
	    	for (MyImage icon: stillItems){
	    		if (icon.type.equals(food)){
	    			icon.x = xPos;
	    			icon.y = yPos;
	    			carriedItems.add(icon);
	    			stillItems.remove(icon);
	    			break;
	    		}
	    	}
    	}
    	DoGoToTable(table);
    }
    
    public void DoRelinquishFood(){
    	carriedItems.clear();
    	bufferText=null;
    }
    
    public void DoGoToDefault(){
    	bufferText = null;
    	
    	if (xfinal==homePosition.getX()*cellSize && yfinal == homePosition.getY()*cellSize)
    		return;
    	
    	xfinal = homePosition.getX()*cellSize;
    	yfinal = homePosition.getY()*cellSize;
    	CalculatePath(homePosition);
    }
    
    public void DoGoOnBreak(){
    	//bufferText="Break";
    	xfinal = homePosition.getX()*cellSize;
    	yfinal = homePosition.getY()*cellSize;
    	CalculatePath(homePosition);
    }
    
    public void DoGoOffBreak(){
    	bufferText = null;
    	DoGoToDefault();
    }
    
    
    public void DoLeaveRestaurant(){
    	//Just leave out the "back" which is the bottom half of the restaurant
    	destination = goal.leaving;
    	xfinal = xPos;
    	yfinal = CityRestaurantLindaCard.CARD_HEIGHT;
    	
    	CalculatePath(homePosition);
    }
    
    public void DoTalk(String text){
		bufferText=text;
	}
    
    public void UpdateInfo(){
    	//gui.updateWaiter(agent);
    }

    public void updatePosition() {
    	
    	if (moveAndCheckDestination())		//Gui has an actual destination that agent wants to be notified about
        {        	
    		if (destination == goal.customerTable){
    			customer.followMe(xPos-personSize, yPos-personSize);
    			customer.goToTable(xPos-personSize, yPos+personSize);
    			destination = goal.none;
    			agent.msgAtDestination();
    		}
    		else if (destination!=goal.none){
	    		path = null;
	    		xDestination = xfinal;
	    		yDestination = yfinal;
	    		destination=goal.none;
	            agent.msgAtDestination();
    		}
        }
    	
    	if (destination == goal.customerTable){
    		customer.followMe(xPos-personSize, yPos-personSize);
		}
    	
    	synchronized(carriedItems){
	    	for (MyImage icon: carriedItems)
	        	icon.updatePosition(xPos,yPos);
    	}
    }

    public void draw(Graphics2D g) {
//        g.setColor(Color.MAGENTA);
//        g.fillRect(xPos, yPos, personSize, personSize);
    	g.setColor(Color.BLACK);
		g.drawImage(currentImage.getImage(),xPos,yPos,personSize,personSize,null);
		g.setFont(stringFont);
		g.drawString(((PersonAgent)agent.p).personID + ":Waiter", xPos-6, yPos-5);
        
        synchronized(carriedItems){
	        for (MyImage icon: carriedItems)
	        	icon.draw(g);
        }
        
        synchronized(stillItems){
	        for (MyImage icon: stillItems)
	        	icon.draw(g);
        }
        
        if (bufferText!=null){
			g.setColor(Color.BLACK);
			g.drawString(bufferText, xPos, yPos+15);
		}
			  
    }
    	
    public void setTables(Map<Integer,Point> map)
    {
     	tableMap = map;
    }
     
    public void setPlates(List<MyImage> plates){
      	platedFoods = plates;
    }
    
    public String getPosition(){
    	return xPos + " " + yPos;
    }
}
