package cityGui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import cityGui.trace.AlertLevel;
import cityGui.trace.AlertLog;
import cityGui.trace.AlertTag;

public class CityControlPanel extends JPanel implements ActionListener{

	SimCityGui city;

	public static final int CP_WIDTH = 600, CP_HEIGHT = 100;
	JButton addParkerRestaurant, addLindaRestaurant, addGabeRestaurant, addYoccaRestaurant, addBobbyRestaurant, addSimonRestaurant, addBank, addHouse, addMarket, addPerson, newScenario;


	JButton dummyScenarioA, simpleBusRide,jScenario,aScenario,bScenario,fCloseScenario, bankRobbery, rWeekend, MarketSimple, cMarket, pRain, qRain, gMarket, parkerRest;




	
	//For managing traces
	JToggleButton InfoButton;		//You could (and probably should) substitute a JToggleButton to replace both of these, but I split it into enable and disable for clarity in the demo.
	JToggleButton DebugButton;
	JToggleButton ErrorButton;
	JToggleButton MessageButton;
	JToggleButton RestaurantGabeTagButton;	
	JToggleButton RestaurantYoccaTagButton;
	JToggleButton RestaurantLindaTagButton;
	JToggleButton RestaurantSimonTagButton;
	JToggleButton BankTagButton;
	JToggleButton MarketTagButton;
	JToggleButton HouseTagButton;
	JToggleButton PersonTagButton;
	JToggleButton BusStopTagButton;
	JToggleButton GeneralCityTagButton;
	JToggleButton CurrentCardButton;

	String name = "Control Panel";

	CityListPanel panel1 = new CityListPanel();
	CityListPanel panel2 = new CityListPanel();
	CityListPanel panel3 = new CityListPanel("trace");

	public CityControlPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);

		this.setLayout(new GridLayout(1,3,5,0));


		add(panel1);
		add(panel2);
		//add(panel3);
		//city.trace.add(panel3);

		//AddBuilding/People Buttons



		addBank = new JButton("Add Bank");
		addBank.addActionListener(this);
		panel1.addButton(addBank);


		addHouse = new JButton("Add House");
		addHouse.addActionListener(this);
		panel1.addButton(addHouse);

		addMarket = new JButton("Add Market");
		addMarket.addActionListener(this);
		panel1.addButton(addMarket);

		addParkerRestaurant = new JButton("Add Parker's Restaurant");
		addParkerRestaurant.addActionListener(this);
		panel1.addButton(addParkerRestaurant);

		addLindaRestaurant = new JButton("Add Linda's Restaurant");
		addLindaRestaurant.addActionListener(this);
		panel1.addButton(addLindaRestaurant);

		addGabeRestaurant = new JButton("Add Gabe's Restaurant");
		addGabeRestaurant.addActionListener(this);
		panel1.addButton(addGabeRestaurant);

		addYoccaRestaurant = new JButton("Add Andrew's Restaurant");
		addYoccaRestaurant.addActionListener(this);
		panel1.addButton(addYoccaRestaurant);

		addBobbyRestaurant = new JButton("Add Bobby's Restaurant");
		addBobbyRestaurant.addActionListener(this);
		panel1.addButton(addBobbyRestaurant);

		addSimonRestaurant = new JButton("Add Simon's Restaurant");
		addSimonRestaurant.addActionListener(this);
		panel1.addButton(addSimonRestaurant);

		addPerson = new JButton("Add Person");
		addPerson.addActionListener(this);
		panel1.addButton(addPerson);

		//		newScenario = new JButton("Select Scenario");
		//		newScenario.addActionListener(this);
		//		panel1.addButton(newScenario);


		//Scenario Buttons
