package util;

import interfaces.Person;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import cityGui.CityComponent;
import cityGui.test.AStarTraversalVehicle;
import cityGui.test.BusAgentGui;
import cityGui.test.BusGui;
import agent.Agent;
import astar.AStarNode;
import astar.Position;

public class BusAgent extends VehicleAgent implements ActionListener{

	
	//Data
	List<Person> passengers = new ArrayList<Person>();
	
	static int TIME_AT_STOP = 3000;
	Timer timer;// = new Timer(TIME_AT_STOP, this);

	public List<BusStop> stops = new ArrayList<BusStop>();
	public int currentStop = 0;
	int randomOffset = (int) Math.floor(3*Math.random());
	public int TIME_BETWEEN_STOPS = 3;
	public Semaphore atStopFreeze = new Semaphore(1,true);
	
	public Semaphore waitingForStop = new Semaphore(1,true);
	
	public BusAgentGui gui;
	public Position currentPosition;
	public AStarTraversalVehicle aStar;
	
	public boolean timeToGo = false;
	public boolean arrivedAtStop = true;
	
	
	//Setters
	
	public void setGui(BusAgentGui bGui){
		this.gui = bGui;
	}
	
	public void setAStar(AStarTraversalVehicle a){
		this.aStar = a;
	}
	
	
	//Constructor 
	
	public BusAgent(){
		timer = new Timer(TIME_AT_STOP, this);
		timer.start();
		timer.addActionListener(this);
	}
	
	
	//Messages
	
	public void msgTimeToGo(){
		//Do("Got the message that it's time to go");
		//Do("Current Stop: "+currentStop);
		
		timeToGo = true;
		timer.stop();
		System.out.println(timer.isRunning());
		stateChanged();
	}
	
	public void msgAtStop(){
		Do("Got the message at Stop");
		waitingForStop.release();
	}
	
	
	//Scheduler
	
	protected boolean pickAndExecuteAnAction() {
		
		if(timeToGo){
			GoToNextStop();
			return true;
		}
		
		return false;
	}
	
	
	//Actions
	
	private void GoToNextStop(){
		
		timeToGo = false;
		
		doGoToNextStop();
		
		//Do("Updating bus stuff");
		currentStop = (currentStop+1)%stops.size();
		
		List<Person> passengersCopy = new ArrayList<Person>(passengers);
		for(Person p:passengersCopy){
			passengers.remove(p);
			p.msgBusAtStop(this, stops.get(currentStop));
		}
		
		List<Person> peopleWaitingCopy = new ArrayList<Person>(stops.get(currentStop).peopleWaiting);
		for(Person p:peopleWaitingCopy){
			passengers.add(p);
			stops.get(currentStop).peopleWaiting.remove(p);
			p.msgBusAtStop(this, stops.get(currentStop));
		}
		
		timer.start();
		
	}
	
	
	//GUI
	
	private void doGoToNextStop(){
		
		Loc nextStop = stops.get((currentStop+1)%stops.size()).location;
		
		/*Loc nextStopGrid = CityComponent.findNearestRoadGridLoc(new Point(nextStop.x,nextStop.y));
		
		guiMoveFromCurrentPostionTo(new Position(nextStopGrid.x,nextStopGrid.y));*/
		
		gui.goTo(nextStop.x,nextStop.y);
		
		waitingForStop = new Semaphore(0,true);
		try {
			waitingForStop.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	//Action Listener Stuff
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(timer) && timer.isRunning()){
			
			msgTimeToGo();
		}
		
	}
	
	
	
	
	 //this is just a subroutine for waiter moves. It's not an "Action"
    //itself, it is called by Actions.
    void guiMoveFromCurrentPostionTo(Position to){
        //System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());

    	//to = new Position(2,5);
    	Loc l = CityComponent.findNearestRoadGridLoc(new Point(gui.rectangle.x,gui.rectangle.y));
    	currentPosition = new Position(l.x,l.y);
    	//System.out.println("("+currentPosition.getX()+","+currentPosition.getY()+")");
    	//System.out.println("("+to.getX()+","+to.getY()+")");



    	//Do("CALLING PERSON MOVE");

        AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
        List<Position> path = aStarNode.getPath();
       // Do("Got here, path calculated");
        Boolean firstStep   = true;
        Boolean gotPermit   = true;

        for (Position tmpPath: path) {
            //The first node in the path is the current node. So skip it.
            if (firstStep) {
                firstStep   = false;
                continue;
            }

            //Try and get lock for the next step.
            int attempts    = 1;
            gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());

            //Did not get lock. Lets make n attempts.
            while (!gotPermit && attempts < 3) {
                //System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

                //Wait for 1sec and try again to get lock.
                try { Thread.sleep(1000); }
                catch (Exception e){}

                gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
                attempts ++;
            }

            //Did not get lock after trying n attempts. So recalculating path.
            if (!gotPermit) {
                //System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
                guiMoveFromCurrentPostionTo(to);
                break;
            }

            //Got the required lock. Lets move.
            //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
            currentPosition.release(aStar.getGrid());
            currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
            //System.out.println(gui==null);
            gui.move(currentPosition.getX(), currentPosition.getY());
        }
        /*
        boolean pathTaken = false;
        while (!pathTaken) {
            pathTaken = true;
            //print("A* search from " + currentPosition + "to "+to);
            AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
            if (a == null) {//generally won't happen. A* will run out of space first.
                System.out.println("no path found. What should we do?");
                break; //dw for now
            }
            //dw coming. Get the table position for table 4 from the gui
            //now we have a path. We should try to move there
            List<Position> ps = a.getPath();
            Do("Moving to position " + to + " via " + ps);
            for (int i=1; i<ps.size();i++){//i=0 is where we are
                //we will try to move to each position from where we are.
                //this should work unless someone has moved into our way
                //during our calculation. This could easily happen. If it
                //does we need to recompute another A* on the fly.
                Position next = ps.get(i);
                if (next.moveInto(aStar.getGrid())){
                    //tell the layout gui
                    guiWaiter.move(next.getX(),next.getY());
                    currentPosition.release(aStar.getGrid());
                    currentPosition = next;
                }
                else {
                    System.out.println("going to break out path-moving");
                    pathTaken = false;
                    break;
                }
            }
        }
        */
    }
	

}
