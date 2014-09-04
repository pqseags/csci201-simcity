//package restaurant.restaurantYocca.gui;
//
//import javax.swing.*;
//
//import restaurant.restaurantLinda.gui.Gui;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.List;
//import java.util.ArrayList;
//
//public class AnimationPanel extends JPanel implements ActionListener {
//    
//    private final int WINDOWX = 450;
//    private final int WINDOWY = 350;
//    private static final int clearRectXValue = 0;
//    private static final int clearRectYValue = 0;
//    private static final int tableRectWidthValue = 50;
//    private static final int tableRectHeightValue = 50;
//	private String moneyString = "";
//    
//    private Image bufferImage;
//    private Dimension bufferSize;
//    
//    private List<Gui> guis = new ArrayList<Gui>();
//    
//    public AnimationPanel() {
//    	setSize(WINDOWX, WINDOWY);
//        setVisible(true);
//        
//        bufferSize = this.getSize();
//        
//    	Timer timer = new Timer(7, this );
//    	timer.start();
//    }
//    	
//	public void setMoneyString(String m) {
//		moneyString = m;
//	}
//	
//	public String getMoneyString() {
//		return moneyString;
//	}
//
//    
//	public void actionPerformed(ActionEvent e) {
//		repaint();  //Will have paintComponent called
//	}
//    
//    public void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D)g;
//        Graphics2D g3 = (Graphics2D)g;
//        Graphics2D g4 = (Graphics2D)g;
//        Graphics2D g5 = (Graphics2D)g;
//        Graphics2D g6 = (Graphics2D)g;
//        Graphics2D g7 = (Graphics2D)g;
//        Graphics2D g8 = (Graphics2D)g;
//        Graphics2D g9 = (Graphics2D)g;
//        Graphics2D g10 = (Graphics2D)g;
//        Graphics2D g11 = (Graphics2D)g;
//           
//        //Clear the screen by painting a rectangle the size of the frame
//        g2.setColor(getBackground());
//        g2.fillRect(clearRectXValue, clearRectYValue, WINDOWX, WINDOWY );
//        
//        //Here is the cook
//        g6.setColor(Color.GREEN);
//        g6.fillRect(350, 0, 30, tableRectHeightValue+10);
//        
//        g9.setColor(Color.GREEN);
//        g9.fillRect(350, 50, tableRectWidthValue+30, 30);
//        
//        g10.setColor(Color.GRAY);
//        g10.fillRect(350, 0, 30, 25);
//        
//        g10.setColor(Color.WHITE);
//        g10.fillRect(395, 53, 25, 25);
//       
//        //Here is the cook
//        g7.setColor(Color.BLACK);
//        g7.fillRect(225, 0, tableRectWidthValue+10, tableRectHeightValue-10);
//        
//        g8.setColor(Color.WHITE);
//        g8.drawString(moneyString, 225, 15);
//        
//        //Here is the table
//        g2.setColor(Color.ORANGE);
//        g2.fillRect(100, 200, tableRectWidthValue, tableRectHeightValue);//200 and 250 need to be table params
//       
//        g3.setColor(Color.ORANGE);
//        g3.fillRect(200, 200, tableRectWidthValue, tableRectHeightValue);
//        
//        g4.setColor(Color.ORANGE);
//        g4.fillRect(300, 200, tableRectWidthValue, tableRectHeightValue);
//        
//        g5.setColor(Color.ORANGE);
//        g5.fillRect(400, 200, tableRectWidthValue, tableRectHeightValue);
//        
//        
//        for(Gui gui : guis) {
//            if (gui.isPresent()) {
//                gui.updatePosition();
//            }
//        }
//        
//        for(Gui gui : guis) {
//            if (gui.isPresent()) {
//                gui.draw(g2);
//                gui.draw(g3);
//                gui.draw(g4);
//                gui.draw(g5);
//                gui.draw(g6);
//                gui.draw(g7);
//                gui.draw(g9);
//                gui.draw(g10);
//                gui.draw(g11);
//            }
//        }
//    }
//    
//    public void addGui(CustomerGui gui) {
//        guis.add(gui);
//    }
//    
//    public void addGui(WaiterGui gui) {
//        guis.add(gui);
//    }
//
//	public void addGui(CookGui gui) {
//		guis.add(gui);		
//	}
//
//	public void add(CookGui cookGui) {
//		guis.add(cookGui);
//		
//	}
//}