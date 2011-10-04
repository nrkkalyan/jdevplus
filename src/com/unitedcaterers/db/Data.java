package com.unitedcaterers.db;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unitedcaterers.util.UCException;

/**
 * This class implements the DataBase interface.
 * 
 * @author Enthuware
 * @version 1.0
 */
public class Data implements DB {
	
	private static Logger					logger					= Logger.getLogger("DataBaseImpl");
	private int								offset;
	private final String[]					fieldnames;
	private final HashMap<String, Short>	fieldmap;
	private int								recordlength;
	private final RandomAccessFile			ras;
	private static final String				CHARSET					= "US-ASCII";
	/** The bytes that store the "magic cookie" value */
	private static final int				MAGIC_COOKIE_BYTES		= 4;
	
	/** The bytes that store the total overall length of each record */
	private static final int				RECORD_LENGTH_BYTES		= 4;
	
	/** The bytes that store the number of fields in each record */
	private static final int				NUMBER_OF_FIELDS_BYTES	= 2;
	
	/** The bytes that store the length of each field name */
	private static final int				FIELD_NAME_BYTES		= 2;
	
	/** The bytes that store the fields length */
	private static final int				FIELD_LENGTH_BYTES		= 2;
	
	/**
	 * This hashmap holds the lock information for records.
	 */
	private final LockManager				locker					= new LockManager();
	
	/**
	 * This is the only constructor. It validates the magiccode at the begining
	 * of the database file and then if valid, initializes the internal
	 * structures.
	 * 
	 * @param dbfilename
	 *            The full file name (including path) that stores the data.
	 * @param magiccode
	 *            The magiccode that identifies the file as the file meant for a
	 *            given application.
	 * @throws IOException
	 *             This exception is thrown if there is a problem in
	 *             reading/writing the db file or if the given magiccode does
	 *             not match with the magiccode stored in the dbfile.
	 * @throws SecurityException
	 */
	public Data(String dbfilename) throws IOException, UCException {
		FileInputStream fis = new FileInputStream(dbfilename);
		DataInputStream dis = new DataInputStream(fis);
		
		int magicCookie = dis.readInt();
		if (magicCookie != DBConstants.MAGIC_COOKIE_REFERENCE) {
			throw new UCException("Invalid cookie in specified database file");
		}
		offset += MAGIC_COOKIE_BYTES + RECORD_LENGTH_BYTES + NUMBER_OF_FIELDS_BYTES;
		recordlength = dis.readInt();
		int nooffields = dis.readShort();
		fieldnames = new String[nooffields];
		logger.fine("No of fields : " + nooffields);
		
		fieldmap = new HashMap<String, Short>();
		
		for (int i = 0; i < nooffields; i++) {
			final int nameLength = dis.readShort();
			offset += FIELD_NAME_BYTES + FIELD_LENGTH_BYTES + nameLength;
			final byte[] fieldNameByteArray = new byte[nameLength];
			dis.read(fieldNameByteArray);
			fieldnames[i] = new String(fieldNameByteArray, CHARSET);
			short fl = dis.readShort();
			fieldmap.put(fieldnames[i], new Short(fl));
			
		}
		
		recordlength = recordlength + 1;// 2 bytes for deleted flag.
		dis.close();
		fis.close();
		
		ras = new RandomAccessFile(dbfilename, "rw");
		
		ras.seek(offset);
		logger.fine("Recordlength : " + recordlength);
		logger.info("DB Initialized.");
		
	}
	
	private final static byte	DELETEDROW_BYTE1	= 0X1;
	private final static byte	VALIDROW_BYTE1		= 0X0;
	
