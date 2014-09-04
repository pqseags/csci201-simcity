package restaurant.restaurantGabe.gui;

import restaurant.restaurantGabe.CustomerRole;
//import restaurant.restaurantGabe.OldHostAgent;






import java.awt.*;

import javax.swing.ImageIcon;

import cityGui.CityRestaurantGabe;
import person.PersonAgent;
import public_Gui.Gui;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	public boolean isPresent =true;
	private boolean isHungry = false;
	
	static private int goneX = -10;
	static private int goneY = 250;

	//private HostAgent host;
	CityRestaurantGabe cityRestaurantGabe;
	//CityRestaurantGabe cityRestaurantGabe;
	
	String food = null;
	
	private int waitingSpot;
	
	private WaiterGui tempWaiter = null;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	ImageIcon currentImage;
	private int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 12;
	
	public static final int[] xTables = {200,300,400};
	public static final int[] yTables = {250,250,250};

	public CustomerGui(CustomerRole c, CityRestaurantGabe cityRestaurantGabe){ //HostAgent m) {
		agent = c;
        currentImage = ((PersonAgent)this.agent.person).downSprites.get(0);
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.cityRestaurantGabe = cityRestaurantGabe;
	}

	@Override
	public void updatePosition() {
		//System.out.println("Updating position");
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat){
				agent.msgAnimationFinishedGoToSeat();
				tempWaiter.atTable();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//cityRestaurantGabe.updateList(agent);
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	static int customer_width = 20;
	static int customer_height = 20;
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.setFont(stringFont);
		g.drawString(((PersonAgent)agent.person).personID + ":Cust", xPos-6, yPos-5);
	    g.drawImage(currentImage.getImage(),xPos, yPos, customer_width, customer_height,null);
	     
		if(food!=null){
			g.drawString(food, xPos+20, yPos+customer_height/2);
		}
	}
	
	public void DoGoToRestaurant(){
		System.out.println("Going to the waiting area");
		for(int i = 0;i<cityRestaurantGabe.numWaitingSpots;++i){
			if(cityRestaurantGabe.waitingSpots[i]){
				this.xDestination = 20;
				this.yDestination = 70 + 30*i;
				cityRestaurantGabe.waitingSpots[i] = false;
				this.waitingSpot = i;
				return;
			}
		}
		
		this.xDestination = -20;
		this.yDestination = -20;
		this.waitingSpot = -1;
		
		//gui.numWaitingCusts++;
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setHungry() {
		isHungry = true;
		agent.msgGotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		cityRestaurantGabe.numWaitingCusts--;
		if(this.waitingSpot!=-1){
			cityRestaurantGabe.waitingSpots[this.waitingSpot] = true;
		}
		
		xDestination = xTables[seatnumber];
		yDestination = yTables[seatnumber];
		command = Command.GoToSeat;
		//System.out.println("I'm supposed to be sitting");
		//gui.updateList(agent);
	}
	
	public void DoGoToSeatDirected(WaiterGui wGui,int xDest, int yDest) {
		if(this.waitingSpot!=-1){
			cityRestaurantGabe.waitingSpots[this.waitingSpot] = true;
		}
		
		xDestination = xDest;
		yDestination = yDest;
		command = Command.GoToSeat;
		tempWaiter = wGui;
	}
	
	public void setFood(String food){
		this.food = food;
	}

	public void DoExitRestaurant() {
		xDestination = goneX;
		yDestination = goneY;
		command = Command.LeaveRestaurant;
		//gui.updateList(agent);
	}
}
