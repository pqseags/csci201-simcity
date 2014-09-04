package cityGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class CityCard extends SimCityPanel {

	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	long time=0;
	Graphics g;
	ActionEvent e;

	public CityCard(SimCityGui city) {
		super(city);
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		background = Color.green;
	}

	public CityCard(SimCityGui city, Color c) {
		super(city);
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		background = c;
	}

	public  void run(long t){
		
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




}
