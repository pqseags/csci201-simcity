//package restaurant.restaurantParker.gui;
//
//import agent.Agent;
//import restaurant.restaurantParker.*;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//
//
///**
// * Panel in frame that contains all the restaurant information,
// * including host, cook, waiters, and customers.
// */
//public class RestaurantPanel extends JPanel implements ActionListener{
//
//
//	private JButton pauseButton = new JButton("Pause");
//	private JButton resumeButton = new JButton ("Resume");
//	private JButton depleteCook = new JButton ("Deplete Cook");
//	private JPanel pauseResumePanel = new JPanel ();
//	
//	//revolving stand
//	private RevolvingStand stand = new RevolvingStand<RevolvingStandOrder>();
//	
//    //Host, cook, waiters and customers
//	private HostRole host = new HostRole("HostA");
//	private CookRole cook = new CookRole("Chef", stand);
//	private CashierRole cashier = new CashierRole("Cashier");
//	private MarketAgent market1 = new MarketAgent("Market1");
//	private MarketAgent market2 = new MarketAgent("Market2");
//	private MarketAgent market3 = new MarketAgent("Market3");
//	
//	private CookGui cookGui = null;
//
//    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
//    private Vector<WaiterRole> waiters = new Vector<WaiterRole>();
//    
//    private List<Agent> agents
//	= new ArrayList<Agent>();
//
//    private JPanel restLabel = new JPanel();
//    private ListPanel customerPanel = new ListPanel(this, "Customers");
//    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
//    private JPanel group = new JPanel();
//
//    private RestaurantGui gui; //reference to main gui
//
//    public RestaurantPanel(RestaurantGui gui) {
//        this.gui = gui;
//        
//     
//        host.startThread();
//        agents.add(host);
//        
//  
//        cook.startThread();
//        agents.add(cook);
//        cookGui = new CookGui(cook);
//        this.gui.animationPanel.addGui(cookGui);
//        cook.setGui(cookGui);
//        
//        
//        cashier.startThread();
//        agents.add(cashier);
//        
//        cook.addMarket(market1);
//        market1.setCook(cook);
//        market1.setCashier(cashier);
//        market1.startThread();
//        agents.add(market1);
//        
//        cook.addMarket(market2);
//        market2.setCook(cook);
//        market2.setCashier(cashier);
//        market2.startThread();
//        agents.add(market2);
//        
//        cook.addMarket(market3);
//        market3.setCook(cook);
//        market3.setCashier(cashier);
//        market3.startThread();
//        agents.add(market3);
//        
//        
//        
//        setLayout(new GridLayout(1, 2, 20, 20));
//        group.setLayout(new GridLayout(1, 2, 10, 10));
//
//     //   group.add(customerPanel);
//     //   group.add(waiterPanel);
//
//        initRestLabel();
//        add(restLabel);
//        
//       // add(group);
//        add(customerPanel);
//        add(waiterPanel);
//        
//        pauseButton.addActionListener(this);
//        resumeButton.addActionListener(this);
//        depleteCook.addActionListener(this);
//        pauseResumePanel.setLayout(new GridLayout(3,1));
//        pauseResumePanel.add(pauseButton);
//        pauseResumePanel.add(resumeButton);
//        pauseResumePanel.add(depleteCook);
//        add(pauseResumePanel);
//         
//      
//         
//    }
//
//    /**
//     * Sets up the restaurant label that includes the menu,
//     * and host and cook information
//     */
//    private void initRestLabel() {
//        JLabel label = new JLabel();
//        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
//      //  restLabel.setLayout(new GridLayout(3,1));
//        label.setText(
//                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$40</td></tr><tr><td>Chicken</td><td>$30</td></tr><tr><td>Pizza</td><td>$20</td></tr><tr><td>Salad</td><td>$10</td></tr></table><br></html>");
//
//        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
//        restLabel.add(label);
//  
//        
//     //   restLabel.add(new JLabel("               "), BorderLayout.EAST);
//      //  restLabel.add(new JLabel("               "), BorderLayout.WEST);
//    }
//
//    /**
//     * When a customer or waiter is clicked, this function calls
//     * updatedInfoPanel() from the main gui so that person's information
//     * will be shown
//     *
//     * @param type indicates whether the person is a customer or waiter
//     * @param name name of person
//     */
//    public void showInfo(String type, String name) {
//
//        if (type.equals("Customers")) {
//
//            for (int i = 0; i < customers.size(); i++) {
//                CustomerRole temp = customers.get(i);
//                if (temp.getName() == name)
//                    gui.updateInfoPanel(temp);
//            }
//        }
//        
//        if (type.equals("Waiters")){
//        	 for (int i = 0; i < waiters.size(); i++) {
//                 WaiterRole temp = waiters.get(i);
//                 if (temp.getName() == name)
//                     gui.updateInfoPanel(temp);
//             }
//        }
//    }
//
//    /**
//     * Adds a customer or waiter to the appropriate list
//     *
//     * @param type indicates whether the person is a customer or waiter (later)
//     * @param name name of person
//     */
//    public void addPerson(String type, String name, Boolean hunger) {
//
//    	if (type.equals("Customers")) {
//    		CustomerRole c = new CustomerRole(name);	
//    		CustomerGui g = new CustomerGui(c);
//
//    		gui.animationPanel.addGui(g);// dw
//    		c.setCashier(cashier);
//    		c.setHost(host);
//    		c.setGui(g);
//    		
//    		//lab2
//    		if(hunger)
//    			c.getGui().setHungry();
//    		 
//    		customers.add(c);
//    		agents.add(c);
//    		c.startThread();
//    	}
//    	
//    	if (type.equals("Waiters")) {
//    		
//    		WaiterRole w = new WaiterPCRole(name);
//    		host.addWaiter(w);
//    		waiters.add(w);
//    		agents.add(w);
//    		
//    		w.setCook(cook); 
//    		w.setHost(host); 
//    		w.setCashier(cashier);
// 
//
//    		int position = waiters.size();
//    		
//    		WaiterGui g = new WaiterGui(w, position); 
//    		
//    		gui.animationPanel.addGui(g);
//       		w.setGui(g);  
//       	
//
//
//
//    		w.startThread();
//    		
//    	}
//    	
//    }
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
////		if (e.getSource() == pauseButton){
////			for (Agent agent : agents){
////				agent.pauseThread();
////				System.out.println("pausing an agent");
////			}
////		}
////		
////		if (e.getSource() == resumeButton){
////			for (Agent agent : agents){
////				agent.restartThread();
////			}
////		}
//		
//		if (e.getSource() == depleteCook){
//			cook.msgDepleteInventory();
//			System.out.println("Depleting the Cook's Inventory");
//		}
//		
//	}
//
//}
