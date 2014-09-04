package cityGui;

import javax.swing.*;

import restaurant.restaurantParker.gui.CookGui;
import restaurant.restaurantParker.gui.CustomerGui;
import restaurant.restaurantParker.gui.HostGui;
import restaurant.restaurantParker.gui.WaiterGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import public_Gui.Gui;

public class CityRestaurantParkerCard extends CityRestaurantCard {//implements ActionListener {

        private final int speed = 5;
        
           //TABLE LOCATIONS
    private final int xTable1 = 50;
    private final int yTable1 = 130;
    
    private final int xTable2 = 180;
    private final int yTable2 = 130;
    
    private final int xTable3 = 310;
    private final int yTable3 = 130;
    
    private final int TABLEX = 50;
    private final int TABLEY = 50;
    
    private final int xKitchen = 310;
    private final int yKitchen = 290;
    private final int KITCHENX = 100;
    private final int KITCHENY = 50;
    
    private final int xBreakRoom = 10;
    private final int yBreakRoom = 290;
    private final int BREAKROOMX = 100;
    private final int BREAKROOMY = 50;
    
    private final int xCashier = 380;
    private final int yCashier = 15;
    private final int CASHIERX = 50;
    private final int CASHIERY = 25;
    
    private final int xWaiterRoom = 10;
    private final int yWaiterRoom = 55;
    private final int WAITERROOMX = 24;
    private final int WAITERROOMY = 224;
    
    private final int xCustomerRoom = 55;
    private final int yCustomerRoom = 15;
    private final int CUSTOMERROOMX = 224;
    private final int CUSTOMERROOMY = 24;
    
    
    
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;

    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

//    private Timer timer;
    
    public CityRestaurantParkerCard(SimCityGui city) {
            super(city);
            setSize(WINDOWX, WINDOWY);
                setBorder(BorderFactory.createRaisedBevelBorder());
        setVisible(true);
        
        bufferSize = this.getSize();
 
//        timer = new Timer(speed, this );
//            timer.start();
    }

        public void actionPerformed(ActionEvent e) {
//                if (e.getSource() == timer)
//                repaint();  //Will have paintComponent called
                
                
    		    
		        for(Gui gui : guis) {
		            if (gui.isPresent()) {
		                gui.updatePosition();
		            }
		        }
	       

	     repaint();
                
        }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
       
       
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        
       //TABLES
        g2.setColor(Color.ORANGE);
        
        g2.fillRect(xTable1, yTable1, TABLEX, TABLEY);//200 and 250 need to be table params
        g2.fillRect(xTable2,yTable2, TABLEX, TABLEY);
        g2.fillRect(xTable3,yTable3, TABLEX, TABLEY);

        
        //KITCHEN
        g2.setColor(Color.RED);
        g2.fillRect(xKitchen, yKitchen, KITCHENX, KITCHENY);
        //grill
        g2.setColor(Color.BLACK);
        g2.fillRect(xKitchen-30, yKitchen , 30, KITCHENY );
        g2.setColor(Color.darkGray);
        g2.fillRect(xKitchen - 25, yKitchen + 5, 20, 10);
        g2.fillRect(xKitchen - 25, yKitchen + 20, 20, 10);
        g2.fillRect(xKitchen - 25, yKitchen + 35, 20, 10);
        //refrigerator
        g2.setColor(Color.BLUE);
        g2.fillRect(xKitchen+KITCHENX, yKitchen, 30, KITCHENY);
        g2.setColor(Color.WHITE);
        g2.fillRect(xKitchen+KITCHENX+15, yKitchen, 1, KITCHENY);
        g2.fillOval(xKitchen+KITCHENX+11, yKitchen + (KITCHENY/2) -3, 8, 8);
        //plating area
        g2.setColor(Color.lightGray);
        g2.fillRect(xKitchen, yKitchen-20, KITCHENX, 20);
        g2.setColor(Color.BLACK);
        g2.fillRect(xKitchen+33, yKitchen-20, 1, 20);
        g2.fillRect(xKitchen+66, yKitchen-20, 1, 20);
        
    
        
        //BREAK ROOM
        g2.setColor(Color.BLUE);
        g2.fillRect(xBreakRoom, yBreakRoom, BREAKROOMX, BREAKROOMY);
        g2.setColor(Color.WHITE);
        g2.drawString("Break Room", xBreakRoom+14, yBreakRoom+45);
        
        //CASHIER
        g2.setColor(Color.GRAY);
        g2.fillRect(xCashier, yCashier, CASHIERX, CASHIERY);
        g2.setColor(Color.BLACK);
        g2.drawString("Cashier", xCashier, yCashier + 20);
        
        //WAITER ROOM
        g2.setColor(Color.lightGray);
        g2.fillRect(xWaiterRoom, yWaiterRoom, WAITERROOMX, WAITERROOMY);
        
        //CUSTOMER ROOM
        g2.setColor(Color.lightGray);
        g2.fillRect(xCustomerRoom, yCustomerRoom, CUSTOMERROOMX, CUSTOMERROOMY);
        
       // System.err.println("paint it duhhh");
        
//        for(Gui gui : guis) {
//            if (gui.isPresent()) {
//                gui.updatePosition();
//            }
//        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
        //System.err.println("CityRestaurantParkerCard.GUIADDED!");
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui){
            guis.add(gui);
    }
    
    public void addGui(CookGui gui){
            guis.add(gui);
    }
    
  
    
}
