package bank.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.Bank;
import util.Bank.BankAccount;
import util.Bank.loan;
import bank.BankCustomerRole;
import bank.BankTellerRole;;

public class BankGui  extends JPanel implements ActionListener {
	
	public static final int WINDOWX = 450;
	public static final int WINDOWY = 350;
	
	public static final int animPanelWidth = 500;
	public static final int animPanelHeight = 500;
	
	public static final int controlPanelWidth = 450;
	public static final int controlPanelHeight = 550;
	
	public static final int xUserScreen = 10;
	public static final int yUserScreen = 10;
	public static final int userScreenWidth = 780;//WINDOWX + 200//Now wide enough to fit everything
	public static final int userScreenHeight = 550;//WINDOWY + 200
	
	public static final int xRestGui = 10;
	public static final int yRestGui = 10;
	public static final int restGuiWidth = userScreenWidth + animPanelWidth;//WINDOWX + 200//Now wide enough to fit everything
	public static final int restGuiHeight = userScreenHeight;//WINDOWY + 200
	
	public static final int infoPanelRows = 1;
	public static final int infoPanelCols = 2;
	public static final int infoPanelXGap = 30;
	public static final int infoPanelYGap = 0;
	public static final int infoPanelMeRows = 1;
	public static final int infoPanelMeCols = 2;
	public static final int infoPanelMeXGap = 30;
	public static final int infoPanelMeYGap = 0;
	
	
	
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	//AnimationPanel animationPanel = new AnimationPanel();
	public static BankAnimationPanel bankAnimationPanel = new BankAnimationPanel();
	
	BankAccount currentAccount = null;
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public BankPanel bankPanel;
    public BankControlPanel bankControlPanel;
    JFrame controlPanel = new JFrame();//changed userScreen to controlPanel
    private JPanel moreInfo = new JPanel();
    
    private JPanel moreInfoPersonal = new JPanel();//left side of more info.
    private JLabel userAmount = new JLabel();
    private JLabel userName = new JLabel();
    private JLabel userPass = new JLabel();
    private JLabel loanAmount = new JLabel();
    
    public ListPanel loans = new ListPanel(bankPanel, "Loans");
    
    
    
//    /* infoPanel holds information about the clicked customer, if there is one*/
//    private JPanel infoPanel;
//    private JPanel infoPanelMe;
//    private JLabel infoLabel; //part of infoPanel
//    private JLabel infoLabelMe;
//    private JCheckBox stateCB;//part of infoLabel
//    private ImageIcon hostess;
//    private JButton pause = new JButton("Pause");
//    private boolean isPaused = false;
//    
//    private Object currentPerson;/* Holds the agent that the info is about.
//    								Seems like a hack */
//    private Object currentWaiter;
    public BankGui(Bank b) {
    	setVisible(true);
    	bankPanel = new BankPanel(this, b);
        controlPanel.setLayout(new BorderLayout());
        //controlPanel.setVisible(true);
        moreInfo.setLayout(new BorderLayout());
        moreInfoPersonal.setLayout(new GridLayout(4, 1));
        Dimension addInfoDim = new Dimension((int)(controlPanelWidth*.5), (int) (controlPanelHeight * .45));
        userName.setBorder(BorderFactory.createTitledBorder("Name"));
        userName.setSize(addInfoDim);
        
        userPass.setBorder(BorderFactory.createTitledBorder("Password"));
        userPass.setSize(addInfoDim);
        userAmount.setBorder(BorderFactory.createTitledBorder("Amount"));
        userAmount.setSize(addInfoDim);
        
        loanAmount.setBorder(BorderFactory.createTitledBorder("Loan Amount"));
        loanAmount.setSize(addInfoDim);
        
        moreInfoPersonal.add(userName);
        moreInfoPersonal.add(userPass);
        moreInfoPersonal.add(userAmount);
        moreInfoPersonal.add(loanAmount);
        moreInfoPersonal.setPreferredSize(addInfoDim);
        
        loans.setSize(addInfoDim);
        moreInfo.add(moreInfoPersonal, BorderLayout.WEST);
        moreInfo.add(loans, BorderLayout.EAST);
        
        
        //setSize(WINDOWX, WINDOWY);
        setBounds(xRestGui, yRestGui, restGuiWidth, restGuiHeight);
        /*animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(xAnimFrame, yAnimFrame, animFrameWidth, animFrameHeight);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); *///tok out because it is now put in userScreen
    	
    	controlPanel.setBounds(xUserScreen, yUserScreen, controlPanelWidth, controlPanelHeight);
    	//setBounds(xRestGui, yRestGui, restGuiWidth, restGuiHeight);//is there a way to do this?/what si
    	setLayout(new BorderLayout());
        controlPanel.setLayout(new BorderLayout(5, 5));

        bankAnimationPanel.setBounds(0, 0, animPanelWidth, animPanelHeight);
        
        Dimension bankDim = new Dimension(controlPanelWidth, (int) (controlPanelHeight * .5));
        bankPanel.setPreferredSize(bankDim);
        bankPanel.setMinimumSize(bankDim);
        bankPanel.setMaximumSize(bankDim);
        Dimension moreInfoDim = new Dimension(controlPanelWidth, (int) (controlPanelHeight * .45));
        moreInfo.setPreferredSize(moreInfoDim);
        moreInfo.setMinimumSize(moreInfoDim);
        moreInfo.setMaximumSize(moreInfoDim);
        controlPanel.add(bankPanel, BorderLayout.NORTH);
        controlPanel.add(moreInfo, BorderLayout.SOUTH);
        Dimension controlDim = new Dimension(controlPanelWidth, (int) (controlPanelHeight));
        controlPanel.setPreferredSize(controlDim);
        controlPanel.setMaximumSize(controlDim);
        controlPanel.setVisible(false);
        //add(controlPanel, BorderLayout.WEST);
        add(bankAnimationPanel, BorderLayout.CENTER);
    }
        
        
        
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */ 
    	public void actionPerformed(ActionEvent e) {
    		
        }
        
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(BankAccount ba) {
        currentAccount = ba;
        userName.setText(ba.custName);
        userPass.setText(ba.passWord);
        userAmount.setText("" + ba.amount);
        loanAmount.setText("");
        loans.clearListButtons();
        if (!ba.myLoans.isEmpty()) {
			for (loan l : ba.myLoans) {
				loans.addListButton("" + l.loanNumber);
			}
		}
        repaint();//moreIno.validate();

    }
    
