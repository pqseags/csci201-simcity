
package restaurant.restaurantParker.gui;

import public_Gui.Gui;
import restaurant.restaurantParker.CustomerRole;
import restaurant.restaurantParker.WaiterRole;
import restaurant.restaurantParker.gui.CustomerGui.AnimState;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterRole agent = null;
   // RestaurantGui gui;
    
    private boolean isOnBreak = false;
    
	private boolean hasFood = false;
	private String food;
	

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    private int xInitial;
    private int yInitial;
    
    private final int waitRoomInitialx = 57;
    private final int waitRoomSpacing = 25;
    
   //TABLE LOCATIONS
    private final int xTable1 = 50;
    private final int yTable1 = 130;
    
    private final int xTable2 = 180;
    private final int yTable2 = 130;
    
    private final int xTable3 = 310;
    private final int yTable3 = 130;
    
    private final int xKitchen = 350;
    private final int yKitchen = 270;
    
    private final int xBreakRoom = 10;
    private final int yBreakRoom = 290;
    
    private final int xCashier = 380;
    private final int yCashier = 10;
    
    private final int xCustomerRoom = 77;
    private final int yCustomerRoom = 37;


    public WaiterGui(WaiterRole agent,/* RestaurantGui gui,*/ int position) {
        this.agent = agent;
       // this.gui = gui;
        
        
        switch (position) {

        case 1: yInitial = waitRoomInitialx;	
        	break;
        case 2: yInitial = waitRoomInitialx + waitRoomSpacing;
        	break;
        case 3: yInitial = waitRoomInitialx + 2*waitRoomSpacing;
        	break;
        case 4: yInitial = waitRoomInitialx + 3*waitRoomSpacing;
    	break;
        case 5: yInitial = waitRoomInitialx + 4*waitRoomSpacing;
    	break;
        case 6: yInitial = waitRoomInitialx + 5*waitRoomSpacing;
    	break;
        case 7: yInitial = waitRoomInitialx + 6*waitRoomSpacing;
    	break;
        case 8: yInitial = waitRoomInitialx + 7*waitRoomSpacing;
    	break;
        case 9: yInitial = waitRoomInitialx + 8*waitRoomSpacing;
    	break;
    	default: return;

        }
        xInitial  = 12;
        
 		xPos = xInitial;
		xDestination = xInitial;
		yPos = yInitial;
		yDestination = yInitial;
        
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination && xPos != xInitial && yPos != yInitial && xPos!= xBreakRoom +50)
        	/*	& (xDestination == xTable + 20) & (yDestination == yTable - 20)) */{
           agent.msgAtDestination();
        }
        /*
        if (xPos == xInitial && yPos == -20)
        {
        	this.agent.isReady = true;
        //	System.out.print("true");
        }*/
        else 
        {
        //	this.agent.isReady = false;
        //	System.out.println("false");
        }
        
       
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        
        if (hasFood){
        	g.setColor(Color.BLACK);
        	g.drawString(food, xPos, yPos);
        	
        }
    }

    public void gotFood(String choice){
    	hasFood = true;
    	food = choice;
    }
    
    public void deliveredFood(){
    	hasFood = false;
    }
    
    public boolean isPresent() {
        return true;
    }
    
    public void setOnBreak(){
    	isOnBreak = true;
    	agent.gotWantBreak();
    	
    }
    
    public void setOffBreak(){
    	isOnBreak = false;
    	agent.gotOffBreak();
    }
    
    public boolean isOnBreak(){
    	return isOnBreak;
    }

    public void DoBringToTable(CustomerRole customer, int tableNumber) {
    	 
        if (tableNumber == 1)
		{
			xDestination = xTable1 + 20;
			yDestination = yTable1 -20;
		}
		if (tableNumber == 2)
		{
			xDestination = xTable2 +20;
			yDestination = yTable2 -20;
		}
		if (tableNumber == 3)
		{
			xDestination = xTable3 +20;
			yDestination = yTable3 -20;
		}
    }
    
    public void DoGoToTable(int tableNumber){
    	 if (tableNumber == 1)
 		{
 			xDestination = xTable1 + 20;
 			yDestination = yTable1 -20;
 		}
 		if (tableNumber == 2)
 		{
 			xDestination = xTable2 +20;
 			yDestination = yTable2 -20;
 		}
 		if (tableNumber == 3)
 		{
 			xDestination = xTable3 +20;
 			yDestination = yTable3 -20;
 		}
    }
    
    public void DoGoToKitchen(){
    	
    		xDestination = xKitchen;
    		yDestination = yKitchen -20;
   
    
    }
    
    public void DoGoToLobbyPosition(int position){
    	xDestination = (xCustomerRoom + (position*25));
    	yDestination = (yCustomerRoom);
    }
    
    public void DoGoToCashier(){
    	xDestination = xCashier + 15;
    	yDestination = yCashier + 25;
    }
    
    public void DoAskForBreak(){
    	//gui.setWaiterAskedBreak(agent);
    }
    
    public void DoGoOnBreak(){
    	xDestination = xBreakRoom + 50;
    	yDestination = yBreakRoom;
    }
    
    
    public void CantGoOnBreak(){
    	isOnBreak = false;
    	//gui.setWaiterOffBreak(agent);
    }

    public void DoLeaveCustomer() {
        xDestination = xInitial;
        yDestination = yInitial;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    

    
}
