package bank.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.BankCustomerRole;
import person.PersonAgent;

public class BankCustomerGui implements Gui{

	private BankCustomerRole role = null;
	private boolean isPresent = false;
	private String dialogue = "";

	//RestaurantGui gui;//TODO bank Gui?

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToTellerWindow, LeaveBank};
	private Command command=Command.noCommand;
	//private boolean hasFood = false;//want hasMoney?


	public static final int BankCustomerWidth = 20;
	public static final int BankCustomerHeight = 20;
	
	ImageIcon currentImage;
	public static final int xDoor = -40;
	public static final int yDoor = 250;
	
	private int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 12;
	
	public BankCustomerGui(BankCustomerRole bcr/*, RestaurantGui gui*/){ 
		isPresent = true;
		role = bcr;
		xPos = xDoor;
		yPos = yDoor;
		xDestination = xDoor;
		yDestination = yDoor;
		//this.gui = gui;//TODO figure out the Gui!
		if (this.role.person instanceof PersonAgent)
			currentImage = ((PersonAgent)this.role.person).downSprites.get(0);
	}

	public void updatePosition(int x, int y) {
		if (xPos < xDestination) {
			xPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.role.person).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.role.person).rightSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (xPos > xDestination) {
			xPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.role.person).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.role.person).leftSprites.size());
				changeSpriteCounter++;
			}			
		}
		if (yPos < yDestination) {
			yPos++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.role.person).downSprites.get(changeSpriteCounter % ((PersonAgent)this.role.person).downSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (yPos > yDestination) {
			yPos--;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.role.person).upSprites.get(changeSpriteCounter % ((PersonAgent)this.role.person).upSprites.size());
				changeSpriteCounter++;
			}
		}
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToTellerWindow) {
				role.msgAtDestination();//release a semaphore?
			}
			else if (command==Command.LeaveBank) {
				role.msgAtDestination();
				//role.msgAnimationFinishedLeaveRestaurant();//set this role.
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				//isHungry = false;//carrying money again?
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}
	
	public void setLoc(int xPos,int yPos){
		this.xPos = xPos;
		this.yPos =yPos;
	}

	public void draw(Graphics2D g) {
		g.drawImage(currentImage.getImage(),xPos,yPos,BankCustomerWidth,BankCustomerWidth,null);
		g.setColor(Color.BLACK);
		g.setFont(stringFont);
		g.drawString(dialogue, xPos, yPos);
		g.drawString(((PersonAgent)role.person).personID + ":Cust", xPos-6, yPos-5);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setDialogue(String dial) {
		dialogue = dial;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToTellerWindow(int windowX, int windowY) {
		xDestination = windowX;
		yDestination = windowY;
		command = Command.GoToTellerWindow;
	}
	
	public void DoGoToSpot(int x, int y) {
		xDestination = x;
		yDestination = y;
	}

	public void DoExitBank() {
		xDestination = xDoor;
		yDestination = yDoor;
		command = Command.LeaveBank;
	}
}
