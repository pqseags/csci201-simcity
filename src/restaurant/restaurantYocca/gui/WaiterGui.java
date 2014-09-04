package restaurant.restaurantYocca.gui;

import person.PersonAgent;
import public_Gui.Gui;
import restaurant.restaurantYocca.CookRole;
import restaurant.restaurantYocca.CustomerRole;
import restaurant.restaurantYocca.HostRole;
import restaurant.restaurantYocca.RestaurantYocca;
import restaurant.restaurantYocca.Table;
import restaurant.restaurantYocca.WaiterRole;
import restaurant.restaurantYocca.WaiterRole.MyCustomer;
import interfaces.restaurantYocca.Customer;
import interfaces.restaurantYocca.Waiter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.TableStringConverter;

public class WaiterGui implements Gui {

	private WaiterRole agent;;
	private HostRole host;

	//    RestaurantGui gui;

	private int xPos = 0, yPos = 300;//default waiter position
	private int xDestination = -20, yDestination = -20;//default start position
	private int homeX = 0;
	private int homeY = 5;
	private boolean startPosition = true;
	private boolean hasArrived = false;
	private boolean onBreak = false;
	private String statusString = "";

	ImageIcon currentImage;
	public int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 12;

	public WaiterGui(WaiterRole agent, RestaurantYocca rYocca) {
		System.out.println("WaiterGui being called");
		this.agent = agent;
		try {
			yPos = 75 + rYocca.waiters.size() * 25;
		} catch (NullPointerException e) {
			yPos = 50;
			System.out.println(yPos);
		}
		xPos = 450;
		homeY = yPos;

		try {
			yDestination = 75 + rYocca.waiters.size() * 25;
		} catch (NullPointerException e) {
			yDestination = 20;
			System.out.println(yDestination);
		}
		xDestination = 450;
		homeX = xDestination;
		currentImage = ((PersonAgent)this.agent.p).downSprites.get(0);
	}

	//    public WaiterGui(WaiterRole w, RestaurantGui gui, HostRole h){ //HostAgent m) {
	//		host = h;
	//    	agent = w;
	//		try {
	//		xPos = 15 + host.waiterList.size() * 25;
	//		} catch (NullPointerException e) {
	//			xPos = 20;
	//			System.out.println(xPos);
	//		}
	//		homeX = xPos;
	//		yPos = 5;
	//		try {
	//		xDestination = 15 + host.waiterList.size() * 25;
	//		} catch (NullPointerException e) {
	//			xDestination = 20;
	//			System.out.println(xDestination);
	//		}
	//		yDestination = 5;
	//		this.gui = gui;
	//	}

	public int xTable = 0;
	public int yTable = 0;
	private int cookX = 375;
	private int cookY = 100;
	private int cashierX = 230;
	private int cashierY = 40;
	private int yWaitingLocation = 0;

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

