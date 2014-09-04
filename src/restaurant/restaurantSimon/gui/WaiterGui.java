package restaurant.restaurantSimon.gui;


import restaurant.restaurantSimon.CustomerRole;
import restaurant.restaurantSimon.WaiterRole;

import java.awt.*;
import java.util.concurrent.Semaphore;

import cityGui.CityRestaurantSimonCard;

public class WaiterGui implements Gui {

	private WaiterRole agent = null;
	CityRestaurantSimonCard animation;
	private boolean pause=false;
	private boolean withFood=false;
	private String foodText=null;

	private int xInitial=-20;
	private int yInitial=-20;
	private  int xPos = xInitial;//default waiter position
	private  int yPos = yInitial;
	private int xResting=40;
	private int yResting=100;		
	private int xDestination = xResting, 		yDestination = yResting;//default start position
	private final int deskX=-20;
	private final int deskY=-20;
	private int cookX=280;
	private int cookY=80;

	private Semaphore atLobby = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atTable = new Semaphore(0,true);


	public WaiterGui(WaiterRole agent, CityRestaurantSimonCard gui) {
		this.agent = agent;
		this.animation = gui;
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

			if ((xPos == xDestination && yPos == yDestination)
					& (xDestination == CustomerGui.xTableUnit + 20) & (yDestination ==  CustomerGui.yTableUnit - 20))
			{
				if(atTable.availablePermits()==0){
					atTable.release();
				}


			}
			else if ((xPos == xDestination && yPos == yDestination)
					&(xDestination == 3*CustomerGui.xTableUnit + 20) & (yDestination ==  CustomerGui.yTableUnit - 20))
			{
				if(atTable.availablePermits()==0){
					atTable.release();
				}

			}
			else if ((xPos == xDestination && yPos == yDestination)
					&(xDestination == 5*CustomerGui.xTableUnit + 20) & (yDestination ==  CustomerGui.yTableUnit - 20))
			{
				if(atTable.availablePermits()==0){
					atTable.release();
				}

			}
			else if ((xPos == xDestination && yPos == yDestination)
					& (xDestination == cookX) & (yDestination ==  cookY))//xy for the cook
			{
				agent.msgAtCook();

			}


			if(xPos==xResting && yPos==yResting)
			{

				if(atLobby.availablePermits()==0){
					atLobby.release();
				}
			}
			if(xPos==cookX && yPos==cookY)
			{

				if(atCook.availablePermits()==0){
					atCook.release();
				}
			}

		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, 20, 20);
		if(withFood){
			g.setColor(Color.BLACK);
			g.drawString(foodText, xPos, yPos-2);

		}
	}

	public boolean isPresent() {
		return true;
	}

	public void DoBringToTable(CustomerRole customer, int tableX, int tableY) {

		xDestination = -20;
		yDestination = -20;
		try {
			atLobby.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		xDestination = tableX + 20;
		yDestination =  tableY - 20;
		try {
			atTable.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void DoGoToLobby() {
		if(atLobby.availablePermits()!=0){
			try {
				atLobby.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		xDestination = xResting;
		yDestination = yResting;
		try {
			atLobby.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void DoGoToTable(CustomerRole customer, int tableX, int tableY){
		if(atTable.availablePermits()!=0){
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		xDestination = tableX + 20;
		yDestination =  tableY - 20;
		try {
			atTable.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void DoLeaveCustomer() {
		xDestination = deskX;
		yDestination = deskY;
	}

	public void DoWalkingToCook(){//hack!!!! this is the pos for cook
		if(atCook.availablePermits()!=0){
			try {
				atCook.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		xDestination = cookX;
		yDestination = cookY;
		try {
			atCook.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void DoGotoWait(int num){
		yDestination= yResting;
		xDestination =xResting+num*20;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}



	public void pause(){
		if(pause){
			pause=false;	
		}
		else
		{pause=true;}
	}

	public void setWithFood(String food){
		if(withFood){
			withFood=false;
			foodText=null;
		}
		else{
			withFood=true;
			foodText=food;
		}

	}
}