    public void updateLoanLabel(int loanNum, BankAccount ba) {
    	if (!ba.myLoans.isEmpty()) {
			for (loan l : ba.myLoans) {
				System.out.println("" + loanNum);
				System.out.println(l.loanNumber + "");
				if (l.loanNumber == loanNum) {
					loanAmount.setText("" + l.total);
				}
			}
		}
    }
    
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
//    public void setCustomerEnabled(CustomerAgent c) {
//        if (currentPerson instanceof CustomerAgent) {
//            CustomerAgent cust = (CustomerAgent) currentPerson;
//            if (c.equals(cust)) {
//                stateCB.setEnabled(true);
//                stateCB.setSelected(false);
//            }
//        }
//    }
//    /**
//     * Message sent from a customer gui to enable that customer's
//     * "I'm hungry" checkbox.
//     *
//     * @param c reference to the customer
//     */
//    public void setWaiterEnabled(WaiterAgent w) {
//        if (currentWaiter instanceof WaiterAgent) {
//            WaiterAgent wait = (WaiterAgent) currentWaiter;
//            if (w.equals(wait)) {
//                stateCB.setEnabled(true);
//                stateCB.setSelected(false);
//            }
//        }
//    }
	
	
	
	
	
	/**
     * Main routine to get gui started
     */
    public static BankGui bankMain(Bank b) {
        BankGui gui = new BankGui(b);
        //gui.setTitle("Bank");
        gui.setVisible(true);
        //gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.bankPanel.enterBankTemp("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        
        String custName = "CustomerAccount";
        String passWord = "12345";
        Bank banka = new Bank();
        BankAccount ba = (banka.new BankAccount(5, 0, custName, passWord));
        ba.myLoans.add(banka.new loan(5, 0));
        ba.myLoans.add(banka.new loan(15, 7));
        ba.myLoans.add(banka.new loan(50, 2));
        gui.bankPanel.addAccount(ba);
        BankAccount ba2 = (banka.new BankAccount(2, 1, custName + "hi", passWord + "123"));
        ba2.myLoans.add(banka.new loan(1, 3));
        ba2.myLoans.add(banka.new loan(2, 6));
        ba2.myLoans.add(banka.new loan(3, 1));
        gui.bankPanel.addAccount(ba2);
        
        //return bankAnimationPanel;
        return gui;
        //cityFrame.addTab("Bank", NULL/*unless you want an image*/,  Component component) 
    }
	
    public void hideControlPanel() {
    	controlPanel.setVisible(false);
    }
    
    public void showControlPanel() {
    	controlPanel.setVisible(true);
    }
    
}
