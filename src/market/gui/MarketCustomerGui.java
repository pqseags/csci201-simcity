package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import market.MarketCustomerRole;
import person.PersonAgent;
import public_Gui.Gui;

public class MarketCustomerGui implements Gui{

	MarketCustomerRole role;
	MarketAnimation animation;	
	
	ImageIcon currentImage;
	private final int panelX = 400;
	private final int panelY = 500;
	
	private final int itemDropX = 190;
	private final int itemDropY = 120;
	
	private final int cashierX = 60;
	private final int cashierY = 90;
	
	private final int exitX = 190;
	private final int exitY = 55;
	
	public int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	
	//default start position
	private final int xInitial = 190;
	private final int yInitial = -40;
	 private int xPos = 190, yPos = -40;
	 private int xDestination = 190, yDestination = -40;
	 
	 private boolean gotToDestination = true;
	 
	public MarketCustomerGui(MarketCustomerRole r){
		role = r;
		currentImage = ((PersonAgent)this.role.p).downSprites.get(0);
	}
	
	public void updatePosition() {
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
	 //   if (!gotToDestination){
	       	if (xPos == xDestination && yPos == yDestination && !(xPos == itemDropX && yPos == itemDropY ) /*&& !(xPos == xInitial && yPos == yInitial)*/)

	       	{
	       		//System.err.println("gui update position says:  got to the destination, so now going to call msgAtDestination"); ---------------------------
	       		role.msgAtDestination();
	       		//System.err.println("Released the semaphor cause at destination");
	       		gotToDestination = true;
	       	}
	       	
	//	}
	}


	public void draw(Graphics2D g) {
		 g.setColor(Color.BLACK);
		 g.setFont(stringFont);
	     g.drawImage(currentImage.getImage(),xPos,yPos,20,20,null);
	     g.drawString(((PersonAgent)role.p).personID + ":Cust", xPos-6, yPos-5);	
	}

	public void DoGoToHost(){
		
		gotToDestination = false;
		xDestination = panelX -82;
		yDestination = 90;
	}
	
	public void DoGoToItemDrop(){
		xDestination = itemDropX;
		yDestination = itemDropY;
	}
	
	public void DoGoToCashier(){
		gotToDestination = false;
		xDestination = cashierX;
		yDestination = cashierY;
	}

	public void DoGoToExit(){
		//System.out.println(role.name+" moving me to host");
		//System.out.flush();
		gotToDestination = false;
		xDestination = exitX;
		yDestination = exitY;
	}
	
	public void DoExitRestaurant(){

		gotToDestination = false;
		xDestination = xInitial;
		yDestination = yInitial-30;
	}
	public boolean isPresent() {
		return true;
	}

	public void setAnimation(MarketAnimation ma){
		animation = ma;
	}
}
