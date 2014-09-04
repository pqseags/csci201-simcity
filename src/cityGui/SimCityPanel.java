package cityGui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import person.PersonAgent;
import util.CityMap;
import cityGui.test.PersonGui;

public abstract class SimCityPanel extends JPanel implements ActionListener, MouseListener {

	protected SimCityGui city;
	public List<CityComponent> statics, movings;
	protected Color background;
	protected Timer timer;
	
	public List<RainGui> rain;
	private boolean raining = false;
	//SC Logo
	java.net.URL logo = getClass().getResource("cityImages/sclogo.png");
	ImageIcon scLogo = new ImageIcon(logo);
		
	public SimCityPanel(SimCityGui city) {
		this.city = city;
		statics = Collections.synchronizedList(new ArrayList<CityComponent>());
		movings = Collections.synchronizedList(new ArrayList<CityComponent>());
		timer = new Timer(10, this);
		timer.start();
		
		rain = new ArrayList<RainGui>();
		for (int i = 0; i < 100; i++){
			rain.add(new RainGui());
		}
	}
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(scLogo.getImage(), 240,240,120,120,null);
		moveComponents();
		drawComponents(g);

	}
	
	public void drawComponents(Graphics g) {
		synchronized(statics){
			for (CityComponent c:statics) {
				c.paint(g);
			}
		}
		
		synchronized(movings){
			for (CityComponent c:movings) {
				c.paint(g);
			}
		}
		
		if (raining){
			for(RainGui r:rain){
				r.draw(g);
			}
		}
	}
	
	public void moveComponents() {
		try{
			synchronized(movings){
				for (CityComponent c:movings) {
					c.updatePosition();
				}
			}
		}
		catch(ConcurrentModificationException e){
			e.printStackTrace();
		}
		
		if (raining){
			for (RainGui r : rain){
				r.updatePosition();
			}
		}
	}
	
	public void addStatic(CityComponent c) {
		statics.add(c);
	}
	
	public void addMoving(CityComponent c) {
		movings.add(c);
	}
	
	public void actionPerformed(ActionEvent e) {
		//System.out.println("repaint being called!");
		//moveComponents();
		repaint();
	}
	
	public void startRaining(){
	
		if (raining)
			raining = false;
		else
			raining = true;
	}
	
	

}
