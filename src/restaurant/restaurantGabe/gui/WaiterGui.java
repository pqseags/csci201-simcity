package restaurant.restaurantGabe.gui;


import restaurant.restaurantGabe.CustomerRole;
//import restaurant.OldHostAgent;
import restaurant.restaurantGabe.WaiterRole;

import java.awt.*;

import javax.swing.ImageIcon;

import person.PersonAgent;
import public_Gui.Gui;

public class WaiterGui implements Gui {

    private WaiterRole agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    public int xCook = 350;
    public int yCook = 90;
    
    ImageIcon currentImage;
	public int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 12;


    private static int N_TABLES = 3;
    //public static final int[] xTables = {200,300,400};
    //public static final int[] yTables = {250,250,250};
    
    public int[] xTables;
    public int[] yTables;
    
    public int homePos;
    int xHome = 40;
    int yHome = 20;
    
    //this will display the food that is being brought to the customer
    private String food;
    
    boolean waitingForCust = true;
    boolean waitingToGetToCook = false;
    boolean waitingForHome = false;

    public WaiterGui(WaiterRole agent) {
        this.agent = agent;
        currentImage = ((PersonAgent)this.agent.person).downSprites.get(0);

    }
    
    public WaiterGui(WaiterRole agent,int homePos) {
        this.agent = agent;
        currentImage = ((PersonAgent)this.agent.person).downSprites.get(0);
        this.homePos = homePos;
        this.xHome = 70 + 30*homePos;
        this.yHome = 20;
        //System.out.println(""+xHome);
    }

    public void updatePosition() {
    	if (xPos < xDestination) {
			xPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.person).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.person).rightSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (xPos > xDestination) {
			xPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.person).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.person).leftSprites.size());
				changeSpriteCounter++;
			}			
		}
		if (yPos < yDestination) {
			yPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.person).downSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.person).downSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (yPos > yDestination) {
			yPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.agent.person).upSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.person).upSprites.size());
				changeSpriteCounter++;
			}
		}

        for(int i = 0;i<N_TABLES;++i){
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xTables[i] + 20) & (yDestination == yTables[i] - 20)) {
	           if(!waitingForCust){
	        	   waitingForCust = true;
	        	   agent.msgAtTable();
	           }
	        	//agent.msgAtTable();
	           //break;
	        }
        }
        if (xPos == xDestination && yPos == yDestination && waitingToGetToCook){
        	waitingToGetToCook = false;
     	   	agent.msgAtCook();
        }
        if(xPos == xDestination && yPos == yDestination && waitingForHome){
        	waitingForHome = false;
        	agent.msgAtHomePosition();
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
    	g.drawString(((PersonAgent)agent.person).personID + ":Waiter", xPos-6, yPos-5);
        if(food!=null){
        	g.drawString(food, xPos, yPos+host_height/2);
        }
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerRole customer,int table_num) {
        xDestination = xTables[table_num] + 20;
        yDestination = yTables[table_num] - 20;
        customer.customerGui.DoGoToSeatDirected(this,xTables[table_num], yTables[table_num]);
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
    
    public void DoGoToHome(boolean waitingForHome){
    	xDestination = xHome;
        yDestination = yHome;
        this.waitingForHome = waitingForHome;
        //System.out.println("Going to (" +xHome+","+yHome+")");
    }
    
    public void DoGiveCookOrder(){
        xDestination = xCook;
        yDestination = yCook;
        waitingToGetToCook = true;
    }
    
    public void doPutOrderOnStand(){
    	xDestination = xCook+10;
    	yDestination = yCook+20;
    	waitingToGetToCook = true;
    	
    }

    public void DoGetOrder(){
    	xDestination = xCook;
        yDestination = yCook - 35;
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
