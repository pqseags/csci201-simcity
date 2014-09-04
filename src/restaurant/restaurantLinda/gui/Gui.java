package restaurant.restaurantLinda.gui;

import java.awt.*;

public interface Gui extends public_Gui.Gui{

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();

}
