/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unitedcaterers;

import java.rmi.RemoteException;

import com.unitedcaterers.util.UCException;

/**
 * 
 * @author Enthuware
 */
@Deprecated
public interface UCServerFactory extends java.rmi.Remote {
	
	/**
	 * Note that the return type here is UCServer and not ClientProxy (even
	 * though as you may have observed, RMIUCServerFactoryImpl actually creates
	 * ClientProxy object. The reason is that ClientProxy is a server side
	 * implementation detail, which the client doesn't care about. Client only
	 * cares about UCServer (or Database if you are exposing the lower level
	 * functionality on client side).
	 */
	UBServer getClientProxy() throws RemoteException, UCException;
}
