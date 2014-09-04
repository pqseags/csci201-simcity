package bank.gui;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ListPanel extends JPanel implements ActionListener{
	public static final int xNameInputPref = 80;
	public static final int yNameInputPref = 25;
	public static final int xPanePref = 200;
	public static final int yPanePref = (int)(BankGui.controlPanelHeight*.4);


	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private List<JButton> list = new ArrayList<JButton>();

	private BankPanel bankPanel;
	private String type;

	/**
	 * Constructor for ListPanel.  Sets up all the gui
	 *
	 * @param rp   reference to the restaurant panel
	 * @param type indicates if this is for customers or waiters
	 */
	public ListPanel(BankPanel bp, String type) {
		bankPanel = bp;
		this.type = type;

		setLayout(new BorderLayout());//(Container) this, BoxLayout.Y_AXIS)
		add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"), BorderLayout.NORTH);

		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		pane.setPreferredSize(new Dimension(xPanePref, yPanePref));
		add(pane, BorderLayout.SOUTH);


	}

	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		// Isn't the second for loop more beautiful?
		/*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
		for (JButton temp:list){
			if (e.getSource() == temp) {
				if (type.equalsIgnoreCase("accounts")) {
					bankPanel.showInfo(type, temp.getText());
				}
				if (type.equalsIgnoreCase("loans")) {
					bankPanel.showInfo(type, temp.getText());
				}
			}
		}
	}

	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 *
	 * @param name name of new person
	 */
	public void addListButton(String name) {
		if (name != null) {
			JButton button = new JButton(name);
			JPanel panel = new JPanel();
			JCheckBox checkBox = new JCheckBox();

			button.setBackground(Color.white);
			panel.setLayout(new FlowLayout());

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height / 7));//was -20
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);

			panel.add(button);
			//panel.add(checkBox);
			panel.setPreferredSize(new Dimension(paneSize.width - 20, (int) (paneSize.height / 5)));
			panel.setMinimumSize(new Dimension(paneSize.width - 20, (int) (paneSize.height / 5)));
			panel.setMaximumSize(new Dimension(paneSize.width - 20, (int) (paneSize.height / 5)));

			list.add(button);
			view.add(button);
			//view.add(button);
			validate();
		}
	}
	
	public void clearListButtons() {
		list.clear();
		view.removeAll();
		validate();
	}
}
