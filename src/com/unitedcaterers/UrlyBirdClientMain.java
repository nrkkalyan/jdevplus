package com.unitedcaterers;

import com.unitedcaterers.client.UrlyBirdClientController;
import com.unitedcaterers.client.UrlyBirdClientFrame;

/**
 * This class starts the client side of the application. In a nutshell, it
 * instantiates ClientController and ClientFrame and ties thethem. It then sets
 * the ClientFrame visible. Note that this follows the MVC pattern, where
 * ClientModel is the model, ClientFrame is the View, and ClientController is
 * the Controller. ClientModel is not instantiated in this class because it is
 * instantiated by ClientController.
 */
public class UrlyBirdClientMain {
	
	/**
	 * Starts up the client.
	 * 
	 * @param clienttype
	 *            Specifies whether we want to use RMI or Socket implementation.
	 *            The value must be "rmi" or "socket" or "none". If the value of
	 *            "none" is given, a local database is used directly.
	 */
	public static void startup(String clienttype) {
		UrlyBirdClientFrame cf = new UrlyBirdClientFrame();
		UrlyBirdClientController cc = new UrlyBirdClientController(cf, clienttype);
		cf.setSize(700, 700);
		cf.setLocationRelativeTo(null); // makes sure that the frame is centered
		cf.setVisible(true);
		cc.doShowAllRooms();
	}
	
}
