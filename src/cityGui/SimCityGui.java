package cityGui;

import house.gui.HouseControlPanel;
import interfaces.PlaceOfWork;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import bank.gui.BankControlPanel;
import market.Market;
import market.gui.MarketMain;
import market.gui.MarketPanel;
import person.PersonAgent;
import restaurant.restaurantGabe.RestaurantGabe;
import restaurant.restaurantGabe.gui.RestaurantGabeControlPanel;
import restaurant.restaurantLinda.gui.RestaurantLindaControlPanel;
import restaurant.restaurantYocca.RestaurantYocca;
import restaurant.restaurantYocca.gui.RestaurantYoccaControlPanel;
import util.Bank;
import util.BankMapLoc;
import util.Bus;
import util.BusAgent;
import util.BusStop;
import util.CarAgent;
import util.CityMap;
import util.CrosswalkStatus;
import util.HouseMapLoc;
import util.Job;
import util.JobType;
import util.Loc;
import util.MarketMapLoc;
import util.OnRamp;
import util.RestaurantMapLoc;
import util.StopLight;
import city.CityObject;
import cityGui.test.AStarTraversalPerson;
import cityGui.test.AStarTraversalVehicle;
import cityGui.test.BusAgentGui;
import cityGui.test.BusGui;
import cityGui.test.CarAgentGui;
import cityGui.test.PersonGui;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;
import cityGui.trace.TracePanel;

public class SimCityGui extends JFrame implements ActionListener {

	//Creates a list of the different lists of sprites
	public List<ArrayList<ImageIcon>> upAll = new ArrayList<ArrayList<ImageIcon>>();
	public List<ArrayList<ImageIcon>> downAll = new ArrayList<ArrayList<ImageIcon>>();
	public List<ArrayList<ImageIcon>> leftAll = new ArrayList<ArrayList<ImageIcon>>();
	public List<ArrayList<ImageIcon>> rightAll = new ArrayList<ArrayList<ImageIcon>>();

	public List<ImageIcon> up = new ArrayList<ImageIcon>();
	java.net.URL up1 = getClass().getResource("personImages/up1.png");
	ImageIcon u1 = new ImageIcon(up1);
	java.net.URL up2 = getClass().getResource("personImages/up2.png");
	ImageIcon u2 = new ImageIcon(up2);
	java.net.URL up3 = getClass().getResource("personImages/up3.png");
	ImageIcon u3 = new ImageIcon(up3);
	public List<ImageIcon> down = new ArrayList<ImageIcon>();
	java.net.URL down1 = getClass().getResource("personImages/down1.png");
	ImageIcon d1 = new ImageIcon(down1);
	java.net.URL down2 = getClass().getResource("personImages/down2.png");
	ImageIcon d2 = new ImageIcon(down2);
	java.net.URL down3 = getClass().getResource("personImages/down3.png");
	ImageIcon d3 = new ImageIcon(down3);
	public List<ImageIcon> left = new ArrayList<ImageIcon>();
	java.net.URL left1 = getClass().getResource("personImages/left1.png");
	ImageIcon l1 = new ImageIcon(left1);
	java.net.URL left2 = getClass().getResource("personImages/left2.png");
	ImageIcon l2 = new ImageIcon(left2);
	java.net.URL left3 = getClass().getResource("personImages/left3.png");
	ImageIcon l3 = new ImageIcon(left3);
	public List<ImageIcon> right = new ArrayList<ImageIcon>();
	java.net.URL right1 = getClass().getResource("personImages/right1.png");
	ImageIcon r1 = new ImageIcon(right1);
	java.net.URL right2 = getClass().getResource("personImages/right2.png");
	ImageIcon r2 = new ImageIcon(right2);
	java.net.URL right3 = getClass().getResource("personImages/right3.png");
	ImageIcon r3 = new ImageIcon(right3);

	//Goku Sprites
	public List<ImageIcon> gup = new ArrayList<ImageIcon>();
	java.net.URL gup1 = getClass().getResource("personImages/gup1.png");
	ImageIcon gu1 = new ImageIcon(gup1);
	java.net.URL gup2 = getClass().getResource("personImages/gup2.png");
	ImageIcon gu2 = new ImageIcon(gup2);
	java.net.URL gup3 = getClass().getResource("personImages/gup3.png");
	ImageIcon gu3 = new ImageIcon(gup3);
	java.net.URL gup4 = getClass().getResource("personImages/gup4.png");
	ImageIcon gu4 = new ImageIcon(gup4);
	public List<ImageIcon> gdown = new ArrayList<ImageIcon>();
	java.net.URL gdown1 = getClass().getResource("personImages/gdown1.png");
	ImageIcon gd1 = new ImageIcon(gdown1);
	java.net.URL gdown2 = getClass().getResource("personImages/gdown2.png");
	ImageIcon gd2 = new ImageIcon(gdown2);
	java.net.URL gdown3 = getClass().getResource("personImages/gdown3.png");
	ImageIcon gd3 = new ImageIcon(gdown3);
	java.net.URL gdown4 = getClass().getResource("personImages/gdown4.png");
	ImageIcon gd4 = new ImageIcon(gdown4);
	public List<ImageIcon> gleft = new ArrayList<ImageIcon>();
	java.net.URL gleft1 = getClass().getResource("personImages/gleft1.png");
	ImageIcon gl1 = new ImageIcon(gleft1);
	java.net.URL gleft2 = getClass().getResource("personImages/gleft2.png");
	ImageIcon gl2 = new ImageIcon(gleft2);
	java.net.URL gleft3 = getClass().getResource("personImages/gleft3.png");
	ImageIcon gl3 = new ImageIcon(gleft3);
	java.net.URL gleft4 = getClass().getResource("personImages/gleft4.png");
	ImageIcon gl4 = new ImageIcon(gleft4);
	public List<ImageIcon> gright = new ArrayList<ImageIcon>();
	java.net.URL gright1 = getClass().getResource("personImages/gright1.png");
	ImageIcon gr1 = new ImageIcon(gright1);
	java.net.URL gright2 = getClass().getResource("personImages/gright2.png");
	ImageIcon gr2 = new ImageIcon(gright2);
	java.net.URL gright3 = getClass().getResource("personImages/gright3.png");
	ImageIcon gr3 = new ImageIcon(gright3);
	java.net.URL gright4 = getClass().getResource("personImages/gright4.png");
	ImageIcon gr4 = new ImageIcon(gright4);

	//Gohan Sprites
	public List<ImageIcon> ghup = new ArrayList<ImageIcon>();
	java.net.URL ghup1 = getClass().getResource("personImages/ghup1.png");
	ImageIcon ghu1 = new ImageIcon(ghup1);
	java.net.URL ghup2 = getClass().getResource("personImages/ghup2.png");
	ImageIcon ghu2 = new ImageIcon(ghup2);
	java.net.URL ghup3 = getClass().getResource("personImages/ghup3.png");
	ImageIcon ghu3 = new ImageIcon(ghup3);
	java.net.URL ghup4 = getClass().getResource("personImages/ghup4.png");
	ImageIcon ghu4 = new ImageIcon(ghup4);
	public List<ImageIcon> ghdown = new ArrayList<ImageIcon>();
	java.net.URL ghdown1 = getClass().getResource("personImages/ghdown1.png");
	ImageIcon ghd1 = new ImageIcon(ghdown1);
	java.net.URL ghdown2 = getClass().getResource("personImages/ghdown2.png");
	ImageIcon ghd2 = new ImageIcon(ghdown2);
	java.net.URL ghdown3 = getClass().getResource("personImages/ghdown3.png");
	ImageIcon ghd3 = new ImageIcon(ghdown3);
	java.net.URL ghdown4 = getClass().getResource("personImages/ghdown4.png");
	ImageIcon ghd4 = new ImageIcon(ghdown4);
	public List<ImageIcon> ghleft = new ArrayList<ImageIcon>();
	java.net.URL ghleft1 = getClass().getResource("personImages/ghleft1.png");
	ImageIcon ghl1 = new ImageIcon(ghleft1);
	java.net.URL ghleft2 = getClass().getResource("personImages/ghleft2.png");
	ImageIcon ghl2 = new ImageIcon(ghleft2);
	java.net.URL ghleft3 = getClass().getResource("personImages/ghleft3.png");
	ImageIcon ghl3 = new ImageIcon(ghleft3);
	java.net.URL ghleft4 = getClass().getResource("personImages/ghleft4.png");
	ImageIcon ghl4 = new ImageIcon(ghleft4);
	public List<ImageIcon> ghright = new ArrayList<ImageIcon>();
	java.net.URL ghright1 = getClass().getResource("personImages/ghright1.png");
	ImageIcon ghr1 = new ImageIcon(ghright1);
	java.net.URL ghright2 = getClass().getResource("personImages/ghright2.png");
	ImageIcon ghr2 = new ImageIcon(ghright2);
	java.net.URL ghright3 = getClass().getResource("personImages/ghright3.png");
	ImageIcon ghr3 = new ImageIcon(ghright3);
	java.net.URL ghright4 = getClass().getResource("personImages/ghright4.png");
	ImageIcon ghr4 = new ImageIcon(ghright4);

