package com.unitedcaterers.client;

import com.unitedcaterers.client.gui.CaterersDataPanel;
import com.unitedcaterers.client.gui.ControlPanel;
import com.unitedcaterers.client.gui.MessagePanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 * This class is the main View of the application and forms the V of MVC pattern. It displays the menu and a table for caterers information.
 * It sends all the user interaction events to the Controller, which is nothing but an ActionListner.
 * Based on the event, Controller takes appropriate action such as search or book,
 * updates the model with new data, and then calls notifyObservers() on the model.
 *
 */
public class ClientFrame extends JFrame {

    /**
     * A panel that houses a menubar.
     */
    private ControlPanel controlPanel = null;
    /**
     * A panel that houses a JTable. It displays caterer information contained within ClientModel.
     */
    private CaterersDataPanel tablePanel = null;
    /**
     * A panel that displays messages contained within MessageModel.
     */
    private MessagePanel messagePanel = null;
    /**
     * The controller.
     */
    private ActionListener al = null;

    /**
     * 1. Constructs the main view frame.
     * 2. instantiates ControlPanel (which contains MenuBar, and in future may contain ToolBar).
     * 3. instantiates CaterersDataPanel(which contains a JTable).
     * 4. instantiates MessagePanel (which displays helpful messages to the user).
     * 5. finally initializes the GUI.
     */
    public ClientFrame() {
        super("United Caterers Booking System");
        controlPanel = new ControlPanel();
        setTablePanel(new CaterersDataPanel());
        messagePanel = new MessagePanel();
        initGUI();
    }

    /**
     * Puts the components in proper places in the Frame.
     */
    private void initGUI() {
        this.getContentPane().add(BorderLayout.NORTH, getControlPanel());
        this.getContentPane().add(BorderLayout.CENTER, getTablePanel());
        this.getContentPane().add(BorderLayout.SOUTH, getMessagePanel());
    }

    /**
     * This method is called by the the startup class to set the controller for this frame(view).
     * This method, in turn, sets the controller for all the internal components(views) that it may contain so that
     * those components can send the user actions to the controller.
     * @param al The controller is nothing but a class that implements ActionListner. The components fire actionevents and the
     * controller performs business rules according to the action command. It then updates the model and notifies the
     * components. This is standard MVC (Model-View-Controller) architecture.
     */
    public void setController(ActionListener al) {
        this.al = al;
        getControlPanel().addUserActionListener(al);
        getTablePanel().addUserActionListener(al);
    }

    /**
     * This method is called by the Controller to set the model for this frame(view).
     * This method, in turn, sets the appropriate models for its internal components.
     * @param cm This is the main model for the view. It should contain submodels for the components inside the frame (in this application, it contains only MessagePanel). This method knows what submodel is needed by what component and so it associates appropriate models with the components.
     */
    public void setModel(ClientModel cm) {
        cm.addObserver(getTablePanel());
        cm.getMessageModel().addObserver(getMessagePanel());
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public CaterersDataPanel getTablePanel() {
        return tablePanel;
    }

    public void setTablePanel(CaterersDataPanel tablePanel) {
        this.tablePanel = tablePanel;
    }

    public MessagePanel getMessagePanel() {
        return messagePanel;
    }
}
