package cityGui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class CityCrosswalk extends CityComponent implements ImageObserver {
	
	private RoadDirection direction;
	
	public CityCrosswalk(int x, RoadDirection direction, boolean topLeft) {
		super(x, 0, Color.white, "Road");
		this.direction = direction;
		if (direction == RoadDirection.HORIZONTAL && topLeft)
			rectangle = new Rectangle(x, 290, 10, 20);
		else if (topLeft)
			rectangle = new Rectangle(290, x, 20, 10);
		if (direction == RoadDirection.HORIZONTAL && !topLeft)
			rectangle = new Rectangle(290, x, 20, 10);
		else if (!topLeft)
			rectangle = new Rectangle(x, 290, 10, 20);
	}

	public CityCrosswalk(int x, RoadDirection direction, String I) {
		super(x, 0, Color.gray, I);
		this.direction = direction;
	}

	public void updatePosition() {
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

//	public void paint(Graphics g) {
//		g.setColor(color);
//		if (direction == RoadDirection.HORIZONTAL)
//			g.fillRect(0, x, 1000, 20);
//		else
//			g.fillRect(x, 0, 20, 1000);
//	}

//	public boolean contains(int x, int y) {
//		if (direction == RoadDirection.HORIZONTAL)
//			if (x >= this.x && x <= this.x+20)
//				return true;
//		if (direction == RoadDirection.VERTICAL)
//			if (y >= this.x && y <= this.x+20)
//				return true;
//		return false;
//	}

}
