package cityGui.test;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.table.TableStringConverter;

import cityGui.CityComponent;
import cityGui.CityPanel;
import cityGui.SimCityGui;
import person.PersonAgent;
import public_Gui.Gui;
import util.CrosswalkStatus;
import util.Loc;
import util.StopLight;

public class PersonGui extends CityComponent implements Gui {

    private PersonAgent person;
    SimCityGui gui;
    
    public int xPos = 20, yPos = 29;//default waiter position
    public int xDestination = 395, yDestination = 29;//default start position
    private int crossWalk = 290;
    public int cWalk1i = 161;
    private int cWalk2i = 429;
    private int cWalk3i = 429;
    private int cWalk4i = 161;
    private int cWalk1o = 69;
    private int cWalk2o = 521;
    private int cWalk3o = 521;
    private int cWalk4o = 69;
    private int xRand;
    private int yRand;
    
    public static int gridScale = 30;
    boolean doingMove = false;
    
    private boolean startPosition = true;
    private boolean hasArrived = false;
    private boolean readyToGoInnerSidewalk = false;
    private boolean readyToGoOuterSidewalk = false;
    private boolean transitionDone = true;
    private boolean choseRand = false;
    private boolean visible = true;
    
    public boolean onTheMove = false;
    public boolean waitingForBus = false;
    public boolean waitingForCarToGetOnRoad = false;
    public List<StopLight> stopLights = new ArrayList<StopLight>();
    
    ImageIcon currentImage;
	public int spriteCounter = 6;
	private int changeSpriteCounter = 0;
	private int spriteChangeSpeed = 24;
    
    private Semaphore crossingStreet = new Semaphore(0,true);
    private Semaphore atMove = new Semaphore(0,true);
     
    public PersonGui(PersonAgent agent) {
        this.person = agent;
    }
    
    public PersonGui(PersonAgent c, SimCityGui gui, int xPos, int yPos, int xDest, int yDest){ //HostAgent m) {
    	super(xPos, yPos, Color.blue, "Dude");
    	person = c;
		xDestination = xDest;
		yDestination = yDest;
		this.gui = gui;
		rectangle = new Rectangle(xPos, yPos, 10, 10);

		this.person.spriteChoice = (int) (Math.random() * gui.upAll.size());
		
		this.person.upSprites = gui.upAll.get(this.person.spriteChoice);
		this.person.downSprites = gui.downAll.get(this.person.spriteChoice);
		this.person.rightSprites = gui.rightAll.get(this.person.spriteChoice);
		this.person.leftSprites = gui.leftAll.get(this.person.spriteChoice);
		
		currentImage = this.person.downSprites.get(0);
	}
    
    public void doGoToBuilding(Loc loc){
    	//System.out.println(
    	//		"Being told to go: "+rectangle.x + " "+rectangle.y + " and "+xDestination+ " "+yDestination);
    	this.xDestination = loc.x;
    	this.yDestination = loc.y;
    	onTheMove = true;
    }
    
    public void doGoToBus(Loc loc){
    	this.xDestination = loc.x;
    	this.yDestination = loc.y;
    	onTheMove = true;
    	waitingForBus = true;
    }
    
    public void onBus(){
    	waitingForBus = false;
    	visible = false;
    }
    
    public void offBus(){
    	visible = true;
    }
    
