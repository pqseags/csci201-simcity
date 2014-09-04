package house.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

import cityGui.CityHouseCard;
import person.PersonAgent;
import public_Gui.Gui;
import house.InhabitantRole;





public class InhabitantGui implements Gui {


        private InhabitantRole role = null;
        
        private boolean pause=false;

        private int xInitial=-20;
        private int yInitial=0;
        private  int xPos = xInitial;//default inhabitant position
        private  int yPos = yInitial;
        private int xResting=100;
        private int yResting=100;                
        private int xDestination = xInitial,                 yDestination = yInitial;//default start position
        public static final int xSize=20;
        public static final int ySize=20;

        public static final int eatingTime=1000;


        private Semaphore atBed = new Semaphore(0,true);
        private Semaphore atFridge = new Semaphore(0,true);
        private Semaphore atGrill = new Semaphore(0,true);
        private Semaphore atTable = new Semaphore(0,true);
        private Semaphore atExit = new Semaphore(0,true);

        ImageIcon currentImage;
    	private int spriteCounter = 6;
    	private int changeSpriteCounter = 0;
    	private int spriteChangeSpeed = 12;

        public InhabitantGui(InhabitantRole role){
                this.role = role;
                //Hack to make unit tests work
                if (this.role.self instanceof PersonAgent)
                	currentImage = ((PersonAgent)this.role.self).downSprites.get(0);

        }

        /*public InhabitantGui(InhabitantRole agent,HouseGui gui) {
                this.role = agent;
                this.gui=gui;
        }*/

        @Override
        public void updatePosition() {
                if(!pause){
                	if (xPos < xDestination) {
            			xPos++;
            			spriteCounter++;
            			if (spriteCounter % spriteChangeSpeed == 0) {
            				currentImage = ((PersonAgent)this.role.self).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.role.self).rightSprites.size());
            				changeSpriteCounter++;
            			}
            		}
            		else if (xPos > xDestination) {
            			xPos--;
            			spriteCounter++;
            			if (spriteCounter % spriteChangeSpeed == 0) {
            				currentImage = ((PersonAgent)this.role.self).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.role.self).leftSprites.size());
            				changeSpriteCounter++;
            			}			
            		}
            		if (yPos < yDestination) {
            			yPos++;
            			spriteCounter++;
            			if (spriteCounter % spriteChangeSpeed == 0) {
            				currentImage = ((PersonAgent)this.role.self).downSprites.get(changeSpriteCounter % ((PersonAgent)this.role.self).downSprites.size());
            				changeSpriteCounter++;
            			}
            		}
            		else if (yPos > yDestination) {
            			yPos--;
            			spriteCounter++;
            			if (spriteCounter % spriteChangeSpeed == 0) {
            				currentImage = ((PersonAgent)this.role.self).upSprites.get(changeSpriteCounter % ((PersonAgent)this.role.self).upSprites.size());
            				changeSpriteCounter++;
            			}
            		}

                        if(xPos==CityHouseCard.bedX && yPos==CityHouseCard.bedY)
                        {

                                if(atBed.availablePermits()==0){
                                        atBed.release();                                }
                        }
                        if(xPos==CityHouseCard.grillX-xSize && yPos==CityHouseCard.grillY)
                        {

                                if(atGrill.availablePermits()==0){
                                        atGrill.release();
                                }
                        }
                        if(xPos==CityHouseCard.refrigeratorX-xSize && yPos==CityHouseCard.refrigeratorY)
                        {

                                if(atFridge.availablePermits()==0){
                                        atFridge.release();
                                }
                        }
                        if(xPos==CityHouseCard.tableX-xSize && yPos==CityHouseCard.tableY)
                        {

                                if(atTable.availablePermits()==0){
                                        atTable.release();
                                }
                        }
                        if(xPos==xInitial && yPos==yInitial)
                        {

                                if(atExit.availablePermits()==0){
                                        atExit.release();
                                }
                        }
                }

        }

        @Override
        public void draw(Graphics2D g) {
//                g.setColor(Color.MAGENTA);
//                g.fillRect(xPos, yPos, xSize, ySize);
//                
                g.drawImage(currentImage.getImage(),xPos,yPos,xSize,ySize,null);
        		g.setColor(Color.BLACK);
        		g.setFont(stringFont);
                g.drawString(((PersonAgent)role.self).personID + ":Cust", xPos-4, yPos-5);
        }

        @Override
        public boolean isPresent() {
                return true;
        }

        public void DoSleep(){
                if(atBed.availablePermits()!=0){
                        try {
                                atBed.acquire();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                xDestination = CityHouseCard.bedX;
                yDestination = CityHouseCard.bedY;
                try {
                        atBed.acquire();
                } catch (InterruptedException e) {

                        e.printStackTrace();
                }
        }

        public void DoGoToFridge(){
                if(atFridge.availablePermits()!=0){
                        try {
                                atFridge.acquire();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                xDestination = CityHouseCard.refrigeratorX-xSize;
                yDestination = CityHouseCard.refrigeratorY;
                try {
                        atFridge.acquire();
                } catch (InterruptedException e) {

                        e.printStackTrace();
                }
        }

        public void DoIdle(){
                xDestination = xResting;
                yDestination = yResting;
        }

        public void DoGoToGrill(){
                if(atGrill.availablePermits()!=0){
                        try {
                                atGrill.acquire();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                xDestination = CityHouseCard.grillX-xSize;
                yDestination = CityHouseCard.grillY;
                try {
                        atGrill.acquire();
                } catch (InterruptedException e) {

                        e.printStackTrace();
                }
        }

        public void DoGoToTable(){
                if(atTable.availablePermits()!=0){
                        try {
                                atTable.acquire();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                xDestination = CityHouseCard.tableX-xSize;
                yDestination = CityHouseCard.tableY;
                try {
                        atTable.acquire();
                } catch (InterruptedException e) {

                        e.printStackTrace();
                }
        }
        public void DoPlateAndEat() {
                DoGoToGrill();
                DoGoToTable();
                try {
         		   
        			Thread.sleep(eatingTime);
        		} catch(InterruptedException ex) {
        		    Thread.currentThread().interrupt();
        		}

        }

        public void DoExit() {
                if(atExit.availablePermits()!=0){
                        try {
                                atBed.acquire();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                xDestination = xInitial;
                yDestination = yInitial;
                try {
                        atExit.acquire();
                } catch (InterruptedException e) {

                        e.printStackTrace();
                }
        }










}