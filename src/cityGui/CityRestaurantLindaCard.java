package cityGui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import public_Gui.Gui;
import restaurant.restaurantLinda.gui.MyImage;
import restaurant.restaurantLinda.gui.RestaurantLindaControlPanel;


public class CityRestaurantLindaCard extends CityRestaurantCard{
	//initial values
	public static final int PERSONSIZE=25;
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	public static final Rectangle REFRIGERATOR = new Rectangle(CARD_WIDTH-100, 0, 79, 30);
	public static final Rectangle STOVE = new Rectangle(CARD_WIDTH-100, CARD_HEIGHT-50, 80, 50);
	public static final Rectangle CASHIER = new Rectangle(0, 100, PERSONSIZE, PERSONSIZE);
	public static final int TABLESIZE=50;

	private Collection<Point> tableMap = Collections.synchronizedList(new ArrayList<Point>());

    public Semaphore[][] grid;

	public CityRestaurantLindaCard(SimCityGui city) {
		super(city);
		// TODO Auto-generated constructor stub
	}


	public void paint(Graphics g2) {
		Graphics2D g = (Graphics2D)g2;

		//Clear the screen by painting a rectangle the size of the frame
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, CARD_WIDTH+50, CARD_HEIGHT+50);

		//Here is the cook's area       
		g.setColor(Color.WHITE);
		g.fillRect(REFRIGERATOR.x, REFRIGERATOR.y, REFRIGERATOR.width+20, REFRIGERATOR.height);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(STOVE.x, STOVE.y, STOVE.width+20, STOVE.height);
		g.draw3DRect(REFRIGERATOR.x, REFRIGERATOR.y, REFRIGERATOR.width+20, REFRIGERATOR.height, true);
		g.drawString("Refrigerator", CARD_WIDTH-80, 20);
		g.setColor(Color.GRAY);
		g.fillRect(CARD_WIDTH-150, 0, 50, CARD_HEIGHT);
		g.drawOval(CARD_WIDTH-92, CARD_HEIGHT-50, 24, 24);
		g.drawOval(CARD_WIDTH-61, CARD_HEIGHT-50, 24, 24);
		g.drawOval(CARD_WIDTH-30, CARD_HEIGHT-50, 24, 24);
		g.drawOval(CARD_WIDTH-92, CARD_HEIGHT-26, 24, 24);
		g.drawOval(CARD_WIDTH-61, CARD_HEIGHT-26, 24, 24);
		g.drawOval(CARD_WIDTH-30, CARD_HEIGHT-26, 24, 24);

		int cellSize = CityRestaurantLinda.cellSize;
		int gridX = CARD_WIDTH/cellSize;
		int gridY = CARD_HEIGHT/cellSize;

		/* for (int i=0; i<gridX ; i++)
    	    for (int j = 0; j<gridY; j++){
    	    	g.drawRect(i*cellSize, j*cellSize, cellSize, cellSize);
    	    	g.drawString(i*cellSize + " " + j*cellSize, i*cellSize+5, j*cellSize+PERSONSIZE);
    	    }*/

		//Here are the tables
		synchronized(tableMap){
		for (Point table: tableMap)
			{
	        	g.setColor(Color.ORANGE);
	        	g.fillRect(table.x, table.y, TABLESIZE, TABLESIZE);
	        }
		}

		synchronized(guis){
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		}

		synchronized(guis){
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw((Graphics2D)g);
	            }
	        }
		}
        
        //The plated foods
        synchronized(platedFoods){
        	for (MyImage plate: platedFoods){
        		plate.draw(g);
        	}
        }
        
        //The cashier
        g.setColor(Color.BLUE);
        g.fillRect(CASHIER.x, CASHIER.y, CASHIER.width, CASHIER.height);
        
        //blocked grids
        /*if (grid!=null){
	        g2.setColor(Color.RED);
	        for (int i=0; i<gridX ; i++)
	    	    for (int j = 0; j<gridY; j++)
	    	    	if (grid[i][j].availablePermits()<=0)
	    	    		g2.drawRect(i*cellSize, j*cellSize, cellSize, cellSize);
        }*/
    }

    
    public void addTable(Point p){
    	tableMap.add(p);
    }
}