    public void move(int x, int y){
    	
    	doingMove = true;
    	
    	this.xDestination = gridScale*x;
    	this.yDestination = gridScale*y;
    	
    	try {
			atMove.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
    }
    
	/*public void updatePosition() {
		
		if (!atDestination()) {
			if (readyToGoInnerSidewalk) {
				//System.out.println("In inner sidewalk method");
				if (b1(getXPos(),getYPos())) {
					goVertical(cWalk1i);
					if (getYPos() == cWalk1i) {
	    				readyToGoInnerSidewalk = false;
	    				transitionDone = true;
	    			}
				}
				if (b2(getXPos(),getYPos())) {
					goHorizontal(cWalk2i);
					if (getXPos() == cWalk2i) {
	    				readyToGoInnerSidewalk = false;
	    				transitionDone = true;
	    			}
				}
				if (b3(getXPos(),getYPos())) {
					goVertical(cWalk3i);
					if (getYPos() == cWalk3i) {
	    				readyToGoInnerSidewalk = false;
	    				transitionDone = true;
	    			}
				}
				if (b4(getXPos(),getYPos())) {
					goHorizontal(cWalk4i);
					if (getXPos() == cWalk4i) {
	    				readyToGoInnerSidewalk = false;
	    				transitionDone = true;
	    			}
				}
			}

			if (transitionDone) {
				//System.out.println("Transition is done");
			}
			
			if (readyToGoOuterSidewalk) {
				//System.out.println("Going to outer sidewalk");
				if (b1(getXPos(),getYPos())) {
					goVertical(cWalk1o);
					if (getYPos() == cWalk1o) {
	    				readyToGoOuterSidewalk = false;
	    				transitionDone = true;
	    			}
					//else System.out.println("Didnt reach crosswalk destination");
	
				}
				if (b2(getXPos(),getYPos())) {
					goHorizontal(cWalk2o);
					if (getXPos() == cWalk2o) {
	    				readyToGoOuterSidewalk = false;
	    				transitionDone = true;
	    			}
				}
				if (b3(getXPos(),getYPos())) {
					goVertical(cWalk3o);
					if (getYPos() == cWalk3o) {
	    				readyToGoOuterSidewalk = false;
	    				transitionDone = true;
	    			}
				}
				if (b4(getXPos(),getYPos())) {
					goHorizontal(cWalk4o);
					if (getXPos() == cWalk4o) {
	    				readyToGoOuterSidewalk = false;
	    				transitionDone = true;
	    			}
				}
			}
			
			if (topLeftCorner(getXPos(),getYPos()) && topLeftCorner(xDestination,yDestination)) {
				//System.out.println("In top left corner");
				goHorizontal(40);
				goVertical(40);
			}
			
			if (topRightCorner(getXPos(),getYPos()) && topRightCorner(xDestination,yDestination)) {
				//System.out.println("In top right corner");
				goHorizontal(550);
				goVertical(40);
			}
			
			if (bottomLeftCorner(getXPos(),getYPos()) && bottomLeftCorner(xDestination,yDestination)) {
				//System.out.println("In bottom left corner");
				goHorizontal(40);
				goVertical(550);
			}
			
			if (bottomRightCorner(getXPos(),getYPos()) && bottomRightCorner(xDestination,yDestination)) {
				//System.out.println("In bottom right corner");
				goHorizontal(550);
				goVertical(550);
			}
			
	    	if (os1(getXPos(),getYPos()) && !readyToGoInnerSidewalk && !readyToGoOuterSidewalk) {
				//System.out.println("XPos: " + getXPos() + " In os1");
	    		if (!transitionDone) {
	    			goVertical(yRand);
	    			if (getYPos() == yRand) {
	    				transitionDone = true;
	    			}
				}
	        	if (os1(xDestination,yDestination) && transitionDone) {
	        		//System.out.println("XDestination: " + getXPos() + " In os1");
	        		goHorizontal(xDestination);
	    			if (getXPos() == xDestination && !atDestination()) {
	            		goVertical(yDestination);
	    			}
	        	}
	    		if (is(xDestination,yDestination) && transitionDone) {
	    			goHorizontal(crossWalk);
	    			goVertical(cWalk1o);
	    			if (getXPos() == crossWalk && getYPos() == cWalk1o) {
	    				readyToGoInnerSidewalk = true;
	    				crossWalk1i();
	    			}
	    		}
	        	if (os(xDestination,yDestination) && !os1(xDestination,yDestination) && transitionDone) {
	        		if (choseRand == false) {
	    				if (os2(xDestination,yDestination)) {
	    					xRand = (int) (520 + (Math.random() * 29));
	    				}
	    				else xRand = (int) (70 - (Math.random() * 29));
	    				choseRand = true;
	    			}
	    			else {
	    				goHorizontal(xRand);
	    				if (getXPos() == xRand) {
	    					if (getYPos() == 70) {
	        					choseRand = false;
	    					}
	    					goVertical(yDestination);
	    				}	
	    			}
	        	}
	    	}
	    	if (os2(getXPos(),getYPos()) && !readyToGoInnerSidewalk && !readyToGoOuterSidewalk) {
	    		//System.out.println("In os2");
	    		if (os2(xDestination,yDestination)) {
	        		goVertical(yDestination);
	    			if (getYPos() == yDestination) {
	    				goHorizontal(xDestination);
	    			}
	        	}
	    		if (is(xDestination,yDestination)) {
	    			goVertical(crossWalk);
	    			goHorizontal(cWalk2o);
	    			if (getYPos() == crossWalk && getXPos() == cWalk2o) {
	    				readyToGoInnerSidewalk = true;
	    				crossWalk2i();
	    			}
	    		}
	        	if (os(xDestination,yDestination) && !os2(xDestination,yDestination)) {
	        		if (choseRand == false) {
	    				if (os1(xDestination,yDestination)) {
	    					yRand = (int) (70 - (Math.random() * 29));
	    				}
	    				else yRand = (int) (520 + (Math.random() * 29));
	    				choseRand = true;
	    			}
	        		if (getYPos() == 71 || getYPos() == 519) {
	    				choseRand = false;
	    				transitionDone = false;
	    			}	
	    			goVertical(yRand);	
	        	}
	    	}
	    	if (os3(getXPos(),getYPos()) && !readyToGoInnerSidewalk && !readyToGoOuterSidewalk) {
	    		//System.out.println("In os3");
	    		if (!transitionDone) {
	    			goVertical(yRand);
	    			if (getYPos() == yRand) {
	    				transitionDone = true;
	    			}
				}
	        	if (os3(xDestination,yDestination) && transitionDone) {
	        		goHorizontal(xDestination);
	    			if (getXPos() == xDestination) {
	    				goVertical(yDestination);
	    			}
	        	}
	    		if (is(xDestination,yDestination) && transitionDone) {
	    			goHorizontal(crossWalk);
	    			goVertical(cWalk3o);
	    			if (getXPos() == crossWalk && getYPos() == cWalk3o) {
	    				readyToGoInnerSidewalk = true;
	    				crossWalk3i();
	    			}
	    		}
	        	if (os(xDestination,yDestination) && !os3(xDestination,yDestination) && transitionDone) {
	        		if (choseRand == false) {
	    				if (os2(xDestination,yDestination)) {
	    					xRand = (int) (520 + (Math.random() * 29));
	    				}
	    				else xRand = (int) (70 - (Math.random() * 29));
	    				choseRand = true;
	    			}
	    			else {
	    				goHorizontal(xRand);
	    				if (getXPos() == xRand) {
	    					if (getYPos() == 520) {
	        					choseRand = false;
	    					}
	    					goVertical(yDestination);
	    				}	
	    			}
	        	}
	    	}
	    	if (os4(getXPos(),getYPos()) && !readyToGoInnerSidewalk && !readyToGoOuterSidewalk) {
	    		//System.out.println("In os4");
	    		if (os4(xDestination,yDestination)) {
	        		goVertical(yDestination);
	    			if (getYPos() == yDestination) {
	    				goHorizontal(xDestination);
	    			}
	        	}
	    		if (is(xDestination,yDestination)) {
	    			goVertical(crossWalk);
	    			goHorizontal(cWalk4o);
	    			if (getYPos() == crossWalk && getXPos() == cWalk4o) {
	    				readyToGoInnerSidewalk = true;
	    				crossWalk4i();
	    			}
	    		}
	        	if (os(xDestination,yDestination) && !os4(xDestination,yDestination)) {
	        		if (choseRand == false) {
	    				if (os1(xDestination,yDestination)) {
	    					yRand = (int) (70 - (Math.random() * 29));
	    				}
	    				else yRand = (int) (520 + (Math.random() * 29));
	    				choseRand = true;
	    			}
	        		if (getYPos() == 71 || getYPos() == 519) {
	    				choseRand = false;
	    				transitionDone = false;
	    			}	
	    			goVertical(yRand);	
	        	}
	    	}
	   
	    	if (is1(getXPos(),getYPos()) && !readyToGoOuterSidewalk && !readyToGoInnerSidewalk) {
	    		//System.out.println("XPos: " + getXPos() + " In is1");
	    		if (!transitionDone) {
	    			goVertical(yRand);
	    			if (getYPos() == yRand) {
	    				transitionDone = true;
	    			}
				}
	        	if (is1(xDestination,yDestination) && transitionDone) {
	        		goHorizontal(xDestination);
	    			if (getXPos() == xDestination) {
	    				goVertical(yDestination);
	    				//atDestination
	    			}
	        	}
	    		if ((os(xDestination,yDestination) || is(xDestination,yDestination)) && !is1(xDestination,yDestination) && !os1(xDestination,yDestination) && transitionDone) {
	    			if (choseRand == false) {
	    				if (is2(xDestination,yDestination) || os2(xDestination,yDestination)) {
	    					xRand = (int) (400 + (Math.random() * 29));
	    				}
	    				else xRand = (int) (190 - (Math.random() * 29));
	    				choseRand = true;
	    			}
	    			else {
	    				goHorizontal(xRand);
	    				if (getXPos() == xRand) {
	    					if (getYPos() == 190) {
	        					choseRand = false;
	    					}
	    					goVertical(yDestination);
	    				}	
	    			}
	    		}
	    		else if (os1(xDestination,yDestination)) {
	        		goHorizontal(crossWalk);
	        		goVertical(cWalk1i);
	        		if (getXPos() == crossWalk && getYPos() == cWalk1i) {
	        			readyToGoOuterSidewalk = true;
	        			crossWalk1o();
	        		}
	        	}
	    	}
	
	    	if (is2(getXPos(),getYPos()) && !readyToGoOuterSidewalk && !readyToGoInnerSidewalk) {
	    		//System.out.println("In is2");
	    		if (is2(xDestination,yDestination)) {
	        		goVertical(yDestination);
	    			if (getYPos() == yDestination) {
	    				goHorizontal(xDestination);
	    				//atDestination
	    			}
	        	}
	    		if ((os(xDestination,yDestination) || is(xDestination,yDestination)) && !is2(xDestination,yDestination) && !os2(xDestination,yDestination)) {
	    			if (choseRand == false) {
	    				if (is3(xDestination,yDestination) ||os3(xDestination,yDestination)) {
	    					yRand = (int) (400 + (Math.random() * 29));
	    				}
	    				else yRand = (int) (190 - (Math.random() * 29));
	    				choseRand = true;
	    			}
	    			if (getYPos() == 191 || getYPos() == 399) {
	    				choseRand = false;
	    				transitionDone = false;
	    			}	
	    			goVertical(yRand);
	    		}
	    		if (os2(xDestination,yDestination)) {
	        		goVertical(crossWalk);
	        		goHorizontal(cWalk2i);
	        		if (getYPos() == crossWalk && getXPos() == cWalk2i) {
	        			readyToGoOuterSidewalk = true;
	        			crossWalk2o();
	        		}
	        	}
	    	}
	    	
	    	if (is3(getXPos(),getYPos()) && !readyToGoOuterSidewalk && !readyToGoInnerSidewalk) {
	    		//System.out.println("In is3");
	    		if (!transitionDone) {
	    			goVertical(yRand);
	    			if (getYPos() == yRand) {
	    				transitionDone = true;
	    			}
				}
	        	if (is3(xDestination,yDestination) && transitionDone) {
	        		goHorizontal(xDestination);
	    			if (getXPos() == xDestination) {
	    				goVertical(yDestination);
	    				//atDestination
	    			}
	        	}
	    		if ((os(xDestination,yDestination) || is(xDestination,yDestination)) && !is3(xDestination,yDestination) && !os3(xDestination,yDestination) && transitionDone) {
	    			if (choseRand == false) {
	    				if (is2(xDestination,yDestination) || os2(xDestination,yDestination)) {
	    					xRand = (int) (400 + (Math.random() * 29));
	    				}
	    				else xRand = (int) (190 - (Math.random() * 29));
	    				choseRand = true;
	    			}
	    			else {
	    				goHorizontal(xRand);
	    				if (getXPos() == xRand) {
	    					if (getYPos() == 400) {
	        					choseRand = false;
	    					}
	    					goVertical(yDestination);
	    				}	
	    			}
	    		}
	    		else if (os3(xDestination,yDestination)) {
	        		goHorizontal(crossWalk);
	        		goVertical(cWalk3i);
	        		if (getXPos() == crossWalk && getYPos() == cWalk3i) {
	        			readyToGoOuterSidewalk = true;
	        			crossWalk3o();
	        		}
	        	}
	    	}
	    
	    	if (is4(getXPos(),getYPos()) && !readyToGoOuterSidewalk && !readyToGoInnerSidewalk) {
	    		//System.out.println("In is4");
	    		if (is4(xDestination,yDestination)) {
	        		goVertical(yDestination);
	    			if (getYPos() == yDestination) {
	    				goHorizontal(xDestination);
	    				//atDestination
	    			}
	        	}
	    		if ((os(xDestination,yDestination) || is(xDestination,yDestination)) && !is4(xDestination,yDestination) && !os4(xDestination,yDestination)) {
	    			if (choseRand == false) {
	    				if (is3(xDestination,yDestination) || os3(xDestination,yDestination)  ) {
	    					yRand = (int) (400 + (Math.random() * 29));
	    				}
	    				else yRand = (int) (190 - (Math.random() * 29));
	    				choseRand = true;
	    			}
	    			if (getYPos() == 191 || getYPos() == 399) {
	    				choseRand = false;
	    				transitionDone = false;
	    			}	
	    			goVertical(yRand);
	    		}
	    		if (os4(xDestination,yDestination)) {
	        		goVertical(crossWalk);
	        		goHorizontal(cWalk4i);
	        		if (getYPos() == crossWalk && getXPos() == cWalk4i) {
	        			readyToGoOuterSidewalk = true;
	        			crossWalk4o();
	        		}
	        	}
	    	}
	    	if(onTheMove && rectangle.x==this.xDestination && rectangle.y==this.yDestination){
	    		onTheMove = false;
	    		person.msgAtDestination();
	    		
	    	}
		}
    }*/
    
    public void doStayPut() {
    	System.out.println("JKFDHSHFJKSDFJHKDDFJKHDSFJKHFJKFJHKFDSFDJHKFDSJHKDFSj");
    	xDestination = rectangle.x;
    	yDestination = rectangle.y;
    	market = true;
    }
    
    public boolean market = false;
    
    public void updatePosition() {

    	for(StopLight s:stopLights){
    		if(s.getStatus()==CrosswalkStatus.Vehicle && 
    				rectangle.intersects(s.safeRegion) &&
    				!s.safeRegion.contains(rectangle)){
    			return;
    		}
    	}
    	
    	if(rectangle.x<xDestination){
    		rectangle.x++;
			spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.person).rightSprites.get(changeSpriteCounter % ((PersonAgent)this.person).rightSprites.size());
				changeSpriteCounter++;
			}
    	}
    	else if(rectangle.x>xDestination){
    		rectangle.x--;
    		spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.person).leftSprites.get(changeSpriteCounter % ((PersonAgent)this.person).leftSprites.size());
				changeSpriteCounter++;
			}
    	}
    	
    	if(rectangle.y<yDestination){
    		rectangle.y++;
    		spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.person).downSprites.get(changeSpriteCounter % ((PersonAgent)this.person).downSprites.size());
				changeSpriteCounter++;
			}
    	}
    	else if(rectangle.y>yDestination){
    		rectangle.y--;
    		spriteCounter++;
			if (spriteCounter % spriteChangeSpeed == 0) {
				currentImage = ((PersonAgent)this.person).upSprites.get(changeSpriteCounter % ((PersonAgent)this.person).upSprites.size());
				changeSpriteCounter++;
			}
    	}
    	
    	
    	
    	if(doingMove && rectangle.x==this.xDestination && rectangle.y==this.yDestination){
    		doingMove = false;
    		//System.out.println("RELEASING THREAD");
    		atMove.release();
    	}
    	
    	if(onTheMove && rectangle.x==this.xDestination && rectangle.y==this.yDestination){
    		onTheMove = false;
    		person.msgAtDestination();
    		
    	}
    }
    

	public boolean getArrived() {
		return hasArrived;
	}
	
	public void setArrived(boolean arrived) {
		hasArrived = arrived;
	}
	
	public void setLoc(Loc loc){
		rectangle.x = loc.x;
		rectangle.y = loc.y;
	}
	
	public boolean getStartPosition() {
		return startPosition;
	}
	
    public void paint(Graphics g) {
    	if(!visible){
    		return;
    	}

    	if (!atDestination() || waitingForBus || waitingForCarToGetOnRoad) {
    		g.setColor(Color.BLACK);
    		g.setFont(mapStringFont);
    		g.drawString(String.valueOf(((PersonAgent)this.person).personID), getXPos(), getYPos());
    		g.drawImage(currentImage.getImage(),getXPos(),getYPos(),10,10,null);
    	}
    }    	

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return rectangle.x;
    }

    public int getYPos() {
        return rectangle.y;
    }
    
    public boolean os1(int x, int y) {
    	if (x >= 40 && x <= 550 && y >= 40 && y <= 70) {
    		return true;
    	}
    	else return false;
    }
    
    public boolean os2(int x, int y) {
    	if (x >= 520 && x <= 550 && y > 70 && y < 520) {
    		return true;
    	}
    	else return false;
    }
	
    public boolean os3(int x, int y) {
    	if (x >= 40 && x <= 550 && y >= 520 && y <= 550) {
    		return true;
    	}
    	else return false;
	}
	
    public boolean os4(int x, int y) {
    	if (x >= 40 && x <= 70 && y > 70 && y < 520) {
    		return true;
    	}
    	else return false;
	}
   
    public boolean is1(int x, int y) {
    	if (x >= 160 && x <= 430 && y >= 160 && y <= 190) {
    		return true;
    	}
    	else return false;
    }
    
    public boolean is2(int x, int y) {
    	if ((x >= 400 && x <= 430 && y > 190 && y < 400)) {
    		return true;
    	}
    	else return false;
    }
	
    public boolean is3(int x, int y) {
    	if ((x >= 160 && x <= 430 && y >= 400 && y <= 430)) {
    		return true;
    	}
    	else return false;
	}
	
    public boolean is4(int x, int y) {
    	if ((x >= 160 && x <= 190 && y > 190 && y < 400)) {
    		return true;
    	}
    	else return false;
	}
    
    public boolean is(int x, int y) {
    	if (is1(x,y) || is2(x,y) || is3(x,y) || is4(x,y)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean b1(int x, int y) {
    	if (getXPos() >= 290 && getXPos() <= 310 && getYPos() >= 40 && getYPos() <= 190) {
    		return true;
    	}
    	return false;
    }
    public boolean b2(int x, int y) {
    	if (getYPos() >= 290 && getYPos() <= 310 && getXPos() >= 400 && getXPos() <= 550) {
    		return true;
    	}
    	return false;
    }
    public boolean b3(int x, int y) {
    	if (getXPos() >= 290 && getXPos() <= 310 && getYPos() >= 400 && getYPos() <= 550) {
    		return true;
    	}
    	return false;
    }
    public boolean b4(int x, int y) {
    	if (getYPos() >= 290 && getYPos() <= 310 && getXPos() >= 40 && getXPos() <= 190) {
    		return true;
    	}
    	return false;
    }    
    
    public boolean os(int x, int y) {
    	if (os1(x,y) || os2(x,y) || os3(x,y) || os4(x,y)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean topLeftCorner(int x, int y) {
    	if (getYPos() >= 0 && getYPos() <= 40 && getXPos() >= 0 && getXPos() <= 40) {
    		return true;
    	}
    	return false;
    }
    public boolean topRightCorner(int x, int y) {
    	if (getYPos() >= 0 && getYPos() <= 40 && getXPos() >= 550 && getXPos() <= 600) {
    		return true;
    	}
    	return false;
    	
    }
    public boolean bottomLeftCorner(int x, int y) {
    	if (getYPos() > 550 && getYPos() <= 600 && getXPos() >= 0 && getXPos() <= 40) {
    		return true;
    	}
    	return false;
    }
    public boolean bottomRightCorner(int x, int y) {
    	if (getYPos() > 550 && getYPos() <= 600 && getXPos() >= 550 && getXPos() <= 600) {
    		return true;
    	}
    	return false;
    }
    
    public boolean atDestination() {
    	if (getXPos() == xDestination && getYPos() == yDestination) {
    		return true;
    	}
    	return false;
    }
    
    public void goHorizontal(int xDest) {
    	if (rectangle.x == xDest) {
    		return;
    	}
    	if (!this.person.belongings.bike) {	
    		if (rectangle.x < xDest) {
    			rectangle.x++;
    			currentImage = this.person.rightSprites.get(rectangle.x % this.person.rightSprites.size());
    		}
    		else if (rectangle.x > xDest) {
    			rectangle.x--;
    			currentImage = this.person.leftSprites.get(rectangle.x % this.person.leftSprites.size());
    		}
    	}
    }
    
    public void goVertical(int yDest) {
    	if (this.rectangle.y == yDest) {
    		return;
    	}
    	else if (!this.person.belongings.bike) {
    		if (this.rectangle.y < yDest) {
    			this.rectangle.y++;
    			currentImage = this.person.downSprites.get(rectangle.y % this.person.downSprites.size());
    		}
    		else if (rectangle.y > yDest) {
    			this.rectangle.y--;
    			currentImage = this.person.upSprites.get(rectangle.y % this.person.upSprites.size());
    		}
    	}
    }

    
    private void crossWalk1o() {   	
    	cWalk1o =  (int) (69 - (Math.random() * 29));
    }
    private void crossWalk2o() {
    	cWalk2o = (int) (521 + (Math.random() * 29));
    }
    private void crossWalk3o() {
    	cWalk3o = (int) (521 + (Math.random() * 29));
    }
    private void crossWalk4o() {
    	cWalk4o = (int) (69 - (Math.random() * 29));
    }
    public void crossWalk1i() {
    	cWalk1i = (int) (161 + (Math.random() * 29));
    }
    public void crossWalk2i() {
    	cWalk2i = (int) (429 - (Math.random() * 29));
    }
    private void crossWalk3i() {
    	cWalk3i = (int) (429 - (Math.random() * 29));
    }
    private void crossWalk4i() {
    	cWalk4i = (int) (161 + (Math.random() * 29));
    }

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
    
}
