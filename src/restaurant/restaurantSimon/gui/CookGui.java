package restaurant.restaurantSimon.gui;

import java.awt.Color;


import java.awt.Graphics2D;
import java.util.concurrent.Semaphore;

import cityGui.CityRestaurantSimonCard;
import restaurant.restaurantSimon.CookRole;
import restaurant.restaurantSimon.WaiterRole;

public class CookGui implements Gui{


	private CookRole role = null;
	CityRestaurantSimonCard animation;
	private boolean pause=false;
	private boolean withFood=false;
	private String foodText=null;
	private boolean grill1=false;
	private boolean grill2=false;
	private boolean grill3=false;
	private String grill1Text=null;
	private String grill2Text=null;
	private String grill3Text=null;
	private boolean plate1=false;
	private boolean plate2=false;
	private boolean plate3=false;
	private String plate1Text=null;
	private String plate2Text=null;
	private String plate3Text=null;


	private int xInitial=280;
	private int yInitial=-20;
	private  int xPos = xInitial;//default cook position
	private  int yPos = yInitial;
	private int xResting=330;
	private int yResting=80;
//	private int xResting=40;
//	private int yResting=100;		
	private int xDestination = xInitial, 		yDestination = yInitial;//default start position

	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0,true);
	private Semaphore atExit = new Semaphore(0,true);
	private Semaphore atPlate = new Semaphore(0,true);



	public CookGui(CookRole agent, CityRestaurantSimonCard gui) {
		this.role = agent;
		this.animation = gui;
	}

	@Override
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


			if(xPos==CityRestaurantSimonCard.deskXPos+30 && yPos==CityRestaurantSimonCard.deskYPos){
				if(atFridge.availablePermits()==0){
					atFridge.release();
				}
			}
			//grill 1 position
			if(xPos==CityRestaurantSimonCard.deskXPos+60 && yPos==CityRestaurantSimonCard.deskYPos){
				if(atGrill.availablePermits()==0){
					atGrill.release();
				}
			}
			//grill 2 position
			if(xPos==CityRestaurantSimonCard.deskXPos+50 && yPos==CityRestaurantSimonCard.deskYPos+33){
				if(atGrill.availablePermits()==0){
					atGrill.release();
				}
			}
			//grill 3 position
			if(xPos==CityRestaurantSimonCard.deskXPos+40 && yPos==CityRestaurantSimonCard.deskYPos+66){
				if(atGrill.availablePermits()==0){
					atGrill.release();
				}
			}
			//plate 1 position
			if(xPos==CityRestaurantSimonCard.deskXPos+30 && yPos==CityRestaurantSimonCard.deskYPos){
				if(atPlate.availablePermits()==0){
					atPlate.release();
				}
			}
			//plate 2 position
			if(xPos==CityRestaurantSimonCard.deskXPos+31 && yPos==CityRestaurantSimonCard.deskYPos+33){
				if(atPlate.availablePermits()==0){
					atPlate.release();
				}
			}
			//plate 3 position
			if(xPos==CityRestaurantSimonCard.deskXPos+32 && yPos==CityRestaurantSimonCard.deskYPos+66){
				if(atPlate.availablePermits()==0){
					atPlate.release();
				}
			}
			//Exit position
			if(xPos==xInitial && yPos==yInitial){
				if(atExit.availablePermits()==0){
					atExit.release();
				}
			}


		}
	}

	@Override
	public void draw(Graphics2D g) {

		g.setColor(Color.PINK);
		g.fillRect(xPos, yPos, 20, 20);
		if(withFood){
			g.setColor(Color.BLACK);
			g.drawString(foodText, xPos, yPos-2);

		}
		if(grill1){
			g.setColor(Color.BLACK);
			g.drawString(grill1Text, CityRestaurantSimonCard.deskXPos+70, CityRestaurantSimonCard.deskYPos+10);
		}
		if(grill2){
			g.setColor(Color.BLACK);
			g.drawString(grill2Text, CityRestaurantSimonCard.deskXPos+60, CityRestaurantSimonCard.deskYPos+43);
		}
		if(grill3){
			g.setColor(Color.BLACK);
			g.drawString(grill3Text, CityRestaurantSimonCard.deskXPos+50, CityRestaurantSimonCard.deskYPos+76);
		}
		if(plate1){
			g.setColor(Color.BLACK);
			g.drawString(plate1Text, CityRestaurantSimonCard.deskXPos, CityRestaurantSimonCard.deskYPos+10);
		}
		if(plate2){
			g.setColor(Color.BLACK);
			g.drawString(plate2Text, CityRestaurantSimonCard.deskXPos, CityRestaurantSimonCard.deskYPos+43);
		}
		if(plate3){
			g.setColor(Color.BLACK);
			g.drawString(plate3Text, CityRestaurantSimonCard.deskXPos, CityRestaurantSimonCard.deskYPos+76);
		}
	}

	@Override
	public boolean isPresent() {
	
		return true;
	}

	public void DoGoToFridge(String choice){
		xDestination=CityRestaurantSimonCard.deskXPos+30;
		yDestination=CityRestaurantSimonCard.deskYPos;
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		withFood=true;
		foodText=choice;


	}
	public void DoGoToDesk(){
		xDestination=xResting;
		yDestination=yResting;
		/*try {
			atDesk.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}*/
	}

	public void DoGoToGrill(int pos, String choice){
		xDestination=CityRestaurantSimonCard.deskXPos+60-10*pos;
		yDestination=CityRestaurantSimonCard.deskYPos+33*pos;
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		withFood=false;
		if(pos==0){
			grill1Text=choice;
			if(grill1){
				grill1=false;
			}else{
				grill1=true;
			}
		}
		else if(pos==1){
			grill2Text=choice;
			if(grill2){
				grill2=false;
			}else{
				grill2=true;
			}
		}
		else if(pos==2){
			grill3Text=choice;
			if(grill3){
				grill3=false;
			}else{
				grill3=true;
			}
		}

	}


	public void DoPlateIt(int pos, String choice){
		xDestination=CityRestaurantSimonCard.deskXPos+30+1*pos;
		yDestination=CityRestaurantSimonCard.deskYPos+33*pos;
		try {
			atPlate.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		withFood=false;
		if(pos==0){
			plate1Text=choice;			
			plate1=true;

		}
		else if(pos==1){
			plate2Text=choice;
			plate2=true;
		}
		else if (pos==2){
			plate3Text=choice;
			plate3=true;
		}

	}
	public void DoExit(){
		xDestination=xInitial;
		yDestination=yInitial;
		try {
			atExit.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
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
	
	public void offSetPlate(int pos){
		if(pos==0){
			plate1=false;
		}
		else if(pos==1){
			plate2=false;
		}
		else if (pos==2){
			plate3=false;
		}
	}










}
