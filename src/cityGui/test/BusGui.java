package cityGui.test;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.table.TableStringConverter;

import cityGui.CityComponent;
import cityGui.CityPanel;
import cityGui.SimCityGui;
import person.PersonAgent;
import public_Gui.Gui;
import util.Bus;
import util.Loc;

public class BusGui extends CityComponent implements Gui {

    private Bus bus;
    

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
    
    private boolean startPosition = true;
    private boolean hasArrived = false;
    private boolean readyToGoInnerSidewalk = false;
    private boolean readyToGoOuterSidewalk = false;
    private boolean transitionDone = true;
    private boolean choseRand = false;
    private boolean clockwise = true;
    private boolean moving = false;
    
    public boolean onTheMove = false;
    
    private Semaphore crossingStreet = new Semaphore(0,true);
    
    

    
    public BusGui(Bus bus) {
        this.bus = bus;
    }
    
    public BusGui(Bus bus,SimCityGui gui,boolean clockwise){
    	super(165,130,Color.YELLOW,"Bus");
    	if(!clockwise){
    		this.x = 460;
    		this.y = 90;
    	}
    	this.bus = bus;
    	this.gui = gui;
    	this.xDestination = this.x;
    	this.yDestination = this.y;
    	this.clockwise = clockwise;
    	rectangle = new Rectangle(x, y, 50, 20);
    }
    
    
    
    public BusGui(Bus b, SimCityGui gui, int xPos, int yPos, int xDest, int yDest){ //HostAgent m) {
        super(xPos, yPos, Color.YELLOW, "Bus");
        bus = b;
        xDestination = xDest;
        yDestination = yDest;
        this.gui = gui;
        rectangle = new Rectangle(xPos, yPos, 50, 20);
                
	}
    
    public void doGoToNextStop(Loc loc){
    	
    	this.xDestination = loc.x;
    	this.yDestination = loc.y;
    	this.moving = true;
    }
    
    public boolean topLaneF(){
    	if(rectangle.y==130 && (rectangle.x>=120 && rectangle.x<=430)){
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
    	if(rectangle.y==90 && (rectangle.x>=90 && rectangle.x<=460)){
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
    	if(rectangle.x==450 && rectangle.y>=130 && rectangle.y<=420){
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
    	if(rectangle.x==490 && rectangle.y>=90 && rectangle.y<=460){
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
    	if(rectangle.y == 450 && rectangle.x>=130 && rectangle.x<=420){
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
    	if(rectangle.y == 490 && rectangle.x>=90 && rectangle.x<=460){
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
    	if(rectangle.x == 130 && rectangle.y<=450 && rectangle.y>=130){
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
    	if(rectangle.x == 90 && rectangle.y<=460 && rectangle.y>=90){
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
    
    
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if(!moving){
			return;
		}
		if(clockwise){
        	if(topLaneF()){
        		rectangle.x++;
        	}
        	if(topRightCornerF()){
        		rectangle = new Rectangle(450,130,20,50);
        	}
        	if(rightLaneF()){
        		rectangle.y++;
        	}
        	if(bottomRightCornerF()){
        		rectangle = new Rectangle(420,450,50,20);
        	}
        	if(bottomLaneF()){
        		rectangle.x--;
        	}
        	if(bottomLeftCornerF()){
        		rectangle = new Rectangle(130,420,20,50);
        	}
        	if(leftLaneF()){
        		rectangle.y--;
        	}
        	if(topLeftCornerF()){
        		rectangle = new Rectangle(130,130,50,20);
        	}
        }
		else{
			if(topLaneB()){
        		rectangle.x--;

        	}
        	if(topLeftCornerB()){
        		rectangle = new Rectangle(90,90,20,50);
        	}
        	if(leftLaneB()){
        		rectangle.y++;
        	}
        	if(bottomLeftCornerB()){
        		rectangle = new Rectangle(90,490,50,20);
        	}
        	if(bottomLaneB()){
        		rectangle.x++;
        	}
        	if(bottomRightCornerB()){
        		rectangle = new Rectangle(490,460,20,50);
        	}
        	if(rightLaneB()){
        		rectangle.y--;
        	}

        	if(topRightCornerB()){
        		rectangle = new Rectangle(460,90,50,20);
        		
        	}





		}
		if(moving && rectangle.x==this.xDestination && rectangle.y ==this.yDestination){
			moving = false;
			this.bus.atStopFreeze.release();
			this.bus.updateBus();
		}
		
		
	}
    
        

    public boolean getArrived() {
            return hasArrived;
    }
    
    public void setArrived(boolean arrived) {
            hasArrived = arrived;
    }
    
    public boolean getStartPosition() {
            return startPosition;
    }
        
    public void paint(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillRect(xPos, yPos, 50, 20);
    }            

    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToCookingStation() {
            xDestination = 385;
            yDestination = 0;
    }

    public int getXPos() {
        return rectangle.x;
    }

    public int getYPos() {
        return rectangle.y;
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