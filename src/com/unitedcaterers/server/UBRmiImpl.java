package com.unitedcaterers.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unitedcaterers.UB;
import com.unitedcaterers.db.SecurityException;
import com.unitedcaterers.util.UBException;

/**
 * Since we need to make this a RMI Remote object we have to extend it from
 * UnicastRemoteObject and so we cannot extend it from UCServer. Instead, we
 * will instantiate UCServer object and delegate all the calls to it. In affect,
 * it is exactly same as UCServer except that it is a remote class.
 */

public class UBRmiImpl extends UnicastRemoteObject implements UB {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private final UBImpl		ucs;
	
	public UBRmiImpl(String dbfilename) throws RemoteException, UBException, SecurityException {
		ucs = new UBImpl(dbfilename);
	}
	
	// @Override
	// public String[][] searchCaterersByHotelName(String hotelName) throws
	// RemoteException, UCException {
	// return ucs.searchCaterersByHotelName(hotelName);
	// }
	
	// @Override
	// public String[][] searchCaterersByLocation(String location) throws
	// RemoteException, UCException {
	// return ucs.searchCaterersByLocation(location);
	// }
	
	@Override
	public String[][] searchCaterersByHotelNameAndLocation(String hotelName, String location) throws RemoteException, UBException {
		return ucs.searchCaterersByHotelNameAndLocation(hotelName, location);
	}
	
	// @Override
	// public String[][] getAllCaterers() throws RemoteException, UCException {
	// return ucs.getAllCaterers();
	// }
	
	@Override
	public boolean bookRoom(String customerid, String[] originalData) throws RemoteException, UBException {
		return ucs.bookRoom(customerid, originalData);
	}
	
}
