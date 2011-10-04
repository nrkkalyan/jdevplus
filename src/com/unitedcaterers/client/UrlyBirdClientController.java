package com.unitedcaterers.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.unitedcaterers.UBServer;
import com.unitedcaterers.client.gui.EightDigitsTextField;
import com.unitedcaterers.server.UCServerImpl;
import com.unitedcaterers.util.PropertiesDialog;

/**
 * This class is the controller of the client application. All the user actions
 * are handled by this class. Depending on user action (for example, user
 * clicking on a menu item), it uses appropriate server method and performs the
 * necessary action. Depending on the result of the operation, if the view has
 * to change, it updates the model and notifies the observers thus refreshing
 * the view.
 */
public class UrlyBirdClientController implements ActionListener {
	
	private static Logger			logger			= Logger.getLogger("com.unitedcaterers.client.ClientController");
	/**
	 * The application frame.
	 */
	private UrlyBirdClientFrame		mClientFrame	= null;
	/**
	 * The mainModel. It contains all the required data for the view. Methods of
	 * this class modify it and notify observers.
	 */
	private ClientModel				mClientModel	= null;
	/**
	 * The current UCServerInterface. It can be changed dynamically. You can
	 * have a option in Menu which allows the user to connect to remote or local
	 * database. All you need to do is create an appropriate DBAdapter and set
	 * it here.
	 */
	private UBServer				mUBServer		= null;
	/**
	 * The current search parameters. It is used to refresh the table.
	 */
	// private String currentQuery = "";
	private String					currentHotelName;
	private String					currentLocation;
	/**
	 * Denotes whether the current server is a local (in process) server or not.
	 */
	private final boolean			localflag;
	/**
	 * This dialog is used to capture connection properties.
	 */
	private final PropertiesDialog	mUBClientPropertiesDialog;
	
	/**
	 * This variable is required only because we have multiple implementations
	 * of the project: rmi and socket. In your actual project, you will not need
	 * this.
	 */
	// public static String mClientType = "rmi"; // other
	// values
	// are
	// "socket"
	// and
	// "none"
	
	/**
	 * This constructor takes a ClientFrame and the clientType, which should be
	 * rmi, socket, or none. If none is passed, it denotes that a no remote
	 * server is to be used. It does the following two things - - creates an
	 * object of class ClientModel, which contains the data needed by the
	 * ClientFrame to display to the user. - sets the clientframe as an observer
	 * to this model and notifies it.
	 * 
	 */
	public UrlyBirdClientController(UrlyBirdClientFrame clientFrame, String clientType) {
		this.mClientFrame = clientFrame;
		mClientModel = new ClientModel();
		mClientFrame.setCPActionListener(this);
		
		// The following two lines use Observer - Observable pattern provided by
		// Java
		// appFrame is (conceptually) an Observer and mainModel is Observable.
		// Note that appFrame itself does not implement java.util.Observer, it
		// actually is a facade for two Observers - JTable and MessagePanel,
		// which implement Observer
		mClientFrame.setModel(mClientModel);
		
		// This ensures that the view is updated after initialization.
		mClientModel.notifyObservers(new Boolean(true));
		
		mUBClientPropertiesDialog = new PropertiesDialog(mClientFrame, false);
		this.localflag = "none".equals(clientType);
		// mClientType = clientType;
		this.mClientFrame.addWindowListener(new WindowAdapter() {
			
			// closes the server side component only if the client is a
			// non-networked standalone client before exiting the JVM
			@Override
			public void windowClosing(WindowEvent we) {
				if (mUBServer != null && localflag) {
					// We know that in case of (non-networked) local mode,
					// currentServer actually
					// points to UCServerImpl object.
					// In case of networked mode, we don't know the actually
					// class of the object
					// referred to by currentServer
					((UCServerImpl) mUBServer).close();
				}
				System.exit(0);
			}
		});
		
		connectToServer(localflag);
		
	}
	
