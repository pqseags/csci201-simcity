package restaurant.restaurantGabe.gui;

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

import restaurant.restaurantGabe.CookRole.FoodItem;
import restaurant.restaurantGabe.RestaurantGabe;
import restaurant.restaurantGabe.gui.RestaurantGabeControlPanel;
import cityGui.BuildingControlPanel;
import cityGui.CityRestaurantCardGabe;

public class RestaurantGabeControlPanel extends BuildingControlPanel implements ActionListener{
        //data
	RestaurantGabe restaurant;
	CityRestaurantCardGabe animation;
	private JLabel title=new JLabel("Gabe's Restaurant");
	private List<InventoryItem> inventoryList = new ArrayList<InventoryItem>();
	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private JButton close=new JButton("Close");
	public Map<String, FoodItem> inventory;

	public RestaurantGabeControlPanel(CityRestaurantCardGabe anim, RestaurantGabe r){
		restaurant=r;
		animation=anim;
		int WINDOWX = 180;//300; there are the fixed size of control panel
		int WINDOWY = 500; //500;
		setVisible(true);
		setLayout(new FlowLayout());
		add(title);
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		add(pane);
		Dimension paneSize = new Dimension (WINDOWX,300);
		pane.setPreferredSize(paneSize);
		pane.setMinimumSize(paneSize);
		pane.setMaximumSize(paneSize);
		Dimension buttonSize = new Dimension(paneSize.width-20, (int) (paneSize.height / 10));
		close.addActionListener(this);
		add(close);

		inventory = r.cook.Foods;
		
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

		validate();

	}
	
	public void closeRestaurant(){
		close.setText("Open");
		this.restaurant.isOpen = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==close){
			if (close.getText().equals("Close")){
				restaurant.isOpen = false;
				close.setText("Open");
			}
			else{
				restaurant.isOpen = true;
				close.setText("Close");
			} 
		}
		// TODO Auto-generated method stub
		for (InventoryItem item : inventoryList){
			if (e.getSource() == item.minus && inventory.get(item.choice).amount >0){
					restaurant.cook.decreaseInventory(item.choice);
					item.inventoryLabel.setText(inventory.get(item.choice).amount+"");
			}
			else if (e.getSource() == item.plus){
				restaurant.cook.increaseInventory(item.choice);
				item.inventoryLabel.setText(inventory.get(item.choice).amount+"");
			}
		}
	}
	
	private class InventoryItem extends JPanel{

		RestaurantGabeControlPanel rcp;
		Map<String,FoodItem> foodMap;
		String choice;


		JButton minus = new JButton("-");
		JLabel choiceLabel = new JLabel();
		JLabel inventoryLabel = new JLabel();
		JButton plus = new JButton("+");

		InventoryItem(String name, RestaurantGabeControlPanel restaurantGabeControlPanel){
			choice = name;
			
			setLayout(new GridLayout(1,5,5,5));
			setBorder(BorderFactory.createRaisedBevelBorder());


			choiceLabel.setText(choice);
			inventoryLabel.setText(restaurantGabeControlPanel.inventory.get(name).amount+"");

			//choiceLabel.setHorizontalTextPosition(JLabel.RIGHT);
			//inventoryLabel.setVerticalTextPosition(SwingConstants.CENTER);

			// button1.addActionListener(this);
			minus.addActionListener(restaurantGabeControlPanel);
			plus.addActionListener(restaurantGabeControlPanel);


			add(minus);
			add(choiceLabel);
			add(inventoryLabel);
			add(plus);

		}
	}

}

