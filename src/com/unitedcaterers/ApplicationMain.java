package com.unitedcaterers;

import com.unitedcaterers.client.UrlyBirdClientController;
import com.unitedcaterers.client.UrlyBirdClientFrame;
import com.unitedcaterers.server.UrlyBirdRmiServer;

/**
 * This class is the entry point to the application. It determines what - the
 * client or the server, needs to be run and then starts up the required
 * component. CS
 * 
 * @autdhor Enthuware
 * @version 1.0
 */
public class ApplicationMain {
	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			startup("rmi");
		} else if ("alone".equalsIgnoreCase(args[0])) {
			startup("none");
		} else if ("server".equalsIgnoreCase(args[0])) {
			UrlyBirdRmiServer.startup();
		} else {
			System.out.println("Invalid argument to main. Supported options are: server, alone, or no arguments.");
		}
	}
	
	public static void startup(String clienttype) {
		UrlyBirdClientFrame cf = new UrlyBirdClientFrame();
		UrlyBirdClientController cc = new UrlyBirdClientController(cf, clienttype);
		cf.setSize(700, 700);
		cf.setLocationRelativeTo(null); // makes sure that the frame is centered
		cf.setVisible(true);
		cc.showAllRooms();
	}
}