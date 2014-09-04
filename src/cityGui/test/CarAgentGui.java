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
import util.Bus;
import util.BusAgent;
import util.CarAgent;
import util.CrosswalkStatus;
import util.Loc;
import util.OnRamp;
import util.StopLight;

public class CarAgentGui extends VehicleAgentGui {

	
	public int xDestination,yDestination;
	public boolean doingMove;
	public Semaphore atMove = new Semaphore(1,true);
	public CarAgent car;
	
	public SimCityGui gui;
	public boolean clockwise;
	
	public static int gridScale = 30;
	public boolean moving = false;
	
	java.net.URL car0 = getClass().getResource("../cityImages/Vehicles/carUp.png");
	ImageIcon carUp = new ImageIcon(car0);
	java.net.URL car1 = getClass().getResource("../cityImages/Vehicles/carDown.png");
	ImageIcon carDown = new ImageIcon(car1);
	java.net.URL car2 = getClass().getResource("../cityImages/Vehicles/carLeft.png");
	ImageIcon carLeft = new ImageIcon(car2);
	java.net.URL car3 = getClass().getResource("../cityImages/Vehicles/carRight.png");
	ImageIcon carRight = new ImageIcon(car3);
	
	public List<StopLight> stopLights = new ArrayList<StopLight>();
	
	
	public CarAgentGui(CarAgent car,SimCityGui gui,boolean clockwise){
    	super(225,130,Color.RED,"Bus");
    	
    	if(!clockwise){
    		this.x = 140;
    		this.y = 90;
    	}
    	this.car = car;
    	this.gui = gui;
    	this.xDestination = this.x;
    	this.yDestination = this.y;
    	this.clockwise = clockwise;
    	rectangle = new Rectangle(x, y, 25, 15);
    	//setBumpers();
    	safeRegion = setSafeRegion(rectangle);
    }
	
	
	public boolean putCarOnRoad(OnRamp ramp){
		//System.out.println("Trying "+ramp.loc.x+", "+ramp.loc.y);
		Rectangle potentialLocation;
		Rectangle potentialSafety;
		
		if(ramp.vertical){
			potentialLocation = new Rectangle(ramp.loc.x,ramp.loc.y,15,25);
		}
		else{
			potentialLocation = new Rectangle(ramp.loc.x,ramp.loc.y,25,15);
		}
		
		potentialSafety = setSafeRegion(potentialLocation);
		
		synchronized(gui.city.movings){
			for(CityComponent block:gui.city.movings){
				if(block instanceof VehicleAgentGui){
					if(potentialSafety.intersects(((VehicleAgentGui)block).safeRegion)){
						//System.out.println("Something's int he way");
						return false;
					}
				}
			}
		}
		
		moving = false;
		rectangle = potentialLocation;
		safeRegion = potentialSafety;
		gui.city.movings.add(this);
		//System.out.println("Returning true");
		
		return true;
		
	}
	
	
	
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		//setBumpers();
		
		safeRegion = setSafeRegion(rectangle);
		
		if(!moving){
			return;
		}
		//System.out.println("UPDATING POSITION");
		
		for(StopLight s:stopLights){
			if(s.getStatus()==CrosswalkStatus.Pedestrian && this.rectangle.intersects(s.safeRegion)
					&& !this.rectangle.intersects(s.letGoRegion)){
				//System.out.println("Waiting at the red light");
				return;
			}
		}
		synchronized(gui.city.movings){
			for(CityComponent block : gui.city.movings){
				//System.out.println("Checking to see if I'm intersecting anything");
				if(block instanceof VehicleAgentGui){
					//System.out.println("Found a vehicle");
					
				}
			}
		}
		
