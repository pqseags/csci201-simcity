package bank.gui;

import interfaces.BankCustomer;
import interfaces.BankTeller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.BankCustomerRole;
import bank.BankTellerRole;
import util.Bank;
import util.Bank.BankAccount;
import util.Bank.loan;


public class BankPanel extends JPanel {


	public ListPanel accountPanel = new ListPanel(this, "Accounts");
	public ListPanel tellerPanel = new ListPanel(this, "Tellers");
	public ListPanel customerPanel = new ListPanel(this, "Customers");
	
	private String currentAccount;
	
	private JPanel groupOfListPanels = new JPanel();
	
	
	private BankGui gui; //reference to main gui may not need a bank gui.
	
	private Bank bank;
	
	public BankPanel(BankGui gui, Bank b) {
        this.gui = gui;
        bank = b;
        setLayout(new GridLayout(1, 3, 0, 0));//number of rows by three objects.
        //initRestLabel();
        Dimension panelDims = new Dimension((int)(BankGui.WINDOWX*.33), (int) (BankGui.WINDOWY * .66));
        accountPanel.setPreferredSize(panelDims);
        tellerPanel.setPreferredSize(panelDims);
        customerPanel.setPreferredSize(panelDims);
        add(accountPanel);
        add(tellerPanel);
        add(customerPanel);
	}
	

	public void enterBank(BankCustomer bcr, BankCustomerGui g) {
		bank.addMeToQueue(bcr);
		customerPanel.addListButton(bcr.getName());
		gui.bankAnimationPanel.addGui(g);
//		if (bcr instanceof BankCustomerRole) {
//			((BankCustomerRole) bcr).startThread();
//		}
	}
	public void enterBankTemp(String bcr) {
		//bank.addMeToQueue(bcr);
		customerPanel.addListButton(bcr);
		//gui.bankAnimationPanel.addGui(g);
//		if (bcr instanceof BankCustomerRole) {
//			((BankCustomerRole) bcr).startThread();
//		}
	}
	
	public void addAccount(BankAccount BA) {
		//bank.addMeToQueue(bcr);
		bank.accounts.add(BA);
		accountPanel.addListButton("" + BA.accountNumber);
		//gui.bankAnimationPanel.addGui(g);
//		if (bcr instanceof BankCustomerRole) {
//			((BankCustomerRole) bcr).startThread();
//		}
	}
	
	public void enterBank(BankTellerRole bt, BankTellerGui g) {
		bank.startTellerShift(bt);
		tellerPanel.addListButton(bt.getName());
		gui.bankAnimationPanel.addGui(g);
//		if (bt instanceof BankTellerRole) {
//			((BankTellerRole) bt).startThread();
//		}
	}
	
	public void leaveBank(Gui g) {
		gui.bankAnimationPanel.removeGui(g);
	}




/**
 * When an account is clicked, this function calls
 * updatedInfoPanel() from the main gui so that person's information
 * will be shown
 *
 * @param type indicates whether the person is a customer or waiter
 * @param name name of person
 */
	public void showInfo(String type, String acctNum) {
		if (type.equalsIgnoreCase("accounts")) {
			for (BankAccount tempAcct : bank.accounts) {
				if (Integer.toString(tempAcct.accountNumber).equalsIgnoreCase(acctNum)) {
					gui.updateInfoPanel(tempAcct);
					currentAccount = acctNum;
				}
			}
		}
		else if (type.equalsIgnoreCase("loans")) {
			for (BankAccount tempAcct : bank.accounts) {
				if (Integer.toString(tempAcct.accountNumber).equalsIgnoreCase(currentAccount)) {
					gui.updateLoanLabel(Integer.parseInt(acctNum), tempAcct);
				}
			}
		}
	}
}

