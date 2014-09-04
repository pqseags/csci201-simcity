package market.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;


public class MarketMain extends JFrame{
	

	
	MarketMain(){
	  /*  int WINDOWX = 800;
	    int WINDOWY = 500;	
		setBounds(100, 50, WINDOWX,WINDOWY);
		setLayout(new GridLayout(1,2,5,5));
		
		MarketAnimation ma = new MarketAnimation();
        Dimension maSize = new Dimension (WINDOWX/2, WINDOWY);
        ma.setPreferredSize(maSize);
        ma.setMinimumSize(maSize);
        ma.setMaximumSize(maSize);	
		
		
		MarketPanel mp = new MarketPanel(ma);
        Dimension mpSize = new Dimension (WINDOWX/2, WINDOWY);
        mp.setPreferredSize(mpSize);
        mp.setMinimumSize(mpSize);
        mp.setMaximumSize(mpSize);	

		
    	add(mp);
		add(ma);

		*/
	}

	public static void main(String[] args) {

		MarketMain gui = new MarketMain();
        gui.setTitle("Market Panel");
        gui.setVisible(true);
        gui.setResizable(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
