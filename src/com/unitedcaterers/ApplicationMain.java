package com.unitedcaterers;

import com.unitedcaterers.server.RMIServerMain;

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
			ClientMain.startup("rmi");
		} else if ("alone".equalsIgnoreCase(args[0])) {
			ClientMain.startup("none");
		} else if ("server".equalsIgnoreCase(args[0])) {
			RMIServerMain.startup();
		} else {
			System.out.println("Invalid argument to main. Supported options are: server, alone, or no arguments.");
		}
	}
}