package com.unitedcaterers;

import com.unitedcaterers.server.RMIServerMain;

/**
 * This class is the entry point to the application. It determines what - the
 * client or the server, needs to be run and then starts up the required
 * component.
 * 
 * @autdhor Enthuware
 * @version 1.0
 */
public class ApplicationMain {
	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			System.out
					.println("Invalid argument to main. Supported option are: rmiclient, socketclient, alone, rmiserver, and socketserver. Exiting.");
		} else if ("rmiclient".equalsIgnoreCase(args[0])) {
			ClientMain.startup("rmi");
		} else if ("alone".equalsIgnoreCase(args[0])) {
			ClientMain.startup("none");
		} else if ("rmiserver".equalsIgnoreCase(args[0])) {
			RMIServerMain.startup();
		} else {
			System.out
					.println("Invalid argument to main. Supported options are: rmiclient, socketclient, alone, rmiserver, or socketserver. Exiting.");
		}
	}
}