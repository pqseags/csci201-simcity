package bank.gui;

import house.House;
import house.gui.HouseControlPanel;




import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import public_Object.Food;
import util.Bank;
import cityGui.BuildingControlPanel;
import cityGui.CityBankCard;
import cityGui.CityHouseCard;

public class BankControlPanel  extends BuildingControlPanel  implements ActionListener{

	//data
		Bank bank;
		CityBankCard animation;
//		public ListPanel accountPanel = new ListPanel(this, "Accounts");
		
		private String currentAccount;

		private JLabel title=new JLabel("Bank");
		private List<Account> accountList = new ArrayList<Account>();
		public JScrollPane accountPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		private JPanel view = new JPanel();
		public Dimension buttonSize;
		private List<JButton> list = new ArrayList<JButton>();
		private JButton close;

		public BankControlPanel (CityBankCard anim, Bank b){
			bank=b;
			animation=anim;
			int WINDOWX = 180;//300; there are the fixed size of control panel
			int WINDOWY = 500; //500;
			setVisible(true);
			setLayout(new FlowLayout());
			add(title);
			view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
			accountPanel.setViewportView(view);
			add(accountPanel);
			Dimension paneSize = new Dimension (WINDOWX,300);
			accountPanel.setPreferredSize(paneSize);
			accountPanel.setMinimumSize(paneSize);
			accountPanel.setMaximumSize(paneSize);
			buttonSize = new Dimension(paneSize.width-20, (int) (paneSize.height / 10));
			close = new JButton("Close");
			close.addActionListener(this);
			add(close);
			addAccountButton(0);
			validate();
		}
		
		public void closeBank(){
			close.setText("Open");
			this.bank.isOpen = false;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (JButton temp:list){
				if (e.getSource() == temp) {
					if (!bank.bankGui.controlPanel.isVisible()) {
						bank.bankGui.controlPanel.setVisible(true);
						//System.err.println("SETTING TO VISIBLE");
					}
					else if (bank.bankGui.controlPanel.isVisible()) {
						bank.bankGui.controlPanel.setVisible(false);
						//System.err.println("SETTING TO NOT VISIBLE");
					}
				}

			}
			
			if(e.getSource() ==close){
				//TODO function call to close restaurant
			
				if(close.getText().equals("Close")){
					close.setText("Open");
					this.bank.isOpen = false;
				}
				else if(close.getText().equals("Open")){
					close.setText("Close");
					this.bank.isOpen = true;
				}
			}
		}
		
		public void updateInventory(){

		}

		public void addAccountButton(int actNm) {
			accountPanel.setViewportView(view);
			Account act = new Account(actNm, bank, this);
			act.setPreferredSize(buttonSize);
			act.setMinimumSize(buttonSize);
			act.setMaximumSize(buttonSize);
			accountList.add(act);
			view.add(act);
			accountPanel.validate();
			repaint();
			validate();
			revalidate();
		}
		
		private class Account extends JPanel{

			BankControlPanel bcp;
			Bank bank;

			JButton account = new JButton();
//			JLabel choiceLabel = new JLabel();
			JLabel accountLabel = new JLabel();

			Account(int accountNum, Bank b, BankControlPanel bcp){
				this.bcp = bcp;
				bank = b;

				setLayout(new GridLayout(1,1,5,5));
				setBorder(BorderFactory.createRaisedBevelBorder());


				//choiceLabel.setText("" + accountNum);//not sure what this is supposed to be
				account.setText("Show ControlPanel");

				//choiceLabel.setHorizontalTextPosition(JLabel.RIGHT);
				//inventoryLabel.setVerticalTextPosition(SwingConstants.CENTER);

				// button1.addActionListener(this);
				//minus.addActionListener(bcp);
				account.addActionListener(bcp);
				list.add(account);
				view.add(account);


				add(account);
				validate();
			}
		}
}
