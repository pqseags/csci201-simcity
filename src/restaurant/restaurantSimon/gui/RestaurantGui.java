//package restaurant.gui;
//
//import restaurant.CustomerAgent;
//import restaurant.WaiterAgent;
//import restaurant.restaurantSimon.RestaurantSimon;
//
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.event.*;
///**
// * Main GUI class.
// * Contains the main frame and subsequent panels
// */
//public class RestaurantGui extends JFrame implements ActionListener {
//	/* The GUI has two frames, the control frame (in variable gui) 
//	 * and the animation frame, (in variable animationFrame within gui)
//	 */
//	JFrame animationFrame = new JFrame("Restaurant Animation");
//	AnimationPanel animationPanel = new AnimationPanel();
//
//	/* restPanel holds 2 panels
//	 * 1) the staff listing, menu, and lists of current customers all constructed
//	 *    in RestaurantPanel()
//	 * 2) the infoPanel about the clicked Customer (created just below)
//	 */    
//	private JPanel controlPanel=new JPanel();
//	private RestaurantSimon restPanel = new RestaurantSimon(this);
//
//	/* infoPanel holds information about the clicked customer, if there is one*/
//	private JPanel infoPanel;
//	private JLabel infoLabel; //part of infoPanel
//	private JCheckBox stateCB;//part of infoLabel
//
//	private JPanel idPanel;
//	private JLabel idLabel;
//
//	private Object currentPerson;/* Holds the agent that the info is about.
//    								Seems like a hack */
//
//	/**
//	 * Constructor for RestaurantGui class.
//	 * Sets up all the gui components.
//	 */
//	public RestaurantGui() {
//		int WINDOWX = 1000;
//		int WINDOWY = 600;
//
//		/*animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
//		animationFrame.setVisible(true);
//		animationFrame.add(animationPanel); 
//		 */
//		setBounds(50, 50, WINDOWX, WINDOWY);
//
//		setLayout(new BorderLayout());
//
//		Dimension restDim = new Dimension((int)(WINDOWX*.5), (int) (WINDOWY * .5));
//		restPanel.setPreferredSize(restDim);
//		restPanel.setMinimumSize(restDim);
//		restPanel.setMaximumSize(restDim);
//		controlPanel.add(restPanel,BorderLayout.NORTH);
//
//
//		// Now, setup the info panel
//		Dimension infoDim = new Dimension((int)(WINDOWX*.5), (int) (WINDOWY * .15));
//		infoPanel = new JPanel();
//		infoPanel.setPreferredSize(infoDim);
//		infoPanel.setMinimumSize(infoDim);
//		infoPanel.setMaximumSize(infoDim);
//		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
//
//
//
//		stateCB = new JCheckBox();
//		stateCB.setVisible(false);
//		stateCB.addActionListener(this);
//
//		infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
//
//		infoLabel = new JLabel(); 
//		infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
//
//
//		infoPanel.add(infoLabel);
//		infoPanel.add(stateCB);
//		//controlPanel.add(infoPanel,BorderLayout.CENTER);
//		add(infoPanel,BorderLayout.SOUTH);
//
//		// Now setup the ID panel
//		Dimension idDim = new Dimension((int)(WINDOWX*.5), (int) (WINDOWY * .35));
//		idPanel = new JPanel();
//		idPanel.setPreferredSize(idDim);
//		idPanel.setMinimumSize(idDim);
//		idPanel.setMaximumSize(idDim);
//		idPanel.setBorder(BorderFactory.createTitledBorder("ID"));
//
//		JLabel imageLabel;
//
//		idLabel = new JLabel(); 
//		idLabel.setText("<html><pre><i>Fangli Hou</i></pre></html>");
//		idPanel.add(idLabel);
//		ImageIcon image;
//		image=new ImageIcon("C:\\Users\\Simon Hou\\Desktop\\Small-Talk-image.jpg");
//		imageLabel= new JLabel(image);
//		idPanel.add(imageLabel);
//
//
//		//controlPanel.add(idPanel,BorderLayout.SOUTH);
//		//add(idPanel,BorderLayout.SOUTH);
//
//		add(controlPanel,BorderLayout.WEST);
//		animationPanel.setPreferredSize(restDim);
//		animationPanel.setMinimumSize(restDim);
//		animationPanel.setMaximumSize(restDim);
//		add(animationPanel,BorderLayout.EAST);
//
//
//	}
//	/**
//	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
//	 * changes the information panel to hold that person's info.
//	 *
//	 * @param person customer (or waiter) object
//	 */
//	public void updateInfoPanel(Object person) {
//		stateCB.setVisible(true);
//		currentPerson = person;
//
//		if (person instanceof CustomerRole) {
//			CustomerRole customer = (CustomerRole) person;
//			stateCB.setText("Hungry?");
//			//Should checkmark be there? 
//			stateCB.setSelected(customer.getGui().isHungry());
//			//Is customer hungry? Hack. Should ask customerGui
//			stateCB.setEnabled(!customer.getGui().isHungry());
//			// Hack. Should ask customerGui
//			infoLabel.setText(
//					"<html><pre>     Name: " + customer.getName() + " </pre></html>");
//		}
//		if(person instanceof WaiterRole){
//			WaiterRole waiter = (WaiterRole) person;
//			stateCB.setText("On Break?");
//			stateCB.setSelected(waiter.isOnBreak());
//			stateCB.setEnabled(!waiter.isOnBreak());
//			infoLabel.setText("<html><pre>     Name: " + waiter.getName() + " </pre></html>");
//			
//		}
//		infoPanel.validate();
//	}
//	/**
//	 * Action listener method that reacts to the checkbox being clicked;
//	 * If it's the customer's checkbox, it will make him hungry
//	 * For v3, it will propose a break for the waiter.
//	 */
//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == stateCB) {
//			if (currentPerson instanceof CustomerRole) {
//				CustomerRole c = (CustomerRole) currentPerson;
//				c.getGui().setHungry();
//				stateCB.setEnabled(false);
//			}
//			if (currentPerson instanceof WaiterRole) {
//				WaiterRole w = (WaiterRole) currentPerson;
//				w.IWantBreak();;
//				stateCB.setEnabled(false);
//			}
//		}
//	}
//	/**
//	 * Message sent from a customer gui to enable that customer's
//	 * "I'm hungry" checkbox.
//	 *
//	 * @param c reference to the customer
//	 */
//	public void setCustomerEnabled(CustomerRole c) {
//		if (currentPerson instanceof CustomerRole) {
//			CustomerRole cust = (CustomerRole) currentPerson;
//			if (c.equals(cust)) {
//				stateCB.setEnabled(true);
//				stateCB.setSelected(false);
//			}
//		}
//	}
//	/**
//	 * Main routine to get gui started
//	 */
//	public static void main(String[] args) {
//		RestaurantGui gui = new RestaurantGui();
//		gui.setTitle("csci201 Restaurant");
//		gui.setVisible(true);
//		gui.setResizable(false);
//		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
//}
