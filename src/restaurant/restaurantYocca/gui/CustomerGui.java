package restaurant.restaurantYocca.gui;

import person.PersonAgent;
import public_Gui.Gui;
import restaurant.restaurantYocca.CustomerRole;
import restaurant.restaurantYocca.HostRole;
import restaurant.restaurantYocca.RestaurantYocca;
import interfaces.restaurantYocca.Customer;

import java.awt.*;

import javax.swing.ImageIcon;

import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

public class CustomerGui implements Gui {
    
	public CustomerRole agent;
	private boolean isPresent = true;
	private boolean isHungry = false;
	private boolean hasArrived = false;
    
//	RestaurantGui gui;
	RestaurantYocca restaurant;
    
	private int cashierX = 230;
	private int cashierY = 40;
	private int homeX;
	private int homeY;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private String foodString = "";
	private String statusString = "";
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	ImageIcon currentImage;
	private int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 12;
	
	public CustomerGui(CustomerRole agent) {
        this.agent = agent;
        currentImage = ((PersonAgent)this.agent.p).downSprites.get(0);
    }    
    
	public int xTable;
	public int yTable;
	
	public CustomerGui(CustomerRole c, RestaurantYocca rYocca){ //HostAgent m) {
		agent = c;
        currentImage = ((PersonAgent)this.agent.p).downSprites.get(0);
		xPos = 0;
		yPos = 300;
		restaurant = rYocca;
		try {
			yDestination = 75 + rYocca.customers.size() * 25;
		} catch (NullPointerException e) {
			yDestination = 75;
			System.out.println(yDestination);
		}
		homeY = yDestination;
		xDestination = 5;
		homeX = xDestination;
	}
  
//	public RestaurantGui getGui() {
//		return gui;
//	}

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
        
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, this.agent.getName(), "Successfully leaving RestaurantYocca",this.agent.restaurant.cityRestaurant.animationPanel.getName());
				agent.msgAnimationFinishedLeaveRestaurant();
				agent.leaveRestaurant();
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
		if (xPos == xDestination && yPos == yDestination && isAtCashier() && (hasArrived == false)){
			agent.msgAtCashier();   	
        } 		
		if (xPos == xDestination && yPos == yDestination && isAtWaitingArea() && (hasArrived == false)) {
        	agent.msgAtWaitingArea();   	
        } 
	}
    
	public void draw(Graphics2D g) {
//		g.setColor(Color.GREEN);
//		g.fillRect(xPos, yPos, 20, 20);
	    g.drawImage(currentImage.getImage(),xPos,yPos,20,20,null);
		g.setColor(Color.BLACK);
		g.setFont(stringFont);
        g.drawString(foodString, xPos+20, yPos+15);
        g.drawString(((PersonAgent)agent.p).personID + ":Cust", xPos-4, yPos-5);
	}
    
	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isAtCashier() {
		if ((xPos == cashierX + 10) && (yPos == cashierY - 10)) {
			return true;
		}
		return false;
	}
	
	public boolean isAtWaitingArea() {
		if ((xPos == homeX) && (yPos == homeY)) {
			return true;
		}
		return false;
	}
	
	
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}

	public boolean isHungry() {
		return isHungry;
	}
    
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public boolean getArrived() {
		return hasArrived;
	}
	
	public void setArrived(boolean arrived) {
		hasArrived = arrived;
	}
	
	public int getTableXLocation(int tableNum) {
		int xLoc = 0;
		if (tableNum == 1) {
			xLoc = 100;
		}
		if (tableNum == 2) {
			xLoc = 200;
		}
		if (tableNum == 3) {
			xLoc = 300;
		}
		if (tableNum == 4) {
			xLoc = 400;
		}
		return xLoc;
	}
	
	public int getTableYLocation(int tableNum) {
		int yLoc = 0;
		if (tableNum == 1) {
			yLoc = 300;
		}
		if (tableNum == 2) {
			yLoc = 300;
		}
		if (tableNum == 3) {
			yLoc = 300;
		}
		if (tableNum == 4) {
			yLoc = 300;
		}
		return yLoc;
	}
	
	public void setFoodString(String s) {
		foodString = s;
	}
	
	public String getFoodString() {
		return foodString;
	}
    
	public void DoGoToSeat(int seatnumber, int xLoc, int yLoc) {//later you will map seatnumber to table coordinates.
		xTable = xLoc;
		yTable = yLoc;
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}
	
	public void DoGoToCashier() {
		xDestination = cashierX + 10;
		yDestination = cashierY - 10;
	}
	
	public void DoGoToWaitingArea(int waitingSpot) {
		AlertLog.getInstance().logInfo(AlertTag.RESTAURANT_YOCCA, this.agent.getName(),"Amount of customers in restaurant: " + this.restaurant.customers.size(),this.agent.restaurant.cityRestaurant.animationPanel.getName());
		try {
			yDestination = 75 + waitingSpot * 25;
		} catch (NullPointerException e) {
			yDestination = 75;
			System.out.println(yDestination);
		}
		homeY = yDestination;
		xDestination = 5;
		homeX = xDestination;
	}
    
	public void DoExitRestaurant() {
		xDestination = -5;
		yDestination = 300;
		command = Command.LeaveRestaurant;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getWaitingYLocation() {
		return homeY;
	}

}
