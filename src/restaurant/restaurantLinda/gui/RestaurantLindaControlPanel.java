package restaurant.restaurantLinda.gui;



import java.awt.Container;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import restaurant.restaurantLinda.CookRole;
import restaurant.restaurantLinda.RestaurantLinda;
import restaurant.restaurantLinda.CookRole.Food;
import cityGui.BuildingControlPanel;
import cityGui.CityRestaurantLindaCard;

public class RestaurantLindaControlPanel  extends BuildingControlPanel  implements ActionListener {
	//data
	RestaurantLinda restaurant;
	CityRestaurantLindaCard animation;
	private JLabel title=new JLabel("Linda's Restaurant");
	private List<InventoryItem> inventoryList = new ArrayList<InventoryItem>();
	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private JButton close_open=new JButton("Close");
	private Map<String, Food> inventory;

	public RestaurantLindaControlPanel(CityRestaurantLindaCard anim, RestaurantLinda r){
		restaurant=r;
		animation=anim;
		int WINDOWX = 180;//300; there are the fixed size of control panel
		int WINDOWY = 500; //500;
		setVisible(true);
		setLayout(new FlowLayout());
		add(title);
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		
		Dimension paneSize = new Dimension (WINDOWX,300);
		pane.setPreferredSize(paneSize);
		pane.setMinimumSize(paneSize);
		pane.setMaximumSize(paneSize);
		Dimension buttonSize = new Dimension(paneSize.width-20, (int) (paneSize.height / 10));
		close_open.addActionListener(this);
		
		inventory = ((CookRole)r.cook).foodMap;
		
		inventoryList.add(new InventoryItem("Steak", this));
		inventoryList.add(new InventoryItem("Salad",this));
		inventoryList.add(new InventoryItem("Chicken", this));
		inventoryList.add(new InventoryItem("Pizza", this));
		
		for(InventoryItem item: inventoryList){
			item.setPreferredSize(buttonSize);
			item.setMinimumSize(buttonSize);
			item.setMaximumSize(buttonSize);
			view.add(item);
		}
		
		add(pane);
		add(close_open);
			
		validate();
		
	}
	
	public void closeRestaurant(){
		close_open.setText("Open");
		this.restaurant.isOpen = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==close_open){
			if (close_open.getText().equals("Close")){
				restaurant.isOpen = false;
				close_open.setText("Open");
			}
			else{
				restaurant.isOpen = true;
				close_open.setText("Close");
			} 
		}
		// TODO Auto-generated method stub
		for (InventoryItem item : inventoryList){
			if (e.getSource() == item.minus && inventory.get(item.choice).quantity >0){
				((CookRole)restaurant.cook).decreaseInventory(item.choice);
					item.inventoryLabel.setText(inventory.get(item.choice).quantity+"");
			}
			else if (e.getSource() == item.plus){
				((CookRole)restaurant.cook).increaseInventory(item.choice);
				item.inventoryLabel.setText(inventory.get(item.choice).quantity+"");
			}
		}
	}
	
	private class InventoryItem extends JPanel{

		RestaurantLindaControlPanel rcp;
		Map<String,Food> foodMap;
		String choice;


		JButton minus = new JButton("-");
		JLabel choiceLabel = new JLabel();
		JLabel inventoryLabel = new JLabel();
		JButton plus = new JButton("+");

		InventoryItem(String name, RestaurantLindaControlPanel rcp){
			choice = name;
			
			setLayout(new GridLayout(1,5,5,5));
			setBorder(BorderFactory.createRaisedBevelBorder());


			choiceLabel.setText(choice);
			inventoryLabel.setText(rcp.inventory.get(name).quantity+"");

			//choiceLabel.setHorizontalTextPosition(JLabel.RIGHT);
			//inventoryLabel.setVerticalTextPosition(SwingConstants.CENTER);

			// button1.addActionListener(this);
			minus.addActionListener(rcp);
			plus.addActionListener(rcp);


			add(minus);
			add(choiceLabel);
			add(inventoryLabel);
			add(plus);

		}
	}

}
