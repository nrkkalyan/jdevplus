package com.unitedcaterers;

import java.rmi.RemoteException;
import com.unitedcaterers.util.UCException;

/**
 * This interface captures all the business methods required by the client.
 * Both - The networked server (RMI as well as Socket based) and non-networked server, implement this interface.
 * The benefit of this approach is that the client can instantiate either a networked server
 * or a non-networked server without having any special code for either of them.
 *
 * Since a server can be either networked or non-networked, all methods throw RemoteException besides
 * throwing a generic UCException, which is an application exception. The implementation of the methods
 * for the non-networked server will only throw UCException, while the implementation of the methods
 * for the networked server will wrap the UCException into RemoteException. The clients should be prepared
 * to handle both the exceptions.
 * */

public interface UCServer extends java.rmi.Remote
{
  String[][] searchCaterersByMaxGuests(int maxGuests) throws RemoteException, UCException;
  String[][] searchCaterersByLocation(String location) throws RemoteException, UCException;
  String[][] searchCaterersByMaxGuestsAndLocation(int maxGuests, String location) throws RemoteException, UCException;
  String[][] getAllCaterers() throws RemoteException, UCException;
  boolean bookCaterer(String customerid, String[] originalData) throws RemoteException, UCException;

}