	/**
	 * Reads a record from the file.
	 * 
	 * @param recNo
	 *            The record to be returned. From 0 to N
	 * @return an array where each element is a record value.
	 * @throws RecordNotFoundException
	 */
	@Override
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		if (recNo < 0) {
			throw new RecordNotFoundException("No such record : " + recNo);
		}
		try {
			ras.seek(offset + recNo * recordlength);
			byte[] ba = new byte[recordlength];
			int noofbytesread = ras.read(ba);
			if (noofbytesread < recordlength) {
				throw new RecordNotFoundException("No such record/Insufficient data : " + recNo);
			}
			
			// String rec = new String(ba);
			if (ba[0] == DELETEDROW_BYTE1) {
				throw new RecordNotFoundException("This record has been deleted : " + recNo);
			}
			return parseRecord(new String(ba, CHARSET));
		} catch (IOException e) {
			throw new RecordNotFoundException("Unable to retrieve the record : " + recNo + " : " + e.getMessage());
		}
	}
	
	/**
	 * This method takes the record data in String format and returns an array
	 * of Strings that corresponds to the fields of the record.
	 * 
	 * @param recorddata
	 * @return array of Strings corresponding to the fields.
	 */
	private String[] parseRecord(String recorddata) {
		String[] returnValue = new String[fieldnames.length];
		int startind = 1;// first 1 bytes are for delete flag so ignore them.
		for (int i = 0; i < fieldnames.length; i++) {
			int fieldlength = (fieldmap.get(fieldnames[i])).intValue();
			returnValue[i] = recorddata.substring(startind, startind + fieldlength);
			startind = startind + fieldlength;
		}
		return returnValue;
	}
	
	/**
	 * This method takes the record number, record data, and the lockkey and
	 * update the record.
	 * 
	 * @param recNo
	 *            The record number to be updated
	 * @param data
	 *            New data for the record
	 * @param lockkey
	 *            The record should have been locked with this key.
	 */
	@Override
	public synchronized void update(int recNo, String[] data, long lockkey) throws RecordNotFoundException, SecurityException {
		if (recNo < 0) {
			throw new RecordNotFoundException("No such record : " + recNo);
		}
		if (data == null || data.length != fieldnames.length) {
			throw new SecurityException("Invalid Data");
		}
		if (lockkey == -1) {
			throw new SecurityException("Invalid lock key");
		}
		Long realkey = locker.getOwner(recNo);
		if (realkey == null) {
			throw new SecurityException("You have to lock the record first before updating it.");
		}
		
		if (realkey.equals(lockkey)) {
			try {
				ras.seek(offset + recNo * recordlength);
				byte[] ba = new byte[recordlength];
				int noofbytesread = ras.read(ba);
				if (noofbytesread < recordlength) {
					throw new RecordNotFoundException("No such record : " + recNo);
				}
				if (ba[0] == DELETEDROW_BYTE1) {
					throw new RecordNotFoundException("This record has been deleted : " + recNo);
				}
				ras.seek(offset + recNo * recordlength);
				ras.writeByte(VALIDROW_BYTE1);
				ras.write(getByteArray(data));
			} catch (IOException e) {
				throw new SecurityException("Unable to update the record : " + recNo + " : " + e.getMessage());
			}
		} else {
			throw new SecurityException("You do not own the lock for this record.");
		}
	}
	
	/**
	 * This method prepares a byte[] for writing to the file.
	 * 
	 * @param data
	 * @return A byte array of length recordsize containing the record data.
	 * @throws IOException
	 */
	private byte[] getByteArray(String[] data) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for (int i = 0; i < fieldnames.length; i++) {
			String field = data[i];
			short flength = (fieldmap.get(fieldnames[i])).shortValue();
			byte[] ca = new byte[flength];
			int j = 0;
			for (byte b : field.getBytes()) {
				ca[j++] = b;
			}
			for (int x = 0; x < flength; x++) {
				dos.write(ca[x]);
			}
		}
		
		dos.flush();
		dos.close();
		byte[] ba = baos.toByteArray();
		return ba;
	}
	
	/**
	 * This method deletes the record.
	 * 
	 * @param recNo
	 *            The record number that is to be deleted.
	 * @param lockkey
	 *            The record should have been locked using this key.
	 * @throws RecordNotFoundException
	 *             Thrown if this record number does not exist.
	 * @throws java.lang.SecurityException
	 *             Thrown if the lockkey does not match the key with which this
	 *             record was locked.
	 */
	@Override
	public synchronized void delete(int recNo, long lockkey) throws RecordNotFoundException, SecurityException {
		if (recNo < 0) {
			throw new RecordNotFoundException("No such record : " + recNo);
		}
		if (lockkey == -1) {
			throw new SecurityException("Invalid lock key");
		}
		Long realkey = locker.getOwner(recNo);
		if (realkey == null) {
			throw new SecurityException("You have to lock the record first before updating it.");
		}
		
		if (realkey.equals(lockkey)) {
			try {
				ras.seek(offset + recNo * recordlength);
				byte[] ba = new byte[recordlength];
				int noofbytesread = ras.read(ba);
				if (noofbytesread < recordlength) {
					throw new RecordNotFoundException("No such record : " + recNo);
				}
				
				if (ba[0] == DELETEDROW_BYTE1) {
					throw new RecordNotFoundException("This record has already been deleted : " + recNo);
				}
				ras.seek(offset + recNo * recordlength);
				ras.writeByte(DELETEDROW_BYTE1);
				
			} catch (IOException e) {
				throw new SecurityException("Unable to delete the record : " + recNo + " : " + e.getMessage());
			}
		}
	}
	
	/**
	 * Returns the record numbers that match the given criteria.
	 * 
	 * @param criteria
	 *            A string array of same length as the number of fields.
	 * @return An array of integers containing record numbers.
	 */
	@Override
	public synchronized int[] find(String[] criteria) {
		ArrayList<Integer> matchingindices = new ArrayList<Integer>();
		try {
			if (criteria == null || criteria.length != fieldnames.length) {
				return new int[0]; // return empty array if criteria is invalid.
			}
			ras.seek(offset);
			byte[] ba = new byte[recordlength];
			int recno = 0;
			
			while (ras.read(ba) == recordlength) {
				String rec = new String(ba, CHARSET);
				if (ba[0] == DELETEDROW_BYTE1) {
					recno++;
					continue;
				}
				String[] fielddata = parseRecord(rec);
				boolean match = true;
				for (int i = 0; i < fieldnames.length; i++) {
					if (criteria[i] == null) {
						continue;
					}
					if (!fielddata[i].trim().matches(criteria[i])) {
						match = false;
						break;
					}
				}
				if (match) {
					matchingindices.add(new Integer(recno));
				}
				recno++;
			}
			int noofmatches = matchingindices.size();
			int[] retvalue = new int[noofmatches];
			for (int i = 0; i < noofmatches; i++) {
				retvalue[i] = (matchingindices.get(i)).intValue();
			}
			return retvalue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * This method creates a new record with the given data. It uses slots
	 * occupied by previously deleted records if available.
	 * 
	 * @param data
	 *            The String array containing all the fields.
	 * @return record number.
	 * @throws DuplicateKeyException
	 *             Thrown if a record with same values for all the fields
	 *             already exists.
	 */
	@Override
	public synchronized int create(String[] data) throws DuplicateKeyException {
		if (data == null || data.length != fieldnames.length) {
			throw new DuplicateKeyException("Invalid Data");
		}
		int[] existingRecNos = find(data);
		if (existingRecNos != null && existingRecNos.length > 0) {
			throw new DuplicateKeyException("A record with given data already exists.");
		}
		
		try {
			int deletedRecNo = getDeletedRecordNo();
			ras.seek(offset + deletedRecNo * recordlength);
			ras.writeByte(VALIDROW_BYTE1);
			ras.write(getByteArray(data));
			return deletedRecNo;
		} catch (Exception e) {
			throw new DuplicateKeyException("Unable to create new record : " + e.getMessage());
		}
	}
	
	/**
	 * Searches the file to find a slot that contains a deleted record. If none
	 * is available, it returns the last+1 record number.
	 * 
	 * @return record number of first available deleted record.
	 */
	private int getDeletedRecordNo() {
		try {
			int retval = 0;
			ras.seek(offset);
			byte[] ba = new byte[recordlength];
			while (ras.read(ba) == recordlength) {
				if (ba[0] == DELETEDROW_BYTE1) {
					return retval;
				}
				retval++;
				ba = new byte[recordlength];
			}
			
			return retval;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Locks the record. If the record has been locked, it waits until it gets
	 * the lock.
	 * 
	 * A client should not try to lock a record that it has already locked since
	 * it will lead to a deadlock. This method does not attempt to check or
	 * prevent deadlock arising because of multiple client requesting locks in
	 * cyclic pattern. For example, let's say Client A holds lock for Rec3 and
	 * Client B hold lock for Rec 5. Now, if Client A requests lock for Rec 5
	 * and Client B requests lock for Rec 3, there will be a deadlock. In
	 * general, locking is done by the application and so deadlock problem
	 * should be taken care of by the application and not by the database. The
	 * Database has no knowledge of who is requesting the lock for what reason.
	 * Application code knows that and is thus better prepared to prevent
	 * deadlocks.
	 * 
	 * @param recNo
	 * @return The lock key.
	 * @throws RecordNotFoundException
	 */
	@Override
	public long lock(int recNo) throws RecordNotFoundException {
		return locker.lock(recNo);
	}
	
	/**
	 * Unlocks the record.
	 * 
	 * @param recNo
	 *            Record to be unlocked. -1 implies unlock the database.
	 * @param lockkey
	 *            The record (or the database) should have been locked using
	 *            this key.
	 * @throws RecordNotFoundException
	 *             Thrown if there is no record by this record number.
	 * @throws java.lang.SecurityException
	 *             Thrown if the lockkey does not match the key with which the
	 *             record was locked.
	 */
	@Override
	public void unlock(int recNo, long lockkey) throws RecordNotFoundException, SecurityException {
		locker.unlock(recNo, lockkey);
	}
	
	/**
	 * This Inner class implements a primitive lock manager. It keeps a
	 * hashtable that stores the record no. as key and an Object (place holder)
	 * as value that denotes that somebody is using the record.
	 */
	private class LockManager {
		
		private final HashMap<Integer, Long>	locks		= new HashMap<Integer, Long>();
		boolean									dblocked	= false;
		long									dbkey		= -1;
		
		public Long getOwner(int recordNo) {
			return locks.get(recordNo);
		}
		
		/**
		 * This method waits till all the locks (entries in the locks HashMap)
		 * are released. It then sets the dblocked flag and returns.
		 */
		private synchronized long lockDB() {
			while (dblocked || locks.size() != 0) {
				try {
					logger.fine("   Thread #" + Thread.currentThread().getId() + " waiting for DB Lock... dblocked flag = " + dblocked + " locker size = " + locks.size());
					wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logger.fine("   Thread #" + Thread.currentThread().getId() + " got DB Lock.");
			
			dblocked = true;
			dbkey = new java.util.Date().getTime();
			return dbkey;
		}
		
		/**
		 * This method waits till the locks HashMap returns null for the
		 * requested record no. Then it puts a key for that record number and
		 * return that key.
		 * 
		 * @param int recordNo The record no. to be locked.
		 * @return The key. This key should be used to unlock or update/delete
		 *         methods.
		 */
		public synchronized long lock(int recordNo) {
			if (recordNo == -1) {
				return lockDB();
			}
			
			Long key = locks.get(recordNo);
			if (key == null && !dblocked) {
				key = new java.util.Date().getTime();
				locks.put(recordNo, key);
				logger.fine("   Thread #" + Thread.currentThread().getId() + " get lock for Record " + recordNo);
				return key;
			} else {
				while (locks.get(recordNo) != null || dblocked) {
					try {
						logger.fine("   Thread #" + Thread.currentThread().getId() + " entering wait for Record " + recordNo);
						wait();
					} catch (Exception e) {
						logger.warning("Some Exception in waiting for record :" + recordNo);
					}
				}
				logger.fine("   Thread #" + Thread.currentThread().getId() + " got lock for Record " + recordNo);
				key = new java.util.Date().getTime();
				locks.put(recordNo, key);
				return key;
			}
		}
		
		/**
		 * This method removes the value stored in the locks HashMap for
		 * recordNo. and notifies all the threads waiting on 'this'.
		 * 
		 * @param int recordNo The record no. to be unlocked.
		 */
		public synchronized void unlock(int recordNo, long lockkey) throws SecurityException {
			if (recordNo == -1) {
				if (lockkey != -1 && dbkey == lockkey) {
					dblocked = false;
					logger.fine("   Thread #" + Thread.currentThread().getId() + " unlocking DB");
					notifyAll();
					return;
				} else {
					throw new SecurityException("You don't own DB Lock");
				}
			}
			
			Long key = locks.get(recordNo);
			
			if (key != null && lockkey == key) {
				locks.remove(recordNo);
				logger.fine("   Thread #" + Thread.currentThread().getId() + " unlocking Record " + recordNo);
				notifyAll();
			} else {
				throw new SecurityException("You don't own lock for this record : " + recordNo);
			}
			
		}
		
	}
	
	/**
	 * This class is just to test the locking functionality. It implements a
	 * thread that continuously acquires and releases locks on random record
	 * numbers including -1 (i.e. db lock) Create multiple instances of this
	 * class and start them to simulate multiple clients trying to acquire
	 * locks.
	 */
	public static class LockTestThread extends Thread {
		
		private String	name	= "";
		private Data	dbi		= null;
		
		public LockTestThread(String name, Data db) {
			LockTestThread.this.dbi = db;
			LockTestThread.this.name = name;
			
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					int recno = (int) (Math.random() * 10) - 1; // substract 1
																// so that recno
																// can also be
																// -1 for
																// database
																// lock.
					int sleeptime1 = (int) (Math.random() * 10);
					int sleeptime2 = (int) (Math.random() * 10);
					if (recno == -1) {
						sleeptime2 = 5;
					}
					// System.out.println(name + " Sleeping : " +
					// sleeptime1+"   "+sleeptime2);
					Thread.sleep(sleeptime1 * 1000);
					long tm1 = System.currentTimeMillis();
					Long key = dbi.lock(recno);
					long tm2 = System.currentTimeMillis();
					System.out.println(name + " got lock for : " + recno + " in " + (tm2 - tm1) + " millis.");
					Thread.sleep(sleeptime2 * 1000);
					// System.out.println(name + " unlocking DB after " +
					// sleeptime2 + " secs.");
					dbi.unlock(recno, key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Closes the database.
	 */
	public void close() {
		try {
			this.lock(-1); // waits till all clients are done
		} catch (RecordNotFoundException ex) {
			Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		synchronized (this) {
			try {
				this.ras.close();
				this.locker.locks.clear();
				logger.info("DataBaseImpl.close() closed the db file.");
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Exception in DataBaseImpl.close().", e);
			}
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		Data db = new Data("db-1x1.db");
		LockTestThread lt1 = new LockTestThread("DB-1", db);
		lt1.start();
		
		LockTestThread lt2 = new LockTestThread("DB-2", db);
		lt2.start();
		
		LockTestThread lt3 = new LockTestThread("DB-3", db);
		lt3.start();
		
		LockTestThread lt4 = new LockTestThread("DB-4", db);
		lt4.start();
	}
}
