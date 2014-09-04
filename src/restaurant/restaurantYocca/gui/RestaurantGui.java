//package restaurant.restaurantYocca.gui;
//
//import restaurant.restaurantYocca.CustomerRole;
//import restaurant.restaurantYocca.WaiterRole;
//import interfaces.restaurantYocca.Customer;
//import interfaces.restaurantYocca.Waiter;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.event.*;
///**
// * Main GUI class.
// * Contains the main frame and subsequent panels
// */
//public class RestaurantGui extends JFrame implements ActionListener {
//    /* The GUI has two frames, the control frame (in variable gui)
//     * and the animation frame, (in variable animationFrame within gui)
//     */
//	//JFrame animationFrame = new JFrame("Restaurant Animation");
////	AnimationPanel animationPanel = new AnimationPanel();
//	
//    /* restPanel holds 2 panels
//     * 1) the staff listing, menu, and lists of current customers all constructed
//     *    in RestaurantPanel()
//     * 2) the infoPanel about the clicked Customer (created just below)
//     */
//    private RestaurantPanel restPanel = new RestaurantPanel(this);
//    
//    /* infoPanel holds information about the clicked customer, if there is one*/
//    private JPanel infoPanel;
//    private JPanel testPanel;
//    public JLabel infoLabel; //part of infoPanel
//    public JLabel market1Label;
//    public JLabel market2Label;
//    public JLabel market3Label;
//    private JCheckBox stateCB;//part of infoLabel
//    public JCheckBox waiterBreakCB;
//    
//    private static final int infoLayoutRows = 1;
//    private static final int infoLayoutColumns = 2;
//    private static final int infoLayoutHGap = 30;
//    private static final int infoLayoutVGap = 0;
//    
//    private static final int xValue = 50;
//    private static final int yValue = 50;
//    
//    private Object currentPerson;/* Holds the agent that the info is about.
//                                  Seems like a hack */
//    
//    /**
//     * Constructor for RestaurantGui class.
//     * Sets up all the gui components.
//     */
//    public RestaurantGui() {
//        int WINDOWX = 450;
//        int WINDOWY = 350;
//        
//        Dimension animationDim = new Dimension(WINDOWX, (int) (WINDOWY));
//        animationPanel.setPreferredSize(animationDim);
//        animationPanel.setMinimumSize(animationDim);
//        animationPanel.setMaximumSize(animationDim);
//    	//animationPanel.add(animationPanel);
//    	add(animationPanel);
//    	
//    	setBounds(xValue, yValue, WINDOWX+100, WINDOWY+700);
//        
//        setLayout(new BoxLayout((Container) getContentPane(),
//                                BoxLayout.Y_AXIS));
//        
//        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
//        restPanel.setPreferredSize(restDim);
//        restPanel.setMinimumSize(restDim);
//        restPanel.setMaximumSize(restDim);
//        add(restPanel);
//        
//        // Now, setup the info panel
//        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
//        infoPanel = new JPanel();
//        infoPanel.setPreferredSize(infoDim);
//        infoPanel.setMinimumSize(infoDim);
//        infoPanel.setMaximumSize(infoDim);
//        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
//        
//        Dimension testDim = new Dimension(WINDOWX, (int) (WINDOWY * .4));
//        testPanel = new JPanel();
//        testPanel.setPreferredSize(testDim);
//        testPanel.setMinimumSize(testDim);
//        testPanel.setMaximumSize(testDim);
//        testPanel.setBorder(BorderFactory.createTitledBorder("Markets"));
//        
//        stateCB = new JCheckBox();
//        stateCB.setVisible(false);
//        stateCB.addActionListener(this);
//        
//        waiterBreakCB = new JCheckBox();
//        waiterBreakCB.setVisible(false);
//        waiterBreakCB.addActionListener(this);
//        
//        
//        infoPanel.setLayout(new GridLayout(infoLayoutRows, infoLayoutColumns, infoLayoutHGap-10, infoLayoutVGap));
//        
//        infoLabel = new JLabel();
//        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
//        infoPanel.add(infoLabel);
//        
//        market1Label = new JLabel();
//        market1Label.setText("<html><pre>Ralph's -- Steak: " + 3 + " Chicken: " + 3 + "  Salad: " + 3 + "  Pizza: " + 3 + "</pre></html>");
//
//        market2Label = new JLabel();
//        market2Label.setText("<html><pre>Costco -- Steak: " + 3 + " Chicken: " + 3 + "  Salad: " + 3 + "  Pizza: " + 3 + "</pre></html>");
//        market3Label = new JLabel();
//        market3Label.setText("<html><pre>Trader J's -- Steak: " + 3 + " Chicken: " + 3 + "  Salad: " + 3 + "  Pizza: " + 3 + "</pre></html>");
//        testPanel.add(market1Label);
//        testPanel.add(market2Label);
//        testPanel.add(market3Label);
//        add(infoPanel);
//        add(testPanel);
//     
//    }
//    /**
//     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
//     * changes the information panel to hold that person's info.
//     *
//     * @param person customer (or waiter) object
//     */
//    public void updateInfoPanel(Object person) {
//        currentPerson = person;
//        
//        if (person instanceof CustomerRole) {
//        	if (waiterBreakCB.isVisible()) {
//        		infoPanel.remove(waiterBreakCB);           	
//            	waiterBreakCB.setVisible(false);
//        	}
//        	infoPanel.add(stateCB);
//            stateCB.setVisible(true);
//            CustomerRole customer = (CustomerRole) person;
//            stateCB.setText("Hungry?");
//            //Should checkmark be there?
//            stateCB.setSelected(customer.getGui().isHungry());
//            //Is customer hungry? Hack. Should ask customerGui
//            stateCB.setEnabled(!(customer.getGui()).isHungry());
//        	if (customer.getName().equals(""))
//        	{
//        		stateCB.setEnabled(false);
//        	}
//            // Hack. Should ask customerGui
//            infoLabel.setText(
//                              "<html><pre> C: " + customer.getName() + " ($" + customer.getMoney() + ") </pre></html>");
//        }
//        
//        if (person instanceof WaiterRole) {
//            WaiterRole waiter = (WaiterRole) person;
//        	if (stateCB.isVisible()) {
//        		infoPanel.remove(stateCB);           	
//            	stateCB.setVisible(false);
//        	}
//        	infoPanel.add(waiterBreakCB);
//        	infoPanel.remove(stateCB);
//            waiterBreakCB.setVisible(true);
//            stateCB.setVisible(false);
//            waiterBreakCB.setText("Go On Break?");
//            //Should checkmark be there?
//            waiterBreakCB.setSelected(waiter.isOnBreak());
//            //Is customer hungry? Hack. Should ask customerGui
//        	if (waiter.getName().equals(""))
//        	{
//        		waiterBreakCB.setEnabled(false);
//        	}
//            // Hack. Should ask customerGui
//            infoLabel.setText(
//                              "<html><pre> Waiter: " + waiter.getName() + " </pre></html>");
//        }
//        infoPanel.validate();
//    }
//    /**
//     * Action listener method that reacts to the checkbox being clicked;
//     * If it's the customer's checkbox, it will make him hungry
//     * For v3, it will propose a break for the waiter.
//     */
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == stateCB) {
//            if (currentPerson instanceof CustomerRole) {
//                CustomerRole c = (CustomerRole) currentPerson;
//                c.getGui().setHungry();
//                stateCB.setEnabled(false);
//            }
//        }
//        if (e.getSource() == waiterBreakCB) {
//            if (currentPerson instanceof WaiterRole) {
//                WaiterRole w = (WaiterRole) currentPerson;
//                if (w.isOnBreak()) {
//                	w.setOnBreak(false);
//                }
//                else w.TryGoOnBreak();
//            }
//        }
//    }
//    /**
//     * Message sent from a customer gui to enable that customer's
//     * "I'm hungry" checkbox.
//     *
//     * @param c reference to the customer
//     */
//    public void setCustomerEnabled(CustomerRole c) {
//        if (currentPerson instanceof CustomerRole) {
//            Customer cust = (Customer) currentPerson;
//            if (c.equals(cust)) {
//                stateCB.setEnabled(true);
//                stateCB.setSelected(false);
//            }
//        }
//    }
//    
//    public void setWaiterBreakEnabled(WaiterRole agent) {
//        if (currentPerson instanceof Waiter) {
//            WaiterRole waiter = (WaiterRole) currentPerson;
//            if (agent.equals(waiter)) {
//               	waiterBreakCB.setEnabled(true);
//                waiterBreakCB.setSelected(false);
//            }
//        }
//    }
//    /**
//     * Main routine to get gui started
//     */
//    public static void main(String[] args) {
//        RestaurantGui gui = new RestaurantGui();
//        gui.setTitle("csci201 Restaurant");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//}