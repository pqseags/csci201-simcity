//package restaurant.restaurantYocca.gui;
//
//import restaurant.restaurantYocca.CustomerRole;
//import restaurant.restaurantYocca.HostRole;
//import restaurant.restaurantYocca.Table;
//import interfaces.restaurantYocca.Host;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.util.List;
//import java.util.ArrayList;
//
///**
// * Subpanel of restaurantPanel.
// * This holds the scroll panes for the customers and, later, for waiters
// */
//public class ListPanel extends JPanel implements ActionListener {
//    
//    public JScrollPane customerPane =
//    new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//    public JScrollPane waiterPane =
//    	    new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//    	                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//    private JPanel view = new JPanel();
//    private List<JButton> list = new ArrayList<JButton>();
//    private JButton addPersonB = new JButton("Add Customer");
//    private JButton addWaiterB = new JButton("Add Waiter");
//    private JButton pauseButton = new JButton("Pause");
//    private JTextField customerNameTextField = new JTextField(16);
//    private JTextField waiterNameTextField = new JTextField(16);
//    
//    private RestaurantPanel restPanel;
//    private String type;
//    
//    private Host agent = null;
//    
//    /**
//     * Constructor for ListPanel.  Sets up all the gui
//     *
//     * @param rp   reference to the restaurant panel
//     * @param type indicates if this is for customers or waiters
//     */
//    public ListPanel(RestaurantPanel rp, String type) {
//        restPanel = rp;
//        this.type = type;
//        
//        int listLayoutRows = 4;
//        int listLayoutColumns = 2;
//        int listLayoutHGap = 10;
//        int listLayoutVGap = 10;
//        
//        setLayout(new GridLayout(listLayoutRows,listLayoutColumns,listLayoutHGap,listLayoutVGap));        
//        
//        if (type == "Customers") {
//        	addPersonB.addActionListener(this);
//        	pauseButton.addActionListener(this);
//            add(customerNameTextField);
//            add(addPersonB); 
//            
//            customerPane.setViewportView(view);
//            add(customerPane);
//        	add(pauseButton);      	
//        }
//        
//        if (type == "Waiters") {
//        	addWaiterB.addActionListener(this);
//        	add(waiterNameTextField);
//        	add(addWaiterB);
//
//        	waiterPane.setViewportView(view);
//        	add(waiterPane);
// 
//        }
//        
//        view.setLayout(new GridLayout(listLayoutRows,listLayoutColumns));       
//    }
//    
//    /**
//     * Method from the ActionListener interface.
//     * Handles the event of the add button being pressed
//     */
//    
//    public void actionPerformed(ActionEvent e) {
////        if (e.getSource() == addPersonB) {
////        	addPerson(customerNameTextField.getText());
////        }
////        else {
////        	for (JButton temp:list){
////                if (e.getSource() == temp)
////                    restPanel.showInfo(type, temp.getText());
////            }
////        }
////        if (e.getSource() == addWaiterB) {
////        	addWaiter(waiterNameTextField.getText());
////        }
////        if (e.getSource() == pauseButton) {
////        	if (pauseButton.getText().equals("Resume")) {
////        		pauseButton.setText("Pause");
////        		agent.setPaused(false);
////        	}
////        	else if (pauseButton.getText().equals("Pause")) {
////        		pauseButton.setText("Resume");
////        		agent.setPaused(true); 		
////        	}
////        	
////        }
//    }
////    
////    /**
////     * If the add button is pressed, this function creates
////     * a spot for it in the scroll pane, and tells the restaurant panel
////     * to add a new person.
////     *
////     * @param name name of new person
////     */
//    public void addPerson(String name) {
////        if (name != null) {
////            JButton button = new JButton(name);
////            button.setBackground(Color.white);
////            
////            Dimension paneSize = customerPane.getSize();
////            Dimension buttonSize = new Dimension(paneSize.width - 20,
////                                                 (int) (paneSize.height / 2));
////            button.setPreferredSize(buttonSize);
////            button.setMinimumSize(buttonSize);
////            button.setMaximumSize(buttonSize);
////            button.addActionListener(this);
////            list.add(button);
////            view.add(button);
////            restPanel.addPerson(type, name);//puts customer on list
////            restPanel.showInfo(type, name);//puts hungry button on panel
////            validate();
////        }
//    }
////   
//    public void addWaiter(String name) {
////    	if (name != null) {
////            JButton button = new JButton(name);
////            button.setBackground(Color.white);
////            
////            Dimension paneSize = waiterPane.getSize();
////            Dimension buttonSize = new Dimension(paneSize.width - 20,
////                                                 (int) (paneSize.height / 2));
////            button.setPreferredSize(buttonSize);
////            button.setMinimumSize(buttonSize);
////            button.setMaximumSize(buttonSize);
////            button.addActionListener(this);
////            list.add(button);
////            view.add(button);
////            restPanel.addPerson(type, name);//puts waiter on list
////            restPanel.showInfo(type, name);
////            validate();
////    	}
//    }
//
//    
//    public void setHost(Host ag) {
//    	agent = ag;
//    }
//}
