package com.unitedcaterers.client;

/**
 * This is the main model for the view. It also contains submodels for the components inside the main view.
 * Data contained by this model is displayed by the view.
 */
public class ClientModel extends java.util.Observable {

    /**
     * The rows to be displayed in the CatererDataPanel. Controller retrives the rows from the DB and updates this. View takes this and displays the data.
     */
    private String[][] displayRows;
    /**
     * This contains the message that has to be displayed by the MessagePanel. For now, it only contains a string but later on more things may be added.
     */
    private MessageModel msgModel = new MessageModel();
    /**
     * The column widths for the CatererDataPanel.
     */
    private int[] columnWidths = new int[0];
    /**
     * The column names for the CatererDataPanel.
     */
    private String[] columns = new String[0];

    /**
     * ClientModel constructor. Initializes the static data like Column Names and Column widths.
     */
    public ClientModel() {
        super();
        this.columns = new String[]{"S No.", "Name", "Location", "Experties", "Max Guests", "Price", "Booking Status"};
        this.columnWidths = new int[]{2, 10, 10, 15, 2, 3, 5};
        setChanged(); //calling setChanged() signifies that the data in the model has changed.
    }

    /**
     * Returns the column names.
     */
    public String[] getColumns() {
        return columns;
    }

    /**
     * Returns the column widths.
     */
    public int[] getColumnWidths() {
        return columnWidths;
    }

    /**
     * Returns the rows to be displayed.
     */
    public String[][] getDisplayRows() {
        return displayRows;
    }

    /**
     * Returns the messagemodel.
     */
    public MessageModel getMessageModel() {
        return msgModel;
    }

    /**
     * Sets the rows to be displayed. Used by the controller.
     * @param newRows data for newRows
     */
    public void setDisplayRows(String[][] newRows) {
        displayRows = newRows;
        setChanged();
    }

    /**
     * Sets the MessageModel. Used by the controller. Should not be called more than once because
     * views would be viewing this Model and replacing the model itself will make the views view a stale
     * model.
     * In general, models are created only once. The data that they contain keeps changing.
     * @param newModel newModel
     */
    public void setMessageModel(MessageModel newModel) {
        msgModel = newModel;
        setChanged();
    }
}
