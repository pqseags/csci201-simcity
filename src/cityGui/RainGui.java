package cityGui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.ImageIcon;

import person.PersonAgent;

public class RainGui {

	ImageIcon raindrop;
	private final int yInitial = 0;
	 private int xPos = 0, yPos = 0;

	
	//constructor
	public RainGui(){
		Random x = new Random();
		xPos = x.nextInt(600);
		yPos = x.nextInt(600);
		
		java.net.URL drop = getClass().getResource("cityImages/raindrop.png");
		raindrop = new ImageIcon(drop);
		
	}
	
	
	public void updatePosition() {
		yPos++;
		
		if (yPos == 600)
			yPos = yInitial;
	}


	public void draw(Graphics g) {
		g.setColor(Color.GRAY);
		// g.fillRect(xPos, yPos, 5, 10);
		 g.drawImage(raindrop.getImage(), xPos, yPos, 10,15, null);
		
	}

	
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

}
