package bank.gui;

import java.awt.*;

public interface Gui {

	public void updatePosition(int xTable, int yTable);
    public void draw(Graphics2D g);
    public boolean isPresent();
    
    public Font stringFont = new Font("TimesRoman",Font.PLAIN,10);
}
