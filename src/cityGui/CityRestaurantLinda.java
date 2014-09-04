package cityGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import restaurant.restaurantLinda.CashierRole;
import restaurant.restaurantLinda.CookRole;
import restaurant.restaurantLinda.CustomerRole;
import restaurant.restaurantLinda.HostRole;
import restaurant.restaurantLinda.RestaurantLinda;
import restaurant.restaurantLinda.RestaurantOrder;
import restaurant.restaurantLinda.WaiterRole;
import restaurant.restaurantLinda.gui.CookGui;
import restaurant.restaurantLinda.gui.RestaurantLindaControlPanel;
import util.Loc;
import util.RestaurantMapLoc;

public class CityRestaurantLinda extends CityRestaurant {
	
	public static int cellSize = 50;
	static int gridX = CityRestaurantLindaCard.CARD_HEIGHT/cellSize;
    static int gridY = CityRestaurantLindaCard.CARD_WIDTH/cellSize;
    //Create grid for AStar
    public Semaphore[][] grid = new Semaphore[gridX][gridY];

    //private JPanel restLabel = new JPanel();
    //private ListPanel customerPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this,"Waiters");
    //private TablePanel tablePanel = new TablePanel(this);
    private Map<Integer,Point> tableMap=new HashMap<Integer,Point>();
    
    public CityRestaurantLindaCard animationPanel;
	
	
//	public CityRestaurantLinda(int x, int y) {
//		super(x, y);
//		//initializeRestaurant();
//		//System.out.println("First");
//	}
	
	public CityRestaurantLinda(int x, int y, String ID) {
		super(x, y, ID);
		
		type="RestaurantLinda";
		rectangle = new Rectangle(x, y, buildingSize, buildingSize);
		//System.out.println("Second");

	}

	public void initializeRestaurant(){	
		restaurant = new RestaurantLinda(this);   
       
        //initialize the semaphores
        for (int i=0; i<gridX ; i++)
    	    for (int j = 0; j<gridY; j++)
    	    	grid[i][j]=new Semaphore(1,true);
        try{
        	//Plating area
        	for (int i=gridX-(150/cellSize); i<gridX-(100/cellSize); i++){
        		for (int j=0; j<gridY; j++){
        			grid[i][j].acquire();
        		}
        	}
        	//Refrigerator
        	for (int i=0; i<(CityRestaurantLindaCard.REFRIGERATOR.width/cellSize); i++){
        		for (int j=0; j<(CityRestaurantLindaCard.REFRIGERATOR.height/cellSize); j++){
        			grid[(CityRestaurantLindaCard.REFRIGERATOR.x/cellSize)+i][(CityRestaurantLindaCard.REFRIGERATOR.y/cellSize)+j].acquire();
        		}
        	}
        	//Stove
        	for(int i=0; i<(CityRestaurantLindaCard.STOVE.width/cellSize); i++){
        		for (int j=0; j<(CityRestaurantLindaCard.STOVE.height/cellSize); j++){
        			grid[(CityRestaurantLindaCard.STOVE.x/cellSize)+i][(CityRestaurantLindaCard.STOVE.y/cellSize)+j].acquire();
        		}
        	}
        	
        	//Cashier
        	grid[0][100/cellSize].acquire();
        	
        }catch (Exception e) {
    	    System.out.println("Unexpected exception caught in during setup:"+ e);
    	}
        
        //add 3 tables
        addTable(1,150,150);
        addTable(1,150,250);
        addTable(1,150,350);
        
	}
    
	
	public void updatePosition() {
		
	}
	
	@Override
	public JPanel addAgentObjectToMap(){
		RestaurantMapLoc rMap = new RestaurantMapLoc(restaurant);
		rMap.loc = new Loc(sidewalkX(x,y),sidewalkY(x,y));
		this.cityObject.cityMap.map.get("Restaurant").add(rMap);
		return null;
	}

	public void paint(Graphics g) {
		if (this.invalidPlacement) {
			g.drawImage(img5.getImage(),x,y,35,35,null);
		}
		else if (this.outerTopSide((int)rectangle.getX(), (int)rectangle.getY()+35) || this.innerBottomSide((int)rectangle.getX(), (int)rectangle.getY())) {
			g.drawImage(img1.getImage(),x,y,35,35,null);
		}
		else if (this.outerRightSide((int)rectangle.getX(), (int)rectangle.getY()) || this.innerLeftSide((int)rectangle.getX(), (int)rectangle.getY())) {
			g.drawImage(img2.getImage(),x,y,35,35,null);
		}
		else if (this.outerBottomSide((int)rectangle.getX(), (int)rectangle.getY())|| this.innerTopSide((int)rectangle.getX(), (int)rectangle.getY())) {
			g.drawImage(img3.getImage(),x,y,35,35,null);
		}
		else if (this.outerLeftSide((int)rectangle.getX()+35, (int)rectangle.getY()) || this.innerRightSide((int)rectangle.getX(), (int)rectangle.getY())) {
			g.drawImage(img4.getImage(),x,y,35,35,null);
		}
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	
//	public boolean contains(int x, int y) {
//		if (x >= this.x && x <= this.x+20)
//			if (y >= this.y && y <= this.y+20)
//				return true;
//		return false;
//	}
	
	public boolean addTable(int size, int xLoc, int yLoc) {
    	xLoc = xLoc/cellSize;
    	yLoc = yLoc/cellSize;
		if(tryAddTable(CityRestaurantLindaCard.TABLESIZE/cellSize, xLoc, yLoc)) {
			tableMap.put(tableMap.size()+1, new Point(xLoc*cellSize,yLoc*cellSize));
			((CityRestaurantLindaCard)animationPanel).addTable(new Point(xLoc*cellSize,yLoc*cellSize));
			((HostRole)(restaurant.host)).addTable(size);
			System.out.println("Added table " + (tableMap.size()+1));
			return true;
		}
		System.out.println("Cannot add table " + (tableMap.size()+1));
		return false;
	}
    
    //Takes in the reduced coordinates and sizes 
    private boolean tryAddTable(int size, int x, int y)
    {
    	try
		{
			int acqCnt = -1;
			int[][] acqList = new int[size*size][2];
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					boolean acquired = grid[x+i][y+j].tryAcquire();
					if(acquired) {
						acqCnt++;
						acqList[acqCnt][0] = x+i;
						acqList[acqCnt][1] = y+j;
					}
				    if(!acquired) {
						for(int k=0; k<=acqCnt; k++) {
							grid[acqList[k][0]][acqList[k][1]].release();
						}
						return false;
					}
				}
			}
		}catch (Exception e)
		{
		    System.out.println("Unexpected exception caught in during setup:"+ e);
		}
    	return true;
    }
    
	
    public void createAnimationPanel(SimCityGui city){
    	animationPanel = new CityRestaurantLindaCard(city);
    	animationPanel.grid = grid;
    	super.animationPanel = animationPanel;
		
		initializeRestaurant();
	}
	
    public void createControlPanel(){
    	animationPanel.controlPanel = new RestaurantLindaControlPanel(animationPanel, (RestaurantLinda) restaurant);
    }
    
	public Map<Integer, Point> getTables(){
		return tableMap;
	}
}
