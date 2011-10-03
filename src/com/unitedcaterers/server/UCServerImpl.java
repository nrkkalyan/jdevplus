package com.unitedcaterers.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unitedcaterers.UBServer;
import com.unitedcaterers.db.DB;
import com.unitedcaterers.db.Data;
import com.unitedcaterers.db.RecordNotFoundException;
import com.unitedcaterers.util.UCException;

/**
 * This abstract class captures all the business methods required by the client.
 * The networked (RMI/Socket based) and non-networked servers, both, extend from
 * this class. The benefit of this approach is that the client can instantiate
 * either a networked server or a non-networked server without having any
 * special code for either of them. Further, we can build higher level
 * operations using low level calls supported by the DataBase class in this
 * class, which will become available to both kinds of servers. For example, you
 * can implement a method say, public boolean bookCaterer(int recordNo, String
 * customerid) { //lock //read //modify //unlock return true/false; }
 * 
 * The above code will be same for both the servers whether it is RMI or Socket.
 * Implementing it in the abstract class will let other adpters inherit it.
 * 
 * Since a server can be either networked or non-networked, all methods throw
 * RemoteException besides throwing a generic UCException, which is an
 * application exception. The implementation of the methods for the
 * non-networked server will only throw UCException, while the implementation of
 * the methods for the networked server will wrap the UCException into
 * RemoteException. The clients should be prepared to handle both the
 * exceptions.
 */

public class UCServerImpl implements UBServer {
	protected static Logger	logger	= Logger.getLogger("com.unitedcaterers.server.UCServer");
	private DB				db;
	
	// a boolean flag to determine whether there is a valid db or not.
	public UCServerImpl(String dbfilename) throws RemoteException, UCException {
		try {
			db = new Data(dbfilename);
		} catch (IOException ioe) {
			throw new UCException("Unable to connect to the database. : " + ioe.getMessage());
		}
	}
	
	// @Override
	// public String[][] searchCaterersByHotelName(String hotelName) throws
	// RemoteException, UCException {
	// if (db == null) {
	// throw new
	// UCException("Connection to the database has been closed. Must restart/recreate the server.");
	// }
	// String[] criteria = new String[] { hotelName, null, null, null, null,
	// null, null };
	// return findAndReturnData(criteria);
	// }
	
	// @Override
	// public String[][] searchCaterersByLocation(String location) throws
	// RemoteException, UCException {
	// if (db == null) {
	// throw new
	// UCException("Connection to the database has been closed. Must restart/recreate the server.");
	// }
	//
	// String[] criteria = new String[] { null, location, null, null, null,
	// null, null };
	// return findAndReturnData(criteria);
	//
	// }
	
	@Override
	public String[][] searchCaterersByHotelNameAndLocation(String hotelName, String location) throws RemoteException, UCException {
		if (db == null) {
			throw new UCException("Connection to the database has been closed. Must restart/recreate the server.");
		}
		String[] criteria = new String[] { hotelName, location, null, null, null, null, null };
		return findAndReturnData(criteria);
		
	}
	
	// @Override
	// public String[][] getAllCaterers() throws RemoteException, UCException {
	// if (db == null) {
	// throw new
	// UCException("Connection to the database has been closed. Must restart/recreate the server.");
	// }
	// String[] criteria = new String[] { null, null, null, null, null, null,
	// null };
	// return findAndReturnData(criteria);
	// }
	
