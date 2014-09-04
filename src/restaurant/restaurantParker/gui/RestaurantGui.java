//
//package restaurant.restaurantParker.gui;
//
//
//import restaurant.restaurantParker.CustomerRole;
//import restaurant.restaurantParker.WaiterRole;
//
//import javax.swing.*;
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
//	JFrame animationFrame = new JFrame("Restaurant Animation");
//	AnimationPanel animationPanel = new AnimationPanel();
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
//    private JLabel infoLabel; //part of infoPanel
//    private JCheckBox stateCB;//part of infoLabel
//
//    private Object currentPerson;/* Holds the agent that the info is about.
//    								Seems like a hack */
//
//    /**
//     * Constructor for RestaurantGui class.
//     * Sets up all the gui components.
//     */
//    public RestaurantGui() {
//        int WINDOWX = 450; //450
//        int WINDOWY = 350;	//350
//
//    /*    
//        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
//        animationFrame.setVisible(true);
//    	animationFrame.add(animationPanel); 
//   	*/
//    	setBounds(50, 50, (int)(WINDOWX*1.5), WINDOWY*2);
//
//        setLayout(new BoxLayout((Container) getContentPane(), 
//        		BoxLayout.Y_AXIS));
//
//        
//        Dimension restDim = new Dimension((int)(WINDOWX*1.4), (int) (WINDOWY * .6));
//        restPanel.setPreferredSize(restDim);
//        restPanel.setMinimumSize(restDim);
//        restPanel.setMaximumSize(restDim);
//        add(restPanel);
//        
// 
//        
//        // Now, setup the info panel
//        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
//        infoPanel = new JPanel();
//        infoPanel.setPreferredSize(infoDim);
//        infoPanel.setMinimumSize(infoDim);
//        infoPanel.setMaximumSize(infoDim);
//        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
//
//        stateCB = new JCheckBox();
//        stateCB.setVisible(false);
//        stateCB.addActionListener(this);
//
//        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
//        
//        infoLabel = new JLabel(); 
//  
//       // infoLabel.setText("Type a name of a new customer or waiter, then click add.");
//        infoLabel.setText("<html><pre><i>Add new people!</i></pre></html>");
//        infoPanel.add(infoLabel);
//        infoPanel.add(stateCB);
//        add(infoPanel);
//        
//        
//     
//        
//        Dimension animDim = new Dimension(WINDOWX, WINDOWY );
//        animationPanel.setPreferredSize(animDim);
//        animationPanel.setMinimumSize(animDim);
//        animationPanel.setMaximumSize(animDim);
//        animationPanel.setBorder(BorderFactory.createTitledBorder("Animation"));
//        add(animationPanel);
//    }
//    /**
//     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
//     * changes the information panel to hold that person's info.
//     *
//     * @param person customer (or waiter) object
//     */
//    public void updateInfoPanel(Object person) {
//        stateCB.setVisible(true);
//        currentPerson = person;
//
//        if (person instanceof CustomerRole) {
//            CustomerRole customer = (CustomerRole) person;
//            stateCB.setText("Hungry?");
//          //Should checkmark be there? 
//            stateCB.setSelected(customer.getGui().isHungry());
//          //Is customer hungry? Hack. Should ask customerGui
//            stateCB.setEnabled(!customer.getGui().isHungry());
//          // Hack. Should ask customerGui
//            infoLabel.setText(
//               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
//        }
//        
//        if (person instanceof WaiterRole){
//        	WaiterRole waiter = (WaiterRole)person;
//        	stateCB.setText("On Break?");
//        	stateCB.setSelected(waiter.getGui().isOnBreak());
//     
//        	stateCB.setEnabled(true);
//        	infoLabel.setText( "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
//        }
//        
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
//            
//            if (currentPerson instanceof WaiterRole){
//            	
//            	if (stateCB.isSelected()){
//            		WaiterRole w = (WaiterRole) currentPerson;
//            		w.getGui().setOnBreak();
//            	}
//            	
//            	else if (!stateCB.isSelected()){
//            		WaiterRole w = (WaiterRole) currentPerson;
//            		w.getGui().setOffBreak();
//            	}
//         
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
//            CustomerRole cust = (CustomerRole) currentPerson;
//            if (c.equals(cust)) {
//                stateCB.setEnabled(true);
//                stateCB.setSelected(false);
//            }
//        }
//    }
//    
//    public void setWaiterOffBreak(WaiterRole w){
//    	if (currentPerson instanceof WaiterRole){
//    		WaiterRole wait = (WaiterRole) currentPerson;
//    		if (w.equals(wait)){
//    			stateCB.setSelected(false);
//    		//	stateCB.setEnabled(true);
//    		}
//    	}
//    }
//    
//    public void setWaiterAskedBreak(WaiterRole w){
//     	if (currentPerson instanceof WaiterRole){
//    		WaiterRole wait = (WaiterRole) currentPerson;
//    		if (w.equals(wait)){
//    		//	stateCB.setEnabled(false);
//    		}
//    	}
//    }
//   
//    
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
