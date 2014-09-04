package cityGui;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import restaurant.restaurantSimon.gui.CookGui;
import restaurant.restaurantSimon.gui.CustomerGui;
import restaurant.restaurantSimon.gui.Gui;
import restaurant.restaurantSimon.gui.HostGui;
import restaurant.restaurantSimon.gui.RestaurantSimonControlPanel;
import restaurant.restaurantSimon.gui.WaiterGui;

public class CityRestaurantSimonCard extends CityRestaurantCard{

	public static final int xSize = 50;
	public static final int ySize = 50;
	public static final int deskXPos=300;
	public static final int deskYPos=40;
	public static final int deskX=30;
	public static final int deskY=100;
	public static final int grillY=30;
	public static final int fridgeY=20;

	private List<Gui> guis = new ArrayList<Gui>();
	
	RestaurantSimonControlPanel panel;

	public CityRestaurantSimonCard(SimCityGui city) {
		super(city);

	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(getBackground());
		g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT );

		//Here is the table
		g2.setColor(Color.ORANGE);
		g2.fillRect(CustomerGui.xTableUnit, CustomerGui.yTableUnit, xSize, ySize);//200 and 250 need to be table params

		//more tables
		g2.fillRect(3*CustomerGui.xTableUnit, CustomerGui.yTableUnit, xSize, ySize);//200 and 250 need to be table params
		g2.fillRect(5*CustomerGui.xTableUnit, CustomerGui.yTableUnit, xSize, ySize);//200 and 250 need to be table params

		//desks!
		g2.setColor(Color.BLUE);
		g2.fillRect(deskXPos, deskYPos, deskX,deskY);

		//grills
		g2.setColor(Color.GRAY);
		g2.fillRect(deskXPos+80, deskYPos, deskX, grillY);
		g2.fillRect(deskXPos+70, deskYPos+33, deskX, grillY);
		g2.fillRect(deskXPos+60, deskYPos+66, deskX, grillY);

		//refrigerator
		g2.setColor(Color.WHITE);
		g2.fillRect(deskXPos+30,deskYPos-20,deskX,fridgeY);


		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}
	public void addGui(CustomerGui gui) {
		guis.add(gui);
	}

	public void addGui(HostGui gui) {
		guis.add(gui);
	}
	public void addGui(WaiterGui gui) {
		guis.add(gui);
	}
	public void addGui(CookGui gui){
		guis.add(gui);
	}
	public void actionPerformed(ActionEvent e) {

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		repaint(); 
	}
	 public void setPanel(RestaurantSimonControlPanel p){
			this.panel = p;
		}
}
