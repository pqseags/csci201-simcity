package cityGui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JPanel;

public class CityView extends JPanel implements MouseListener, ActionListener {

	HashMap<String, CityCard> cards;
	SimCityGui city;
	public static final int VIEW_WIDTH = 500, VIEW_HEIGHT = 500;
	CardLayout layout;
	String currentCard=null;


	public CityView(SimCityGui city) {

		this.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		this.city = city;

		cards = new HashMap<String, CityCard>();
		cards.put("Welcome to CITY!!!", new CityCard(city, Color.DARK_GRAY));
		//cards.put("Road", new CityCard(city));
		//cards.put("Restaurant 1", new CityCard(city, Color.blue));
		//cards.put("Restaurant 2", new CityCard(city, Color.red));

		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}


		layout.show(this, "null");
	}

	public boolean addView(CityCard panel, String key) {
		if (cards.containsKey(key))
			return false;
		cards.put(key, panel);
		if(currentCard!=null){
			cards.get(currentCard).time=city.time;
		}
		/*if (cards.get(key) == null) {
			System.out.println("Card is null");
		}
		else System.out.println("Card is not null");*/
		//System.out.println(panel==null);
		this.add(cards.get(key), key);
		currentCard=key;
		return true;
	}

	public void setView(String key) {
		//System.out.println("In set view");
		if (cards.containsKey(key)) {
			if(currentCard!=null){
				cards.get(currentCard).time=city.time;
			}
			//System.out.println("In the condition");
			//System.out.println(cards.get(key) instanceof CityRestaurantSimonCard);
			cards.get(key).run(city.time);
			layout.show(this, key);
			currentCard=key;
			city.info.setText(key);
		}
	}


	public void actionPerformed(ActionEvent arg0) {

	}


	public void mouseClicked(MouseEvent e) {

	}


	public void mouseEntered(MouseEvent e) {

	}


	public void mouseExited(MouseEvent e) {

	}


	public void mousePressed(MouseEvent e) {

	}


	public void mouseReleased(MouseEvent e) {

	}
	
	public String getCurrentCard(){
		return currentCard;
	}

}
