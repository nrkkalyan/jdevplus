package com.unitedcaterers.server;

import com.unitedcaterers.UCServer;
import com.unitedcaterers.util.UCException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;




/**
 * Since we need to make this a RMI Remote object we have to extend it from UnicastRemoteObject and
 * so we cannot extend it from UCServer. Instead, we will instantiate UCServer object and delegate all the calls to it.
 * In affect, it is exactly same as UCServer except that it is a remote class.
 */

public class RMIUCServerImpl extends UnicastRemoteObject implements UCServer {
    
    private UCServerImpl ucs;
    
    public RMIUCServerImpl(String dbfilename, String magiccode) throws RemoteException, UCException {
        ucs = new UCServerImpl(dbfilename, magiccode);
    }
    
    @Override
    public String[][] searchCaterersByMaxGuests(int maxGuests) throws RemoteException, UCException {
        return ucs.searchCaterersByMaxGuests(maxGuests);
    }
    
    @Override
    public String[][] searchCaterersByLocation(String location) throws RemoteException, UCException {
        return ucs.searchCaterersByLocation(location);
    }
    
    @Override
    public String[][] searchCaterersByMaxGuestsAndLocation(int maxGuests, String location) throws RemoteException, UCException {
        return ucs.searchCaterersByMaxGuestsAndLocation(maxGuests, location);
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
