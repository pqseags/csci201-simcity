package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import person.PersonAgent;
import public_Gui.Gui;
import market.MarketEmployeeRole;

public class MarketEmployeeGui implements Gui{

	MarketEmployeeRole role;
	
	private final int panelX = 400;
	private final int panelY = 500;
	
	private final int itemDropX = 190;
	private final int itemDropY = 165;
	
	private final int xCashier = 15;
	private final int yCashier = 150;
	
	private final int xDock = 30;
	private final int yDock = 420;
	
	private int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	ImageIcon currentImage;
	
	//default start position
	private final int xInitial = 370;
	private final int yInitial = 200;
	 private int xPos = 370, yPos = 200;
	 private int xDestination = 370, yDestination = 200;

	 private boolean gotToDestination = true;
	 
	 
	public MarketEmployeeGui(MarketEmployeeRole e){
		role = e;
		currentImage = ((PersonAgent)this.role.p).downSprites.get(0);
	}


	public void updatePosition() {
		//System.err.println("up" + gotToDestination);
		if (xPos < xDestination) {
			xPos++;
			spriteCounter++;
			if (spriteCounter % 6 == 0) {
				currentImage = ((PersonAgent)this.role.p).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.role.p).rightSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (xPos > xDestination) {
			xPos--;
			spriteCounter++;
			if (spriteCounter % 6 == 0) {
				currentImage = ((PersonAgent)this.role.p).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.role.p).leftSprites.size());
				changeSpriteCounter++;
			}			
		}
		if (yPos < yDestination) {
			yPos++;
			spriteCounter++;
			if (spriteCounter % 6 == 0) {
				currentImage = ((PersonAgent)this.role.p).downSprites.get(changeSpriteCounter % ((PersonAgent)this.role.p).downSprites.size());
				changeSpriteCounter++;
			}
		}
		else if (yPos > yDestination) {
			yPos--;
			spriteCounter++;
			if (spriteCounter % 6 == 0) {
				currentImage = ((PersonAgent)this.role.p).upSprites.get(changeSpriteCounter % ((PersonAgent)this.role.p).upSprites.size());
				changeSpriteCounter++;
			}
		}
	    //if (!gotToDestination){
	    	//System.err.println("EMPLOYEE: GoiNG!");
	     	if (xPos == xDestination && yPos == yDestination && !(xPos == xInitial && yPos == yInitial) )	{
	     		//System.err.println("EMPLOYEE:  GOT HERE!!!GOT HERE!!!GOT HERE!!!GOT HERE!!!GOT HERE!!!GOT HERE!!!GOT HERE!!!GOT HERE!!!GOT HERE!!!");
	       		role.msgAtDestination();
	       		gotToDestination = true;
	      	}
	   // }	
	}

	@Override
	public void draw(Graphics2D g) {
		 g.setColor(Color.BLACK);
		 g.setFont(stringFont);
	     g.drawImage(currentImage.getImage(),xPos,yPos,20,20,null);	
	     g.drawString(((PersonAgent)role.p).personID + ":Worker", xPos-6, yPos-5);	
	}
	
	public void DoGetItem(String i){
		// switch this from a switch to a else if b/c of version issues	
		switch(i){
		case "Steak": xDestination = 245; yDestination = 315; break;
		case "Chicken":  xDestination = 245; yDestination = 245; break;
		case "Pizza": xDestination = 115; yDestination = 245; break;
		case "Salad": xDestination = 115; yDestination = 315; break;
		case "Car": xDestination = 115; yDestination = 385; break;
		case "Other": xDestination = 245; yDestination = 385; break;
		default: /*System.err.println("We don't have that item!");*/ return;
		}
		
		
		if (i.equals("Steak")){
			xDestination = 245; 
			yDestination = 315;;
		}
		else if (i.equals("Chicken")){
			xDestination = 245; 
			yDestination = 245; 
		}
		else if (i.equals("Pizza")){
			xDestination = 115; 
			yDestination = 245;
		}
		else if (i.equals("Salad")){
			xDestination = 115; 
			yDestination = 315;
		}
		else if (i.equals("Car")){
			xDestination = 115; 
			yDestination = 385;
		}
		else if (i.equals("Other")){
			xDestination = 245; 
			yDestination = 385;
		}
		else{
			//System.err.println("We don't have that item!"); 
			return;
		}
		gotToDestination = false;
	}
	
	public void DoGiveCustomerItems(){
		xDestination = itemDropX;
		yDestination = itemDropY;
		gotToDestination = false;
		//System.err.println("setting gotToDestination false");
	}
	
	public void DoGoHomePosition(){
		xDestination = xInitial;
		yDestination = yInitial;
	}
	
	public void DoGoToCashier(){
		xDestination = xCashier;
		yDestination = yCashier;
		gotToDestination = false;
	}
	
	public void DoGoToDock(){
		xDestination = xDock;
		yDestination = yDock;
		gotToDestination = false;
	}

	@Override
	public boolean isPresent() {
		return true;
	}
}
