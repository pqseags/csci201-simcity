package public_Gui;

import java.awt.Font;
import java.awt.Graphics2D;

public interface Gui {
	public void updatePosition();	 
	public void draw(Graphics2D g);
	public boolean isPresent();

	public Font stringFont = new Font("TimesRoman",Font.PLAIN,10);
	public Font mapStringFont = new Font("TimesRoman",Font.PLAIN,10);
}
