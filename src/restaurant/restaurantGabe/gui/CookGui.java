package restaurant.restaurantGabe.gui;


import restaurant.restaurantGabe.CookRole;
import restaurant.restaurantGabe.CustomerRole;
//import restaurant.restaurantGabe.OldHostAgent;
import restaurant.restaurantGabe.WaiterRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import person.PersonAgent;
import public_Gui.Gui;

public class CookGui implements Gui {

    private WaiterRole agent = null;
    private CookRole cook;

    private int xPos = 350, yPos = 40;//default waiter position
    private int xDestination = 20, yDestination = 20;//default start position

    private static int N_TABLES = 3;
    //public static final int[] xTables = {200,300,400};
    //public static final int[] yTables = {250,250,250};
    
    List<String> cooking = Collections.synchronizedList(new ArrayList<String>());
    List<String> plated = Collections.synchronizedList(new ArrayList<String>());
    
    public int[] xTables;
    public int[] yTables;
    
    public int homePos;
    public int xHome = 375;
    public int yHome = 90;
    
    public int xGrill = 415;
    public int yGrill = 105;
    
    public int xPlate = 415;
    public int yPlate = 55;
    
    public int xFridge = 480;
    public int yFridge = 90;
    
    private Semaphore atGrill = new Semaphore(0,true);
    private Semaphore atPlate = new Semaphore(0,true);
    private Semaphore atFridge = new Semaphore(0,true);
    
    //unused for now, could be used if cook has to travel farther distances and we
    //want to see what he's carrying
    String ownLabel;
    
    //this will display the food that is being brought to the customer
    private String food;
    
    boolean waitingForCust = true;
    boolean waitingToGetToCook = false;
    
    ImageIcon currentImage;
	private int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 12;

    public CookGui(CookRole agent) {
        this.cook = agent;
        if (this.cook == null) {
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_GABE, cook.getName(), "AAAAA Gabe cook is null",null);	
        }
        if (this.cook.person == null) {
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_GABE, cook.getName(), "AAAA Gabe cook.person is null",null);
        }
        currentImage = ((PersonAgent)this.cook.person).downSprites.get(0);
    }
    
    public void updatePosition() {
    	//System.out.println("Going home!");
    	if (xPos < xDestination) {
			xPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.cook.person).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.cook.person).rightSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (xPos > xDestination) {
			xPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.cook.person).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.cook.person).leftSprites.size());
				changeSpriteCounter++;
			}			
		}
		if (yPos < yDestination) {
			yPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.cook.person).downSprites.get(changeSpriteCounter % ((PersonAgent)this.cook.person).downSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (yPos > yDestination) {
			yPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.cook.person).upSprites.get(changeSpriteCounter % ((PersonAgent)this.cook.person).upSprites.size());
				changeSpriteCounter++;
			}
		}

        /*for(int i = 0;i<N_TABLES;++i){
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xTables[i] + 20) & (yDestination == yTables[i] - 20)) {
	           if(!waitingForCust){
	        	   waitingForCust = true;
	        	   agent.msgAtTable();
	           }
	        	//agent.msgAtTable();
	           //break;
	        }
        }*/
        if (xPos == xDestination && yPos == yDestination && xPos==xGrill && yPos==yGrill){
        	//waitingToGetToCook = false;
     	   	//agent.msgAtCook();
        	atGrill.release();
        }
        if (xPos == xDestination && yPos == yDestination && xPos==xPlate && yPos==yPlate+20){
        	//waitingToGetToCook = false;
     	   	//agent.msgAtCook();
        	atPlate.release();
        }
        
        if (xPos == xDestination && yPos == yDestination && xPos==xFridge && yPos==yFridge){
        	//waitingToGetToCook = false;
     	   	//agent.msgAtCook();
        	atFridge.release();
        }
    }
    static int host_width = 20;
    static int host_height = 20;
    
    
    
    public void atTable(){
    	agent.msgAtTable();
    }
    
    public void draw(Graphics2D g) {
    	g.setColor(Color.black);
	    g.drawImage(currentImage.getImage(),xPos, yPos, host_width, host_height,null);
	    g.setFont(stringFont);
	    g.drawString(((PersonAgent)cook.person).personID + ":Cook", xPos-6, yPos-5);
        
        int i = 0;
        synchronized(cooking){
	        for(String food:cooking){
	        	String show = food.substring(0, 2);
	        	g.drawString(show, xGrill + i*20, yGrill + 35);
	        	++i;
	        }
        }
        
        i = 0;
        synchronized(plated){
	        for(String food:plated){
	        	String show = food.substring(0, 2);
	        	g.drawString(show, xPlate + i*20, yPlate + 15);
	        	++i;
	        }
        }   
    }

    public boolean isPresent() {
        return true;
    }

   
    
    public void DoGoToTable(int table_num,boolean waitingForCust){
        xDestination = xTables[table_num] + 20;
        yDestination = yTables[table_num] - 20;
        this.waitingForCust = waitingForCust;
    }

    public void DoLeaveCustomer() {
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoGoToHome(){
    	xDestination = xHome;
        yDestination = yHome;
        //System.out.println("Going to (" +xHome+","+yHome+")");
    }
    
    public void DoCookOrder(String food){
    	xDestination = xFridge;
    	yDestination = yFridge;
    	try {
			atFridge.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	ownLabel = food;
    	xDestination = xGrill;
    	yDestination = yGrill;
    	try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	xDestination = xHome;
    	yDestination = yHome;
    	cooking.add(food);
    }
    
    public void DoPlateOrder(String choice){
    	xDestination = xGrill;
    	yDestination = yGrill;
    	try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	cooking.remove(choice);
    	xDestination = xPlate;
    	yDestination = yPlate+20;
    	try {
			atPlate.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	plated.add(choice);
    	xDestination = xHome;
    	yDestination = yHome;
    }
    
    public void DoRemoveFood(String choice){
    	//xDestination = xPlate;
    	//yDestination = yPlate+20;
    	plated.remove(choice);
    }
    
    public void DoGiveCookOrder(){
        xDestination = -20;
        yDestination = -20;
        waitingToGetToCook = true;
    }

    public void DoGetOrder(){
    	xDestination = -20;
        yDestination = -20;
        waitingToGetToCook = true;
    }
    
    public void setFood(String food){
    	this.food = food;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
