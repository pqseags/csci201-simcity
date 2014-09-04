
package restaurant.restaurantParker.gui;

import public_Gui.Gui;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import restaurant.restaurantParker.CookRole;

public class CookGui implements Gui {

    private CookRole agent = null;
   // RestaurantGui gui;
   

    private int xPos = 340, yPos = 305;//default cook position
    private int xDestination = 340, yDestination = 305;//default start position
    
    //Kitchen Dimensions
    private final int xKitchen = 310;
    private final int yKitchen = 290;
    private final int KITCHENX = 100;
    private final int KITCHENY = 50;
    
    //grills
    boolean grill1 = false;
    boolean grill2 = false;
    boolean grill3 = false;
    
    Plate plate1 = new Plate();
    Plate plate2 = new Plate();
    Plate plate3 = new Plate();


    public CookGui(CookRole agent) {
        this.agent = agent;
       // this.gui = gui;
        
        
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

//        if (xPos == xDestination && yPos == yDestination && xPos != xInitial && yPos != yInitial && xPos!= xBreakRoom +50){
//           agent.msgAtDestination();
//        }
        	
      	
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, 20, 20);
        
        g.setColor(Color.RED);
        if(grill1){
        	g.fillRect(xKitchen - 25, yKitchen + 5, 20, 10);
        }
        if(grill2){
        	g.fillRect(xKitchen - 25, yKitchen + 20, 20, 10);
        }
        if(grill3){
        	g.fillRect(xKitchen - 25, yKitchen + 35, 20, 10);
        }
        
        g.setColor(Color.BLACK);
       // g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 10));

        if(plate1.inUse){
        	g.drawString(plate1.choice, xKitchen+11, yKitchen-5);
        }
        if(plate2.inUse){
        	g.drawString(plate2.choice, xKitchen+44, yKitchen-5);
        }
        if(plate3.inUse){
        	g.drawString(plate3.choice, xKitchen+77, yKitchen-5);
        }
      
    }
    
    public void drawGrill(int position){
    	switch (position){
    	case 0: grill1 = true;
    	break;
    	case 1: grill2 = true;
    	break;
    	case 2: grill3 = true;
    	break;
    	default: System.err.println("4th grill???  Position: " + position);
    	}
    }
    
    public void unDrawGrill(int position){
    	switch (position){
    	case 0: grill1 = false;
    	break;
    	case 1: grill2 = false;
    	break;
    	case 2: grill3 = false;
    	break;
    	default: System.err.println("4th plate???  Position: " + position);
    	}
    }
    
    public void drawPlate(int position, String fullChoice){
    	String choice;
    	switch (fullChoice){
    	case "Pizza": choice = "P";
    					break;
    	case "Salad": choice = "Sa";
    					break;
    	case "Steak": choice = "St";
    					break;
    	case "Chicken": choice = "C";
    					break;
    	default: System.err.println("Invalid choice to print at plating area");
    			choice = "XX";
    	}
    	
    	switch (position){
    	case 0: plate1.inUse = true;
    			plate1.choice = choice;
    			break;
    	case 1: plate2.inUse = true;
    			plate2.choice = choice;
    			break;
    	case 2: plate3.inUse = true;
    			plate3.choice = choice;
    			break;
    	default: System.err.println("4th plate???  Position: " + position);
    	}
    }
    
    public void unDrawPlate(int position){
    	switch (position){
    	case 0: plate1.inUse = false;
    			break;
    	case 1: plate2.inUse = false;
    			break;
    	case 2: plate3.inUse = false;
    			break;
    	default: System.err.println("4th plate???  Position: " + position);
    	}
    }
 
    public boolean isPresent() {
        return true;
    }
    

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    private class Plate{
    	boolean inUse;
    	String choice;
    	
    	Plate(){
    		inUse = false;
    	}
    }
    
}