	/**
	 * It collects the required parameters from the user and then either
	 * connects to a local DB file or to a remote UCServer depending upon
	 * localFlag.
	 * 
	 * @param pLocalflag
	 *            If true, it means it is a standalone client
	 */
	private void connectToServer(boolean pLocalflag) {
		try {
			mUBClientPropertiesDialog.setLocalFlag(pLocalflag);
			Properties props = mUBClientPropertiesDialog.loadProperties("suncertify.properties");
			if (props == null) {
				return; // should not happen
			}
			
			if (pLocalflag) {
				UBServer newServer = new UCServerImpl(props.getProperty("dbfile"));
				// close the existing server if any
				if (this.mUBServer != null) {
					((UCServerImpl) this.mUBServer).close();
				}
				this.mUBServer = newServer;
			} else {
				String host = props.getProperty("serverhost");
				String port = props.getProperty("serverport");
				UBServer newServer = null;
				String name = "rmi://" + host + ":" + port + "/RemoteUBServer";
				logger.info("ClientController - RMI version - connecting to " + name);
				Remote remoteObj = Naming.lookup(name);
				
				// Now here, you have two choices. If you are running the server
				// from
				// com.unitedcaterers.server package (i.e. lock functionality
				// with lock cookie),
				// the remoteObj actually refers to UCServer interface.
				// So you need to use the following line -
				
				newServer = (UBServer) remoteObj;
				
				// close the currentServer only if it is a local one, in which
				// case
				// we know that it is an object of class UCServerImpl.
				if (this.mUBServer != null && this.localflag) {
					((UCServerImpl) this.mUBServer).close();
				}
				// JOptionPane.showMessageDialog(mClientFrame,
				// "Connected to RMI Server at " + name, "UC Client",
				// JOptionPane.INFORMATION_MESSAGE);
				// }
				this.mUBServer = newServer;
				// this.localflag = false;
				
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mClientFrame, "Exception occured in connecting to the server: " + e.getMessage(), "UC Message", JOptionPane.ERROR_MESSAGE);
			logger.log(Level.SEVERE, "Exception in connecting to server :" + e.getMessage(), e);
		}
	}
	
