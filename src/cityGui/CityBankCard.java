package cityGui;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import bank.BankTellerRole;
import bank.gui.BankControlPanel;
import bank.gui.BankCustomerGui;
import bank.gui.BankTellerGui;
import bank.gui.Gui;

public class CityBankCard extends CityCard{

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

	java.net.URL gun1 = getClass().getResource("cityImages/Gun/gunUp.png");
	ImageIcon gunUp = new ImageIcon(gun1);
	java.net.URL gun2 = getClass().getResource("cityImages/Gun/gunUpFire.png");
	ImageIcon gunUpFire = new ImageIcon(gun2);
	java.net.URL gun3 = getClass().getResource("cityImages/Gun/gunDown.png");
	ImageIcon gunDown = new ImageIcon(gun3);
	java.net.URL gun4 = getClass().getResource("cityImages/Gun/gunDownFire.png");
	ImageIcon gunDownFire = new ImageIcon(gun4);

	public Timer gunTimer;
	BankTellerRole notifyLater;

	public int time = 0;

	public boolean drawGuns = false;

	public List<Gui> guis = new ArrayList<Gui>();

	BankControlPanel panel;

	public CityBankCard(SimCityGui city) {
		super(city);
		//System.out.println();
	}

	public void paint(Graphics g) {
		time++;
		//System.out.println("PAINT IS BEING CALLED");
		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.setColor(Color.WHITE);
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

		if (!guis.isEmpty()) {
			/*for(Gui gui : guis) {
    			if (gui.isPresent()) {
    				gui.updatePosition(0, 0);//the number here doesn't actually matter.
    			}
    		}*/

			synchronized (guis) {
				try {
					for(Gui gui : guis) {
						if (gui.isPresent()) {
							gui.draw(g2);
						}
					}
				}
				catch (ConcurrentModificationException e) {
				}
			}
		}
		if(drawGuns){
			if(time%12 <6){
				g2.drawImage(gunUp.getImage(),215,160,20,40,null);
				g2.drawImage(gunDown.getImage(),260,45,20,40,null);
			}
			else{
				g2.drawImage(gunUpFire.getImage(),215,150,20,57,null);
				g2.drawImage(gunDownFire.getImage(),260,40,20,57,null);
			}
		}
	}

	public void run(long t){
		//System.out.println("RUN IS BEING CALLED");
		for(long i=0; i<t-time;i++){
			try {
				for(Gui gui : guis) {
					//if (gui.isPresent()) {
					gui.updatePosition(0, 0);
					//}
				}
			}
			catch (ConcurrentModificationException e) {
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


	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("repaint being called!");
		//moveComponents();

		if(!guis.isEmpty()){
			try {
				for(Gui gui : guis) {
					if (gui.isPresent()) {
						gui.updatePosition(0, 0);//the number here doesn't actually matter.
					}
				}
			}
			catch (ConcurrentModificationException ex) {
			}
		}

		repaint();
	}

	public void stopGuns(){
		drawGuns = false;
	}

	public void fireFightOver(){
		this.notifyLater.msgFireFightOver();
	}


	public void addGuns(BankTellerRole notifyOver){
		drawGuns = true;
		gunTimer = new Timer();
		this.notifyLater = notifyOver;


		gunTimer.schedule(new TimerTask() {
			public void run() {
				stopGuns();
				fireFightOver();
			}
		},
		2000);


	}
	public void setPanel(BankControlPanel p){
		this.panel = p;
	}

}
