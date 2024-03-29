package com.unitedcaterers.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.unitedcaterers.client.ClientModel;

/**
 * This panel contains a JTable. It looks at the model and updates the rows that
 * are displayed in the JTable.
 * 
 */
public class TablePanel extends JPanel implements Observer {
	
	DataModel					mTableDataModel			= new DataModel();
	JTable						recordJTable			= new JTable(mTableDataModel);
	ClientModel					mClientModel			= null;
	private static final int	PREFERRED_JTABLE_WIDTH	= 640;
	private static final int	PREFERRED_JTABLE_HEIGHT	= 480;
	
	/**
	 * This inner class implements the TableModel used by JTable.
	 */
	private class DataModel extends AbstractTableModel {
		
		String[]	mColumnNames	= new String[0];	// Column names will come
													// from the model. The
													// following line was used
													// for testing purposes only
													// and has now been
													// commented out.
		// new String[]{"S. No.", "Name", "Location", "Experties", "Max Guests",
		// "Price", "Booking Status"};
		String[][]	displayRows	= new String[0][0];
		
		public DataModel() {
			if (mClientModel != null) {
				displayRows = mClientModel.getDisplayRows();
			} else {
				displayRows = new String[0][0];
			}
		}
		
		public void refresh() {
			if (mClientModel != null) {
				displayRows = mClientModel.getDisplayRows();
				mColumnNames = mClientModel.getColumns();
			}
		}
		
		@Override
		public int getColumnCount() {
			return mColumnNames.length;
		}
		
		@Override
		public int getRowCount() {
			if (displayRows != null) {
				return displayRows.length;
			}
			return 0;
		}
		
		@Override
		public String getColumnName(int col) {
			return mColumnNames[col];
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			if (col == 0) {
				return "" + (row + 1);
			}
			if (displayRows != null && displayRows.length >= row && displayRows[row].length >= col) {
				return displayRows[row][col].trim();
			} else {
				return "";
			}
		}
	}
	
	public TablePanel() {
		
		this.setLayout(new BorderLayout());
		recordJTable.setPreferredScrollableViewportSize(new Dimension(PREFERRED_JTABLE_WIDTH, PREFERRED_JTABLE_HEIGHT));
		// First we add all room records.
		recordJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		recordJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		recordJTable.setToolTipText("Select a Room.");
		JScrollPane jsp = new JScrollPane(recordJTable);
		
		add(BorderLayout.CENTER, jsp);
	}
	
	public static void main(String[] args) {
		ClientModel cm = new ClientModel();
		JFrame jf = new JFrame();
		TablePanel cdp = new TablePanel();
		cdp.update(cm, new Boolean(true));
		jf.setSize(600, 400);
		jf.getContentPane().add("Center", cdp);
		jf.setVisible(true);
	}
	
	/**
	 * Updates the view.
	 * 
	 * @param model
	 *            The ClientModel object that contains the rows to be displayed.
	 * @param obj
	 *            This parameter should be of class Boolean and it indicates
	 *            whether the whole table structure has to be changed (for
	 *            change in table columns) or only new rows have to be
	 *            displayed.
	 * 
	 */
	@Override
	public void update(Observable model, Object obj) {
		
		// System.out.println("in update of tabePanel");
		this.mClientModel = (ClientModel) model;
		if (obj instanceof Boolean) {
			updateTableView(((Boolean) obj).booleanValue());
		} else {
			updateTableView(false);
		}
	}
	
	/**
	 * Updates the Jtable with new rows.
	 * 
	 * @param boolean totalUpdate if true, it means the columns have changed and
	 *        the table structure has to change. If false, only the rows are
	 *        refreshed.
	 */
	private void updateTableView(boolean totalUpdate) {
		mTableDataModel.refresh();
		if (totalUpdate) {
			mTableDataModel.fireTableStructureChanged();
			this.revalidate();
			recordJTable.revalidate();
			recordJTable.repaint();
		} else {
			mTableDataModel.fireTableDataChanged();
		}
	}
	
	public int getSelectedIndex() {
		return recordJTable.getSelectedRow();
	}
}
