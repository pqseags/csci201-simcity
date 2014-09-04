//package restaurant.restaurantYocca.gui;
//
//import restaurant.restaurantYocca.CashierRole;
//import restaurant.restaurantYocca.CookRole;
//import restaurant.restaurantYocca.CustomerRole;
//import restaurant.restaurantYocca.HostRole;
//import restaurant.restaurantYocca.HostRole.MyWaiter;
//import restaurant.restaurantYocca.WaiterRole;
//import interfaces.restaurantYocca.Cashier;
//import interfaces.restaurantYocca.Cook;
//import interfaces.restaurantYocca.Customer;
//import interfaces.restaurantYocca.Host;
//import interfaces.restaurantYocca.Waiter;
//
//import javax.swing.*;
//
//import market.Market;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.util.Vector;
//
///**
// * Panel in frame that contains all the restaurant information,
// * including host, cook, waiters, and customers.
// */
//public class RestaurantPanel extends JPanel {
//    
//    //Host, cook, waiters and customers
//    private WaiterRole waiter = new WaiterRole("Rafael");
//    private WaiterGui waiterGui = new WaiterGui(waiter,null);
//    private HostRole host = new HostRole("Host",null);
//    private CookRole cook = new CookRole("Cook");
//    //private CookGui cookGui = new CookGui(cook);
//    private CashierRole cashier = new CashierRole("Cashier");
//    private Market market1 = new Market();
//    private Market market2 = new Market();
//    private Market market3 = new Market();
//    
//    private Vector<Customer> customers = new Vector<Customer>();
//    private Vector<Waiter> waiters = new Vector<Waiter>();
//    
//    private JPanel restLabel = new JPanel();
//    private ListPanel customerPanel = new ListPanel(this, "Customers");
//    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
//    private JPanel group = new JPanel();
//    
//    private static final int restPanelRows = 1;
//    private static final int restPanelColumns = 2;
//    private static int restPanelHGap = 20;
//    private static int restPanelVGap = 20;
//    
//    private RestaurantGui gui; //reference to main gui
//    
//    public RestaurantPanel(RestaurantGui gui) {
//        this.gui = gui;
//        
//        //gui.animationPanel.addGui(waiterGui);
//      
//        cashier.setAnimationPanel(gui.animationPanel);
//        cook.startThread();  
//        CookGui cookGui = new CookGui(cook,gui);
//        cook.setGui(cookGui);
//        
//        cashier.setWaiter(waiter);
//        cashier.startThread();
//        
//        waiter.setGui(waiterGui);
//        waiter.setHost(host);       
//        waiter.setCook(cook);
//        waiter.setCashier(cashier);
//        waiter.startThread();
//        
//        waiterGui.setWaiter(waiter);
//
//        host.setWaiter(waiter);
//        host.setCashier(cashier);
//        host.setCook(cook);   
//        host.startThread();
//        
//        setLayout(new GridLayout(restPanelRows, restPanelColumns, restPanelHGap, restPanelVGap));
//        restPanelVGap = 10;
//        restPanelHGap = 10;
//        group.setLayout(new GridLayout(restPanelRows, restPanelColumns, restPanelHGap, restPanelVGap)); //was 10,10
//        
//        group.add(customerPanel);
//        group.add(waiterPanel);
//        
//        initRestLabel();
//        add(restLabel);
//        add(group);
//    }
//    
//    /**
//     * Sets up the restaurant label that includes the menu,
//     * and host and cook information
//     */
//    private void initRestLabel() {
//        JLabel label = new JLabel();
//        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
//        restLabel.setLayout(new BorderLayout());
//        label.setText(
//                      "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + waiter.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
//        
//        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
//        restLabel.add(label, BorderLayout.CENTER);
//        restLabel.add(new JLabel("            "), BorderLayout.EAST);
//        restLabel.add(new JLabel("            "), BorderLayout.WEST);
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
//                Customer temp = customers.get(i);
//                if (temp.getName() == name)
//                    gui.updateInfoPanel(temp);
//            }
//        }
//        
//        if (type.equals("Waiters")) {
//            
//            for (int i = 0; i < waiters.size(); i++) {
//                Waiter temp = waiters.get(i);
//                if (temp.getName() == name)
//                    gui.updateInfoPanel(temp);
//            }
//        }
//    }
//    
//    /**
//     * Adds a customer or waiter to the appropriate list
//     *
//     * @param type indicates whether the person is a customer or waiter (later)
//     * @param name name of person
//     */
////    public void addPerson(String type, String name) {
////        
////    	if (type.equals("Customers")) {
////    		CustomerRole c = new CustomerRole(name);
////    		CustomerGui g = new CustomerGui(c, gui);
////            
////    		gui.animationPanel.addGui(g);// dw
////    		c.setWaiter(waiter);
////    		c.setHost(host);
////    		c.setGui(g);
////    		customers.add(c);
////    		c.startThread();
////    	}
////    	
////    	if (type.equals("Waiters")) {
////    		WaiterRole w = new WaiterRole(name);
////			host.addWaiter(w);
////			
////			WaiterGui g = new WaiterGui(w, gui,host);
////	        
////			gui.animationPanel.addGui(g);// dw
////			w.setHost(host);
////			w.setGui(g);
////			w.setCook(cook);
////			w.setCashier(cashier);
////			waiters.add(w);
////			w.startThread();
////		}
////    }
//}