	@Override
	public boolean bookRoom(String customerid, String[] originalData) throws RemoteException, UCException {
		if (db == null) {
			throw new UCException("Connection to the database has been closed. Must restart/recreate the server.");
		}
		// you might also want to implement server side data validation on
		// customerid here.
		boolean status = false;
		Long lockkey = null;
		int recordNo = -100;
		try {
			recordNo = Integer.parseInt(originalData[0]);
			lockkey = db.lock(recordNo);
			String[] data = db.read(recordNo);
			if (data[6] == null || data[6].trim().length() == 0) {
				boolean datachanged = false;
				for (int n = 1; n < originalData.length; n++) {
					// First element of original data is the record number so
					// second element of originalData matches with the first
					// element of data.
					if (!originalData[n].trim().equals(data[n - 1].trim())) {
						datachanged = true;
						break;
					}
				}
				
				if (datachanged) {
					// row will be unlocked in finally clause
					throw new UCException("The caterer data was updated. Please refresh your view and try again.");
				}
				data[6] = customerid;
				db.update(recordNo, data, lockkey);
				status = true;
			} else if (data[6].trim().equals(customerid)) {
				status = true;
			} else
				throw new UCException("This caterer is already booked.");
			
			return status;
			
		} catch (RecordNotFoundException re) {
			throw new UCException("Unable to book the caterer because it does not exist.");
		} catch (com.unitedcaterers.db.SecurityException se) {
			throw new UCException("Unable to book the caterer because of SecurityException. This should not happen.");
		} finally {
			try {
				if (lockkey != null)
					db.unlock(recordNo, lockkey);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Serious problem in unlocking. Should not happen.", e);
			}
		}
		
	}
	
	/**
	 * This is an additional method (not specified in the DataBase interface)
	 * that is very useful for the application. It returns the actual data in
	 * String[][] format instead of just the record numbers that match the given
	 * criteria. The first value of the String[] is the record number the rest
	 * are the fields.
	 * 
	 * @param criteria
	 *            A string array of same length as the number of fields.
	 * @return An array of integers containing record numbers.
	 */
	public String[][] findAndReturnData(String[] criteria) throws UCException {
		if (db == null) {
			throw new UCException("Connection to the database has been closed. Must restart/recreate the server.");
		}
		String[][] retval = null;
		Long lockkey = null;
		try {
			// Here is an important situation that you need to understand.
			// Let's say User A's search results in two records: 3, 5
			// Now, after getting the record numbers using the DataBase.find()
			// method,
			// User A, tries to retrieve the record data using
			// DataBase.read(recordNo)
			// method. It reads record 3, and before it could read record 5,
			// User B
			// goes ahead and updates(or deletes) record 5. So when User A,
			// tries
			// to retrieve record 5, either it will not satisfy the seach
			// criteria
			// (if the record was updated by User B) or it will get an exception
			// (if
			// the record was deleted by User B)
			
			// There are two approahes to solve this problem:
			
			// You can lock the db to make sure that User A finds and reads the
			// records
			// without anybody else modifying the records.
			// This is a passimistic approach because we are assuming that
			// somebody
			// might modify the records while User A is reading them.
			
			// There is another way to accomplish this without locking the db.
			// After getting the record numbers using the find(criteria)
			// method, you can read the records one by one. But you must match
			// each record
			// with the critiria again to verify that the record has not been
			// changed.
			// You also need to catch the exceptions in case the record was
			// deleted.
			// This is optimistic approach because here you are assuming that
			// records
			// will not be modified while User A is reading them.
			
			// Here we are using the pessimistic approach because it is easier
			// to understand,
			// implement, and debug. However, its performance is less than
			// optimal.
			
			lockkey = db.lock(-1);
			int[] ia = db.find(criteria);
			retval = new String[ia.length][];
			for (int i = 0; i < ia.length; i++) {
				String[] data = db.read(ia[i]);
				retval[i] = new String[data.length + 1];
				retval[i][0] = "" + ia[i];
				for (int j = 0; j < data.length; j++) {
					retval[i][j + 1] = data[j].trim();
				}
			}
			db.unlock(-1, lockkey);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception in findAndReturnData()", e);
			return retval;
		} finally {
			try {
				if (lockkey != null)
					db.unlock(-1, lockkey);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Serious problem in unlocking. Should not happen.", e);
			}
		}
		return retval;
	}
	
	/**
	 * This method is used by the local clients so that when the client exits,
	 * it can close the database file cleanly. Ideally, the DataBase interface
	 * should have a close() method. Since it does not, db needs to be cast to
	 * DataBaseImpl
	 */
	public void close() {
		((Data) db).close();
		db = null;
	}
}
