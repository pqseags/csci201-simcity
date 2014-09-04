package cityGui.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import public_Gui.Gui;
import cityGui.CityComponent;

public class VehicleAgentGui extends CityComponent implements Gui{

	
	public Rectangle frontBumper;
	public Rectangle backBumper;
	
	
	
	public Rectangle safeRegion;
	public int safeBorder = 4;
	
	
	
	public VehicleAgentGui(int x, int y, Color c, String I) {
		super(x,y,c,I);
	}
	
	
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	
    public Rectangle setSafeRegion(Rectangle rectangle){
    	return new Rectangle(rectangle.x-safeBorder,rectangle.y-safeBorder,rectangle.width+2*safeBorder,rectangle.height+2*safeBorder);
    }
	
	
	
	

	
	
}
