package com.unitedcaterers.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.unitedcaterers.UBServer;
import com.unitedcaterers.util.UCException;

/**
 * Since we need to make this a RMI Remote object we have to extend it from
 * UnicastRemoteObject and so we cannot extend it from UCServer. Instead, we
 * will instantiate UCServer object and delegate all the calls to it. In affect,
 * it is exactly same as UCServer except that it is a remote class.
 */

public class RMIUCServerImpl extends UnicastRemoteObject implements UBServer {
	
	private final UCServerImpl	ucs;
	
	public RMIUCServerImpl(String dbfilename) throws RemoteException, UCException {
		ucs = new UCServerImpl(dbfilename);
	}
	
	@Override
	public String[][] searchCaterersByHotelName(String hotelName) throws RemoteException, UCException {
		return ucs.searchCaterersByHotelName(hotelName);
	}
	
	@Override
	public String[][] searchCaterersByLocation(String location) throws RemoteException, UCException {
		return ucs.searchCaterersByLocation(location);
	}
	
	@Override
	public String[][] searchCaterersByHotelNameAndLocation(String hotelName, String location) throws RemoteException, UCException {
		return ucs.searchCaterersByHotelNameAndLocation(hotelName, location);
	}
	
	@Override
	public String[][] getAllCaterers() throws RemoteException, UCException {
		return ucs.getAllCaterers();
	}
	
	@Override
	public boolean bookCaterer(String customerid, String[] originalData) throws RemoteException, UCException {
		return ucs.bookCaterer(customerid, originalData);
	}
	
}
