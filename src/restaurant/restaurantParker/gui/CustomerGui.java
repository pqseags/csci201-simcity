package restaurant.restaurantParker.gui;

import restaurant.restaurantParker.CustomerRole;
import restaurant.restaurantParker.HostRole;
import public_Gui.Gui;
import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	
	private String choice;
	enum AnimState {Passive, Ready, Ordered, Eating, WantCheck};
	AnimState state = AnimState.Passive;

	//private HostAgent host;
	//RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToLobby, GoToLobbySeat, GoToSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;

    private final int xCustomerRoom = 10;
    private final int yCustomerRoom = 15;

	
	
	   //TABLE LOCATIONS
    private final int xTable1 = 50;
    private final int yTable1 = 130;
    
    private final int xTable2 = 180;
    private final int yTable2 = 130;
    
    private final int xTable3 = 310;
    private final int yTable3 = 130;
    
    private final int xCashier = 380;
    private final int yCashier = 10;
 

	public CustomerGui(CustomerRole c  ){ //, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -20;
		yPos = -20;
		xDestination = -20;
		yDestination = -20;
		//maitreD = m;
		//this.gui = gui;
	}

	public void updatePosition() {
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
			 if (command ==Command.GoToCashier) agent.msgAnimationFinishedGoToCashier();
			 if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//^doesn't matter
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
			//	gui.setCustomerEnabled(agent);
			 }
			 if (command == Command.GoToLobby)agent.msgAnimationFinishedGoToLobby(); 
			
			 if (command == Command.GoToLobbySeat){
				agent.msgAnimationFinishedGoToLobbySeat();
				//System.err.println("Got To Lobby Seat");
			 }
			 

			
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		
		if (state == AnimState.Ready){
			g.setColor(Color.BLACK);
			g.drawString("?", xPos+8, yPos+14);
		}
		
		if (state == AnimState.Ordered){
			g.setColor(Color.BLACK);
			g.drawString(choice + "?", xPos , yPos+ 30);
		}
		
		if (state == AnimState.Eating){
			g.setColor(Color.BLACK);
			g.drawString(choice, xPos, yPos+30);
		}
		
		if (state == AnimState.WantCheck){
			g.setColor(Color.BLACK);
			g.drawString("$", xPos+8, yPos+14);
			
		}
	}

	
	public void readyToOrder(){
		state = AnimState.Ready;
	}
	
	public void Ordered(String choice){
		state = AnimState.Ordered;
		this.choice = choice;
	}
	
	public void gotFood(){
		state = AnimState.Eating;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		//agent.atRestaurant();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void setChoice(String c){
		choice = c;
	}

	//lab3
	public void DoGoToSeat(int seatNumber) {
		if (seatNumber == 1)
		{
			xDestination = xTable1;
			yDestination = yTable1;
		}
		if (seatNumber == 2)
		{
			xDestination = xTable2;
			yDestination = yTable2;
		}
		if (seatNumber == 3)
		{
			xDestination = xTable3;
			yDestination = yTable3;
		}
		command = Command.GoToSeat;
	}

	
	
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
		state = AnimState.Passive;
	}
	
	public void DoGoToCashier(){
		xDestination = xCashier+15;
		yDestination = yCashier+25;
		command = Command.GoToCashier;
		state = AnimState.Passive;
		
	}
	
	public void DoGoToLobby(){
		xDestination = xCustomerRoom;
		yDestination = yCustomerRoom;
		command = Command.GoToLobby;
		
	}
	
	public void DoGoToLobbySeat(int position){
		yDestination = (yCustomerRoom +2);
		xDestination = (57 + (position *25));
		command = Command.GoToLobbySeat;
	}
	
	public void wantCheck(){
		state = AnimState.WantCheck;
	}
}
