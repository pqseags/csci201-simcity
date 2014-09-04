package restaurant.restaurantParker.gui;

import public_Gui.Gui;
import restaurant.restaurantParker.CustomerRole;
import restaurant.restaurantParker.WaiterRole;
import restaurant.restaurantParker.HostRole;

import java.awt.*;

public class HostGui implements Gui {

    private HostRole agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    //lab3
    public static final int xTable = 200;
    public static final int yTable = 250;

    public HostGui(HostRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
      
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }


}
