package com.unitedcaterers.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

/**
 * This is a helper class that implements most of the logic to capture the
 * properties to run the server or the client through a GUI. The subclass will
 * have to implement initControls() method, in which it can build the GUI with
 * any number of text field, checkboxes etc. It should have OK and Cancel
 * Buttons. To use it, just instantiate the subclass and call showDialog(). This
 * call blocks until the user clicks OK or Cancel. The values entered in the
 * input fields are stored in properties object, which can be accessed using
 * getProperties() method.
 * 
 * @author Enthuware
 * @version 1.0
 */
public class PropertiesDialog extends JDialog implements ActionListener {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID		= 1L;
	protected int				mStatus					= -1;
	public static final int		OK						= 0;
	public static final int		CANCEL					= -1;
	private Properties			mProperties;
	private final JButton		mOkButton				= new JButton("OK");
	private final JButton		mCancelButton			= new JButton("Cancel");
	private final JButton		mResetButton			= new JButton("Reset");
	private final JButton		mBrowseButton			= new JButton("Browse");
	private final JTextField	mFileNameSelectionText	= new JTextField("db-1x1.db");
	private final JTextField	mServerHostText			= new JTextField("localhost");
	private final JTextField	mServerPortText			= new JTextField("1099");
	private final boolean		mIsRmiServer;
	private boolean				mLocalFlag;
	
	public PropertiesDialog(JFrame parentFrame, final boolean isRmiServer) {
		super(parentFrame);
		mIsRmiServer = isRmiServer;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(370, 240);
		mOkButton.addActionListener(this);
		mCancelButton.addActionListener(this);
		mResetButton.addActionListener(this);
		mBrowseButton.addActionListener(this);
		initGui();
		setTitle("Kalyan");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				PropertiesDialog.this.mStatus = CANCEL;
				synchronized (PropertiesDialog.this) {
					PropertiesDialog.this.notifyAll();
				}
			}
		});
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.gui.PropertiesDialog#initGuiComponents()
	 */
	public void initGuiComponents() {
		// mServerHostText.setVisible(!mIsRmiServer);
		mServerHostText.setEnabled(!mLocalFlag);
		mServerPortText.setEnabled(!mLocalFlag);
		mFileNameSelectionText.setEnabled(mLocalFlag || mIsRmiServer);
		mBrowseButton.setEnabled(mLocalFlag || mIsRmiServer);
		if (mIsRmiServer) {
			mFileNameSelectionText.setText(getProperties().getProperty("dbfile", "db-1x1.db"));
			mServerPortText.setText(getProperties().getProperty("serverport", "1099"));
		} else {
			if (!mLocalFlag) {
				mServerHostText.setText(getProperties().getProperty("serverhost", "localhost"));
				mServerPortText.setText(getProperties().getProperty("serverport", "1099"));
				mFileNameSelectionText.setText("");
			} else {
				mServerHostText.setText("");
				mServerPortText.setText("");
				mFileNameSelectionText.setText(getProperties().getProperty("dbfile", "db-1x1.db"));
			}
		}
	}
	
	/**
	 * 
	 */
	private void initGui() {
		setSize(370, 240);
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = null;
		
		gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 0);
		
		String text = "<html>Please specify or confirm the following server properties.<br><br></html>";
		
		this.getContentPane().add(new JLabel(text), gbc);
		
		int pos = 2;
		gbc.gridy = pos++;
		gbc.gridwidth = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		
		gbc.weightx = 0.0;
		this.getContentPane().add(new JLabel("Database File"), gbc);
		gbc.weightx = 1.0;
		this.getContentPane().add(mFileNameSelectionText, gbc);
		gbc.weightx = 0.0;
		this.getContentPane().add(mBrowseButton, gbc);
		
		if (!mIsRmiServer) {
			gbc.gridy = pos++;
			this.getContentPane().add(new JLabel("Server Host"), gbc);
			this.getContentPane().add(mServerHostText, gbc);
		}
		
		gbc.gridy = pos++;
		this.getContentPane().add(new JLabel("Server port"), gbc);
		this.getContentPane().add(mServerPortText, gbc);
		
		JPanel temppanel = new JPanel();
		GridLayout gl = new GridLayout(1, 3);
		gl.setHgap(20);
		temppanel.setLayout(gl);
		temppanel.add(mOkButton);
		temppanel.add(mResetButton);
		temppanel.add(mCancelButton);
		
		gbc.gridy = pos++;
		gbc.gridwidth = 3;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.insets = new java.awt.Insets(5, 25, 15, 25);
		this.getContentPane().add(temppanel, gbc);
		
	}
	
	public int showDialog() {
		initGuiComponents();
		setLocationRelativeTo(null);
		setVisible(true);
		synchronized (this) {
			try {
				this.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setVisible(false);
		return mStatus;
	}
	
	/**
	 * @param localFlag
	 *            the localFlag to set
	 */
	public void setLocalFlag(boolean localFlag) {
		this.mLocalFlag = localFlag;
	}
	
	/**
	 * @return the localFlag
	 */
	public boolean isLocalFlag() {
		return mLocalFlag;
	}
	
	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.mProperties = properties;
	}
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return mProperties;
	}
	
	/**
	 * Reads a property file and loads the properties object. It then calls
	 * showDialog() to display the GUI. Values in this properties object are
	 * used as default values in the input fields. If the user clicks OK, the
	 * new values are stored back into the file. Instead of using showDialog()
	 * directly, this method can be used to have the additional functionality of
	 * loading the properties from a file and saving them back in the same file.
	 */
	public Properties loadProperties(String fileName) {
		Properties prop = new Properties();
		File file = null;
		try {
			file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();
			
			setProperties(prop);
			int status = showDialog();
			if (status == OK) {
				file = new File(fileName);
				FileOutputStream fos = new FileOutputStream(file);
				prop.store(fos, "Settings Changed");
				fos.close();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(rootPane, "Error Occured :" + e.getLocalizedMessage());
		}
		return prop;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if (src == mOkButton) {
			if (mLocalFlag) {
				getProperties().setProperty("dbfile", mFileNameSelectionText.getText());
			} else {
				getProperties().setProperty("serverhost", mServerHostText.getText());
				getProperties().setProperty("serverport", mServerPortText.getText());
			}
			synchronized (this) {
				this.mStatus = OK;
				this.notifyAll();
			}
		} else if (src == mCancelButton) {
			System.exit(0);
			synchronized (this) {
				this.mStatus = CANCEL;
				this.notifyAll();
			}
		} else if (src == mResetButton) {
			mFileNameSelectionText.setText("");
			mServerHostText.setText("");
			mServerPortText.setText("");
		} else if (src == mBrowseButton) {
			final JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setFileFilter(new DBFileFilter());
			fileChooser.setDialogTitle("Video directory locator");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnValue = fileChooser.showOpenDialog(this);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					mFileNameSelectionText.setText(fileChooser.getSelectedFile().getCanonicalPath());
				} catch (IOException e) {
					mFileNameSelectionText.setText(fileChooser.getSelectedFile().getName());
				}
			}
		}
		
	}
	
	private class DBFileFilter extends FileFilter {
		
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			if (f.getName().toLowerCase().endsWith(".db")) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		public String getDescription() {
			return "UrlyBird DB(*.db)";
		}
		
	}
	
}
