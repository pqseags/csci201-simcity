package restaurant.restaurantLinda.gui;

import restaurant.restaurantLinda.CookRole;
import restaurant.restaurantLinda.WaiterRole;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import person.PersonAgent;
import cityGui.CityRestaurantLindaCard;

public class CookGui implements Gui {

	public static final int WINDOWX = CityRestaurantLindaCard.CARD_WIDTH;		//Same as animation panel
    public static final int WINDOWY = CityRestaurantLindaCard.CARD_WIDTH;
	
	CookRole agent;
	

	private int personSize=CityRestaurantLindaCard.PERSONSIZE;
    private int xPos, yPos;//default waiter position
    private int xDestination, yDestination;//default start position
    private goal destination=goal.none;
    private enum goal{none, refrigerator, stoveCooking, stovePlating, platingWindow, leaving};
    private Point home = new Point(WINDOWX-personSize, (int)(WINDOWY*0.5));
    
    private List<MyImage> cookingFoods = new ArrayList<MyImage>();			//Doesn't really need to be a list, since we're only using 1 image at a time...but this does make it possible 
    private List<MyImage> carriedItems = new ArrayList<MyImage>();
    private List<MyImage> platedFoods;
    int plateNum=0; //Just a cheat to cycle through plating areas (lazy way to avoid plate-stacking)
    private MyImage currentItem;
	private String bufferText;
	public final String path="../../images/";
	
	ImageIcon currentImage;
	protected int spriteCounter = 6;
	protected int changeSpriteCounter = 0;
	protected int spriteChangeSpeed = 12;

    public CookGui(CookRole agent) {
    	this.agent = agent;
		currentImage = ((PersonAgent)this.agent.p).downSprites.get(0);
		
    	xPos = xDestination = home.x;
        yPos = yDestination = home.y;        
    }

    public void updatePosition() {    	
    	
    	if (xPos < xDestination) {
			xPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.p).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.p).rightSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (xPos > xDestination) {
			xPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.p).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.p).leftSprites.size());
				changeSpriteCounter++;
			}			
		}
		if (yPos < yDestination) {
			yPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.p).downSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.p).downSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (yPos > yDestination) {
			yPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.p).upSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.p).upSprites.size());
				changeSpriteCounter++;
			}
		}
        
        for (MyImage icon: carriedItems)
        	icon.updatePosition(xPos,yPos);

        if (destination!=goal.none)		//Gui has an actual destination that agent wants to be notified about
        {
        	if (xPos == xDestination && yPos == yDestination && destination==goal.refrigerator){
            	destination=goal.stoveCooking;
        		xDestination = CityRestaurantLindaCard.STOVE.x;
        		yDestination = CityRestaurantLindaCard.STOVE.y-personSize;
        		currentItem = new MyImage("RawFood",xPos,yPos);
        		carriedItems.add(currentItem);
            }
        	else if(xPos == xDestination && yPos == yDestination && destination==goal.stoveCooking){
        		currentItem.x=CityRestaurantLindaCard.STOVE.x;
        		currentItem.y = CityRestaurantLindaCard.STOVE.y;
        		carriedItems.remove(currentItem);
        		cookingFoods.add(currentItem);
        		currentItem=null;
        		destination = goal.none;
        		agent.msgAtDestination();
        	}
        	else if (xPos == xDestination && yPos == yDestination && destination==goal.stovePlating){
            	try{
	        		currentItem.x = xPos;
	        		currentItem.y = yPos;
	        		
	        		destination=goal.platingWindow;
	            	xDestination = WINDOWX-100;
	        		yDestination = CityRestaurantLindaCard.REFRIGERATOR.height+(40*plateNum++);
	        		
	        		plateNum %= (WINDOWY-CityRestaurantLindaCard.REFRIGERATOR.height-CityRestaurantLindaCard.STOVE.height)/40;
      		
	        		for (MyImage i: cookingFoods){
	        			if (i.type.equals("RawFood")){
	        				cookingFoods.remove(i);
	        				break;
	        			}
	        		}
	        		carriedItems.add(currentItem);
            	}
            	catch(NullPointerException e){
            		//Nothing to do. Was just checking to make sure the image has been created, otherwise do nothing until it has been (rare scenario where cook is already at the location)
            	}
            }
        	else if (xPos == xDestination && yPos == yDestination && destination==goal.platingWindow){
        		destination = goal.none;
        		
        		currentItem.x-=50;
        		carriedItems.remove(currentItem);
        		platedFoods.add(currentItem);
        		currentItem=null;
        		
        		agent.msgAtDestination();
        	}
        	else if (xPos == xDestination && yPos == yDestination && destination == goal.leaving){        		
        		destination = goal.none;
        		agent.msgAtDestination();
        	}
        }  
    }

    public void draw(Graphics2D g) {
//        g.setColor(Color.CYAN);
//        g.fillRect(xPos, yPos, personSize, personSize);
    	g.setColor(Color.BLACK);
		g.drawImage(currentImage.getImage(),xPos,yPos,personSize,personSize,null);
		g.setFont(stringFont);
		g.drawString(((PersonAgent)agent.p).personID + ":Cook", xPos-6, yPos-5);
        
        for (MyImage icon: cookingFoods)
        	icon.draw(g);
        
        for (MyImage icon: carriedItems)
        	icon.draw(g);
        
        if (bufferText!=null){
			g.setColor(Color.BLACK);
			g.drawString(bufferText, xPos, yPos+15);
		}
			  
    }

    public boolean isPresent() {
        return agent.p!=null;
    }
    
    public void DoCooking(){
    	destination = goal.refrigerator;

    	xDestination = CityRestaurantLindaCard.REFRIGERATOR.x;
    	yDestination = CityRestaurantLindaCard.REFRIGERATOR.y+CityRestaurantLindaCard.REFRIGERATOR.height;
    }
    
    public void DoPlating(String food){
    	destination = goal.stovePlating;
    	
    	xDestination = CityRestaurantLindaCard.STOVE.x;
		yDestination = CityRestaurantLindaCard.STOVE.y-personSize;
		
		currentItem = new MyImage(food);
    }
    
    public void DoGoToDefault(){
    	xDestination = home.x;
    	yDestination = home.y;
    }
    
    public void DoLeaveRestaurant(){
    	destination = goal.leaving;
    	
    	DoGoToDefault();
    }
    
    public void DoTalk(String text){
		bufferText=text;
	}
    
    public MyImage getCurrentItem(){
    	return currentItem;
    }
    
    public void setPlates(List<MyImage> plates){
    	platedFoods = plates;
    }
}
