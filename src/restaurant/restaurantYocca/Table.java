package restaurant.restaurantYocca;

import interfaces.restaurantYocca.Customer;

public class Table {
	Customer occupiedBy;
	int tableNumber;
	int xLocation;
	int yLocation;

	Table(int tableNumber, int xLocation, int yLocation) {
		this.tableNumber = tableNumber;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
	}

	void setOccupant(Customer c) {
		occupiedBy = c;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	Customer getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
	
	int getTableNumber() {
		return tableNumber;
	}
	
	public int getXLocation() {
		return xLocation;
	}
	
	public int getYLocation() {
		return yLocation;
	}
	
	public void setTableNumber(int tableNum) {
		tableNumber = tableNum;
	}
	
	public void setXLocation(int xLoc) {
		xLocation = xLoc;
	}
	
	public void setYLocation(int yLoc) {
		yLocation = yLoc;
	}

}
