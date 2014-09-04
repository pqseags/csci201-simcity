package cityGui;

import house.House;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import util.Bank;
import util.BankMapLoc;
import util.HouseMapLoc;
import util.Loc;

/**
 * Not to be confused with CitiBank
 */
public class CityHouse extends CityComponent implements ImageObserver {
	java.net.URL imgURL1 = getClass().getResource("cityImages/house1.png");
	ImageIcon img1 = new ImageIcon(imgURL1);
	java.net.URL imgURL2 = getClass().getResource("cityImages/house2.png");
	ImageIcon img2 = new ImageIcon(imgURL2);
	java.net.URL imgURL3 = getClass().getResource("cityImages/house3.png");
	ImageIcon img3 = new ImageIcon(imgURL3);
	java.net.URL imgURL4 = getClass().getResource("cityImages/house4.png");
	ImageIcon img4 = new ImageIcon(imgURL4);
	java.net.URL imgURL5 = getClass().getResource("cityImages/house5.png");
	ImageIcon img5 = new ImageIcon(imgURL5);
	public House house;
	private int buildingSize = 35;
	public CityHouse(int x, int y) {
		super(x, y, Color.yellow, "House 1");
		rectangle = new Rectangle(x, y, buildingSize, buildingSize);
		initializeHouse();
	}

	public CityHouse(int x, int y, String I) {
		
		super(x, y, Color.yellow, I);
		//System.out.println("CITY COMPONENTY BEING INSTANTIATED");
		type="House";
		rectangle = new Rectangle(x, y, buildingSize, buildingSize);
		initializeHouse();
		//System.out.println(rectangle==null);
		
	}
	
	public void initializeHouse(){
		//System.out.println("House address is: "+sidewalkX(x,y)+" "+sidewalkY(x,y));
		house = new House();

	}
	
	@Override
	public JPanel addAgentObjectToMap(){
		
		house.address = new Loc(sidewalkX(x,y),sidewalkY(x,y));
		System.out.println("House Address: "+house.address.x+","+house.address.y);
		HouseMapLoc hMap = new HouseMapLoc(house);
		hMap.loc = new Loc(sidewalkX(x,y),sidewalkY(x,y));
		this.cityObject.cityMap.map.get("House").add(hMap);
		return null;
	}

	public void updatePosition() {

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

		return false;
	}
}
