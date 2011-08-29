/**
 *
 */
package com.unitedcaterers.db;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class DBLockTester extends Thread {
	
	private static DataBaseImpl	data	= null;
	
	static {
		try {
			String filePath = "c:\\temp\\ucdb.db";
			data = new DataBaseImpl(filePath, "UCDB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new DBLockTester().startTests();
	}
	
	// Making it non-static so actual Thread classes will have their name in the
	// log msg
	private Logger	logger;
	
	public DBLockTester() {
		logger = Logger.getLogger("DBLockTester");
		// logger.setLevel(Level.FINEST);
		Handler[] ha = logger.getParent().getHandlers();
		
		Formatter f = new SimpleFormatter();
		ha[0].setFormatter(f);
		ha[0].setLevel(Level.FINEST);
		logger.getParent().setLevel(Level.ALL);
	}
	
	private static int	recNo	= 3;
	
	public void startTests() {
		try {
			
			for (int i = 0; i < 10; i++) {
				new UpdateRandomRecordThread().start();
				new UpdateRecordThread().start();
				new CreateRecordThread().start();
				new DeleteRecordThread().start();
				new FindRecordsThread().start();
			}
		} catch (Exception e) {
			logger.fine(e.getMessage());
		}
		
	}
	
	private class UpdateRandomRecordThread extends DBLockTester {
		public UpdateRandomRecordThread() {
			logger = Logger.getLogger("UpdRdmRecordThread");
		}
		
		@Override
		public void run() {
			String[] record = new String[6];
			record[0] = "India Cafe";
			record[1] = "Manhatton";
			record[2] = "Wedding";
			record[3] = "30";
			record[4] = "13.50";
			record[5] = "";
			int randomRecNo = (int) (Math.random() * 9);
			// a while loop would be better here, but what are the chances!
			if (randomRecNo == DBLockTester.recNo) {
				randomRecNo = (int) (Math.random() * 9);
			}
			
			Long lockkey = null;
			try {
				logger.fine("Thread " + getId() + " trying to lock record #" + randomRecNo);
				
				lockkey = data.lock(randomRecNo);
				
				logger.fine("Thread " + getId() + " trying to update record #" + randomRecNo);
				
				data.update(randomRecNo, record, lockkey);
				
				logger.fine("Thread " + getId() + " trying to unlock record #" + randomRecNo
						+ " on UpdatingRandomRecordThread");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					data.unlock(randomRecNo, lockkey);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logger.fine("Thread " + getId() + " Done.");
		}
	}
	
	private class UpdateRecordThread extends Thread {
		public UpdateRecordThread() {
			logger = Logger.getLogger("UpdateRecordThread");
		}
		
		@Override
		public void run() {
			String[] record = new String[6];
			record[0] = "India Cafe";
			record[1] = "Manhatton";
			record[2] = "Wedding";
			record[3] = "30";
			record[4] = "16.50";
			record[5] = "";
			Long lockkey = null;
			try {
				logger.fine(getId() + " trying to lock record #" + recNo);
				lockkey = data.lock(recNo);
				logger.fine(getId() + " trying to update record #" + recNo);
				data.update(recNo, record, lockkey);
				logger.fine(getId() + " trying to unlock record #" + recNo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					/*
					 * If you comment out this line, all other threads that are
					 * trying to lock the same record should keep waiting.
					 * However, UpdateRandomThread, which may try other record
					 * should keep working.
					 */
					data.unlock(recNo, lockkey);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logger.fine(getId() + " Done.");
		}
	}
	
	private class CreateRecordThread extends Thread {
		public CreateRecordThread() {
			logger = Logger.getLogger("CreateRecordThread");
		}
		
		@Override
		public void run() {
			String[] record = new String[6];
			record[0] = "India Cafe" + getId();
			record[1] = "Manhatton";
			record[2] = "Wedding Aswell";
			record[3] = "30";
			record[4] = "13.50";
			record[5] = "";
			
			Long lockkey = null;
			try {
				logger.fine(getId() + " trying to create a record");
				lockkey = data.lock(-1);
				data.create(record);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					data.unlock(-1, lockkey);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
			logger.fine(getId() + " Done.");
		}
	}
	
	private class DeleteRecordThread extends Thread {
		public DeleteRecordThread() {
			logger = Logger.getLogger("DeleteRecordThread");
		}
		
		@Override
		public void run() {
			Long lockkey = null;
			try {
				logger.fine(getId() + " trying to lock record #" + recNo);
				lockkey = data.lock(recNo);
				logger.fine(getId() + " trying to delete record #" + recNo);
				data.delete(recNo, lockkey);
				logger.fine(getId() + " trying to unlock record #" + recNo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					data.unlock(recNo, lockkey);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logger.fine(getId() + " Done");
		}
	}
	
	private class FindRecordsThread extends Thread {
		public FindRecordsThread() {
			logger = Logger.getLogger("FindRecordThread  ");
		}
		
		@Override
		public void run() {
			try {
				logger.fine(getId() + " trying to find records...");
				final String[] criteria = { "US Pizza", "Manhattan", null, null, null, null };
				final int[] results = data.find(criteria);
				
				for (int i = 0; i < results.length; i++) {
					logger.fine(results.length + " results found.");
					try {
						final String[] record = data.read(results[i]);
						logger.fine("Read Record " + i + " : " + record[0]);
					} catch (Exception e) {
						logger.log(Level.WARNING, "Exception in while reading record ", e);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.fine(this.getName() + " Done.");
		}
	}
	
	class SimpleFormatter extends Formatter {
		
		public SimpleFormatter() {
			super();
		}
		
		@Override
		public String format(LogRecord record) {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(record.getLoggerName() + " : " + formatMessage(record));
			sb.append("\n");
			
			return sb.toString();
		}
	}
}