	//Hercule Sprites
	public List<ImageIcon> hup = new ArrayList<ImageIcon>();
	java.net.URL hup1 = getClass().getResource("personImages/hup1.png");
	ImageIcon hu1 = new ImageIcon(hup1);
	java.net.URL hup2 = getClass().getResource("personImages/hup2.png");
	ImageIcon hu2 = new ImageIcon(hup2);
	java.net.URL hup3 = getClass().getResource("personImages/hup3.png");
	ImageIcon hu3 = new ImageIcon(hup3);
	java.net.URL hup4 = getClass().getResource("personImages/hup4.png");
	ImageIcon hu4 = new ImageIcon(hup4);
	public List<ImageIcon> hdown = new ArrayList<ImageIcon>();
	java.net.URL hdown1 = getClass().getResource("personImages/hdown1.png");
	ImageIcon hd1 = new ImageIcon(hdown1);
	java.net.URL hdown2 = getClass().getResource("personImages/hdown2.png");
	ImageIcon hd2 = new ImageIcon(hdown2);
	java.net.URL hdown3 = getClass().getResource("personImages/hdown3.png");
	ImageIcon hd3 = new ImageIcon(hdown3);
	java.net.URL hdown4 = getClass().getResource("personImages/hdown4.png");
	ImageIcon hd4 = new ImageIcon(hdown4);
	public List<ImageIcon> hleft = new ArrayList<ImageIcon>();
	java.net.URL hleft1 = getClass().getResource("personImages/hleft1.png");
	ImageIcon hl1 = new ImageIcon(hleft1);
	java.net.URL hleft2 = getClass().getResource("personImages/hleft2.png");
	ImageIcon hl2 = new ImageIcon(hleft2);
	java.net.URL hleft3 = getClass().getResource("personImages/hleft3.png");
	ImageIcon hl3 = new ImageIcon(hleft3);
	java.net.URL hleft4 = getClass().getResource("personImages/hleft4.png");
	ImageIcon hl4 = new ImageIcon(hleft4);
	public List<ImageIcon> hright = new ArrayList<ImageIcon>();
	java.net.URL hright1 = getClass().getResource("personImages/hright1.png");
	ImageIcon hr1 = new ImageIcon(hright1);
	java.net.URL hright2 = getClass().getResource("personImages/hright2.png");
	ImageIcon hr2 = new ImageIcon(hright2);
	java.net.URL hright3 = getClass().getResource("personImages/hright3.png");
	ImageIcon hr3 = new ImageIcon(hright3);
	java.net.URL hright4 = getClass().getResource("personImages/hright4.png");
	ImageIcon hr4 = new ImageIcon(hright4);

	//(-Parker's layout)
	public BuildingControlPanelHolder buildingCP;


	public CityPanel city;
	public CityObject cityObject;
	InfoPanel info;
	public CityView view;
	CityControlPanel CP;
	TracePanel tracePanel;
	GridBagConstraints c = new GridBagConstraints();
	int SHIFTS = 2;
	int MAXTIME = 100;
	protected Timer timer;
	public long time=0;
	boolean hasBuses = false;

	//public List<BusStop> busStops = new ArrayList<BusStop>();
	public List<OnRamp> onRamps = new ArrayList<OnRamp>();
	public List<StopLight> stopLights = new ArrayList<StopLight>();

	public JPanel trace = new JPanel();

	int gridX = 600;
	int gridY = 600;

	public static int cityScale = 30;

	public Semaphore[][] grid = new Semaphore[(int) ((int) gridX/cityScale)][(int) ((int) gridY/cityScale)];


	public SimCityGui() throws HeadlessException {
		//Adds person images to its sprite array
		up.add(u1); up.add(u2); up.add(u1); up.add(u3);
		down.add(d1); down.add(d2); down.add(d1); down.add(d3);
		left.add(l1); left.add(l2); left.add(l1); left.add(l3);
		right.add(r1); right.add(r2); right.add(r1); right.add(r3);

		upAll.add((ArrayList<ImageIcon>) up);
		downAll.add((ArrayList<ImageIcon>) down);
		leftAll.add((ArrayList<ImageIcon>) left);
		rightAll.add((ArrayList<ImageIcon>) right);

		//Adds goku images to sprite arrays
		gup.add(gu1); gup.add(gu2); gup.add(gu3); gup.add(gu4);
		gdown.add(gd1); gdown.add(gd2); gdown.add(gd3); gdown.add(gd4);
		gleft.add(gl1); gleft.add(gl2); gleft.add(gl3); gleft.add(gl4);
		gright.add(gr1); gright.add(gr2); gright.add(gr3); gright.add(gr4);

		upAll.add((ArrayList<ImageIcon>) gup);
		downAll.add((ArrayList<ImageIcon>) gdown);
		leftAll.add((ArrayList<ImageIcon>) gleft);
		rightAll.add((ArrayList<ImageIcon>) gright);

		//Adds gohan images to sprite arrays
		ghup.add(ghu1); ghup.add(ghu2); ghup.add(ghu3); ghup.add(ghu4);
		ghdown.add(ghd1); ghdown.add(ghd2); ghdown.add(ghd3); ghdown.add(ghd4);
		ghleft.add(ghl1); ghleft.add(ghl2); ghleft.add(ghl3); ghleft.add(ghl4);
		ghright.add(ghr1); ghright.add(ghr2); ghright.add(ghr3); ghright.add(ghr4);

		upAll.add((ArrayList<ImageIcon>) ghup);
		downAll.add((ArrayList<ImageIcon>) ghdown);
		leftAll.add((ArrayList<ImageIcon>) ghleft);
		rightAll.add((ArrayList<ImageIcon>) ghright);

		//Adds hercule images to sprite arrays
		hup.add(hu1); hup.add(hu2); hup.add(hu3); hup.add(hu4);
		hdown.add(hd1); hdown.add(hd2); hdown.add(hd3); hdown.add(hd4);
		hleft.add(hl1); hleft.add(hl2); hleft.add(hl3); hleft.add(hl4);
		hright.add(hr1); hright.add(hr2); hright.add(hr3); hright.add(hr4);

		upAll.add((ArrayList<ImageIcon>) hup);
		downAll.add((ArrayList<ImageIcon>) hdown);
		leftAll.add((ArrayList<ImageIcon>) hleft);
		rightAll.add((ArrayList<ImageIcon>) hright);

		CP = new CityControlPanel(this);

		//Makes the A* grid for the city
		setScale(30);
		initializeGrid();



		//THIS IS THE AGENT CITY
		cityObject = new CityObject(this);
		cityObject.MAXTIME = this.MAXTIME;

		city = new CityPanel(this);
		city.cityObject = cityObject;

		view = new CityView(this);

		info = new InfoPanel(this);

		tracePanel = new TracePanel(this);
		tracePanel.setPreferredSize(new Dimension(CP.getPreferredSize().width, (int)(1.4*CP.getPreferredSize().height)));
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();

		///////-----v----v--v-----v-------PREVIOUS LAYOUT------v----v--v-----v-----v---v----////
		//		this.setLayout(new GridBagLayout());

		//		
		//		//city animation
		//		c.gridx = 0; c.gridy = 0;
		//		c.gridwidth = 6; c.gridheight = 6;
		//		this.add(city, c);
		//
		//		//building info (name)
		//		c.gridx = 6; c.gridy = 0;
		//		c.gridwidth = 5; c.gridheight = 1;
		//		this.add(info, c);
		//
		//		//building animation view
		//		c.gridx = 6; c.gridy = 1;
		//		c.gridwidth = 5; c.gridheight = 5;
		//		this.add(view, c);
		//
		//		//city control panel
		//		c.gridx = 0; c.gridy = 6;
		//		c.gridwidth = 11; c.gridheight = 1;
		//		this.add(CP, c);

		/////////--^-----^------^------^----PREVIOUS LAYOUT-----^-----^-----^-----^-----^----////
		//			|	|		|		|						|		|	|		|	|
		///////-----v----v------v------v-NEW (PARKER) LAYOUT----v------v-----v-----v-----v----//// 
		this.setLayout(new GridBagLayout());
		//setBounds(0,0,1300,700);

		Dimension dim = new Dimension(180, 500); //x value can't be over 180

		buildingCP = new BuildingControlPanelHolder();




		//trace = new JPanel();
		dim = new Dimension(500, 100);
		trace.setMaximumSize(dim);
		trace.setMinimumSize(dim);
		trace.setPreferredSize(dim);
		//trace.setBackground(Color.BLUE);
		trace.setLayout(new FlowLayout());


		JPanel cityCP = new JPanel();
		dim = new Dimension(600, 100); //y value can't be over 178
		cityCP.setMaximumSize(dim);
		cityCP.setMinimumSize(dim);
		cityCP.setPreferredSize(dim);
		//buildingCP.setBackground(Color.WHITE);



		//city animation
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 6; c.gridheight = 6;
		c.fill = GridBagConstraints.BOTH;
		this.add(city, c);

		//building animation view
		c.gridx = 6; c.gridy = 0;
		c.gridwidth = 5; c.gridheight = 5;
		c.fill = GridBagConstraints.BOTH;
		this.add(view, c);

		//building control panel
		c.gridx = 11; c.gridy = 0;
		c.gridwidth = 2; c.gridheight = 5;
		c.fill = GridBagConstraints.BOTH;
		this.add(buildingCP, c);
		//buildingCP.setBackground(Color.BLACK);

		//city control panel
		c.gridx = 0; c.gridy = 6;
		c.gridwidth = 6; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(CP, c);

		//cityCP.setBackground(Color.WHITE);

		//trace log
		c.gridx = 6; c.gridy = 5;
		c.gridwidth =7; c.gridheight = 2; 
		c.fill = GridBagConstraints.BOTH;
		this.add(trace, c);
		trace.setBackground(Color.BLUE);



		/////////--^-----^------^------^----NEW (PARKER) LAYOUT-----^-----^-----^-----^-----^----////

		timer = new Timer(10,  this);
		timer.start();
		/*
		c.gridx = 0; c.gridy = 7;
		c.gridwidth = 11; c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		this.add(tracePanel, c);*/
		//this.add(tracePanel, c)
		Dimension d = new Dimension(500,185);
		tracePanel.setPreferredSize(d);
		tracePanel.setMaximumSize(d);
		tracePanel.setMinimumSize(d);

		trace.add(tracePanel);
		//traceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//traceFrame.setBounds(1000, 50 , 400, 300);
		//traceFrame.setVisible(true);
		//traceFrame.add(tracePanel);

		//Sets the stoplights and onramps
		setStopLights();
		setOnRamps();

		CP.addTraceControls();
	}