	/**
	 * All the action events in the application GUI, upon which some action has
	 * to be taken, data has to be changed, or the view has to be changed, come
	 * to this method. It identifies the action and delegates the control to
	 * appropriate doXXX method.
	 * 
	 * @param ae
	 *            The action event. ActionCommand identifies the action.
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand(); // ActionCommand is hjust a
												// string
												// that we use to determine the
												// intention of the user. Its
												// value is set in ControlPanel
		System.out.println("In  ClientController actionPerformed " + action);
		
		if ("EXIT".equals(action)) {
			doExit();
		} else if ("CONNECT_LOCAL".equals(action)) {
			new Thread() {
				
				@Override
				public void run() {
					connectToServer(true);
				}
			}.start();
		} else if ("CONNECT_REMOTE".equals(action)) {
			new Thread() {
				
				@Override
				public void run() {
					connectToServer(false);
				}
			}.start();
		}
		/*
		 * else if ("DISCONNECT".equals(action)) { if (this.mUBServer != null &&
		 * this.localflag) { ((UCServerImpl) this.mUBServer).close(); } // Note
		 * that there is nothing to do if the client is connected to a // remote
		 * server. mUBServer = null; } else if ("CLEAR_CATERERS".equals(action))
		 * { // The following statements can also be refactored into a method //
		 * doClearCaterers() as in cases further below. String[][] data = new
		 * String[0][0]; // This is done for simplicity. // You can also make it
		 * a static // final variable and reuse it // instead instantiating a
		 * new // String array everytime. mClientModel.setDisplayRows(data);
		 * mClientModel.notifyObservers(); //
		 * mainModel.getMessageModel().updateModel
		 * ("Please use File Menu to search for caterers."); //
		 * mainModel.getMessageModel().notifyObservers();
		 * 
		 * } else if (action.equals("SEARCH_CATERERS")) { searchCaterers(); }
		 */
		else if ("VIEWALL_CATERERS".equals(action)) {
			showAllRooms();
		} else if (action.startsWith("SEARCH_CATERERS_WITH_PARAMS")) {
			int i1 = action.indexOf(":");
			// int i2 = action.indexOf(",");
			// String name = null;
			// if (i2 > i1 + 1) {
			// name = action.substring(i1 + 1, i2);
			// }
			// String loc = null;
			// if (action.length() > i2 + 1) {
			// loc = action.substring(i2 + 1);
			// }
			
			String[] aSplit = action.substring(i1 + 1).split(",");
			String name = null;
			String loc = null;
			if (aSplit.length == 1) {
				name = aSplit[0];
			} else if (aSplit.length >= 2) {
				name = aSplit[0];
				loc = aSplit[1];
			}
			searchByHotelNameAndLocation(name, loc);
		} else if (action.indexOf("BOOK_CATERER") != -1) {
			bookRoom(); // ActionCommand for this would be like:
						// BOOK_CATERER:12
		} /*
		 * else if ("APP_HELP".equals(action)) {
		 * JOptionPane.showMessageDialog(mClientFrame,
		 * "To learn how to use the application please see \\docs\\userguide.txt "
		 * , "Help", JOptionPane.INFORMATION_MESSAGE); } else if
		 * ("ABOUT".equals(action)) {
		 * JOptionPane.showMessageDialog(mClientFrame,
		 * "Copyright, Enthuware Inc.", "About",
		 * JOptionPane.INFORMATION_MESSAGE); }
		 */
		
	}
	
	/**
	 * Books a caterer. .It identifies the caterer by looking at the row no.
	 * attached to the action parameter ( BOOK_CATERER:12, here 12 is the row
	 * index). .It calls the bookCaterer method on the server. .It pops up
	 * appropriate messages if something is wrong. .Finally, it refreshes the
	 * display.
	 * 
	 * @param action
	 *            The the complete action command that has row index attached to
	 *            it.
	 */
	public void bookRoom() {
		if (mUBServer == null) {
			JOptionPane.showMessageDialog(mClientFrame, "Please connect to a server before booking.", "Book Caterer", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String[] data = null;
		
		int ind = mClientFrame.getTablePanel().getSelectedIndex();
		if (ind == -1) {
			JOptionPane.showMessageDialog(mClientFrame, "Please select a caterer from the list of caterers first.", "Book Caterer", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		data = mClientModel.getDisplayRows()[ind];
		
		if (data[7] != null && data[7].trim().length() > 0) {
			JOptionPane.showMessageDialog(mClientFrame, "Room is already booked.", "Book Caterer", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		EightDigitsTextField customerIDTextField = new EightDigitsTextField();
		// KALYAN: Change to Booking D
		Object[] arrayMessage = { "Enter customer ID (8 digits only):", customerIDTextField };
		int value = JOptionPane.showConfirmDialog(mClientFrame, arrayMessage, "Input", JOptionPane.OK_CANCEL_OPTION);
		if (value == 0) {
			if (!customerIDTextField.isEditValid()) {
				JOptionPane.showMessageDialog(mClientFrame, "Invalid Customer id.(8 digits) ", "Book Caterer", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String customerid = customerIDTextField.getText();
			// you may also add validation code for customerid here. For
			// example, you can check if it contains any spaces or junk
			// characters
			try {
				
				boolean status = mUBServer.bookRoom(customerid, data);
				if (status) {
					searchByHotelNameAndLocation(currentHotelName, currentLocation);
				} else {
					JOptionPane.showMessageDialog(mClientFrame, "Sorry, Unable to book this caterer.", "Book Caterer", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(mClientFrame, "Unable to book the caterer. " + e.getMessage(), "Book Caterer", JOptionPane.INFORMATION_MESSAGE);
				searchByHotelNameAndLocation(currentHotelName, currentLocation);
			}
			
		}
	}
	
	/**
	 * Closes the connections and exits
	 */
	public void doExit() {
		
		// MsgDialog md = new MsgDialog(appFrame, "Exit System",
		// "Do you really want to exit?", new String[]{"Yes", "No"}, new
		// int[]{1, 2});
		// md.setSize(200, 100);
		int choice = JOptionPane.showConfirmDialog(mClientFrame, "Do you really want to exit?", "Exit System", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			if (mUBServer != null && UrlyBirdClientController.this.localflag) { // this
				// is
				// done
				// so
				// that
				// any
				// open
				// file
				// is
				// closed
				// properly
				// before
				// exiting
				((UCServerImpl) mUBServer).close();
			}
			// Note that there is nothing to do if it is connected to a remote
			// client except exiting.
			System.exit(0);
		}
	}
	
	/**
	 * It pops up standard imputdialog boxes to get the search criteria and then
	 * uses the searchCaterer(...) methods to retrive the matching caterers. It
	 * calls refreshView() method that updates the model and notifies observers
	 * thereby updating the view frame.
	 */
	// public void searchCaterers() {
	// Object obj = JOptionPane.showInputDialog(mClientFrame,
	// "How many number of guests do you want to cater to?(Click Cancel if no preference)",
	// "Max Guests",
	// JOptionPane.INFORMATION_MESSAGE, null, null, "50");
	// String hotelName = null;
	// if (obj != null) {
	// try {
	// hotelName = obj.toString().trim();
	// currentHotelName = hotelName;
	// } catch (Exception e) {
	// // do nothing or pop up error message.
	// }
	// }
	//
	// String location = null;
	// obj = JOptionPane.showInputDialog(mClientFrame,
	// "Which location are you in?(Click Cancel if no preference)",
	// "View Routes", JOptionPane.INFORMATION_MESSAGE, null, null,
	// "Iselin");
	// if (obj != null) {
	// location = obj.toString().trim();
	// currentLocation = location;
	// }
	//
	// searchByHotelNameAndLocation(hotelName, currentLocation);
	//
	// }
	
	public void showAllRooms() {
		searchByHotelNameAndLocation(null, null);
	}
	
	/**
	 * This is just a helper method that calls appropriate searchXXX method.
	 * 
	 * @param query
	 * @param maxguests
	 * @param location
	 */
	private void searchByHotelNameAndLocation(String hotelName, String location) {
		if (mUBServer == null) {
			JOptionPane.showMessageDialog(mClientFrame, "Please connect to a server first. ", "Unconnected", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			String[][] data = new String[0][0];
			data = mUBServer.searchCaterersByHotelNameAndLocation(hotelName, location);
			mClientModel.setDisplayRows(data);
			mClientModel.notifyObservers();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mClientFrame, "Exception occured in processing request : " + e.getMessage(), "UC Message", JOptionPane.ERROR_MESSAGE);
			logger.log(Level.WARNING, "Exception in processing request", e);
		}
		
	}
}
