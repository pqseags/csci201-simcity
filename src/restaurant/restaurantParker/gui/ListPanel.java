//package restaurant.restaurantParker.gui;
//
//import restaurant.restaurantParker.CustomerRole;
//import restaurant.restaurantParker.HostRole;
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
//public class ListPanel extends JPanel implements ActionListener, KeyListener {
//
//    public JScrollPane pane =
//            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//    private JPanel view = new JPanel();
//    private List<JButton> list = new ArrayList<JButton>();
//    private JButton addPersonB = new JButton("Add");
//    
//    //lab2
//    private JTextField newCustomerField = new JTextField();
//    private JCheckBox hungryBox = new JCheckBox();
//
//
//    private RestaurantPanel restPanel;
//    private String type;
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
//        //lab2
////        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
//        setLayout(new GridLayout(5,1,5,5));
//        
//        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
//        
//        newCustomerField.addActionListener(this);
//        newCustomerField.addKeyListener(this);
//        add(newCustomerField);
//        
//        if (this.type == "Customers"){
//        	hungryBox.setText("Hungry?");
//        	//make it only enabled when something has been typed
//        	hungryBox.setEnabled(false);
//        	add(hungryBox);
//        }
//     
//        addPersonB.addActionListener(this);
//        addPersonB.setEnabled(false);
//        add(addPersonB);
//        
//        
//        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
//        pane.setViewportView(view);
//        add(pane);
//        
//    }
//
//    /**
//     * Method from the ActionListener interface.
//     * Handles the event of the add button being pressed
//     */
//    public void actionPerformed(ActionEvent e) {
//       if (e.getSource() == addPersonB) {
//        	// Chapter 2.19 describes showInputDialog()
//           // addPerson(JOptionPane.showInputDialog("Please enter a name:"));
//    	   
//    	   //lab2
//        	String enteredName = newCustomerField.getText();
//       
//        	
//        	Boolean boxChecked = false;
//        	if (this.type == "Customers"){
//        			boxChecked = hungryBox.isSelected();
//        	}
//        	else if (this.type == "Waiters"){
//        			boxChecked = false;
//        	}
//        	
//        	addPerson(enteredName, boxChecked);
//        	
//        }
//        else {
//        	// Isn't the second for loop more beautiful?
//            /*for (int i = 0; i < list.size(); i++) {
//                JButton temp = list.get(i);*/
//        	for (JButton temp:list){
//                if (e.getSource() == temp)
//                    restPanel.showInfo(type, temp.getText());
//            }
//        }
//    }
//
//    /**
//     * If the add button is pressed, this function creates
//     * a spot for it in the scroll pane, and tells the restaurant panel
//     * to add a new person.
//     *
//     * @param name name of new person
//     */
//    public void addPerson(String name, Boolean hunger) {
//        if (name != null) {
//        	
//        	hungryBox.setSelected(false);
//        	hungryBox.setEnabled(false);
//        	addPersonB.setEnabled(false);
//            JButton button = new JButton(name);
//            button.setBackground(Color.white);
//
//            Dimension paneSize = pane.getSize();
//            Dimension buttonSize = new Dimension(paneSize.width - 20,
//                    (int) (paneSize.height / 3));
//            button.setPreferredSize(buttonSize);
//            button.setMinimumSize(buttonSize);
//            button.setMaximumSize(buttonSize);
//            button.addActionListener(this);
//            list.add(button);
//            view.add(button);
//            
//            //lab2
//            restPanel.addPerson(type, name, hunger);//puts customer on list
//            restPanel.showInfo(type, name);//puts hungry button on panel
//            validate();
//        }
//    }
//
//	@Override
//	public void keyTyped(KeyEvent e) {
//		// TODO Auto-generated method stub
//		if (newCustomerField.getText() != null && !newCustomerField.getText().isEmpty())
//			hungryBox.setEnabled(true);
//			addPersonB.setEnabled(true);
//
//		
//	}
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//    
//}
