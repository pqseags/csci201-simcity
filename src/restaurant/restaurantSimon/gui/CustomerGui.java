package restaurant.restaurantSimon.gui;

import restaurant.restaurantSimon.CustomerRole;
import restaurant.restaurantSimon.HostRole;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import cityGui.CityRestaurantSimonCard;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean pause=false;
	private String foodText=null;
	private boolean waitingForFood=false;
	private int cashierX=150;
	private int cashierY=-20;
	private Semaphore atCashier = new Semaphore(0,true);
	//private HostAgent host;
	CityRestaurantSimonCard animation;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;


	public static final int xTableUnit = 50;
	public static final int yTableUnit = 250;
	public static final int customerSizex = 20;
	public static final int customerSizey = 20;
	public static final int xInitial = -20;
	public static final int yInitial = -20;
	public static final int xResting = 20;
	public static final int yResting = 80;
	Map <Integer,Pos> tablePos;
	private Semaphore atReady = new Semaphore(0,true);
	
	

	class Pos{
		int X;
		int Y;
		Pos(int x,int y){
			X=x;
			Y=y;

		}

	}
	public CustomerGui(CustomerRole c, CityRestaurantSimonCard gui){ //HostAgent m) {
		agent = c;
		tablePos = new HashMap<>();
		Pos pos1  = new Pos(xTableUnit, yTableUnit) ;
		Pos pos2  = new Pos(3*xTableUnit, yTableUnit) ;
		Pos pos3  = new Pos(5*xTableUnit, yTableUnit) ;
		tablePos.put(1,pos1);
		tablePos.put(2,pos2);
		tablePos.put(3,pos3);
		xPos = xInitial;
		yPos = yInitial;
		xDestination = xResting;
		yDestination = yResting-agent.waitingNum*20;
		//maitreD = m;
		this.animation = gui;//hack!!!! gui in waiter just implemented till here

	}

	public void updatePosition() {
		if(!pause){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;

			if (xPos == xDestination && yPos == yDestination) {
				if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
				else if (command==Command.LeaveRestaurant) {
					agent.msgAnimationFinishedLeaveRestaurant();
					isHungry = false;
					
					//TODO
					//gui.setCustomerEnabled(agent);
				}
				command=Command.noCommand;
			}
			
			if(xPos==cashierX && yPos==cashierY)
			{

				if(atCashier.availablePermits()==0){
					atCashier.release();
				}
			}
			if(xPos==xResting && yPos==yResting){
				if(atReady.availablePermits()==0){
					atReady.release();
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, customerSizex, customerSizey);
		if(waitingForFood){
			g.setColor(Color.BLACK);
			g.drawString(foodText+"?", xPos, yPos-2);

		}
	}

	public boolean isPresent() {
		return isPresent;
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

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		xDestination = tablePos.get(seatnumber).X;
		yDestination = tablePos.get(seatnumber).Y;
		//System.err.println(yDestination);
		command = Command.GoToSeat;
	}
	
	public void DoGoToWait(int waitingNum){
		yDestination =yResting-waitingNum*20;
		//System.err.println(yDestination);
	}
	
	public void DoGoToReadyPosition(){
		yDestination =yResting;
		xDestination=xResting;
		try {
			atReady.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void DoWalkToCashier(){
		xDestination = cashierX;
		yDestination = cashierY;
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	
	public void DoExitRestaurant() {
		xDestination = xInitial;
		yDestination = yInitial;
		command = Command.LeaveRestaurant;
	}
	public void pause(){
		if(pause){
			pause=false;	
		}
		else
		{pause=true;}
	}

	public void setWaitingForFood(String food){
		if(waitingForFood){
			waitingForFood=false;
			foodText=null;
		}
		else{
			waitingForFood=true;
			foodText=food;
		}

	}
}