		if(clockwise){
        	if(topLaneF()){
        		tryToMoveTo(rectangle.x+1,rectangle.y,rectangle.width,rectangle.height);
        		//rectangle.x++;
        	}
        	if(topRightCornerF()){
        		tryToMoveTo(450,130,15,25);
        		//rectangle = new Rectangle(450,130,15,25);
        	}
        	if(rightLaneF()){
        		tryToMoveTo(rectangle.x,rectangle.y+1,rectangle.width,rectangle.height);
        		//rectangle.y++;
        	}
        	if(bottomRightCornerF()){
        		tryToMoveTo(445,450,25,15);
        		//rectangle = new Rectangle(445,450,25,15);
        	}
        	if(bottomLaneF()){
        		tryToMoveTo(rectangle.x-1,rectangle.y,rectangle.width,rectangle.height);
        		//rectangle.x--;
        	}
        	if(bottomLeftCornerF()){
        		tryToMoveTo(130,420,15,25);
        		//rectangle = new Rectangle(130,420,15,25);
        	}
        	if(leftLaneF()){
        		tryToMoveTo(rectangle.x,rectangle.y-1,rectangle.width,rectangle.height);
        		//rectangle.y--;
        	}
        	if(topLeftCornerF()){
        		tryToMoveTo(130,130,25,15);
        		//rectangle = new Rectangle(130,130,25,15);
        	}
		}
    	else{
			if(topLaneB()){
				tryToMoveTo(rectangle.x-1,rectangle.y,rectangle.width,rectangle.height);
        		//rectangle.x--;
        	}
        	if(topLeftCornerB()){
        		tryToMoveTo(90,90,15,25);
        		//rectangle = new Rectangle(90,90,15,25);
        	}
        	if(leftLaneB()){
        		//System.out.println("IN LEFT LANE B");
        		tryToMoveTo(rectangle.x,rectangle.y+1,rectangle.width,rectangle.height);
        		//rectangle.y++;
        	}
        	if(bottomLeftCornerB()){
        		tryToMoveTo(90,490,25,15);
        		//rectangle = new Rectangle(90,490,25,15);
        	}
        	if(bottomLaneB()){
        		tryToMoveTo(rectangle.x+1,rectangle.y,rectangle.width,rectangle.height);
        		//rectangle.x++;
        	}
        	if(bottomRightCornerB()){
        		tryToMoveTo(490,480,15,25);
        		//rectangle = new Rectangle(490,480,15,25);
        	}
        	if(rightLaneB()){
        		tryToMoveTo(rectangle.x,rectangle.y-1,rectangle.width,rectangle.height);
        		//rectangle.y--;
        	}

        	if(topRightCornerB()){
        		tryToMoveTo(460,90,25,15);
        		//rectangle = new Rectangle(460,90,25,15);
        		
        	}





		}
		if(moving && rectangle.x==this.xDestination && rectangle.y ==this.yDestination){
			moving = false;
			
			
			//gui.city.movings.remove(this);
			car.msgCarArrived(new Loc(rectangle.x,rectangle.y));
			//bus.msgAtStop();
//			this.bus.atStopFreeze.release();
//			this.bus.updateBus();
		}
		
		
	}
	
	public void tryToMoveTo(int x, int y,int width,int height){
		Rectangle testRect = rectangle;
		//System.out.println("Trying to move");
		for(CityComponent block:gui.city.movings){
			if(!this.equals(block) && block instanceof VehicleAgentGui){
				testRect = new Rectangle(x,y,width,height);
				if(setSafeRegion(testRect).intersects(((VehicleAgentGui) block).safeRegion)){
					//System.out.println("Condition Tripped");
					testRect = rectangle;
					return;
				}
			}
		}
//		for(CityComponent block:gui.city.movings){
//			if(!this.equals(block) && block instanceof VehicleAgentGui){
//				testRect = new Rectangle(x,y,width,height);
//				if(testRect.intersects(((VehicleAgentGui) block).rectangle)){
//					testRect = rectangle;
//					return;
//				}
//			}
//		}
		
		//rectangle  = testRect;
		rectangle = new Rectangle(x,y,width,height);
		
	}
	
	
	public void goTo(int x, int y){
		moving = true;
		//System.out.println("Current Position "+rectangle.x+", "+rectangle.y);
		//System.out.println("goTo being called with "+x+", "+y);
		this.xDestination = x;
		this.yDestination = y;
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
	
	

	
	public void paint(Graphics g1) {
		//System.out.println("BEING CALLED");
		Graphics2D g = (Graphics2D) g1;
		
//		g.setColor(Color.GREEN);
//		g.fillRect(safeRegion.x, safeRegion.y, safeRegion.width, safeRegion.height);
		
		
		//THIS USED TO DRAW A RECTANGLE
		//g.setColor(Color.RED);
		//g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		
		if(topLaneF()){
			g1.drawImage(carRight.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		if(rightLaneF()){
			g1.drawImage(carDown.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		if(bottomLaneF()){
			g1.drawImage(carLeft.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		if(leftLaneF()){
			g1.drawImage(carUp.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		
		
		if(topLaneB()){
			g1.drawImage(carLeft.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		if(rightLaneB()){
			g1.drawImage(carUp.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		if(bottomLaneB()){
			g1.drawImage(carRight.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		if(leftLaneB()){
			g1.drawImage(carDown.getImage(),rectangle.x,rectangle.y,rectangle.width,rectangle.height,null);
		}
		
		
//        g.setColor(Color.BLUE);
//        //g.fillRect(rectangle.x, rectangle.y, 50, 20);
//        
//        g.fillRect(frontBumper.x, frontBumper.y, frontBumper.width, frontBumper.height);
//        g.fillRect(backBumper.x, backBumper.y, backBumper.width, backBumper.height);
    }  
	
	@Override
	public void draw(Graphics2D g) {
		
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	
	
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	public boolean topLaneF(){
    	if(rectangle.y==130 && (rectangle.x>=120 && rectangle.x<=430 && rectangle.width == 25)){
    		return true;
    	}
    	return false;
    }
    public boolean topRightCornerF(){
    	if(rectangle.x>=430 && rectangle.y==130){
    		return true;
    	}
    	return false;
    }
    public boolean topLaneB(){
    	//System.out.println("YEAAAAAAAA");
    	//System.out.println(rectangle.x+" "+rectangle.y);
    	if(rectangle.y==90 && (rectangle.x>=90 && rectangle.x<=460 && rectangle.width == 25 )){
    		//System.out.println("NOOOOOO");
    		return true;
    	}
    	return false;
    }
    public boolean topRightCornerB(){
    	if(rectangle.x==490 && rectangle.y<=90){
    		return true;
    	}
    	return false;
    }
    public boolean rightLaneF(){
    	if(rectangle.x==450 && rectangle.y>=130 && rectangle.y<=445  && rectangle.width == 15){
    		return true;
    	}
    	return false;
    }
    public boolean bottomRightCornerF(){
    	if(rectangle.x==450 && rectangle.y>=130 && rectangle.y>=420){
    		return true;
    	}
    	return false;
    }
    public boolean rightLaneB(){
    	if(rectangle.x==490 && rectangle.y>=90 && rectangle.y<=480  && rectangle.width == 15){
    		return true;
    	}
    	return false;	
    }
    public boolean bottomRightCornerB(){
    	if(rectangle.x>=460 && rectangle.y==490 ){
    		return true;
    	}
    	return false;
    }
    public boolean bottomLaneF(){
    	if(rectangle.y == 450 && rectangle.x>=130 && rectangle.x<=450 && rectangle.width == 25){
    		return true;
    	}
    	return false;
    }
    public boolean bottomLeftCornerF(){
    	if(rectangle.x<=130 && rectangle.y==450){
    		return true;
    	}
    	return false;
    }
    
	
	 public boolean bottomLaneB(){
	    	if(rectangle.y == 490 && rectangle.x>=90 && rectangle.x<=460 && rectangle.width == 25){
	    		return true;
	    	}
	    	return false;
	    }
	    public boolean bottomLeftCornerB(){
	    	if(rectangle.x==90 && rectangle.y>=460){
	    		return true;
	    	}
	    	return false;
	    }
	    public boolean leftLaneF(){
	    	if(rectangle.x == 130 && rectangle.y<=450 && rectangle.y>=130 && rectangle.width == 15){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    public boolean topLeftCornerF(){
	    	if(rectangle.x ==130 && rectangle.y<=130){
	    		return true;
	    	}
	    	return false;
	    }
	    public boolean leftLaneB(){
	    	if(rectangle.x == 90 && rectangle.y<=460 && rectangle.y>=90 && rectangle.width == 15){
	    		return true;
	    	}
	    	return false;
	    }
	    public boolean topLeftCornerB(){
	    	if(rectangle.x <=90 && rectangle.y==90){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    public void setBumpers(){
	    	
	    	if(topLaneF()){
	    		this.frontBumper = new Rectangle(rectangle.x+1+25,rectangle.y,1,rectangle.height);
	    		this.backBumper = new Rectangle(rectangle.x-2,rectangle.y,1,rectangle.height);
	    	}
	    	if(rightLaneF()){
	    		this.frontBumper = new Rectangle(rectangle.x,rectangle.y+1+25,rectangle.width,1);
	    		this.backBumper = new Rectangle(rectangle.x,rectangle.y-2,rectangle.width,1);
	    	}
	    	if(bottomLaneF()){
	    		this.frontBumper = new Rectangle(rectangle.x-2,rectangle.y,1,rectangle.height);
	    		this.backBumper = new Rectangle(rectangle.x+1+25,rectangle.y,1,rectangle.height);
	    	}
	    	if(leftLaneF()){
	    		this.frontBumper = new Rectangle(rectangle.x,rectangle.y-2,rectangle.width,1);
	    		this.backBumper = new Rectangle(rectangle.x,rectangle.y+1+25,rectangle.width,1);
	    	}
	    	
	    }
	    

	

}