//		dummyScenarioA = new JButton("DummyScenarioA");
//		dummyScenarioA.addActionListener(this);
//		panel2.addButton(dummyScenarioA);

		
		aScenario = new JButton("A. Big City");
		aScenario.addActionListener(this);
		panel2.addButton(aScenario);
		
		bScenario = new JButton("B. Big City");
		bScenario.addActionListener(this);
		panel2.addButton(bScenario);
		
		cMarket = new JButton("C. Market Delivery");
		cMarket.addActionListener(this);
		panel2.addButton(cMarket);
		
		simpleBusRide = new JButton("E. Simple Bus Ride");
		simpleBusRide.addActionListener(this);
		panel2.addButton(simpleBusRide);
		
		fCloseScenario = new JButton("F. Close Buildings");
		fCloseScenario.addActionListener(this);
		panel2.addButton(fCloseScenario);
		
		gMarket = new JButton("G. Closed Rest. Deliv.");
		gMarket.addActionListener(this);
		panel2.addButton(gMarket);
		
		jScenario = new JButton("J. Big City");
		jScenario.addActionListener(this);
		panel2.addButton(jScenario);
		
		bankRobbery = new JButton("O. Bank Robbery");
		bankRobbery.addActionListener(this);
		panel2.addButton(bankRobbery);
		
		pRain = new JButton("P. Vehicle Collision");
		pRain.addActionListener(this);
		panel2.addButton(pRain);
		
		qRain = new JButton("Q. Vehicle Hit Pedestrian");
		qRain.addActionListener(this);
		panel2.addButton(qRain);
		
		rWeekend = new JButton("R. Weekend Behavior");
		rWeekend.addActionListener(this);
		panel2.addButton(rWeekend);
		
		MarketSimple = new JButton("Simple Market");
		MarketSimple.addActionListener(this);
		//panel2.addButton(MarketSimple);
		
		parkerRest = new JButton("Parker's Restaurant");
		parkerRest.addActionListener(this);
		panel2.addButton(parkerRest);
		





		//Trace panel buttons
		InfoButton = new JToggleButton("Hide Level: INFO");
		InfoButton.setSelected(true);
		InfoButton.addActionListener(this);
		panel3.addToggleButton(InfoButton);
		
		MessageButton = new JToggleButton("Hide Level: MESSAGE");
		MessageButton.setSelected(true);
		MessageButton.addActionListener(this);
		panel3.addToggleButton(MessageButton);

		DebugButton = new JToggleButton("Hide Level: DEBUG");
		DebugButton.setSelected(true);
		DebugButton.addActionListener(this);
		panel3.addToggleButton(DebugButton);

		ErrorButton = new JToggleButton("Hide Level: ERROR");
		ErrorButton.setSelected(true);
		ErrorButton.addActionListener(this);
		panel3.addToggleButton(ErrorButton);

		CurrentCardButton = new JToggleButton("Current Building");
		CurrentCardButton.setSelected(false);
		CurrentCardButton.addActionListener(this);
		panel3.addToggleButton(CurrentCardButton);

		PersonTagButton = new JToggleButton("Hide Tag: PERSON");
		PersonTagButton.setSelected(true);
		PersonTagButton.addActionListener(this);
		panel3.addToggleButton(PersonTagButton);

		BusStopTagButton = new JToggleButton("Hide Tag: BUS_STOP");
		BusStopTagButton.setSelected(true);
		BusStopTagButton.addActionListener(this);
		panel3.addToggleButton(BusStopTagButton);

		GeneralCityTagButton = new JToggleButton("Hide Tag: GENERAL_CITY");
		GeneralCityTagButton.setSelected(true);
		GeneralCityTagButton.addActionListener(this);
		panel3.addToggleButton(BusStopTagButton);

		BankTagButton = new JToggleButton("Hide Tag: BANK");
		BankTagButton.setSelected(true);
		BankTagButton.addActionListener(this);
		panel3.addToggleButton(BankTagButton);
		
		HouseTagButton = new JToggleButton("Hide Tag: HOUSE");
		HouseTagButton.setSelected(true);
		HouseTagButton.addActionListener(this);
		panel3.addToggleButton(HouseTagButton);

		MarketTagButton = new JToggleButton("Hide Tag: MARKET");
		MarketTagButton.setSelected(true);
		MarketTagButton.addActionListener(this);
		panel3.addToggleButton(MarketTagButton);
		
		RestaurantGabeTagButton = new JToggleButton("Hide Tag: RESTAURANT_GABE");
		RestaurantGabeTagButton.setSelected(true);
		RestaurantGabeTagButton.addActionListener(this);
		panel3.addToggleButton(RestaurantGabeTagButton);

		RestaurantYoccaTagButton = new JToggleButton("Hide Tag: RESTAURANT_YOCCA");
		RestaurantYoccaTagButton.setSelected(true);
		RestaurantYoccaTagButton.addActionListener(this);
		panel3.addToggleButton(RestaurantYoccaTagButton);

		RestaurantLindaTagButton = new JToggleButton("Hide Tag: RESTAURANT_LINDA");
		RestaurantLindaTagButton.setSelected(true);
		RestaurantLindaTagButton.addActionListener(this);
		panel3.addToggleButton(RestaurantLindaTagButton);

		RestaurantSimonTagButton = new JToggleButton("Hide Tag: RESTAURANT_SIMON");
		RestaurantSimonTagButton.setSelected(true);
		RestaurantSimonTagButton.addActionListener(this);
		panel3.addToggleButton(RestaurantSimonTagButton);

	}

	public void actionPerformed(ActionEvent e) {
		//		if (e.getSource().equals(addRestaurant)) {
		//			city.city.addObject(CityComponents.RESTAURANT);
		//			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New Restaurant");
		//		}

		//^^add new checks for each person's restaurant!

	if(e.getSource().equals(addLindaRestaurant)){
		 city.city.addObject(CityComponents.RESTAURANTLINDA);
	}
		else if(e.getSource().equals(addGabeRestaurant)){
			 city.city.addObject(CityComponents.RESTAURANTGABE);
		}
		else if (e.getSource().equals(addSimonRestaurant)){
			 city.city.addObject(CityComponents.RESTAURANTSIMON);
		
		}
		else if (e.getSource().equals(addYoccaRestaurant)){
			 city.city.addObject(CityComponents.RESTAURANTYOCCA);
		
		}
		else if (e.getSource().equals(addParkerRestaurant)){
			 city.city.addObject(CityComponents.RESTAURANTPARKER);
		}
	
	
	
		else if(e.getSource().equals(jScenario)){
			city.jScenario();
		}
		else if(e.getSource().equals(aScenario)){
			city.aScenario();
		}
		else if(e.getSource().equals(bScenario)){
			city.bScenario();
		}
		else if(e.getSource().equals(fCloseScenario)){
			city.aScenario();
		}
		else if(e.getSource().equals(simpleBusRide)){
			city.busRideScenario();
		}
		else if(e.getSource().equals(bankRobbery)){
			city.bankRobbery();
		}
		else if(e.getSource().equals(rWeekend)){
			city.rWeekendScenario();
		}



		else if (e.getSource().equals(addBank)) {

			//AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Adding New Bank");
			city.city.addObject(CityComponents.BANK);
		}
		else if (e.getSource().equals(addHouse)) {
			city.city.addObject(CityComponents.HOUSE);
		}
		else if (e.getSource().equals(addMarket)) {
			city.city.addObject(CityComponents.MARKET);
		}
		//		else if (e.getSource().equals(newScenario)){
		//			//AlertLog.getInstance().logInfo(AlertTag., this.name, "Adding New Restaurant");
		//			//System.out.println("HEREERERERRE");
		//			ScenarioPanel scenarioPanel = new ScenarioPanel(this.city);
		//		}
		else if(e.getSource().equals(addPerson)) {
			city.NewPersonCreationPanel();
		}
		else if(e.getSource().equals(InfoButton)) {
			if (InfoButton.isSelected()){
				InfoButton.setText("Hide Level: INFO");
				city.tracePanel.showAlertsWithLevel(AlertLevel.INFO);
			}
			else{
				InfoButton.setText("Show Level: INFO");
				city.tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
			}
		}
		else if(e.getSource().equals(MessageButton)) {
			if (MessageButton.isSelected()){
				MessageButton.setText("Hide Level: MESSAGE");
				city.tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
			}
			else{
				InfoButton.setText("Show Level: MESSAGE");
				city.tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
			}
		}
		else if(e.getSource().equals(DebugButton)) {
			if (DebugButton.isSelected()){
				DebugButton.setText("Hide Level: DEBUG");
				city.tracePanel.showAlertsWithLevel(AlertLevel.DEBUG);
			}
			else{
				DebugButton.setText("Show Level: DEBUG");
				city.tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
			}
		}
		else if(e.getSource().equals(ErrorButton)) {
			if (ErrorButton.isSelected()){
				ErrorButton.setText("Hide Level: ERROR");
				city.tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
			}
			else{
				ErrorButton.setText("Show Level: ERROR");
				city.tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
			}
		}
		else if(e.getSource().equals(RestaurantGabeTagButton)) {
			if (RestaurantGabeTagButton.isSelected()){
				RestaurantGabeTagButton.setText("Hide Tag: RESTAURANT_GABE");
				city.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT_GABE);
			}
			else{
				RestaurantGabeTagButton.setText("Show Tag: RESTAURANT_GABE");
				city.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT_GABE);
			}
		}
		else if(e.getSource().equals(RestaurantYoccaTagButton)) {
			if (RestaurantYoccaTagButton.isSelected()){
				RestaurantYoccaTagButton.setText("Hide Tag: RESTAURANT_YOCCA");
				city.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT_YOCCA);
			}
			else{
				RestaurantYoccaTagButton.setText("Show Tag: RESTAURANT_YOCCA");
				city.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT_YOCCA);
			}
		}
		else if(e.getSource().equals(RestaurantLindaTagButton)) {
			if (RestaurantLindaTagButton.isSelected()){
				RestaurantLindaTagButton.setText("Hide Tag: RESTAURANT_LINDA");
				city.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT_LINDA);
			}
			else{
				RestaurantLindaTagButton.setText("Show Tag: RESTAURANT_LINDA");
				city.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT_LINDA);
			}
		}
		else if(e.getSource().equals(RestaurantSimonTagButton)) {
			if (RestaurantSimonTagButton.isSelected()){
				RestaurantSimonTagButton.setText("Hide Tag: RESTAURANT_SIMON");
				city.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT_SIMON);
			}
			else{
				RestaurantSimonTagButton.setText("Show Tag: RESTAURANT_SIMON");
				city.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT_SIMON);
			}
		}
		else if(e.getSource().equals(BankTagButton)) {
			if (BankTagButton.isSelected()){
				BankTagButton.setText("Hide Tag: BANK");
				city.tracePanel.showAlertsWithTag(AlertTag.BANK);
			}
			else{
				BankTagButton.setText("Show Tag: BANK");
				city.tracePanel.hideAlertsWithTag(AlertTag.BANK);
			}
		}
		else if(e.getSource().equals(MarketTagButton)) {
			if (MarketTagButton.isSelected()){
				MarketTagButton.setText("Hide Tag: MARKET");
				city.tracePanel.showAlertsWithTag(AlertTag.MARKET);
			}
			else{
				MarketTagButton.setText("Show Tag: MARKET");
				city.tracePanel.hideAlertsWithTag(AlertTag.MARKET);
			}
		}
		else if(e.getSource().equals(HouseTagButton)) {
			if (HouseTagButton.isSelected()){
				HouseTagButton.setText("Hide Tag: HOUSE");
				city.tracePanel.showAlertsWithTag(AlertTag.HOUSE);
			}
			else{
				HouseTagButton.setText("Show Tag: HOUSE");
				city.tracePanel.hideAlertsWithTag(AlertTag.HOUSE);
			}
		}
		else if(e.getSource().equals(PersonTagButton)) {
			if (PersonTagButton.isSelected()){
				PersonTagButton.setText("Hide Tag: PERSON");
				city.tracePanel.showAlertsWithTag(AlertTag.PERSON);
			}
			else{
				PersonTagButton.setText("Show Tag: PERSON");
				city.tracePanel.hideAlertsWithTag(AlertTag.PERSON);
			}
		}
		else if(e.getSource().equals(BusStopTagButton)) {
			if (BusStopTagButton.isSelected()){
				BusStopTagButton.setText("Hide Tag: BUS_STOP");
				city.tracePanel.showAlertsWithTag(AlertTag.BUS_STOP);
			}
			else{
				BusStopTagButton.setText("Show Tag: BUS_STOP");
				city.tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);
			}
		}

		 
		 //SCENARIOS
		else if (e.getSource().equals(MarketSimple)){
			city.marketSimpleScenario();
		}
	
		else if (e.getSource().equals(cMarket)){
			city.cMarketDeliveryScenario();
		}
	
		else if (e.getSource().equals(pRain)){
			city.rainScenario();
		}
	
		else if (e.getSource().equals(qRain)){
			city.rainScenario();
		}
	
		else if (e.getSource().equals(gMarket)){
			city.gMarketScenario();
		}
	
		else if (e.getSource().equals(parkerRest)){
			city.parkerScenario();
		}
	
		else if(e.getSource().equals(GeneralCityTagButton)) {
			if (GeneralCityTagButton.isSelected()){
				GeneralCityTagButton.setText("Hide Tag: GENERAL_CITY");
				city.tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);
			}
			else{
				GeneralCityTagButton.setText("Show Tag: GENERAL_CITY");
				city.tracePanel.hideAlertsWithTag(AlertTag.GENERAL_CITY);
			}
		}
		else if(e.getSource().equals(CurrentCardButton)) {
			if (CurrentCardButton.isSelected()){
				CurrentCardButton.setText("Stop Filtering Current Card");
				city.tracePanel.showOnlyCurrentCard();
			}
			else{
				CurrentCardButton.setText("CurrentBuilding");
				city.tracePanel.stopShowingOnlyCurrentCard();
			}
		}



	}

	public void addTraceControls(){
		city.trace.add(panel3);
	}

	private class CityListPanel extends JPanel{
		private JPanel view = new JPanel();
		private List<JButton> inventoryList = new ArrayList<JButton>();
		public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Dimension paneSize = new Dimension (150, 90);	
		CityListPanel(){
			Dimension size = new Dimension (150, 90);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);

			setLayout(new FlowLayout());
			view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
			pane.setViewportView(view);
			add(pane);


			//new


			pane.setPreferredSize(paneSize);
			pane.setMinimumSize(paneSize);
			pane.setMaximumSize(paneSize);




		}


		CityListPanel(String trace){
			Dimension size = new Dimension (150, 185);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);

			setLayout(new FlowLayout());
			view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
			pane.setViewportView(view);
			add(pane);

			pane.setPreferredSize(size);
			pane.setMinimumSize(size);
			pane.setMaximumSize(size);

		}


		public void addButton(JButton item){

			Dimension buttonSize = new Dimension(paneSize.width-20, (int) (paneSize.height / 5));
			item.setPreferredSize(buttonSize);
			item.setMinimumSize(buttonSize);
			item.setMaximumSize(buttonSize);
			view.add(item);
		}

		public void addToggleButton(JToggleButton item){

			Dimension buttonSize = new Dimension(paneSize.width-20, (int) (paneSize.height / 5));
			item.setPreferredSize(buttonSize);
			item.setMinimumSize(buttonSize);
			item.setMaximumSize(buttonSize);
			view.add(item);
		}
	}

}