	public void setScale(int scale){
		AStarTraversalPerson.scale = scale;
		AStarTraversalVehicle.scale = scale;
		CityComponent.scale = scale;
		PersonGui.gridScale = scale;
		BusAgentGui.gridScale = scale;
		this.cityScale = scale;

	}

	public void NewPersonCreationPanel(){
		PersonCreationPanel pCreate = new PersonCreationPanel(this);
	}

	public void addNewPerson(PersonAgent p){

		CarAgent car = new CarAgent();
		car.gui = new CarAgentGui(car,this,true);
		car.cityGui = this;
		car.passenger = p;
		car.startThread();
		car.gui.stopLights = stopLights;

		p.myCar = car;
		p.onRamps = onRamps;

		/*String name = "p0";
		PersonAgent p = new PersonAgent(name,cityObject.cityMap);
		PersonGui personGui = new PersonGui(p,this,0,0,0,0);
		p.gui = personGui;
		cityObject.people.add(p);
		city.addMoving(personGui);
		p.startThread();*/
		p.setAStar(new AStarTraversalPerson(grid));
		PersonGui personGui = new PersonGui(p,this,0,0,0,0);
		personGui.stopLights = stopLights;
		p.setGui(personGui);
		cityObject.people.add(p);
		city.addMoving(personGui);
		p.startThread();
	}

	public void addNewPersonHard(String name, PlaceOfWork placeOfWork,
			JobType jobType,int start,int end,int bankNum,int houseNum){

		PersonAgent person = new PersonAgent(name,cityObject.cityMap);
		if (placeOfWork != null && jobType != null){
			person.setJob(placeOfWork, jobType, start, end);
		}
		person.setBank(bankNum);
		//System.out.println(cityObject.cityMap.map.get("House").isEmpty());
		if(!cityObject.cityMap.map.get("House").isEmpty()){
			//System.out.println("Setting person's house to "+((HouseMapLoc) cityObject.cityMap.map.get("House").get(houseNum)).house.address.x + ", "+
			//		((HouseMapLoc) cityObject.cityMap.map.get("House").get(houseNum)).house.address.y);
			person.setHouse(((HouseMapLoc) cityObject.cityMap.map.get("House").get(houseNum)).house);
		}
		//person.setHouse(((HouseMapLoc) cityObject.cityMap.map.get("House").get(houseNum)).house);
		boolean wantsToRideBus = false;
		if(Math.random()>.75 && hasBuses){
			wantsToRideBus = true;
		}
		person.wantsToRideBus = wantsToRideBus;
		addNewPerson(person);

	}

	public void addNewBuilding(String type,int x, int y){
		if(type.equals("Bank")){
			/*city.addObject(CityComponents.BANK);
			city.moveBuildingTo(new Loc(x,y));
			city.setBuildingDown();*/
			CityComponent temp = new CityBank(x, y, "Bank " + (city.statics.size()-19));
			CityBankCard tempAnimation = new CityBankCard(this);
			BankControlPanel panel= new BankControlPanel(tempAnimation,((CityBank)temp).bank);
			
			buildingCP.addPanelCard(panel, temp.ID);
			
			((CityBank)temp).bank.setAnimationPanel(tempAnimation);
			
			tempAnimation.setPanel(panel);
			//Just to test the open stuff
			//((CityBank)temp).bank.isOpen  = false;
			
			
			city.banks.add(((CityBank)temp).bank);
			this.view.addView(tempAnimation, temp.ID);
			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
			city.statics.add(temp);
			return;
		}
		if(type.equals("Market")){
			CityComponent temp = new CityMarket(x, y, "Market " + (city.statics.size()-19));
			CityMarketCard tempAnimation = new CityMarketCard(this);

			MarketPanel panel = new MarketPanel(tempAnimation, ((CityMarket)temp).market);
			buildingCP.addPanelCard(panel, temp.ID);

			((CityMarket)temp).market.setMarketPanel(panel);
			//Just to test the open stuff
			//((CityMarket)temp).market.isOpen  = false;
			tempAnimation.setPanel(panel);
			city.markets.add(((CityMarket)temp).market);
			this.view.addView(tempAnimation, temp.ID);
			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
			city.statics.add(temp);
			return;
		}

		if(type.contains("Restaurant")){
			CityRestaurant temp = null;

			if (type.contains("Linda"))
			{
				temp = new CityRestaurantLinda(x, y, "RestaurantLinda " + (city.statics.size()-19));
			}

			else if(type.contains("Gabe")){
				temp = new CityRestaurantGabe(x,y, "RestaurantGabe " + (city.statics.size()-19));

			}
			else if(type.contains("Simon")) {
				temp = new CityRestaurantSimon(x, y, "RestaurantSimon " + (city.statics.size()-19));
			}		

			else if(type.contains("Yocca")) {
				temp = new CityRestaurantYocca(x,y, "RestaurantYocca " + (city.statics.size()-19));
			}
			
			else if (type.contains("Parker")){
				temp = new CityRestaurantParker(x,y, "RestaurantParker " + (city.statics.size()-19));
			}

			temp.createAnimationPanel(this);
			temp.createControlPanel();
			temp.restaurant.markets = city.markets;
			city.restaurants.add(temp.restaurant);
			if (temp.animationPanel.controlPanel!=null)
				buildingCP.addPanelCard(temp.animationPanel.controlPanel, temp.ID);
			this.view.addView(temp.animationPanel, temp.ID);

			//HACK FOR CLOSING RESTAURANTS
			//			if(type.contains("Gabe")){
			//				((RestaurantGabe) ((CityRestaurantGabe) temp).restaurant).isOpen = false;
			//			}

			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
			city.statics.add(temp);
			return;

		}

		/*
		else if(type.equals("RestaurantXXX")){
			CityComponent temp = new CityRestaurantXXX(x, y, "Restaurant " + (city.statics.size()-19));
			CityRestaurantLindaCard tempAnimation = new CityRestaurantLindaCard(this);
			((CityRestaurant)temp).setAnimationPanel(tempAnimation);
			city.restaurants.add(((CityRestaurant)temp).restaurant);
			this.view.addView(tempAnimation, temp.ID);
			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
			city.statics.add(temp);
			return;
		}*/
		if(type.equals("House")){
			CityComponent temp = new CityHouse(x, y, "House " + (city.statics.size()-19));
			CityHouseCard tempAnimation = new CityHouseCard(this);
			HouseControlPanel panel=new HouseControlPanel(tempAnimation,((CityHouse)temp).house);
			
			buildingCP.addPanelCard(panel, temp.ID);
			
			((CityHouse)temp).house.setAnimationPanel(tempAnimation);
			
			tempAnimation.setPanel(panel);
			
			city.houses.add(((CityHouse)temp).house);
			this.view.addView(tempAnimation, temp.ID);
			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
			city.statics.add(temp);
			return;
		}

		/*
		 	CityHouseCard tempAnimation= new CityHouseCard(city);
			((CityHouse)temp).house.setAnimationPanel(tempAnimation);
			houses.add(((CityHouse)temp).house);//hack: this is not necessary because we have the cityObject already. Change it later
			city.view.addView(tempAnimation, temp.ID);
			temp.cityObject = this.cityObject;
			temp.addAgentObjectToMap();
		 */

	}

