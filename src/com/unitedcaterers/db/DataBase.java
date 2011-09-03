package com.unitedcaterers.db;

/**
 * This is the database interface given in the requirements of the assignment. <br>
 * <br>
 * In "Bodgitt and Scarper" assignment given by Sun, the lock() method returns a
 * lock key and the same lock key is passed as an argument to update, delete,
 * and unlock methods. <br>
 * <br>
 * In "UrlyBird" assignment, the lock method does not return any key/lock cookie
 * and the update, delete, and unlock methods do not take lockCookie argument.
 * Yet, the assignment includes the requirement that only the client that locked
 * record should be able to unlock it. There are three ways to solve this issue
 * -
 * <ol>
 * <li>Modify the signatures of the interface methods to include a lock key and
 * explain it in details in your choices.txt. The justification for this
 * approach is as follows- We are building and exporting a stateless service
 * through RMI. Thus, it is not correct to provide a stateful object on server
 * per client just to implement the locking mechanism. In a stateless service,
 * ideally, the locking should be taken care of by the business service exposed
 * by the server. The client should not be locking and unlocking the db rows
 * explicilty. If at all this is required, then the signature of the lock and
 * unlock method should be changed to include lockCookie (lock cookie) so that
 * the service may still remain stateless.
 * <li>Ignore the requirement that the row can only be unlocked by the same
 * client. Explain in choices.txt that since the interface methods do not
 * support a lock key mechanism, this functionality will depend on identifying a
 * client based on its ip, which is unreliable. Further, lock/unlock methods are
 * not directly used by clients anyway. The clients use the higher level book()
 * method, so there is no chance of a client unlocking a row locked by some
 * other client.
 * <li>Associate a client proxy object with each connected client on the server.
 * This approach is described in some books and is implemented in
 * com.unitedcaterers.serverwithoutlockcookie package.
 * 
 * 
 * </ol>
 * <br>
 * <br>
 * In this interface, we have included both kind of methods - with as well as
 * without lock cookie. <br>
 * com.unitedcaterers.server package uses methods that take "cookie" parameter
 * while com.unitedcaterers.serverwithoutlockcookies package uses methods that
 * don't take "cookie" parameter. <br>
 * <br>
 * You can use either of the packages. Please see
 * com.unitedcaterers.ApplicationMain and
 * com.unitedcaterers.client.ClientController classes to see where you need to
 * make a change to select the appropriate package.
 * 
 * @author Enthuware
 * @version 1.0
 */
public interface DataBase {
	
	/**
	 * 
	 * Reads a record from the file.
	 * 
	 * @param recNo
	 *            The record number to be read.
	 * @return an array where each element is a record value.
	 */
	public String[] read(int recNo) throws RecordNotFoundException;
	
	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n]. Throws SecurityException if the record is locked with a key
	 * other than lockCookie.
	 * 
	 * @param recNo
	 *            The record to be updated.
	 * @param data
	 *            The values that need to be stored.
	 * @param lockCookie
	 *            The key using which the record was locked.
	 */
	public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException;
	
	/**
	 * Deletes a record, making the record number and associated data slot (row)
	 * available for reuse.
	 * 
	 * @param recNo
	 *            The record that needs to be deleted.
	 * @param lockCookie
	 *            The key using which the record was locked.
	 * @throws SecurityException
	 *             If the record is locked with a key other than lockCookie.
	 */
	public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException;
	
	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n]. (For example,
	 * "Queen" matches "Queen" or "Queens".)
	 * 
	 * @param criteria
	 *            The values that need to be searched.
	 * @return all the record numbers that match the criteria
	 */
	public int[] find(String[] criteria);
	
	/**
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 * 
	 * @param data
	 *            The values for the new record.
	 * @return The record number.
	 */
	public int create(String[] data) throws DuplicateKeyException;
	
	/**
	 * Locks a record so that it can only be updated or deleted by this client.
	 * Returned value is a string (lockCookie) that must be used when the record
	 * is unlocked, updated, or deleted. If the specified record is already
	 * locked by a different client, the current thread gives up the CPU and
	 * consumes no CPU cycles until the record is unlocked.
	 * 
	 * @param recNo
	 *            The record to be locked.
	 * @return A Lock Key that should be used in subsequent calls to update,
	 *         unlock, or delete.
	 */
	public long lock(int recNo) throws RecordNotFoundException;
	
	/**
	 * Releases the lock on a record. lockCookie must be the key that was
	 * returned when the record was locked; otherwise it throws
	 * SecurityException.
	 * 
	 * @param recNo
	 *            The record to be unlocked.
	 * @param lockCookie
	 *            The key that was used to lock the record.
	 */
	public void unlock(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException;
}
