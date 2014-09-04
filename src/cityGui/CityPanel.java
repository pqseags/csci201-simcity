package cityGui;

import house.House;
import house.gui.HouseControlPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import bank.gui.BankControlPanel;
import market.Market;
import market.gui.MarketPanel;
import person.PersonAgent;
import restaurant.Restaurant;
import restaurant.restaurantGabe.RestaurantGabe;
import restaurant.restaurantGabe.gui.RestaurantGabeControlPanel;
import restaurant.restaurantLinda.RestaurantLinda;
import restaurant.restaurantSimon.RestaurantSimon;
import restaurant.restaurantLinda.gui.RestaurantLindaControlPanel;
import restaurant.restaurantSimon.gui.RestaurantSimonControlPanel;
import restaurant.restaurantYocca.RestaurantYocca;
import restaurant.restaurantYocca.gui.*;
import util.Bank;
import util.CityMap;
import util.Loc;
import city.CityObject;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

public class CityPanel extends SimCityPanel implements MouseMotionListener {

	private BufferedImage fountainImage;
	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	public CityObject cityObject;




	String name = "City Panel";
	List<House> houses=new ArrayList<House>();
	List<Bank> banks = new ArrayList<Bank>();
	List<Market> markets = new ArrayList<Market>();
	List<Restaurant> restaurants = new ArrayList<Restaurant>();


	public CityPanel(SimCityGui city) {
		super(city);
		//setSize(CITY_WIDTH,CITY_HEIGHT);
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);