		if ((xPos == xDestination) && (yPos == yDestination) && (hasArrived == false) && (!isAtTheOrderStand()) && (!isAtWaitingArea()) && (!isAtCook()) && (!isAtStart()) && (!isAtCashier())) {
			agent.msgAtTable();        
		}
		if (xPos == xDestination && yPos == yDestination && isAtCook() && (hasArrived == false) && (!isAtWaitingArea()) && (!isAtTable()) && (!isAtStart()) && (!isAtCashier())) {
			agent.msgAtCook();
		}
		if (xPos == xDestination && yPos == yDestination && isAtTheOrderStand() && !isAtCook() && (hasArrived == false) && (!isAtWaitingArea()) && (!isAtTable()) && (!isAtStart()) && (!isAtCashier())) {
			agent.msgAtOrderStand();
		}
		if (xPos == xDestination && yPos == yDestination && isAtStart() &&  (hasArrived == false) && (!isAtWaitingArea()) && (!isAtTable()) && (!isAtCook()) && (!isAtCashier())) {
			agent.msgAtStart();   	
		}
		if (xPos == xDestination && yPos == yDestination && isAtCashier() && (hasArrived == false) && (!isAtTable()) && (!isAtCook()) && (!isAtStart())) {
			agent.msgAtCashier();   	
		} 
		if (xPos == xDestination && yPos == yDestination && isAtWaitingArea() && (hasArrived == false) && (!isAtTable()) && (!isAtCook())) {
			agent.msgAtWaitingArea();   	
		} 
		//        if (xPos == xDestination && yPos == yDestination && isAtTheOrderStand()) {
			//        	System.out.println("At Destination");
		//        }
		//System.out.println(getArrived());


	}

	public boolean isOnBreak() {
		return onBreak;
	}

	public void setOnBreak() {
		onBreak = true;
		agent.TryGoOnBreak();
	}

	//	public void cantGoOnBreak() {
	//		onBreak = false;
	//		gui.setWaiterBreakEnabled(agent);
	//	}

	public boolean isAtStart() {
		if (xPos == homeX && yPos == homeY) {
			return true;
		}
		return false;
	}

	public boolean isAtCook() {
		if ((xPos == cookX) && (yPos == cookY - 20)) {
			return true;
		}
		return false;
	}

	public boolean isAtTheOrderStand() {
		if ((getXPos() == cookX + 20) && (getYPos() == cookY - 20)) {
			return true;
		}
		return false;
	}

	public boolean isAtTable() {
		if (((xPos == 100) || (xPos == 200) || (xPos == 300) || (xPos == 400)) && (yPos == 300)) {
			return true;
		}
		return false;
	}

	public boolean isAtCashier() {
		if ((xPos == cashierX + 10) && (yPos == cashierY - 10)) {
			return true;
		}
		return false;
	}

	public boolean isAtWaitingArea() {
		if (xPos == 30 && yPos == yWaitingLocation) {
			return true;
		}
		return false;
	}

	public boolean getArrived() {
		return hasArrived;
	}

	public void setArrived(boolean arrived) {
		hasArrived = arrived;
	}

	public boolean getStartPosition() {
		return startPosition;
	}

	public void setTableLocation(int xLoc, int yLoc) {
		xTable = xLoc;
		yTable = yLoc;
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

	public void setStatusString(String s) {
		statusString = s;
	}

	public String getStatusString() {
		return statusString;
	}

	public void draw(Graphics2D g) {
		//g.setColor(Color.MAGENTA);
		//g.fillRect(xPos, yPos, 20, 20);
		g.drawImage(currentImage.getImage(),xPos,yPos,20,20,null);
		g.setColor(Color.BLACK);
		g.setFont(stringFont);
		g.drawString(statusString, xPos+5, yPos+15);
		g.drawString(((PersonAgent)agent.p).personID + ":Waiter", xPos-6, yPos-5);	}    	


	public boolean isPresent() {
		return true;
	}

	public void DoGoToTable(int tableNum) {
		xDestination = getTableXLocation(tableNum) + 20;
		yDestination = getTableYLocation(tableNum) - 20;
	}

	public void DoGoToCook() {
		xDestination = cookX;
		yDestination = cookY - 20;
	}

	public void DoGoToTheOrderStand() {
		xDestination = cookX + 20;
		yDestination = cookY - 20;
	}

	public void DoGoToCashier() {
		xDestination = cashierX + 10;
		yDestination = cashierY - 10;
	}

	public void DoBringToTable(Customer customer) {
		xDestination = xTable + 20;
		yDestination = yTable - 20;
	}

	public void DoLeaveCustomer() {
		xDestination = homeX;
		yDestination = homeY;
	}

	public void DoGetCustomer(int yPosition) {
		xDestination = 30;
		yWaitingLocation = yPosition;
		yDestination = yPosition;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setWaiter(WaiterRole w) {
		agent = w;
	}

	public void setHost(HostRole h) {
		host = h;
	}

}
