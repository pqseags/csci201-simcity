package cityGui;

import java.awt.*;
import java.awt.geom.*;

import util.Loc;


public class Vehicle extends Rectangle2D.Double {
	Color vehicleColor;
	
	public void doDriveFromLocToLoc(Loc current,Loc destination){
		
	}
	
	public Vehicle( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}
	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public Color getColor() {
		return vehicleColor;
	}
	
	public void move( int xv, int yv ) {
		setRect( x+xv, y+yv, getWidth(), getHeight() );
	}
}
