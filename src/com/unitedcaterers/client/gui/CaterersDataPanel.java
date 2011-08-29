package com.unitedcaterers.client.gui;

import com.unitedcaterers.client.ClientModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * This panel contains a JTable. It looks at the model and updates the rows that are displayed in the JTable.
 *
 */
public class CaterersDataPanel extends BasePanel {

    JTable cTable = null;
    CDModel cdModel = null;
    ClientModel mainModel = null;

    /**
     * This inner class implements the TableModel used by JTable.
     */
    private class CDModel extends AbstractTableModel {

        String[] colNames = new String[0]; //Column names will come from the model. The following line was used for testing purposes only and has now been commented out.
        //new String[]{"S. No.", "Name", "Location", "Experties", "Max Guests", "Price", "Booking Status"};
        String[][] displayRows = new String[0][0];

        public CDModel() {
            if (mainModel != null) {
                displayRows = mainModel.getDisplayRows();
            } else {
                displayRows = new String[0][0];
            }
        }

        public void refresh() {
            if (mainModel != null) {
                displayRows = mainModel.getDisplayRows();
                colNames = mainModel.getColumns();
            }
        }

        public int getColumnCount() {
            return colNames.length;
        }

        public int getRowCount() {
            if (displayRows != null) {
                return displayRows.length;
            }
            return 0;
        }

        public void setColumnWidths() {
            TableColumn column = null;
            if (mainModel != null) {
                int[] widths = mainModel.getColumnWidths();
                if (widths != null) {
                    for (int i = 0; i < widths.length; i++) {
                        column = cTable.getColumnModel().getColumn(i);
                        int i1 = colNames[i].length();
                        int i2 = widths[i];

                        column.setPreferredWidth((i1 > i2 ? i1 : i2) * 10);
                    }
                }
            }
        }

        public String getColumnName(int col) {
            return colNames[col];
        }

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

    private class TableMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent me) {
        }

        public void mouseClicked(MouseEvent e) {
            //System.out.println("MouseEvent in table ");
            //System.out.println("Click Count "+e.getClickCount());
            //System.out.println("Modifiers "+e.getModifiers()+" LC="+e.BUTTON1_MASK+" MC="+e.BUTTON2_MASK+" RC="+e.BUTTON3_MASK);
            //System.out.println(e.getPoint());

            int cc = e.getClickCount();
            if (cc > 1) {
                CaterersDataPanel.this.doDoubleClick(e);
            }
        }
    }

    public CaterersDataPanel() {
        super();

        cdModel = new CDModel();

        cTable = new JTable(cdModel);
        cTable.addMouseListener(new TableMouseListener());

        initGUI();
    //tpm = new TablePopupMenu(this);
    //this.setBackground(Color.green);
    }

    public void actionPerformed(ActionEvent ae) {
        postUserActionEvent(ae);
    }

    private void doDoubleClick(MouseEvent me) {
        int ind = cTable.rowAtPoint(me.getPoint());
        if (mainModel != null) {
            ActionEvent ae = new ActionEvent(this, me.getID(), "BOOK_CATERER:" + ind);
            postUserActionEvent(ae);
        }
    }

    private void initGUI() {

        this.setLayout(new BorderLayout());

        cTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane jsp = new JScrollPane(cTable);

        add(BorderLayout.CENTER, jsp);

    }

    public static void main(String[] args) {
        ClientModel cm = new ClientModel();
        JFrame jf = new JFrame();
        CaterersDataPanel cdp = new CaterersDataPanel();
        cdp.update(cm, new Boolean(true));
        jf.setSize(600, 400);
        jf.getContentPane().add("Center", cdp);
        jf.setVisible(true);
    }

    /**
     * Updates the view.
     * @param model The ClientModel object that contains the rows to be displayed.
     * @param obj This parameter should be of class Boolean and it indicates whether the whole table structure has to be changed (for change in table columns) or only new rows have to be displayed.
     *
     */
    public void update(Observable model, Object obj) {

        //System.out.println("in update of tabePanel");
        this.mainModel = (ClientModel) model;
        if (obj instanceof Boolean) {
            updateTableView(((Boolean) obj).booleanValue());
        } else {
            updateTableView(false);
        }
    }

    /**
     * Updates the Jtable with new rows.
     * @param boolean totalUpdate if true, it means the columns have changed and the table structure has to change. If false, only the rows are refreshed.
     */
    private void updateTableView(boolean totalUpdate) {
        cdModel.refresh();
        if (totalUpdate) {
            cdModel.fireTableStructureChanged();
            cdModel.setColumnWidths();
            this.revalidate();
            cTable.revalidate();
            cTable.repaint();
        } else {
            cdModel.fireTableDataChanged();
        }
    }

    public int getSelectedIndex() {
        return cTable.getSelectedRow();
    }
}

