//package restaurant.gui;
//
//import java.awt.Color;
//import java.awt.Container;
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//
//import restaurant.restaurantSimon.RestaurantSimon;
//
//public class WaiterListPanel  extends JPanel implements ActionListener{
//
//	public JScrollPane pane =
//			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//	private JPanel view = new JPanel();
//	private List<JButton> list = new ArrayList<JButton>();
//	private JButton addPersonB = new JButton("Add");
//	private JTextField textField;
//	//private JCheckBox initial;
//	private RestaurantSimon restPanel;
//	private String type;
//	//private JButton OnBreak=new JButton("Put On Break");
//	
//	public static boolean p=false;
//
//	/**
//	 * Constructor for ListPanel.  Sets up all the gui
//	 *
//	 * @param rp   reference to the restaurant panel
//	 * @param type indicates if this is for customers or waiters
//	 */
//	public WaiterListPanel(RestaurantSimon rp, String type) {
//		restPanel = rp;
//		this.type = type;
//
//		setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
//		add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
//
//		addPersonB.addActionListener(this);
//		add(addPersonB);
//
//
//		textField=new JTextField("Add names here");
//		textField.setMaximumSize(new Dimension(250,7));
//		textField.addKeyListener(new KeyListener() {
//
//			@Override
//			public void keyTyped(KeyEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//				// TODO Auto-generated method stub
//				if(textField.getText().length()==0)
//				{
//					//initial.setEnabled(false);
//					addPersonB.setEnabled(false);
//
//				}
//				else
//				{
//
//					//initial.setEnabled(true);
//					addPersonB.setEnabled(true);
//
//
//				}
//			}
//
//
//		});
//		add(textField);
//		//hack!!!! not using the available check now 
//		// initial = new JCheckBox("Available");
//		//  initial.setVisible(true);
//		// add(initial);
//
//		//OnBreak.addActionListener(this);
//		//add(OnBreak);
//
//
//
//		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
//		pane.setViewportView(view);
//		add(pane);
//
//	}
//
//	/**
//	 * Method from the ActionListener interface.
//	 * Handles the event of the add button being pressed
//	 */
//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == addPersonB) {
//			// Chapter 2.19 describes showInputDialog()
//			if(!p){
//			String text=textField.getText();
//			addPerson(text,true);//hack!!!! used to be initial.isSelected()
//			}
//		}
//		else {
//			// Isn't the second for loop more beautiful?
//			/*for (int i = 0; i < list.size(); i++) {
//                JButton temp = list.get(i);*/
//			for (JButton temp:list){
//				if (e.getSource() == temp)
//					restPanel.showInfo(type, temp.getText());
//			}
//		}
//	}
//
//	/**
//	 * If the add button is pressed, this function creates
//	 * a spot for it in the scroll pane, and tells the restaurant panel
//	 * to add a new person.
//	 *
//	 * @param name name of new person
//	 */
//	public void addPerson(String name, Boolean ifAvailable) {
//		if (name != null) {
//			JButton button = new JButton(name);
//			button.setBackground(Color.white);
//
//			Dimension paneSize = pane.getSize();
//			Dimension buttonSize = new Dimension(paneSize.width - 20,
//					(int) (paneSize.height / 7));
//			button.setPreferredSize(buttonSize);
//			button.setMinimumSize(buttonSize);
//			button.setMaximumSize(buttonSize);
//			button.addActionListener(this);
//			list.add(button);
//			view.add(button);
//			restPanel.addPerson(type, name, ifAvailable);//puts waiter on list
//			restPanel.showInfo(type, name);//hack!!!! need improve to show waiter?
//			validate();
//		}
//
//
//	}
//
//}
