package restaurant.restaurantLinda.gui;

import restaurant.restaurantLinda.CustomerRole;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import person.PersonAgent;
import astar.AStarTraversal;
import astar.Position;
import cityGui.CityRestaurantLindaCard;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

public class CustomerGui extends GuiPerson implements Gui{

	private CustomerRole agent = null;
	private boolean isHungry = false;

	private enum Command {noCommand, waitInLine, followWaiter, GoToSeat, LeaveSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;

	private int personSize=CityRestaurantLindaCard.PERSONSIZE;
	
	private List<MyImage> carriedItems = new ArrayList<MyImage>();
	private String bufferText;
    private Dimension bufferSize;
    
    public boolean isPresent = true;

	public CustomerGui(CustomerRole c, AStarTraversal aStar){ //HostAgent m) {
		agent = c;
		pSprites = c.p;
		currentImage = ((PersonAgent)this.agent.p).downSprites.get(0);
		xPos = 0;
		yPos = 0;
		xfinal = xDestination = xPos;
		yfinal = yDestination = yPos;
		//maitreD = m;
		this.aStar = aStar;
	}

	public void followMe(int x, int y){

		
		/*if (previousPosition!=null){
			previousPosition.release(aStar.getGrid());
		}
		
		previousPosition = currentPosition;*/
		xDestination = xPos = x;
		yDestination = yPos = y;
		/*currentPosition = new Position(x/cellSize, y/cellSize);
		if (!currentPosition.moveInto(aStar.getGrid()))
			currentPosition = null;*/
	}

	public void goToTable(int x, int y){
		/*if (previousPosition!=null)
			previousPosition.release(aStar.getGrid());

		previousPosition = currentPosition = null;*/
		
		command = Command.GoToSeat;
		xfinal = x;
		yfinal = y;
	}

	public void updatePosition() {
		if (command==Command.waitInLine){
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
		}
		else if (command==Command.GoToSeat) {
			if (yfinal>yPos) {
				yPos++;
				spriteCounter++;
				if (spriteCounter % spriteChangeSpeed == 0) {
					currentImage = ((PersonAgent)this.agent.p).downSprites.get(changeSpriteCounter % ((PersonAgent)this.agent.p).downSprites.size());
					changeSpriteCounter++;
				}
			}

			if (xPos==xfinal && yfinal==yPos){
				command = Command.noCommand;
				agent.msgAnimationFinishedGoToSeat();
			}
		}

		/*else if (command==Command.LeaveSeat ){//&& currentPosition==null){
				DoLeaveTable();
		}*/

		else if (command!=Command.noCommand){
			if (moveAndCheckDestination()){
				if (xPos!=xfinal || yPos!=yfinal){
					//System.err.println("Position: " + xPos + " " + yPos + ", destination: " + xDestination+" "+yDestination+", final:"+xfinal+" "+yfinal);
					return;
				}
				if (command==Command.LeaveSeat){
					command=Command.noCommand;
					agent.msgAnimationFinishedLeavingSeat();
				}
				if (command==Command.GoToCashier){
					command=Command.noCommand;
					agent.msgAnimationFinishedGoToCashier();
				}
				else if (command==Command.LeaveRestaurant) {
					/*if (previousPosition!=null)
						previousPosition.release(aStar.getGrid());
					if (currentPosition!=null && currentPosition!=previousPosition)

						currentPosition.release(aStar.getGrid());*/
					

					isPresent = false;
					command=Command.noCommand;
					agent.msgAnimationFinishedLeaveRestaurant();
					bufferText = null;
					carriedItems.clear();
					//System.out.println("about to call gui.setCustomerEnabled(agent);");
					
					//AlertLog.getInstance().logDebug(AlertTag.RESTAURANT_LINDA, agent.getName(), "Managed to finish leaving restaurant animation",agent.r.cityRestaurant.ID);
					isHungry = false;
				}
				command=Command.noCommand;
			}
		}

		//if (command==Command.LeaveSeat)
		//System.out.println(currentPosition);
	}


	public void draw(Graphics2D g) {
		//		g.setColor(Color.GREEN);
		//		g.fillRect(xPos, yPos, personSize, personSize);
		g.drawImage(currentImage.getImage(),xPos,yPos,personSize,personSize,null);
		g.setColor(Color.BLACK);
		g.setFont(stringFont);
		g.drawString(((PersonAgent)agent.p).personID + ":Cust", xPos-4, yPos-5);

		if (bufferText!=null){
			g.setColor(Color.BLACK);
			g.drawString(bufferText, xPos, yPos+25);
		}

		for (MyImage icon: carriedItems)
			icon.draw(g);

	}

	public boolean isPresent() {
		return true;
	}
	public void setHungry() {
		isHungry = true;
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void DoWaitInLine(int position){
		command = Command.waitInLine;

		int limit = (CityRestaurantLindaCard.CARD_WIDTH-300)/cellSize;
        if (position>=limit){
        	position%=limit;
        }
        
        xPos = xDestination = xfinal = cellSize*(position+1);
        yPos = yDestination = yfinal = 0;
	}

	public void DoMoveInLine(){
		xDestination = xfinal -=personSize+cellSize;
	}

	public void DoWaitForWaiter(){
		xDestination = cellSize;
		yDestination = cellSize;
	}

	public void DoGoToSeat(int xLoc, int yLoc) {
		xfinal = xLoc;
		yfinal = yLoc;
		command = Command.GoToSeat;
	}

	public void DoLeaveTable(){
		xfinal = xDestination = xPos-personSize;
		yfinal = yDestination = yPos;

		command = Command.LeaveSeat;

		//CalculatePath(new Position(xfinal/cellSize, yfinal/cellSize));
		/*previousPosition = new Position(xfinal/cellSize, yfinal/cellSize);
		
		if (previousPosition.open(aStar.getGrid())){
			currentPosition = previousPosition;
			CalculatePath(previousPosition);
		}*/
	}

	public void DoGoToCashier(){
		xDestination = xfinal = CityRestaurantLindaCard.CASHIER.x+CityRestaurantLindaCard.CASHIER.width;
		yDestination = yfinal = CityRestaurantLindaCard.CASHIER.y;
		command = Command.GoToCashier;
		CalculatePath(new Position(xfinal/cellSize+1, yfinal/cellSize));
	}

	public void DoExitRestaurant() {
		xfinal = -2*personSize;
		yfinal = -2*personSize;
		command = Command.LeaveRestaurant;
		CalculatePath(new Position(0, 0));
	}

	public void DoTalk(String text){
		bufferText=text;
	}

	public void DoReceiveFood(String choice){
		bufferText=null;
		carriedItems.add(new MyImage(choice,xPos,yPos));
	}

	public void DoFinishFood(){
		carriedItems.clear();
		bufferText=null;
	}

	//Utilities
	public Point getDestination(){
		return new Point(xDestination, yDestination);
	}

	public Point getLocation(){
		return new Point(xPos,yPos);
	}
}
