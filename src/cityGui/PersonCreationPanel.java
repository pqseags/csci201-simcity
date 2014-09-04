package cityGui;

import interfaces.Person;
import interfaces.PlaceOfWork;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import person.PersonAgent;
import restaurant.restaurantGabe.RestaurantGabe;
import restaurant.restaurantLinda.RestaurantLinda;

import restaurant.restaurantParker.RestaurantParker;

import restaurant.restaurantSimon.RestaurantSimon;

import restaurant.restaurantYocca.RestaurantYocca;
import util.BankMapLoc;
import util.HouseMapLoc;
import util.JobType;
import util.MarketMapLoc;
import util.Place;
import util.RestaurantMapLoc;

public class PersonCreationPanel extends JFrame implements ActionListener, ListSelectionListener{

	JButton add;
	JPanel namePanel,creationPanel;
	/*JScrollPane scrollPlacesToWork =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JScrollPane scrollTypesOfJobs =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);*/
	JList placesToWork;
	JList typesOfJobs;
	JList<String> placeOptions;
	JList<String> jobsOptions;
	JList<String> houseOptions;
	DefaultListModel<String> listModel2;
	JTextField nameField, shiftStartField,shiftEndField,errorField;
	SimCityGui c;

	public PersonCreationPanel(SimCityGui c){
		this.c = c;

		setSize(600,400);
		setTitle("Person Creation Panel");

		//TITLE
		//JLabel titleLabel = new JLabel("Person Creation Panel");

		//Name Panel
		namePanel = new JPanel();
		//namePanel.setSize(200,100);
		namePanel.setLayout(new FlowLayout());

		JLabel nameLabel = new JLabel("Name");
		nameField = new JTextField();

		//Dimension d = new Dimension(100,10);
		Dimension b = new Dimension(100,20);
		nameField.setMinimumSize(b);
		nameField.setPreferredSize(b);
		nameField.setMaximumSize(b);
		//namePanel.setMinimumSize(d);
		//namePanel.setPreferredSize(d);
		namePanel.add(nameLabel);
		namePanel.add(nameField);



		//Place Of Work Panel
		JPanel placeOfWorkPanel = new JPanel();
		JLabel placeOfWorkLabel = new JLabel("Employer");
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		placeOptions = new JList<String>(listModel);
		listModel.add(listModel.size(), "None");
		int i = 0;
		for(Place bankPlace:c.cityObject.cityMap.map.get("Bank")){
			listModel.add(listModel.size(), "Bank "+(i+1));
			i+=1;
		}
		i = 0;
		for(Place marketPlace:c.cityObject.cityMap.map.get("Market")){
			listModel.add(listModel.size(), "Market "+(i+1));
			i+=1;
		}
		i = 0;
		for(Place restaurantPlace:c.cityObject.cityMap.map.get("Restaurant")){
			listModel.add(listModel.size(), "Restaurant "+(i+1));
			i+=1;
		}

		//String [] placeOptions = {"Bank 1","Bank 2","Bank 3","Bank 4","Bank 5"};
		//placesToWork = new JList(placeOptions);
		placeOptions.addListSelectionListener(this);
		JScrollPane placesS = new JScrollPane(placeOptions);
		Dimension ld = new Dimension(150,60);
		//placesS.setMinimumSize(ld);
		placesS.setPreferredSize(ld);
		//placesS.setMaximumSize(ld);
		/*scrollPlacesToWork.setMinimumSize(ld);
		scrollPlacesToWork.setPreferredSize(ld);
		scrollPlacesToWork.add(placesToWork);*/
		placeOfWorkPanel.setLayout(new FlowLayout());
		placeOfWorkPanel.add(placeOfWorkLabel);
		placeOfWorkPanel.add(placesS);

		//JobType Panel
		JPanel jobTypePanel = new JPanel();
		JLabel jobTypeLabel = new JLabel("Job");
		listModel2 = new DefaultListModel<String>();
		jobsOptions = new JList<String>(listModel2);
		//String [] jobOptions = {"Market Employee","Bank 2","Bank 3","Bank 4","Bank 5"};
		//typesOfJobs = new JList(jobOptions);
		//jobsOptions.addListSelectionListener(this);
		JScrollPane jobsS = new JScrollPane(jobsOptions);

		Dimension d2 = new Dimension(150,60);
		jobsS.setMinimumSize(d2);
		jobsS.setPreferredSize(d2);
		jobsS.setMaximumSize(d2);
		//JScrollPane jobsS = new JScrollPane(typesOfJobs);
		/*jobTypePanel.setLayout(new FlowLayout());
		jobTypePanel.add(jobTypeLabel);
		jobTypePanel.add(jobsS);*/
		placeOfWorkPanel.setLayout(new FlowLayout());
		placeOfWorkPanel.add(jobTypeLabel);
		placeOfWorkPanel.add(jobsS);


		//ShiftTimes Panel
		JPanel shiftTimes = new JPanel();
		JLabel shiftStartLabel = new JLabel("Shift start");
		JLabel shiftEndLabel = new JLabel("Shift end");
		shiftStartField = new JTextField();
		shiftEndField = new JTextField();
		Dimension shiftDim = new Dimension(75,20);

		shiftStartField.setPreferredSize(shiftDim);
		shiftEndField.setPreferredSize(shiftDim);
		shiftTimes.setLayout(new FlowLayout());
		shiftTimes.add(shiftStartLabel);
		shiftTimes.add(shiftStartField);
		shiftTimes.add(shiftEndLabel);
		shiftTimes.add(shiftEndField);

		//House Panel
		JPanel housePanel = new JPanel();
		JLabel houseLabel = new JLabel("House");
		DefaultListModel<String> listModel3 = new DefaultListModel<String>();
		houseOptions = new JList<String>(listModel3);
		listModel3.add(listModel3.size(), "None");
		i = 0;
		for(Place housePlace:c.cityObject.cityMap.map.get("House")){
			listModel3.add(listModel3.size(), "House "+(i+1));
			i+=1;
		}

		//String [] placeOptions = {"Bank 1","Bank 2","Bank 3","Bank 4","Bank 5"};
		//placesToWork = new JList(placeOptions);
		//houseOptions.addListSelectionListener(this);
		JScrollPane housesS = new JScrollPane(houseOptions);
		Dimension d4 = new Dimension(150,60);
		//placesS.setMinimumSize(ld);
		housesS.setPreferredSize(d4);
		//placesS.setMaximumSize(ld);
		/*scrollPlacesToWork.setMinimumSize(ld);
		scrollPlacesToWork.setPreferredSize(ld);
		scrollPlacesToWork.add(placesToWork);*/
		housePanel.setLayout(new FlowLayout());
		housePanel.add(houseLabel);
		housePanel.add(housesS);

		//Creation Action
		creationPanel = new JPanel();
		creationPanel.setSize(200,30);
		add = new JButton("Add");
		creationPanel.add(add);

		//ErrorField
		JPanel errorPanel = new JPanel();
		errorField = new JTextField();
		errorField.setEditable(false);
		Dimension errorDim = new Dimension(400,20);
		errorField.setPreferredSize(errorDim);
		errorPanel.add(errorField);


		getContentPane().setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.Y_AXIS));
		add(namePanel);
		//add(scrollPlacesToWork);
		//add(placesS);
		add(placeOfWorkPanel);
		//add(jobsS);
		//add(jobTypePanel);
		add(shiftTimes);
		add(housePanel);
		add(creationPanel);
		add(errorPanel);

		//pack();
		setVisible(true);
		//validate();


		add.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if(e.getSource().equals(add)){
			if(addNewPerson()){
				this.dispose();
			}

		}

	}

	private boolean addNewPerson(){
		String name = nameField.getText();
		if(name.length()==0){
			errorField.setText("Please enter a name");
			return false;
		}
		if(name.equals("Bus Ride")){
			c.busRideScenario();
			return true;
		}
		if(name.equals("Bank")){
			c.bankScenario();
			return true;
		}
		if(name.equals("Market")){
			c.marketScenario();
			return true;
		}
		if(name.equals("Busy")){
			c.busyScenario();
			return true;
		}
		if(name.contains("Drive")){
			if(name.contains("1")){
				c.driveScenario(1);
				return true;
			}
			else if(name.contains("2")){
				c.driveScenario(2);
				return true;
			}
		}
		if(name.equals("RestaurantSimon")){
			c.restaurantSimonScenario();
		}
		if(name.contains("Restaurant")){
			c.restaurantScenario(name);
			return true;
		}
		if(name.contains("JScenario")){
			c.jScenario();
			return true;
		}
		if(placeOptions.getSelectedValue()==null){
			errorField.setText("Please select an employer");
			return false;
		}
		if(!placeOptions.getSelectedValue().toString().equals("None") &&
				jobsOptions.getSelectedValue() == null){
			errorField.setText("Please select a job");
			return false;
		}
		if(!placeOptions.getSelectedValue().toString().equals("None")&& 
				(shiftStartField.getText().length()==0 || 
				shiftEndField.getText().length()==0)){

			errorField.setText("Please specify shift start and end times");
			return false;
		}
		if(houseOptions.getSelectedValue()==null){
			errorField.setText("Please select a house");
			return false;
		}




		PersonAgent person  = new PersonAgent(name,c.cityObject.cityMap);

		//extract the place of work from the panel
		Place placeOfWorkMapLoc;
		PlaceOfWork placeOfWork;
		if(placeOptions.getSelectedValue().toString().equals("None")){
			placeOfWorkMapLoc = null;
			placeOfWork = null;
		}
		else{
			String [] split = placeOptions.getSelectedValue().toString().split(" ");
			int employmentBuildingNum = Integer.parseInt(split[split.length-1]);
			placeOfWorkMapLoc = c.cityObject.cityMap.map.get(split[0]).get(employmentBuildingNum-1);
			if(placeOfWorkMapLoc instanceof BankMapLoc){
				placeOfWork = ((BankMapLoc) placeOfWorkMapLoc).bank;
			}
			else if(placeOfWorkMapLoc instanceof MarketMapLoc){
				placeOfWork = ((MarketMapLoc) placeOfWorkMapLoc).market;
			}
			else if(placeOfWorkMapLoc instanceof RestaurantMapLoc){
				placeOfWork = ((RestaurantMapLoc) placeOfWorkMapLoc).restaurant;

			}
			else{
				placeOfWork = null;
//				System.err.println("Something weird is going on in the placeOfWork section of "
//						+ "person creation.");

			}
		}




		//extract the job from the panel
		JobType jobType = null;
		System.out.println("PCP: "+jobsOptions.getSelectedValue());
		if(jobsOptions.getSelectedValue()==null){
			jobType = JobType.NOTSELECTED;
		}
		else{
			String parsedJobName = jobsOptions.getSelectedValue().toString().replace(" ", "");
			for(JobType jt : JobType.values()){
				//System.out.println(jt.toString());
				if(parsedJobName.equalsIgnoreCase(jt.name())){
					jobType = jt;
				}	
			}
		}
		if(jobType==null){
			jobType = JobType.NOTSELECTED;
		}

		//System.out.println(jobType.toString());
		int start;
		int end;
		if(jobType != JobType.NOTSELECTED){
			start = Integer.parseInt(shiftStartField.getText());
			end = Integer.parseInt(shiftEndField.getText());
		}
		else{
			start = 0;
			end = 0;
		}

		//see if we need to put the person in a house
		if(!houseOptions.getSelectedValue().equals("None")){
			String [] split = houseOptions.getSelectedValue().toString().split(" ");
			int houseNum = Integer.parseInt(split[split.length-1]);
			HouseMapLoc houseMapLoc = (HouseMapLoc) c.cityObject.cityMap.map.get(split[0]).get(houseNum-1);
			person.setHouse(houseMapLoc.house);
		}


		person.setJob(placeOfWork, jobType, start, end);
		person.setBank((int) Math.floor(c.cityObject.cityMap.map.get("Bank").size()*Math.random()));

		c.addNewPerson(person);
		return true;

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(placeOptions.getSelectedValue().toString().equals("None")){
			listModel2.clear();
			return;
		}
		if(placeOptions.getSelectedValue().toString().contains("Bank")){
			listModel2.clear();
			listModel2.add(0,"Bank Teller");
			return;
		}
		if(placeOptions.getSelectedValue().toString().contains("Market")){
			listModel2.clear();
			listModel2.add(listModel2.size(),"Market Host");
			listModel2.add(listModel2.size(),"Market Cashier");
			listModel2.add(listModel2.size(),"Market Employee");
			listModel2.add(listModel2.size(),"Market Delivery Man");
			return;
		}
		if(placeOptions.getSelectedValue().toString().contains("Restaurant")){
			String name =  placeOptions.getSelectedValue().toString();
			int l = name.length();
			int n = Integer.parseInt(name.substring(l-1,l)) - 1;
			if(((RestaurantMapLoc) c.cityObject.cityMap.map.get("Restaurant").get(n)).restaurant instanceof RestaurantGabe){

				listModel2.clear();
				listModel2.add(listModel2.size(),"Restaurant Host");
				listModel2.add(listModel2.size(),"Restaurant Cashier");
				listModel2.add(listModel2.size(),"Restaurant Gabe Waiter1");
				listModel2.add(listModel2.size(),"Restaurant Gabe Waiter2");
				listModel2.add(listModel2.size(),"Restaurant Cook");
				return;
			}
			if(((RestaurantMapLoc) c.cityObject.cityMap.map.get("Restaurant").get(n)).restaurant instanceof RestaurantLinda){

				listModel2.clear();
				listModel2.add(listModel2.size(),"Restaurant Host");
				listModel2.add(listModel2.size(),"Restaurant Cashier");
				listModel2.add(listModel2.size(),"Restaurant Linda Waiter1");
				listModel2.add(listModel2.size(),"Restaurant Linda Waiter2");
				listModel2.add(listModel2.size(),"Restaurant Cook");
				return;
			}
			if(((RestaurantMapLoc) c.cityObject.cityMap.map.get("Restaurant").get(n)).restaurant instanceof RestaurantYocca){

				listModel2.clear();
				listModel2.add(listModel2.size(),"Restaurant Host");
				listModel2.add(listModel2.size(),"Restaurant Cashier");
				listModel2.add(listModel2.size(),"Restaurant Yocca Waiter1");
				listModel2.add(listModel2.size(),"Restaurant Yocca Waiter2");
				listModel2.add(listModel2.size(),"Restaurant Cook");
				return;
			}

			if(((RestaurantMapLoc) c.cityObject.cityMap.map.get("Restaurant").get(n)).restaurant instanceof RestaurantParker){
				
				listModel2.clear();
				listModel2.add(listModel2.size(),"Restaurant Host");
				listModel2.add(listModel2.size(),"Restaurant Cashier");
				listModel2.add(listModel2.size(),"Restaurant Parker Waiter1");
				listModel2.add(listModel2.size(),"Restaurant Parker Waiter2");
				listModel2.add(listModel2.size(),"Restaurant Cook");
				return;
			}
			

			if(((RestaurantMapLoc) c.cityObject.cityMap.map.get("Restaurant").get(n)).restaurant instanceof RestaurantSimon){

				listModel2.clear();
				listModel2.add(listModel2.size(),"Restaurant Host");
				listModel2.add(listModel2.size(),"Restaurant Cashier");
				listModel2.add(listModel2.size(),"Restaurant Simon Waiter1");
				listModel2.add(listModel2.size(),"Restaurant Simon Waiter2");
				listModel2.add(listModel2.size(),"Restaurant Cook");
				return;
			}



		}

	}




}
