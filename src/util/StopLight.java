package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import cityGui.CityComponent;
import public_Gui.Gui;

public class StopLight extends CityComponent implements Gui,ActionListener{

	
	public Loc loc;
	
	public CrosswalkStatus status;
	
	public Rectangle safeRegion;
	public Rectangle letGoRegion;
	
	public int LIGHT_TIME = 3000;
	
	public Timer lightTimer = new Timer(LIGHT_TIME,this);
	
	public StopLight(){
		lightTimer.start();
		rectangle = new Rectangle(0,0,0,0);
	}
	
	public CrosswalkStatus getStatus(){
		return status;
	}

	
	public void paint(Graphics g1){
		//System.out.println(status);
		Graphics2D g = (Graphics2D) g1;
		if(status==CrosswalkStatus.Pedestrian){
			g.setColor(Color.RED);
		}
		if(status==CrosswalkStatus.Vehicle){
			g.setColor(Color.GREEN);
		}
		g.fillRect(loc.x, loc.y, 5, 5);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource().equals(lightTimer)){
			//System.out.println("\t\tswitching: "+status);
			toggleLight();
			//repaint();
		}
		
	}
	
	
	public void toggleLight(){
		//System.out.println("\t\tTOGGLE BEING CALLED");
		if(status == CrosswalkStatus.Pedestrian){
			status = CrosswalkStatus.Vehicle;
			return;
		}
		
		if(status == CrosswalkStatus.Vehicle){
			status = CrosswalkStatus.Pedestrian;
			return;
		}
	}
	
	
	
	
	
	
	
}
