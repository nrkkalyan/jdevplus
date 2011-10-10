package com.unitedcaterers.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import com.unitedcaterers.util.PropertiesDialog;
import com.unitedcaterers.util.UBException;

/**
 * This class provides the startup method for the RMI Server application. It
 * startsup the registry, instantiates the remote server object and binds it to
 * /RemoteUBServer name.
 * 
 * @author Enthuware
 * @version 1.0
 */
public class UBRmiServer {
	private static PropertiesDialog	pd	= new PropertiesDialog(null, true);
	
	public static void startup() throws UBException {
		Properties props = null;
		String name = "/RemoteUBServer";
		
		props = pd.loadProperties("suncertify.properties");
		if (props == null)
			throw new UBException("No suncertify.properties file found.");
		try {
			String host = props.getProperty("serverhost").trim();
			String port = props.getProperty("serverport").trim();
			LocateRegistry.createRegistry(Integer.parseInt(port));
			name = "rmi://" + host + ":" + port + "/RemoteUBServer";
			// Create the remote object
			UBRmiImpl theserver = new UBRmiImpl(props.getProperty("dbfile"));
			// Bind the remote object
			Naming.rebind(name, theserver);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UBException(e.getLocalizedMessage());
		}
	}
	
}
