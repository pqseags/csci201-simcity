package restaurant.restaurantLinda.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MyImage {
	
	public String bufferText;
	String type;
	public Image bufferImage;
	public int x, y;
	public final String path="../../images/";
	
	MyImage(String image){
		try{
			bufferText = null;
			bufferImage=ImageIO.read(this.getClass().getResource(path+image+".png"));
		}
		catch(IOException e){
			bufferText=image;
		}
		catch(IllegalArgumentException e){
			bufferText=image;
		}
		
		type = image;
	}
	
	MyImage(String image,int x,int y){
		try{
			bufferText = null;
			bufferImage=ImageIO.read(this.getClass().getResource(path+image+".png"));
		}
		catch(IOException e){
			bufferText=image;
		}
		catch(IllegalArgumentException e){
			bufferText=image;
		}
		
		
		this.x=x;
		this.y=y;
		type = image;
	}
	
	public void updatePosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g){
		if (bufferImage!=null)
			g.drawImage(bufferImage,x,y, null);
		
		if (bufferText!=null){
			g.setColor(Color.BLACK);
			g.drawString(bufferText, x, y+15);
		}
		
	}

}
