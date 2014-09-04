package bank.gui;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class BankAnimationPanel extends JPanel implements ActionListener{

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	static final int TableX = 200;//a global for the x position of the table.
	static final int TableY = 250;//a global for the y position of the table.
	static final int TableWidth = 50;//a global for the x position of the table.
	static final int TableHeight = 50;//a global for the y position of the table.
	
	static final int windowWidth = 274;//a global for the x position of the table.
    static final int windowHeight = 15;//a global for the height of each window.
    static final int counterWidth = 300;//a global for the x position of the table.
    static final int counterHeight = 45;//a global for the height of each window.
    
    private static final Color Window = new Color(148,244,244, 123);//Color of the window to be used.
    private static final Color BankCounter = new Color(169, 109, 55);
    
    private static final Color BankDivider = new Color(84, 54, 23);

	private Image bufferImage;
	private Dimension bufferSize;
	//insert a list of tables in here from HostAgent that is updated, so that it can get them all.
	public List<Gui> guis = new ArrayList<Gui>();
	//public Collection<tellerWindow> tellerWindows;? easily take out.

	public BankAnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		bufferSize = this.getSize();

		Timer timer = new Timer(50, this );
		timer.start();
	}


	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}


	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );
		g2.setColor(BankCounter);
    	g2.fillRect(100, 100, counterWidth, counterHeight);//200 and 250 need to be table params
    	g2.fillRect(100, 400, counterWidth, counterHeight);//200 and 250 need to be table params
    	g2.fillRect(400-counterHeight, 100, counterHeight, 300);//300 = 400-100 starting 
    	
    	
    	g2.setColor(Window);
    	//System.out.println(table.getTableNumber());
    	g2.fillRect(100+(counterWidth-windowWidth)/2, 100+ (counterHeight-windowHeight)/2, windowWidth, windowHeight);//200 and 250 need to be table params
    	g2.fillRect(100+(counterWidth-windowWidth)/2, 400+ (counterHeight-windowHeight)/2, windowWidth, windowHeight);
    	g2.fillRect(100+(counterWidth-windowWidth)/2 + windowWidth - windowHeight, 100 + (counterHeight-windowHeight)/2 + windowHeight, windowHeight, 300-windowHeight);//300 = 400-100 starting 

    	//		        //Here is the table//replace with what I want to draw.
    	//		        g2.setColor(Color.ORANGE);
    	//		        for (Table table : collTables)
    	//		        {
    	//		        	//System.out.println(table.getTableNumber());
    	//		        	g2.fillRect(table.getXPos(), table.getYPos(), TableWidth, TableHeight);//200 and 250 need to be table params
    	//		        }
    	//		
    	//		        for(Gui gui : guis) {
    	//		            if (gui.isPresent()) {
    	//		            	for (Table table : collTables)
    	//		            	{
    	//		            		gui.updatePosition(table.getXPos(), table.getYPos());
    	//		            	}
    	//		            }
    	//		        }

    	for(Gui gui : guis) {
    		if (gui.isPresent()) {
    			gui.draw(g2);
    		}
    	}
	}

	public void addGui(BankCustomerGui gui) {
		guis.add(gui);
	}

	public void addGui(BankTellerGui gui) {
		guis.add(gui);
	}

	public void removeGui(Gui gui) {
		guis.remove(gui);
	}


}
