package cityGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import restaurant.ProducerConsumerMonitor;
import restaurant.Restaurant;
import restaurant.restaurantYocca.RestaurantYocca;
import restaurant.restaurantYocca.gui.RestaurantYoccaControlPanel;
import restaurant.restaurantLinda.CashierRole;
import restaurant.restaurantLinda.CookRole;
import restaurant.restaurantLinda.CustomerRole;
import restaurant.restaurantLinda.HostRole;
import restaurant.restaurantLinda.RestaurantOrder;
import restaurant.restaurantLinda.WaiterRole;
import restaurant.restaurantLinda.gui.CookGui;
import util.Loc;
import util.RestaurantMapLoc;

public class CityRestaurantYocca extends CityRestaurant implements ImageObserver {
	java.net.URL imgURL1 = getClass().getResource("cityImages/restaurant1.png");
	ImageIcon img1 = new ImageIcon(imgURL1);
	java.net.URL imgURL2 = getClass().getResource("cityImages/restaurant2.png");
	ImageIcon img2 = new ImageIcon(imgURL2);
	java.net.URL imgURL3 = getClass().getResource("cityImages/restaurant3.png");
	ImageIcon img3 = new ImageIcon(imgURL3);
	java.net.URL imgURL4 = getClass().getResource("cityImages/restaurant4.png");
	ImageIcon img4 = new ImageIcon(imgURL4);
	java.net.URL imgURL5 = getClass().getResource("cityImages/restaurant5.png");
	ImageIcon img5 = new ImageIcon(imgURL5);
	
	private int buildingSize = 35;
	
	public static int cellSize = 50;
	static int gridX = CityRestaurantYoccaCard.CARD_HEIGHT/cellSize;
    static int gridY = CityRestaurantYoccaCard.CARD_WIDTH/cellSize;
    //Create grid for AStar
    public Semaphore[][] grid = new Semaphore[gridX][gridY];

    //private JPanel restLabel = new JPanel();
    //private ListPanel customerPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this,"Waiters");
    //private TablePanel tablePanel = new TablePanel(this);
    private Map<Integer,Point> tableMap=new HashMap<Integer,Point>();
    	
	
    public CityRestaurantYocca(int x, int y) {
		super(x, y);
		//initializeRestaurant();
		//System.out.println("First");
	}
	
	public CityRestaurantYocca(int x, int y, String ID) {
		super(x, y, ID);
		
		type="RestaurantYocca";
		rectangle = new Rectangle(x, y, buildingSize, buildingSize);
		//System.out.println("Second");

	}

	public void initializeRestaurant(){	
		restaurant = new RestaurantYocca(this);       
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
    
	
	public void setAnimationPanel(CityRestaurantYoccaCard p){
		animationPanel = p;
        
        //host.startThread();
        //cook.startThread();
        //cashier.startThread();
        
        initializeRestaurant();
 
        
	}
	
	public Map<Integer, Point> getTables(){
		return tableMap;
	}

	@Override
	public void createAnimationPanel(SimCityGui city) {
		animationPanel = new CityRestaurantYoccaCard(city);
		
		initializeRestaurant();
	}
	
	public void createControlPanel(){
    	animationPanel.controlPanel = new RestaurantYoccaControlPanel((CityRestaurantYoccaCard)animationPanel, (RestaurantYocca) restaurant);
    }

}