		//fountainImage = ImageIO.read(new File(""));
		background = new Color(0, 183, 96);
		for (int i = 80; i < 700; i += 360) {
			this.addStatic(new CityRoad(i, RoadDirection.HORIZONTAL));
			this.addStatic(new CityRoad(i, RoadDirection.VERTICAL));
		}
		for (int i = 40; i < 600; i += 480) {
			this.addStatic(new CitySidewalk(i, RoadDirection.HORIZONTAL,false,false));
			this.addStatic(new CitySidewalk(i, RoadDirection.VERTICAL,false,false));
		}
		for (int i = 160; i < 500; i += 240) {
			this.addStatic(new CitySidewalk(i, RoadDirection.HORIZONTAL,true,false));
			this.addStatic(new CitySidewalk(i, RoadDirection.VERTICAL,true,false));
		}
		//Creates boundaries
		//this.addStatic(new CitySidewalk(100, RoadDirection.VERTICAL,true,true));
		this.addStatic(new CitySidewalk(0,0));
		this.addStatic(new CitySidewalk(560,0));
		this.addStatic(new CitySidewalk(0,560));
		this.addStatic(new CitySidewalk(560,560));
		for (int i = 120; i < 480; i += 30) {
			if (i != 300) {
				this.addStatic(new CityRoadLines(i, RoadDirection.HORIZONTAL,true));
				this.addStatic(new CityRoadLines(i, RoadDirection.VERTICAL,true));
				this.addStatic(new CityRoadLines(i, RoadDirection.HORIZONTAL,false));
				this.addStatic(new CityRoadLines(i, RoadDirection.VERTICAL,false));
			}
		}
		for (int i = 85; i < 160; i += 20) {
			this.addStatic(new CityCrosswalk(i, RoadDirection.VERTICAL,true));
		}
		for (int i = 445; i < 520; i += 20 ) {
			this.addStatic(new CityCrosswalk(i, RoadDirection.VERTICAL,false));
		}
		for (int i = 85; i < 160; i += 20) {
			this.addStatic(new CityCrosswalk(i, RoadDirection.HORIZONTAL,true));
		}
		for (int i = 445; i < 520; i += 20) {
			this.addStatic(new CityCrosswalk(i, RoadDirection.HORIZONTAL,false));
		}

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		if (addingObject) {
			//make sure we aren't overlapping anything
			for (CityComponent c: statics) {
				if (c.equals(temp))
					continue;
				if (c.rectangle.intersects(temp.rectangle)) {
					return;
				}
			}
			addingObject = false;

			/*if(temp.type.equals("RestaurantGabe")){
				((CityRestaurant)temp).createAnimationPanel(city);

				CityRestaurantCardGabe tempAnimation=(CityRestaurantCardGabe)((CityRestaurant)temp).animationPanel;
				RestaurantGabeControlPanel panel=new RestaurantGabeControlPanel(tempAnimation,((RestaurantGabe)((CityRestaurant)temp).restaurant));
				city.buildingCP.addPanelCard(panel, temp.ID);
				tempAnimation.setPanel(panel);

				restaurants.add(((CityRestaurant)temp).restaurant);
				city.view.addView(((CityRestaurant)temp).animationPanel, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();

			}

			else if(temp.type.equals("RestaurantLinda")){
				((CityRestaurant)temp).createAnimationPanel(city);
				((CityRestaurant)temp).createControlPanel();

				city.buildingCP.addPanelCard(((CityRestaurant)temp).restaurant.controlPanel, temp.ID);

				restaurants.add(((CityRestaurant)temp).restaurant);
				city.view.addView(((CityRestaurant)temp).animationPanel, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();
			}
			else if(temp.type.equals("RestaurantYocca")){
				((CityRestaurant)temp).createAnimationPanel(city);

				CityRestaurantYoccaCard tempAnimation=(CityRestaurantYoccaCard)((CityRestaurant)temp).animationPanel;
				RestaurantYoccaControlPanel panel=new RestaurantYoccaControlPanel(tempAnimation,((RestaurantYocca)((CityRestaurant)temp).restaurant));
				city.buildingCP.addPanelCard(panel, temp.ID);
				tempAnimation.setPanel(panel);

				restaurants.add(((CityRestaurant)temp).restaurant);
				city.view.addView(((CityRestaurant)temp).animationPanel, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();
			}
			else if(temp.type.equals("RestaurantSimon")){
				((CityRestaurant)temp).createAnimationPanel(city);

				CityRestaurantSimonCard tempAnimation=(CityRestaurantSimonCard)((CityRestaurant)temp).animationPanel;
				RestaurantSimonControlPanel panel=new RestaurantSimonControlPanel(tempAnimation,((RestaurantSimon)((CityRestaurant)temp).restaurant));
				city.buildingCP.addPanelCard(panel, temp.ID);
				tempAnimation.setPanel(panel);

				restaurants.add(((CityRestaurant)temp).restaurant);
				city.view.addView(((CityRestaurant)temp).animationPanel, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();
			}
			else if(temp.type.equals("House")){
				CityHouseCard tempAnimation= new CityHouseCard(city);

				HouseControlPanel panel=new HouseControlPanel(tempAnimation,((CityHouse)temp).house);
				city.buildingCP.addPanelCard(panel, temp.ID);

				((CityHouse)temp).house.setAnimationPanel(tempAnimation);

				tempAnimation.setPanel(panel);

				houses.add(((CityHouse)temp).house);//hack: this is not necessary because we have the cityObject already. Change it later
				city.view.addView(tempAnimation, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();

			}*/
			if (temp.type.contains("Restaurant")){
				/*if(temp.type.equals("RestaurantSimon")){
					((CityRestaurant)temp).createAnimationPanel(city);

					CityRestaurantSimonCard tempAnimation=(CityRestaurantSimonCard)((CityRestaurant)temp).animationPanel;
					RestaurantSimonControlPanel panel=new RestaurantSimonControlPanel(tempAnimation,((RestaurantSimon)((CityRestaurant)temp).restaurant));
					city.buildingCP.addPanelCard(panel, temp.ID);
					tempAnimation.setPanel(panel);

					restaurants.add(((CityRestaurant)temp).restaurant);
					city.view.addView(((CityRestaurant)temp).animationPanel, temp.ID);
					temp.cityObject = this.cityObject;
					temp.addAgentObjectToMap();
				}
				else{*/
					((CityRestaurant)temp).createAnimationPanel(city);
					((CityRestaurant)temp).createControlPanel();
					((CityRestaurant)temp).restaurant.markets = city.city.markets;
					city.city.restaurants.add(((CityRestaurant)temp).restaurant);

					if (((CityRestaurant)temp).animationPanel.controlPanel!=null)
						city.buildingCP.addPanelCard(((CityRestaurant)temp).animationPanel.controlPanel, temp.ID);
					city.view.addView(((CityRestaurant)temp).animationPanel, temp.ID);

					temp.cityObject = this.cityObject;
					temp.addAgentObjectToMap();
				//}
			}
			else if (temp.type.equals("Bank")) {
				CityBankCard tempAnimation = new CityBankCard(city);

				BankControlPanel panel= new BankControlPanel(tempAnimation,((CityBank)temp).bank);
				city.buildingCP.addPanelCard(panel, temp.ID);

				((CityBank)temp).bank.setAnimationPanel(tempAnimation);

				tempAnimation.setPanel(panel);

				banks.add(((CityBank)temp).bank);
				city.view.addView(tempAnimation, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();

			}

			else if (temp.type.equals("Market")){
				CityMarketCard tempAnimation = new CityMarketCard(city);
				MarketPanel panel = new MarketPanel(tempAnimation, ((CityMarket)temp).market);
				city.buildingCP.addPanelCard(panel, temp.ID);
				((CityMarket)temp).market.setMarketPanel(panel);
				tempAnimation.setPanel(panel);
				markets.add(((CityMarket)temp).market);
				city.view.addView(tempAnimation, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();
			}
			else if(temp.type.equals("House")){
				CityHouseCard tempAnimation= new CityHouseCard(city);

				HouseControlPanel panel=new HouseControlPanel(tempAnimation,((CityHouse)temp).house);
				city.buildingCP.addPanelCard(panel, temp.ID);

				((CityHouse)temp).house.setAnimationPanel(tempAnimation);

				tempAnimation.setPanel(panel);

				houses.add(((CityHouse)temp).house);//hack: this is not necessary because we have the cityObject already. Change it later
				city.view.addView(tempAnimation, temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();

			}
			else{

				city.view.addView(new CityCard(city, Color.pink), temp.ID);
				temp.cityObject = this.cityObject;
				temp.addAgentObjectToMap();
				//System.err.println("Bank Added");
			}
			temp = null;
		}
		synchronized(statics){
			for (CityComponent c: statics) {
				if (c.contains(arg0.getX(), arg0.getY())) {
					//city.info.setText(c.ID);
					city.view.setView(c.ID);
					city.buildingCP.showCard(c.ID);
				}
			}
		}

		city.tracePanel.filterTracePanel();
	}

	public void mouseReleased(MouseEvent arg0) {

	}

	public void addObject(CityComponents c) {
		if (addingObject)
			return;
		addingObject = true;
		switch (c) {
		//Need to make multiple restaurant enums, probably

		//case RESTAURANT: temp = new CityRestaurantSimon(-100, -100, "Restaurant " + (statics.size()-19)); break;

		case RESTAURANTSIMON: temp = new CityRestaurantSimon(-100, -100, "Restaurant " + (statics.size()-19)); break;
		case RESTAURANTGABE: temp = new CityRestaurantGabe(-100, -100, "Restaurant " + (statics.size()-19)); break;
		case RESTAURANTLINDA: temp = new CityRestaurantLinda(-100, -100, "Restaurant " + (statics.size()-19)); break;
		case RESTAURANTYOCCA: temp = new CityRestaurantYocca(-100, -100, "Restaurant " + (statics.size()-19)); break;
		case RESTAURANTPARKER: temp = new CityRestaurantParker(-100, -100, "Restaurant " + (statics.size()-19)); break;
		
		case ROAD: temp = new CityRoad(-100, RoadDirection.HORIZONTAL); break; //NOTE: DON'T MAKE NEW ROADS
		case BANK: temp = new CityBank(-100, -100, "Bank " + (statics.size()-19)); break;
		case HOUSE: temp = new CityHouse(-100, -100, "House " + (statics.size()-19)); break;
		case MARKET: temp = new CityMarket(-100, -100, "Market " + (statics.size()-19)); break;
		default: return;
		}
		addStatic(temp);
	}

	public void mouseDragged(MouseEvent arg0) {

	}

	public void mouseMoved(MouseEvent arg0) {
		if (addingObject) {
			temp.setPosition(arg0.getPoint());
			for (CityComponent c: statics) {
				if (c.equals(temp)){
					continue;
				}
				if(c.rectangle==null){
					//System.out.println(c);
				}

				//System.out.println("AHHH: "+temp==null);
				if (c.rectangle.intersects(temp.rectangle)) {
					temp.invalidPlacement = true;
					return;
				}
				else temp.invalidPlacement = false;
			}
		}
	}

	public void moveBuildingTo(Loc loc){
		//FOR HACKS
		if(addingObject){
			temp.setPosition(new Point(loc.x,loc.y));
		}
	}

	public void setBuildingDown(){
		//FOR HACKS
		if (addingObject) {
			//make sure we aren't overlapping anything
			for (CityComponent c: statics) {
				if (c.equals(temp))
					continue;
				if (c.rectangle.intersects(temp.rectangle)) {
					return;
				}
			}
			addingObject = false;

			city.view.addView(new CityCard(city, Color.pink), temp.ID);
			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
			//			}
			temp = null;
		}
	}

}
