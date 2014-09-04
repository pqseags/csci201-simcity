package market.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import public_Gui.Gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;


public class MarketAnimation extends JPanel implements ActionListener {

	private final int speed = 10;
    private final int doorSpeed = 4;
    
    private final int panelX = 400;
    private final int panelY = 500;
    private int height;
    private int width;
    
    
    private final int doorWidth = 80;
    private final int doorHeight = 50;
    private final int doorX = panelX/2 - doorWidth/2;
    private final int doorY = 0;
    private final int doorMiddleX = doorX + doorWidth/2;
    
    private final int barrierY = 150;

    private List<Gui> guis = new ArrayList<Gui>();
    
    private boolean doorOpen = false;
    private int paintCount = 0;
    private int doorCount = 0;

    private Timer timer;

    public MarketAnimation() {
    	setSize(panelX, panelY);
		setBorder(BorderFactory.createRaisedBevelBorder());
        setVisible(true);
        
 
        timer = new Timer(speed, this );
    	timer.start();
    }
    

    
	public void actionPerformed(ActionEvent e) {

			repaint();  //Will have paintComponent called
	
			

	}

	 public void paintComponent(Graphics g) {
		 
		 	width = getSize().width;
		 	height = getSize().height;
	        Graphics2D paint = (Graphics2D)g;
	        
	        paint.setColor(getBackground());
	        paint.fillRect(0, 0, panelX, panelY );

	        //ENTRANCE
	        paint.setColor(Color.ORANGE);
	        paint.fillRect(0, 0, panelX, 50);
	        paint.setColor(Color.GRAY);
	   
	        paint.fillRect(doorX, doorY, doorWidth, doorHeight);
	        
	        paint.setColor(Color.BLACK);
	       
	        paint.fillRect(doorX, doorY, doorWidth, 5);
	        paint.fillRect(doorX, doorHeight - 5, doorWidth, 5);
	        paint.fillRect(doorX, doorY, 5, doorHeight);
	        paint.fillRect(doorX + doorWidth -5, doorY, 5, doorHeight);
	        
	        paint.fillRect(doorMiddleX, doorY, 2, doorHeight);
	        
	        if (doorOpen){
	        	paintCount ++;

	        	if (paintCount %doorSpeed == 0){
	        		doorCount = paintCount/doorSpeed;
	        	}
	        	if (doorCount == doorWidth/2 -6){
	        		doorOpen = false; 
	        		doorCount = 0;
	        		paintCount = 0;
	        	}

	        	paint.setColor(Color.WHITE);
	        	paint.fillRect(doorMiddleX - doorCount, 5, doorCount * 2, doorHeight-10);
	

	        }

	 
	        //BARRIER
	        paint.setColor(Color.BLACK);
	        paint.fillRect(45,barrierY, panelX-95, 5);
	        paint.fillRect(panelX-50, 50, 5, barrierY-45);
	        paint.fillRect(45, 50, 5, barrierY-45);
	        
	        //HOST DESK
	        paint.setColor(Color.GRAY);
	        paint.fillRect(panelX-57, 70, 19, 60);
	        //CASHIER DESK
	        paint.fillRect(38, 70, 19, 60);
	        //PICKUP DESK
	        paint.fillRect(panelX/2-30, barrierY - 7, 60, 19);
	        
	        //CASHIER
	        paint.setColor(Color.PINK);
	        paint.fillRect(10, 90, 20, 20);
	        
	        //HOST
	        paint.setColor(Color.MAGENTA);
	        paint.fillRect(panelX - 30, 90, 20, 20);
	        
	        //LABELS
	        paint.setColor(Color.BLACK);
	        paint.drawString("Cashier", 2, 45);
	        paint.drawString("Host", panelX - 40, 45);
	     
	        //DOCK
	        paint.setColor(new Color(156, 93, 82));
	        paint.fillRect(0,panelY-120, 25, 120);
	        paint.setColor(Color.WHITE);
	        paint.drawString("D", 8, panelY-100);
	        paint.drawString("O", 8, panelY-80);
	        paint.drawString("C", 8, panelY-60);
	        paint.drawString("K", 8, panelY-40);
	        
	        
	        
	        //STOCK
	        
	        paint.setColor(Color.CYAN);
	        paint.fillRect(100, 225, 50, 20);
	        paint.fillRect(100, 295, 50, 20);
	        paint.fillRect(100, 365, 50, 20);
	        paint.fillRect(230, 225, 50, 20);
	        paint.fillRect(230, 295, 50, 20);
	        paint.fillRect(230, 365, 50, 20);
	        
	        paint.setColor(Color.BLACK);
	        paint.drawString("Pizza", 100, 241);
	        paint.drawString("Salad", 100, 311);
	        paint.drawString("Steak", 230, 311);
	        paint.drawString("Chicken", 230, 241);
	        paint.drawString("Cars", 100, 381);
	        paint.drawString("Other", 230, 381);
	        
	        
	        
	        
	        
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }

	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(paint);
	            }
	        }
	 }
	
	 public void enterCustomer(){
		doorOpen = true;
	 }
	 

	 
	  public void addGui(Gui gui){
	    	guis.add(gui);
	    }
	 
	  public void removeGui(Gui gui){
		  for (Gui g : guis){
			  if (gui == g){
				  guis.remove(gui);
			  }
		  }
	  }
}
