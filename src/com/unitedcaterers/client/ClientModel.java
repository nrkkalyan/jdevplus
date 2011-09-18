package com.unitedcaterers.client;

/**
 * This is the main model for the view. It also contains submodels for the
 * components inside the main view. Data contained by this model is displayed by
 * the view.
 */
public class ClientModel extends java.util.Observable {
	
	/**
	 * The rows to be displayed in the CatererDataPanel. Controller retrives the
	 * rows from the DB and updates this. View takes this and displays the data.
	 */
	private String[][]	displayRows;
	
	/**
	 * The column names for the CatererDataPanel.
	 */
	private String[]	columns	= new String[0];
	
	/**
	 * ClientModel constructor. Initializes the static data like Column Names
	 * and Column widths.
	 */
	public ClientModel() {
		super();
		this.columns = new String[] { "Sr No", "Name", "Location", "Size", "Smoking", "Rate", "Date", "Owner" };
		setChanged(); // calling setChanged() signifies that the data in the
						// model has changed.
	}
	
	/**
	 * Returns the column names.
	 */
	public String[] getColumns() {
		return columns;
	}
	
	/**
	 * Returns the rows to be displayed.
	 */
	public String[][] getDisplayRows() {
		return displayRows;
	}
	
	/**
	 * Sets the rows to be displayed. Used by the controller.
	 * 
	 * @param newRows
	 *            data for newRows
	 */
	public void setDisplayRows(String[][] newRows) {
		displayRows = newRows;
		setChanged();
	}
	
}
