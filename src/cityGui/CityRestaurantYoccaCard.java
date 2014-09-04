package cityGui;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import public_Gui.Gui;

import restaurant.restaurantYocca.gui.RestaurantYoccaControlPanel;
//import restaurant.restaurantYocca.gui.RestaurantPanel;


public class CityRestaurantYoccaCard extends CityRestaurantCard {
	//initial values
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;

	private static final int clearRectXValue = 0;
	private static final int clearRectYValue = 0;
	private static final int tableRectWidthValue = 50;
	private static final int tableRectHeightValue = 50;
	
	RestaurantYoccaControlPanel panel;

	public CityRestaurantYoccaCard(SimCityGui city) {
		super(city);
		// TODO Auto-generated constructor stub
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(clearRectXValue, clearRectYValue, CARD_WIDTH, CARD_HEIGHT);

		//Here is the cook
		g2.setColor(Color.GREEN);
		g2.fillRect(300, 0, 30, tableRectHeightValue+10);

		g2.setColor(Color.GREEN);
		g2.fillRect(300, 50, tableRectWidthValue+80, 30);

		g2.setColor(Color.GRAY);
		g2.fillRect(300, 0, 30, 25);

		g2.fillOval(395, 53, 25, 25);

		g2.setColor(Color.WHITE);
		g2.fillRect(365, 53, 25, 25);

		//Here is the cook
		g2.setColor(Color.BLACK);
		g2.fillRect(225, 0, tableRectWidthValue+10, tableRectHeightValue-10);

		//        g.setColor(Color.WHITE);
		//        g.drawString(moneyString, 225, 15);

		//Here is the table
		g2.setColor(Color.ORANGE);
		g2.fillRect(100, 300, tableRectWidthValue, tableRectHeightValue);//200 and 250 need to be table params

		g2.setColor(Color.ORANGE);
		g2.fillRect(200, 300, tableRectWidthValue, tableRectHeightValue);

		g2.setColor(Color.ORANGE);
		g2.fillRect(300, 300, tableRectWidthValue, tableRectHeightValue);

		g2.setColor(Color.ORANGE);
		g2.fillRect(400, 300, tableRectWidthValue, tableRectHeightValue);

		try {
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
		catch (ConcurrentModificationException e) {
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		try {
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		catch (ConcurrentModificationException ex) {
		}
		repaint();
	}
	 public void setPanel(RestaurantYoccaControlPanel p){
			this.panel = p;
		}
}


