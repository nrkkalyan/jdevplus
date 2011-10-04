package com.unitedcaterers.server;

import java.rmi.Naming;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.unitedcaterers.util.PropertiesDialog;

/**
 * This class provides the startup method for the RMI Server application. It
 * startsup the registry, instantiates the remote server object and binds it to
 * /RemoteUBServer name.
 * 
 * @author Enthuware
 * @version 1.0
 */
public class UrlyBirdRMIServer {
	private static Logger			logger	= Logger.getLogger("com.unitedcaterers.server.RMIServerMain");
	public static PropertiesDialog	pd		= new PropertiesDialog(null, true);
	
	public static void startup() {
		Properties props = null;
		String name = "/RemoteUBServer";
		boolean retryflag = true;
		
		while (retryflag) {
			props = pd.loadProperties("suncertify.properties");
			if (props == null)
				break;
			try {
				// This starts up the registry at port 1099 on the local
				// machine.
				java.rmi.registry.Registry regs = java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(props.getProperty("serverport")));
				logger.fine("RMI registry created on port :" + props.getProperty("serverport"));
				name = "rmi://localhost:" + props.getProperty("serverport").trim() + "/RemoteUBServer";
				logger.fine("Creating RMIUCServer with arguments: " + props.getProperty("dbfile") + " , " + props.getProperty("server.dbmagiccode") + "  by name : " + name);
				
				// Create the remote object
				RMIUCServerImpl theserver = new RMIUCServerImpl(props.getProperty("dbfile"));
				
				// Bind the remote object
				Naming.rebind(name, theserver);
				logger.fine("RMIUCServer bound.");
				JOptionPane.showMessageDialog(null, "RemoteUBServer is now bound and available for clients.");
				retryflag = false;
			} catch (Exception e) {
				System.out.println("RMIUCServer got exception while instantiation : " + e.getMessage());
				e.printStackTrace();
				int choice = JOptionPane.showConfirmDialog(null, "Unable to startup the server because of exception : " + e.getMessage() + ". Do you want to try again?",
						"United Caterers App", JOptionPane.YES_NO_OPTION);
				retryflag = (choice == JOptionPane.YES_OPTION);
				if (!retryflag)
					System.exit(0);
			}
		}
	}
	
}