	//HACK
	public void fullyManBuilding(String type,int num){

		if(type.equals("Bank")){
			int j = 0;
			//int randOffset = (int) Math.floor(MAXTIME/SHIFTS/2*Math.random());
			int randOffset = 0;
			//System.out.println("Rand offset: "+randOffset);
			for(int i = 0;i<SHIFTS;++i){
				int start = (i*(MAXTIME/SHIFTS) + randOffset+MAXTIME-2)%MAXTIME;
				int end = ((i+1)*(MAXTIME/SHIFTS) + randOffset+2)%MAXTIME;
				//System.out.println("Shift start, end: "+start+" " +end);
				int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
				int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());
				addNewPersonHard("p"+j,
						((BankMapLoc) cityObject.cityMap.map.get("Bank").get(num)).bank,
						JobType.BankTeller,start,end,bankNum,houseNum);
				j = j+1;
			}
			return;
		}
		if(type.equals("Market")){
			int j = 0;
			int randOffset = (int) Math.floor(MAXTIME/SHIFTS/2*Math.random());
			//System.out.println("Rand offset: "+randOffset);
			randOffset = 0;
			for(int i = 0;i<SHIFTS;++i){
				int start = (i*(MAXTIME/SHIFTS) + randOffset+MAXTIME-2)%MAXTIME;
				int end = ((i+1)*(MAXTIME/SHIFTS) + randOffset+2)%MAXTIME;
				System.out.println(j+ " Shift start, end: "+start+" " +end);
				int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
				int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());
				addNewPersonHard("p"+j,
						((MarketMapLoc) cityObject.cityMap.map.get("Market").get(num)).market,
						JobType.MarketHost,start,end,bankNum,houseNum);
				j = j+1;
			}
			randOffset = (int) Math.floor(MAXTIME/SHIFTS/2*Math.random());
			randOffset = 0;
			for(int i = 0;i<SHIFTS;++i){
				int start = (i*(MAXTIME/SHIFTS) + randOffset+MAXTIME-2)%MAXTIME;
				int end = ((i+1)*(MAXTIME/SHIFTS) + randOffset+2)%MAXTIME;
				System.out.println(j+" Shift start, end: "+start+" " +end);
				int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
				int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());
				addNewPersonHard("p"+j,
						((MarketMapLoc) cityObject.cityMap.map.get("Market").get(num)).market,
						JobType.MarketCashier,start,end,bankNum,houseNum);
				j = j+1;
			}

			//for(int numEmployees = 0;numEmployees<2;++numEmployees){
			randOffset = (int) Math.floor(MAXTIME/SHIFTS/2*Math.random());
			randOffset = 0;
			for(int i = 0;i<SHIFTS;++i){
				int start = (i*(MAXTIME/SHIFTS) + randOffset+MAXTIME-2)%MAXTIME;
				int end = ((i+1)*(MAXTIME/SHIFTS) + randOffset+2)%MAXTIME;
				System.out.println(j+" Shift start, end: "+start+" " +end);
				int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
				int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());
				addNewPersonHard("p"+j,
						((MarketMapLoc) cityObject.cityMap.map.get("Market").get(num)).market,
						JobType.MarketEmployee,start,end,bankNum,houseNum);
				j = j+1;

			}
			//}

			randOffset = (int) Math.floor(MAXTIME/SHIFTS/2*Math.random());
			randOffset = 0;
			for(int i = 0;i<SHIFTS;++i){
				int start = (i*(MAXTIME/SHIFTS) + randOffset+MAXTIME-2)%MAXTIME;
				int end = ((i+1)*(MAXTIME/SHIFTS) + randOffset+2)%MAXTIME;
				System.out.println(j+" Shift start, end: "+start+" " +end);
				int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
				int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());
				addNewPersonHard("p"+j,
						((MarketMapLoc) cityObject.cityMap.map.get("Market").get(num)).market,
						JobType.MarketDeliveryMan,start,end,bankNum,houseNum);
				j = j+1;

			}
		}
		if(type.contains("Restaurant")){
			/*int j = 0;
			int randOffset = (int) Math.floor(MAXTIME/SHIFTS/2*Math.random());
			//System.out.println("Rand offset: "+randOffset);
			for(int i = 0;i<SHIFTS;++i){
				int start = (i*(MAXTIME/SHIFTS) + randOffset)%MAXTIME;
				int end = ((i+1)*(MAXTIME/SHIFTS) + randOffset)%MAXTIME;
				//System.out.println("Shift start, end: "+start+" " +end);
				int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
				int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());
				addNewPersonHard("prhost"+j,
						((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
						JobType.RestaurantHost,start,end,bankNum,houseNum);
				addNewPersonHard("pcook"+j,
						((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
						JobType.RestaurantCook,start,end,bankNum,houseNum);

				if (type.contains("Linda")){
					addNewPersonHard("pboringwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantLindaWaiter1,start,end,bankNum,houseNum);
					addNewPersonHard("pnewwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantLindaWaiter2,start,end,bankNum,houseNum);
				}
				else if (type.contains("Gabe")){
					addNewPersonHard("pboringwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantGabeWaiter1,start,end,bankNum,houseNum);
					addNewPersonHard("pnewwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantGabeWaiter2,start,end,bankNum,houseNum);
				}
				else if (type.contains("Yocca")){
					addNewPersonHard("pboringwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantYoccaWaiter1,start,end,bankNum,houseNum);
					addNewPersonHard("pnewwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantYoccaWaiter2,start,end,bankNum,houseNum);
				}

				else if (type.contains("Parker")){
					addNewPersonHard("pboringwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantParkerWaiter1,start,end,bankNum,houseNum);
					addNewPersonHard("pnewwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantParkerWaiter2,start,end,bankNum,houseNum);
				}
				else if (type.contains("Simon")){
					addNewPersonHard("pboringwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantSimonWaiter1,start,end,bankNum,houseNum);
					addNewPersonHard("pnewwaiter"+j,
							((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
							JobType.RestaurantSimonWaiter2,start,end,bankNum,houseNum);

				}

				addNewPersonHard("prcash"+j,
						((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(num)).restaurant,
						JobType.RestaurantCashier,start,end,bankNum,houseNum);

				j = j+1;
			}
		}*/
			
			addNewPersonHard("p0",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,0,15, 0,0);
			
			addNewPersonHard("p3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,0,15, 0,0);
			addNewPersonHard("p4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,0,15, 0,0);

			addNewPersonHard("replacementHOST",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,11,100, 0,0);
			addNewPersonHard("replacementCASHIER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,11,100, 0,0);
			addNewPersonHard("replacementCOOK",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,11,100, 0,0);

			if (type.contains("Gabe")){
				addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,15, 0,0);
				addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,15, 0,0);
				addNewPersonHard("replacementWAITER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,22,100, 0,0);
			}
			else if (type.contains("Simon")){
				addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantSimonWaiter1,0,15, 0,0);
				addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantSimonWaiter1,0,15, 0,0);
				addNewPersonHard("replacementWAITER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantSimonWaiter1,22,100, 0,0);
			}
			else if (type.contains("Linda")){
				addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantLindaWaiter1,0,15, 0,0);
				addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantLindaWaiter1,0,15, 0,0);
				addNewPersonHard("replacementWAITER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantLindaWaiter1,22,100, 0,0);
			}
			else if (type.contains("Yocca")){
				addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantYoccaWaiter1,0,15, 0,0);
				addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantYoccaWaiter1,0,15, 0,0);
				addNewPersonHard("replacementWAITER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantYoccaWaiter1,22,100, 0,0);
			}
			else if (type.contains("Parker")){
				addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantParkerWaiter1,0,15, 0,0);
				addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantParkerWaiter1,0,15, 0,0);
				addNewPersonHard("replacementWAITER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantParkerWaiter1,22,100, 0,0);
			}
		}
	}

	public void addBuses(SimCityGui simCityGui){
		BusAgent b = new BusAgent();
		BusAgent b2 = new BusAgent();
		//b.gui = new BusGui(b,test,110,110,30,50);

		b.gui = new BusAgentGui(b,simCityGui,true);
		b2.gui = new BusAgentGui(b2,simCityGui,false);

		cityObject.fBus = b;
		cityObject.bBus = b2;

		cityObject.cityMap.fStops.add(new BusStop(new Loc(170,130)));
		cityObject.cityMap.fStops.add(new BusStop(new Loc(380,450)));

		cityObject.cityMap.fStops.get(0).sidewalkLoc = new Loc(180,160);
		cityObject.cityMap.fStops.get(1).sidewalkLoc = new Loc(410,430);

		cityObject.cityMap.bStops.add(new BusStop(new Loc(460,90)));
		cityObject.cityMap.bStops.add(new BusStop(new Loc(100,490)));

		cityObject.cityMap.bStops.get(0).sidewalkLoc = new Loc(490,70);
		cityObject.cityMap.bStops.get(1).sidewalkLoc = new Loc(120,520);

		b.stops = cityObject.cityMap.fStops;
		b2.stops = cityObject.cityMap.bStops;

		city.addMoving(b.gui);
		city.addMoving(b2.gui);



		//b.setAStar(new AStarTraversalVehicle(grid));
		//b2.setAStar(new AStarTraversalVehicle(grid));

		b.gui.stopLights = stopLights;
		b2.gui.stopLights = stopLights;


		b.startThread();
		b2.startThread();
	}


	public void addVehicles(SimCityGui simCityGui){
		BusAgent b = new BusAgent();
		BusAgent b2 = new BusAgent();
		CarAgent c = new CarAgent();
		//b.gui = new BusGui(b,test,110,110,30,50);

		b.gui = new BusAgentGui(b,simCityGui,true);
		b2.gui = new BusAgentGui(b2,simCityGui,false);
		c.gui = new CarAgentGui(c,simCityGui,false);

		cityObject.fBus = b;
		cityObject.bBus = b2;

		cityObject.cityMap.fStops.add(new BusStop(new Loc(170,130)));
		cityObject.cityMap.fStops.add(new BusStop(new Loc(380,450)));

		cityObject.cityMap.fStops.get(0).sidewalkLoc = new Loc(180,160);
		cityObject.cityMap.fStops.get(1).sidewalkLoc = new Loc(410,430);

		cityObject.cityMap.bStops.add(new BusStop(new Loc(460,90)));
		cityObject.cityMap.bStops.add(new BusStop(new Loc(100,490)));

		cityObject.cityMap.bStops.get(0).sidewalkLoc = new Loc(490,70);
		cityObject.cityMap.bStops.get(1).sidewalkLoc = new Loc(120,520);

		b.stops = cityObject.cityMap.fStops;
		b2.stops = cityObject.cityMap.bStops;

		city.addMoving(b.gui);
		city.addMoving(b2.gui);
		city.addMoving(c.gui);

		List<StopLight> lights = new ArrayList<StopLight>();

		StopLight light = new StopLight();
		light.loc = new Loc(285,80);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(270,80,60,80);
		light.letGoRegion = new Rectangle(290,80,20,80);
		city.addStatic(light);
		lights.add(light);

		light = new StopLight();
		light.loc = new Loc(285,440);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(270,440,60,80);
		light.letGoRegion = new Rectangle(290,440,20,80);
		city.addStatic(light);
		lights.add(light);

		light = new StopLight();
		light.loc = new Loc(80,285);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(80,270,80,60);
		light.letGoRegion = new Rectangle(80,290,80,20);
		city.addStatic(light);
		lights.add(light);

		light = new StopLight();
		light.loc = new Loc(440,285);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(440,270,80,60);
		light.letGoRegion = new Rectangle(440,290,80,20);
		city.addStatic(light);
		lights.add(light);



		//b.setAStar(new AStarTraversalVehicle(grid));
		//b2.setAStar(new AStarTraversalVehicle(grid));

		b.gui.stopLights = lights;
		b2.gui.stopLights = lights;
		c.gui.stopLights = lights;
		c.gui.goTo(410,490);


		b.startThread();
		b2.startThread();

	}

	public void simpleCarScenario(){

		PersonAgent person = new PersonAgent("CarTestPerson",cityObject.cityMap);
		addNewPerson(person);

		CarAgent car = new CarAgent();
		CarAgentGui carGui = new CarAgentGui(car,this,false);
		CarAgentGui carGui2 = new CarAgentGui(car,this,false);

		car.gui = carGui;
		car.cityGui = this;
		car.passenger = person;
		person.myCar = car;

		carGui.stopLights = stopLights;
		person.onRamps = onRamps;
		person.startThread();
		//		carGui2.stopLights = lights;
		//		
		//		carGui2.rectangle.x = 90;
		//		carGui2.rectangle.y = 120;
		//		carGui2.rectangle = new Rectangle(90,120,15,25);
		//		carGui2.goTo(0, 0);
		//		city.addMoving(carGui2);

		car.startThread();
		//car.msgTakeMeTo(ramp0, ramp1);


	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//System.out.println(""+ (-2)%100);

		SimCityGui test = new SimCityGui();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setResizable(false);
		test.pack();
		test.setVisible(true);

		int xStartTest = 0;
		int yStartTest = 0;


		//test.addBuses(test);
		//test.simpleCarScenario();


		//test.simpleCarScenario();
		//test.addVehicles(test);
		//test.gabeRestaurant();
		//test.addBuses(test);


		//THIS SHOWS THE MARKET TESTS I'VE (GABE) BEEN WORKING ON
		//test.marketScenario();

		/*test.addNewBuilding("Market", 250, 200);
		test.addNewPersonHard("p"+0,
				((MarketMapLoc) test.cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketHost,0,33,0,0);
		test.addNewPersonHard("p"+1,
				((MarketMapLoc) test.cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketEmployee,0,33,0,0);*/
		//Bank b = test.cityObject.cityMap.map.get("Bank").get(0).bank;
		//test.addNewPerson("p0");
		//HACK ADDS BUILDINGS TO THE MAP

		//test.addBuses(test);


		//		test.addBuses(test);

		//test.addNewBuilding("Bank", 5, 400);
		//test.addNewBuilding("Market",200,250);
		//test.addNewBuilding("RestaurantLinda", 5, 300);
		//		test.addNewBuilding("House", 200, 5);
		//test.fullyManBuilding("Bank",0);
		//test.fullyManBuilding("Market",0);
		//test.fullyManBuilding("Restaurant",0);

		//		test.fullyManBuilding("Bank",0);
		//		test.fullyManBuilding("Market",0);
		//		test.fullyManBuilding("Market",1);
		//test.addBuses(test);
		//test.addNewBuilding("Bank", 5, 400);
		//test.addNewBuilding("Market",200,250);
		//test.addNewBuilding("Restaurant", 5, 200);
		//test.addNewBuilding("House", 200, 5);

		//test.addBuses(test);
		//test.addNewBuilding("Bank", 5, 400);
		//test.addNewBuilding("Market",200,250);
		//test.addNewBuilding("Restaurant", 5, 200);

		//test.fullyManBuilding("Bank",0);
		//test.fullyManBuilding("Market",0);
		//test.fullyManBuilding("Market",1);
		//test.fullyManBuilding("Bank",0);
		//test.fullyManBuilding("Market",0);


		//PARKER TESTING
		//		test.addNewBuilding("Market", 200,250);
		//		test.fullyManBuilding("Market", 0);


		//Bank b = test.cityObject.cityMap.map.get("Bank").get(0).bank;
		//test.addNewPerson("p0");
		//test.addNewPerson("p1");
		//test.cityObject.people.get(0).setJob(placeOfWork, jobType, start, end);
		/*cityObject.people.add(new PersonAgent("p0",cityObject.cityMap));
		cityObject.people.get(0).startThread();*/

		//		int xStartTest = 300;
		//		int yStartTest = 520;


		//		PersonGui pg1 = new PersonGui(new PersonAgent("A",new CityMap()),test, xStartTest, yStartTest, 300, 520);
		//      PersonGui pg2 = new PersonGui(new PersonAgent("B",new CityMap()),test, xStartTest, yStartTest, 300, 70);
		//		PersonGui pg3 = new PersonGui(new PersonAgent("C",new CityMap()),test, xStartTest, yStartTest, 300, 400);
		//		PersonGui pg4 = new PersonGui(new PersonAgent("D",new CityMap()),test, xStartTest, yStartTest, 300, 190);
		//		PersonGui pg5 = new PersonGui(new PersonAgent("E",new CityMap()),test, xStartTest, yStartTest, 190, 300);
		//		PersonGui pg6 = new PersonGui(new PersonAgent("F",new CityMap()),test, xStartTest, yStartTest, 520, 300);
		//		PersonGui pg7 = new PersonGui(new PersonAgent("G",new CityMap()),test, xStartTest, yStartTest, 400, 300);
		//		PersonGui pg8 = new PersonGui(new PersonAgent("G",new CityMap()),test, xStartTest, yStartTest, 70, 300);
		//		test.city.addMoving(pg1);
		//		test.city.addMoving(pg2);
		//		test.city.addMoving(pg3);
		//		test.city.addMoving(pg4);
		//		test.city.addMoving(pg5);
		//		test.city.addMoving(pg6);
		//		test.city.addMoving(pg7);
		//		test.city.addMoving(pg8);

	}

	public void parkerScenario(){
		System.err.println("Parker's scenario beginning");
		
		addNewBuilding("RestaurantParker",200,5);
		
		addNewPersonHard("p0",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,0,100, 0,0);
		addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantParkerWaiter1,0,100, 0,0);
		addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantParkerWaiter2,0,100, 0,0);
		addNewPersonHard("p3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,0,100, 0,0);
		addNewPersonHard("p4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,0,100, 0,0);
		
	}

	public void busRideScenario(){

		hasBuses = true;

		addNewBuilding("House", 200, 5);
		addBuses(this);

		PersonAgent busRider = new PersonAgent("Rider",this.cityObject.cityMap);
		busRider.myJob = new Job();
		busRider.wantsToRideBus = true;
		busRider.setHouse(((HouseMapLoc) cityObject.cityMap.map.get("House").get(0)).house);

		addNewPerson(busRider);

	}

	public void jScenario() {
		setMAXTIME(500);

		addBuses(this);

		int firstShiftStart = 0;
		int firstShiftEnd = 200;
		int secondShiftStart = 200;
		int secondShiftEnd = 200;
		int thirdShiftStart = 200;
		int thirdShiftEnd = 200;

		//Creates houses
		for (int i=50; i<520; i+=40) {
			addNewBuilding("House", i, 5);	
		}

		for (int i=50; i<520; i+=40) {
			addNewBuilding("House", i, 560);	
		}

		//Creates bank 1
		addNewBuilding("Bank",560,283);	

		//Creates bank 2
		addNewBuilding("Bank",5,283);

		//Creates Gabe's restaurant
		addNewBuilding("RestaurantGabe", 200,283);

		//Creates Linda's restaurant
		addNewBuilding("RestaurantLinda", 283,200);

		//Creates Yocca's restaurant
		addNewBuilding("RestaurantYocca", 365 ,283);	

		addNewPersonHard("Bus1",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Bus2",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Drive1",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Drive2",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 1
		//Early Shift
		addNewPersonHard("BankTeller1a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller1b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//addNewPersonHard("BankTeller1c", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Middle Shift
		addNewPersonHard("BankTeller2a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller2b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//addNewPersonHard("BankTeller2c", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller3a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller3b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//addNewPersonHard("BankTeller3c", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 2
		//Early Shift
		addNewPersonHard("BankTeller4a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller4b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//addNewPersonHard("BankTeller4c", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Middle Shift
		addNewPersonHard("BankTeller5a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller5b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//addNewPersonHard("BankTeller5c", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller6a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller6b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//addNewPersonHard("BankTeller6c", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Creates market
		addNewBuilding("Market",283,365);


		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost3",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Creates market#2
		addNewBuilding("Market",350,200);

		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost2",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));




		//		//Shifts for Gabe's Restaurant
		//Early Shift
		addNewPersonHard("GabeRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("GabeRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("GabeRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for Linda's Restaurant
		//Early Shift
		addNewPersonHard("LindaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("LindaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaLindaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("LindaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Shifts for Yocca's Restaurant
		//Early Shift
		addNewPersonHard("YoccaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("YoccaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));			
		addNewPersonHard("YoccaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("YoccaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
	}

	public void aScenario() {
		setMAXTIME(200);

		addBuses(this);

		int firstShiftStart = 0;
		int firstShiftEnd = 200;
		int secondShiftStart = 200;
		int secondShiftEnd = 200;
		int thirdShiftStart = 200;
		int thirdShiftEnd = 200;

		//Creates houses
		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", i, 5);	
		}

		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", i, 560);	
		}

		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", 5,i);	
		}

		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", 560, i);	
		}


		//Creates bank 1
		addNewBuilding("Bank",283,365);	

		//Creates bank 2
		addNewBuilding("Bank",210,365);

		//Creates Gabe's restaurant
		addNewBuilding("RestaurantGabe", 200,283);

		//Creates Linda's restaurant
		addNewBuilding("RestaurantLinda", 240,200);

		//Creates Yocca's restaurant
		addNewBuilding("RestaurantYocca", 365 ,283);	

		//People that use buses
		addNewPersonHard("Bus1",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Bus2",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 1
		//Early Shift
		addNewPersonHard("BankTeller1a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller1b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Middle Shift
		addNewPersonHard("BankTeller2a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller2b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller3a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller3b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 2
		//Early Shift
		addNewPersonHard("BankTeller4a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller4b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Middle Shift
		addNewPersonHard("BankTeller5a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller5b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller6a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller6b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Creates market
		addNewBuilding("Market",350,365);

		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost3",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Creates market#2
		addNewBuilding("Market",350,200);

		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost2",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));




		//		//Shifts for Gabe's Restaurant
		//Early Shift
		addNewPersonHard("GabeRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("GabeRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("GabeRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for Linda's Restaurant
		//Early Shift
		addNewPersonHard("LindaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("LindaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaLindaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("LindaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Shifts for Yocca's Restaurant
		//Early Shift
		addNewPersonHard("YoccaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("YoccaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));			
		addNewPersonHard("YoccaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("YoccaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
	}

	public void bScenario() {
		setMAXTIME(200);

		addBuses(this);

		int firstShiftStart = 0;
		int firstShiftEnd = 200;
		int secondShiftStart = 200;
		int secondShiftEnd = 200;
		int thirdShiftStart = 200;
		int thirdShiftEnd = 200;

		//Creates houses
		for (int i=50; i<520; i+=60) {
			addNewBuilding("House", 5,i);	
		}

		for (int i=50; i<520; i+=60) {
			addNewBuilding("House", 560, i);	
		}

		for (int i=50; i<230; i+=60) {
			addNewBuilding("House", i, 560);	
		}

		for (int i=50; i<230; i+=60) {
			addNewBuilding("House", i, 5);	
		}

		for (int i=370; i<520; i+=60) {
			addNewBuilding("House", i, 560);	
		}

		for (int i=370; i<520; i+=60) {
			addNewBuilding("House", i, 5);	
		}

		//Creates bank 1
		addNewBuilding("Bank",283,560);	

		//Creates bank 2
		addNewBuilding("Bank",283,5);

		//Creates Gabe's restaurant
		addNewBuilding("RestaurantGabe", 283,365);

		//Creates Linda's restaurant
		addNewBuilding("RestaurantLinda", 283,200);

		//Creates Yocca's restaurant
		addNewBuilding("RestaurantYocca", 365 ,283);	

		//People that use buses
		addNewPersonHard("Bus1",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Bus2",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Drive1",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Drive2",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 1
		//Early Shift
		addNewPersonHard("BankTeller1a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller1b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Middle Shift
		addNewPersonHard("BankTeller2a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller2b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller3a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller3b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 2
		//Early Shift
		addNewPersonHard("BankTeller4a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller4b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Middle Shift
		addNewPersonHard("BankTeller5a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller5b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller6a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller6b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Creates market
		addNewBuilding("Market",200,283);


		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost3",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Creates market#2
		addNewBuilding("Market",225,5);

		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost2",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));




		//		//Shifts for Gabe's Restaurant
		//Early Shift
		addNewPersonHard("GabeRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("GabeRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("GabeRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for Linda's Restaurant
		//Early Shift
		addNewPersonHard("LindaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("LindaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaLindaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("LindaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Shifts for Yocca's Restaurant
		//Early Shift
		addNewPersonHard("YoccaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("YoccaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));			
		addNewPersonHard("YoccaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("YoccaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
	}

	public void bankRobbery(){
		hasBuses = false;
		setMAXTIME(50);
		addNewBuilding("House",400,5);
		addNewBuilding("Bank",365,250);
		addNewPersonHard("p0",((BankMapLoc)cityObject.cityMap.map.get("Bank").get(0)).bank,JobType.BankTeller,0,100,0,0);
		addNewPersonHard("BankRobber", null,JobType.NOTSELECTED,0,0,0,0);
		for(PersonAgent p:cityObject.people){
			p.hungerLevel = 0;
		}
	}


	public void bankScenario(){
		hasBuses = false;
		setMAXTIME(50);
		addNewBuilding("House", 200, 5);
		addNewBuilding("Bank",200,250);
		fullyManBuilding("Bank",0);

		for(PersonAgent p:cityObject.people){
			p.hungerLevel = 0;
		}

	}

	public void driveScenario(int which){
		addNewBuilding("House",200,5);
		addNewPersonHard("Drive"+which,null,JobType.NOTSELECTED,0,0,0,0);
	}

	public void rainScenario(){
		
		city.startRaining();
		
	}
	
	public void marketScenario(){
		hasBuses = false;
		setMAXTIME(50);
		//cityObject.MAXTIME = 50;
		addNewBuilding("House", 200, 560);
		addNewBuilding("Market",250,200);
		
		addNewBuilding("Bank", 200, 5);
		fullyManBuilding("Bank",0);
		fullyManBuilding("Market",0);

		/*addNewPersonHard("p"+0,
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketHost,0,5,0,0);

		addNewPersonHard("p"+1,
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketEmployee,0,5,0,0);

		addNewPersonHard("p"+2,
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketCashier,0,6,0,0);

		addNewPersonHard("replacementHOST",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketHost,5,100,0,0);

		addNewPersonHard("replacementEMPLOYEE",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketEmployee,4,100,0,0);


		addNewPersonHard("relacementCASHIER",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketCashier,6,100,0,0);


		addNewPersonHard("p"+3,null,null,0,0,0,0);
		addNewPersonHard("p"+4,null,null,0,0,0,0);


		/*addNewPersonHard("p"+5,null,null,0,0,0,0);
		addNewPersonHard("p"+6,null,null,0,0,0,0);
		addNewPersonHard("p"+7,null,null,0,0,0,0);
		addNewPersonHard("p"+8,null,null,0,0,0,0);
		addNewPersonHard("p"+9,null,null,0,0,0,0);
		addNewPersonHard("p"+10,null,null,0,0,0,0);*/

		//fullyManBuilding("Market",0);
	}

	public void marketSimpleScenario(){
		System.out.println("Beginning Simple Market Scenario");

		hasBuses = false;
		setMAXTIME(50);
		addNewBuilding("House", 200, 560);
		addNewBuilding("Market",250,200);
		addNewBuilding("Bank", 200, 5);
		fullyManBuilding("Bank",0);

		//employees
		addNewPersonHard("H1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketHost,0,100,0,0);

		//		addNewPersonHard("H2",
		//				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
		//				JobType.MarketHost,10,50,0,0);


		addNewPersonHard("E1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketEmployee,0,100,0,0);

		//		addNewPersonHard("E2",
		//				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
		//				JobType.MarketEmployee,10,50,0,0);

		addNewPersonHard("C1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketCashier,0,100,0,0);

		//		addNewPersonHard("C2",
		//				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
		//				JobType.MarketCashier,10,50,0,0);


		//customers
		addNewPersonHard("p1",null,null,0,0,0,0);
		addNewPersonHard("p"+5,null,null,0,0,0,0);
		addNewPersonHard("p"+6,null,null,0,0,0,0);
		addNewPersonHard("p"+7,null,null,0,0,0,0);
	}

	public void cMarketDeliveryScenario(){
		

		//make all restaurants
		//deplete their inventories
		//set their markets
		//make them ask the market for food
		addNewBuilding("Market",250,200);
		addNewBuilding("RestaurantGabe",200,250);
		addNewBuilding("RestaurantLinda", 200, 300);
		//addNewBuilding("RestaurantSimon", 283,365);
		
		addNewBuilding("House", 500, 5);
		addNewBuilding("Bank", 200, 5);
		fullyManBuilding("Bank",0);


		addNewPersonHard("H1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketHost,0,100,0,0);

		addNewPersonHard("E1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketEmployee,0,100,0,0);

		addNewPersonHard("C1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketCashier,0,100,0,0);

		addNewPersonHard("D1", 
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketDeliveryMan,0,100,0,0);

		//man gabes restaurant - rest.get(0)
		addNewPersonHard("g0",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,0,100, 0,0);
		addNewPersonHard("g1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,100, 0,0);
		addNewPersonHard("g2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,100, 0,0);
		addNewPersonHard("g3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,0,100, 0,0);
		addNewPersonHard("g4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,0,100, 0,0);
		
		((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant.cook.depleteInventory();
		
		//man lindas restaurant - rest.get(1)
		addNewPersonHard("l1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,0,100, 0,0);
		addNewPersonHard("l1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,0,100, 0,0);
		addNewPersonHard("l2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,0,100, 0,0);
		addNewPersonHard("l3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,0,100, 0,0);
		addNewPersonHard("l4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,0,100, 0,0);
		
		((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant.cook.depleteInventory();

	
//		//man simons restaurant - rest.get(2)
//		addNewPersonHard("s0",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,0,100, 0,0);
//		addNewPersonHard("s1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,100, 0,0);
//		addNewPersonHard("s2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,100, 0,0);
//		addNewPersonHard("s3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,0,100, 0,0);
//		addNewPersonHard("s4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,0,100, 0,0);
//		
//		//cook is null because needs to shadow variables (super.cook = cook) in restaurantCard (i think?)
//		((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant.cook.depleteInventory();
	
	
	}
	
	
	
	

	public void gabeRestaurant(){
		addNewBuilding("RestaurantGabe",200,250);
		addNewBuilding("House", 500, 5);


		addNewPersonHard("p0",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,0,15, 0,0);
		addNewPersonHard("p1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,15, 0,0);
		addNewPersonHard("p2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,15, 0,0);
		addNewPersonHard("p3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,0,15, 0,0);
		addNewPersonHard("p4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,0,15, 0,0);

		addNewPersonHard("replacementHOST",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,11,100, 0,0);
		addNewPersonHard("replacementCASHIER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,11,100, 0,0);
		addNewPersonHard("replacementCOOK",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,11,100, 0,0);

		addNewPersonHard("replacementWAITER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,22,100, 0,0);
		//addNewPersonHard("replacementCASHIER",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,10,100, 0,0);


		//System.out.println(cityObject.cityMap.map.get("House").get(0).loc.x +", "+cityObject.cityMap.map.get("House").get(0).loc.y);


	}


	public void busyScenario(){
		hasBuses = true;
		addBuses(this);
		addNewBuilding("Bank", 5, 400);
		addNewBuilding("Market",200,250);
		addNewBuilding("Market", 250, 200);
		addNewBuilding("House", 200, 5);
		addNewBuilding("House", 500, 5);
		fullyManBuilding("Bank",0);
		fullyManBuilding("Market",0);
		fullyManBuilding("Market",1);
	}

	public void restaurantScenario(String scenarioName){
		hasBuses = false;
		setMAXTIME(20);
		addNewBuilding("House", 200, 5);
		addNewBuilding(scenarioName,5, 300);
		//addNewBuilding("Bank", 200, 5);
		fullyManBuilding(scenarioName,0);
		//fullyManBuilding("Bank",0);

		int bankNum = (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random());
		int houseNum = (int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random());

		PersonAgent person = new PersonAgent("restaurant",cityObject.cityMap);
		person.setJob(null, JobType.NOTSELECTED, 0, 0);
		person.setBank(bankNum);
		if(!cityObject.cityMap.map.get("House").isEmpty()){
			person.setHouse(((HouseMapLoc) cityObject.cityMap.map.get("House").get(houseNum)).house);
		}
		//person.setHouse(((HouseMapLoc) cityObject.cityMap.map.get("House").get(houseNum)).house);
		boolean wantsToRideBus = false;
		if(Math.random()>.75 && hasBuses){
			wantsToRideBus = true;
		}
	}

	public void restaurantSimonScenario(){
		hasBuses = false;
		setMAXTIME(50);
		addNewBuilding("House", 200, 5);
		addNewBuilding("Resataurant",200,250);
		//fullyManBuilding("Restaurant",0);

	}
	
	public void gMarketScenario(){
		//System.err.println("DOOT");

		addNewBuilding("Market",250,200);
		addNewBuilding("RestaurantGabe",200,250);
		addNewBuilding("RestaurantLinda", 200, 300);
		//addNewBuilding("RestaurantSimon", 283,365);
		
		addNewBuilding("House", 500, 5);
		addNewBuilding("Bank", 200, 5);
		fullyManBuilding("Bank",0);


		addNewPersonHard("H1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketHost,0,100,0,0);

		addNewPersonHard("E1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketEmployee,0,100,0,0);

		addNewPersonHard("C1",
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketCashier,0,100,0,0);

		addNewPersonHard("D1", 
				((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,
				JobType.MarketDeliveryMan,0,100,0,0);

		//man gabes restaurant - rest.get(0)
		addNewPersonHard("g0",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,0,100, 0,0);
		addNewPersonHard("g1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,100, 0,0);
		addNewPersonHard("g2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,0,100, 0,0);
		addNewPersonHard("g3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,0,100, 0,0);
		addNewPersonHard("g4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,0,100, 0,0);
		
		((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant.cook.depleteInventory();
		
		//man lindas restaurant - rest.get(1)
		addNewPersonHard("l1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,0,100, 0,0);
		addNewPersonHard("l1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,0,100, 0,0);
		addNewPersonHard("l2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,0,100, 0,0);
		addNewPersonHard("l3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,0,100, 0,0);
		addNewPersonHard("l4",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,0,100, 0,0);
		
		((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant.cook.depleteInventory();
		

	}
	
	
	public void rWeekendScenario() {
		setMAXTIME(200);

		addBuses(this);

		int firstShiftStart = 0;
		int firstShiftEnd = 200;
		int secondShiftStart = 200;
		int secondShiftEnd = 200;
		int thirdShiftStart = 200;
		int thirdShiftEnd = 200;

		//Creates houses
		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", i, 5);	
		}

		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", i, 560);	
		}

		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", 5,i);	
		}

		for (int i=50; i<520; i+=80) {
			addNewBuilding("House", 560, i);	
		}


		//Creates bank 1
		addNewBuilding("Bank",283,365);	

		//Creates bank 2
		addNewBuilding("Bank",210,365);

		//Creates Gabe's restaurant
		addNewBuilding("RestaurantGabe", 200,283);

		//Creates Linda's restaurant
		addNewBuilding("RestaurantLinda", 240,200);

		//Creates Yocca's restaurant
		addNewBuilding("RestaurantYocca", 365 ,283);	
		
		for(int i = 0;i<cityObject.cityMap.map.get("Bank").size();++i){
			((BankMapLoc)cityObject.cityMap.map.get("Bank").get(i)).bank.animation.panel.closeBank();
		}
		
		
		((RestaurantGabeControlPanel)((RestaurantGabe) ((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(0)).restaurant).cityRestaurantGabe.animationPanel.controlPanel).closeRestaurant();
		((RestaurantLindaControlPanel)(((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(1)).restaurant).cityRestaurant.animationPanel.controlPanel).closeRestaurant();
		((RestaurantYoccaControlPanel)((RestaurantYocca) ((RestaurantMapLoc) cityObject.cityMap.map.get("Restaurant").get(2)).restaurant).cityRestaurant.animationPanel.controlPanel).closeRestaurant();
		

		//People that use buses
		addNewPersonHard("Bus1",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("Bus2",null, JobType.NOTSELECTED,0,0, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 1
		//Early Shift
		addNewPersonHard("BankTeller1a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller1b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Middle Shift
		addNewPersonHard("BankTeller2a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller2b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller3a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller3b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(0)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for bank 2
		//Early Shift
		addNewPersonHard("BankTeller4a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller4b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Middle Shift
		addNewPersonHard("BankTeller5a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller5b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Late Shift
		addNewPersonHard("BankTeller6a", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("BankTeller6b", ((BankMapLoc) cityObject.cityMap.map.get("Bank").get(1)).bank, JobType.BankTeller,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Creates market
		addNewBuilding("Market",350,365);
		((MarketMapLoc)cityObject.cityMap.map.get("Market").get(0)).market.panel.closeMarket();

		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost3",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(0)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Creates market#2
		addNewBuilding("Market",350,200);
		((MarketMapLoc)cityObject.cityMap.map.get("Market").get(1)).market.panel.closeMarket();


		//Early Shift
		addNewPersonHard("MarketHost1"+0,	((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee1b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketEmployee,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketCashier1"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketCashier,firstShiftStart,100,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		//		addNewPersonHard("MarketHost2"+0,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2a"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee2b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3"+2,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,	JobType.MarketCashier,secondShiftStart,secondShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//LateShift
		//		addNewPersonHard("MarketHost2",((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market,JobType.MarketHost,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3a", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("MarketEmployee3b"+1,((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketEmployee,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		//		addNewPersonHard("MarketCashier3", ((MarketMapLoc) cityObject.cityMap.map.get("Market").get(1)).market, JobType.MarketCashier,thirdShiftStart,thirdShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));




		//		//Shifts for Gabe's Restaurant
		//Early Shift
		addNewPersonHard("GabeRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("GabeRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("GabeRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantGabeWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("GabeRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(0)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//Shifts for Linda's Restaurant
		//Early Shift
		addNewPersonHard("LindaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("LindaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaLindaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("LindaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantLindaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("LindaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(1)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));


		//Shifts for Yocca's Restaurant
		//Early Shift
		addNewPersonHard("YoccaRestaurantHost1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,firstShiftStart,firstShiftEnd,(int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter1b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier1",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,firstShiftStart,firstShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//MiddleShift
		addNewPersonHard("YoccaRestaurantHost2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));			
		addNewPersonHard("YoccaRestaurantWaiter2a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter2b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier2",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,secondShiftStart,secondShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));

		//LateShift
		addNewPersonHard("YoccaRestaurantHost3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantHost,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3a",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter1,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantWaiter3b",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantYoccaWaiter2,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCook3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCook,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
		addNewPersonHard("YoccaRestaurantCashier3",((RestaurantMapLoc)this.cityObject.cityMap.map.get("Restaurant").get(2)).restaurant, JobType.RestaurantCashier,thirdShiftStart,thirdShiftEnd, (int) Math.floor(cityObject.cityMap.map.get("Bank").size()*Math.random()),(int) Math.floor(cityObject.cityMap.map.get("House").size()*Math.random()));
	}
	

	public void setMAXTIME(int m){
		MAXTIME = m;
		cityObject.MAXTIME = m;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		time++;

	}

	public void initializeGrid(){
		for (int i=0; i<gridX/cityScale ; i++)
			for (int j = 0; j<gridY/cityScale; j++)
				grid[i][j]=new Semaphore(1,true);

		try{
			for(int i = (int) Math.ceil(200/cityScale);i<(int) Math.floor(400/cityScale);++i){
				for(int j = (int) Math.ceil(200/cityScale);j<(int) Math.floor(400/cityScale);++j){
					//System.out.println("("+i+","+j+")");
					grid[i][j].acquire();
				}
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public void setOnRamps(){
		//clockwise
		onRamps.add(new OnRamp(new Loc(410,130),new Loc(410,160),false,true));
		onRamps.add(new OnRamp(new Loc(130,180),new Loc(160,180),true,true));

		//counterclockwise
		onRamps.add(new OnRamp(new Loc(90,120),new Loc(70,120),true,false));
		onRamps.add(new OnRamp(new Loc(490,120),new Loc(530,120),true,false));
	}

	public void setStopLights(){
		StopLight light = new StopLight();
		light.loc = new Loc(285,80);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(270,80,60,80);
		light.letGoRegion = new Rectangle(290,80,20,80);
		city.addStatic(light);
		stopLights.add(light);

		light = new StopLight();
		light.loc = new Loc(285,440);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(270,440,60,80);
		light.letGoRegion = new Rectangle(290,440,20,80);
		city.addStatic(light);
		stopLights.add(light);

		light = new StopLight();
		light.loc = new Loc(80,285);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(80,270,80,60);
		light.letGoRegion = new Rectangle(80,290,80,20);
		city.addStatic(light);
		stopLights.add(light);

		light = new StopLight();
		light.loc = new Loc(440,285);
		light.status = CrosswalkStatus.Pedestrian;
		light.safeRegion = new Rectangle(440,270,80,60);
		light.letGoRegion = new Rectangle(440,290,80,20);
		city.addStatic(light);
		stopLights.add(light);
	}
}
