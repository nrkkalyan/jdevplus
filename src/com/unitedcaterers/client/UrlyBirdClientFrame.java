package com.unitedcaterers.client;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.unitedcaterers.client.gui.ControlPanel;
import com.unitedcaterers.client.gui.TablePanel;

/**
 * This class is the main View of the application and forms the V of MVC
 * pattern. It displays the menu and a table for caterers information. It sends
 * all the user interaction events to the Controller, which is nothing but an
 * ActionListner. Based on the event, Controller takes appropriate action such
 * as search or book, updates the model with new data, and then calls
 * notifyObservers() on the model.
 * 
 */
public class UrlyBirdClientFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * A panel that houses a menubar.
	 */
	private ControlPanel		controlPanel		= null;
	/**
	 * A panel that houses a JTable. It displays caterer information contained
	 * within ClientModel.
	 */
	private TablePanel			tablePanel			= null;
	
	/**
	 * 1. Constructs the main view frame. 2. instantiates ControlPanel (which
	 * contains MenuBar, and in future may contain ToolBar). 3. instantiates
	 * CaterersDataPanel(which contains a JTable). 4. instantiates MessagePanel
	 * (which displays helpful messages to the user). 5. finally initializes the
	 * GUI.
	 */
	public UrlyBirdClientFrame() {
		super("United Caterers Booking System");
		controlPanel = new ControlPanel();
		tablePanel = new TablePanel();
		this.getContentPane().add(BorderLayout.NORTH, controlPanel);
		this.getContentPane().add(BorderLayout.CENTER, tablePanel);
	}
	
	/**
	 * This method is called by the the startup class to set the controller for
	 * this frame(view). This method, in turn, sets the controller for all the
	 * internal components(views) that it may contain so that those components
	 * can send the user actions to the controller.
	 * 
	 * @param al
	 *            The controller is nothing but a class that implements
	 *            ActionListner. The components fire actionevents and the
	 *            controller performs business rules according to the action
	 *            command. It then updates the model and notifies the
	 *            components. This is standard MVC (Model-View-Controller)
	 *            architecture.
	 */
	public void setCPActionListener(ActionListener al) {
		controlPanel.setCPActionListener(al);
	}
	
	/**
	 * This method is called by the Controller to set the model for this
	 * frame(view). This method, in turn, sets the appropriate models for its
	 * internal components.
	 * 
	 * @param cm
	 *            This is the main model for the view. It should contain
	 *            submodels for the components inside the frame (in this
	 *            application, it contains only MessagePanel). This method knows
	 *            what submodel is needed by what component and so it associates
	 *            appropriate models with the components.
	 */
	public void setModel(ClientModel cm) {
		cm.addObserver(tablePanel);
	}
	
	public TablePanel getTablePanel() {
		return tablePanel;
	}
	
}
