package com.unitedcaterers.client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.unitedcaterers.util.PropertiesDialog;

/**
 * The purpose of this class is to capture initialization parameters such a
 * server hostname, port, from the user.
 * 
 * @author Enthuware
 */
public class UBClientPropertiesDialog extends PropertiesDialog implements ActionListener {
	
	private boolean				mLocalFlag;
	
	private final JButton		mOkButton				= new JButton("OK");
	private final JButton		mCancelButton			= new JButton("Cancel");
	private final JButton		mResetButton			= new JButton("Reset");
	private final JButton		mBrowseButton			= new JButton("Browse");
	private final JTextField	mFileNameSelectionText	= new JTextField("db-1x1.db");
	private final JTextField	mServerNameText			= new JTextField("localhost");
	private final JTextField	mServerPortText			= new JTextField("1099");
	
	public UBClientPropertiesDialog(JFrame parent) {
		super(parent);
		initGUI();
		// initGui();
		mOkButton.addActionListener(this);
		mCancelButton.addActionListener(this);
		mResetButton.addActionListener(this);
		mBrowseButton.addActionListener(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
	}
	
	public void setLocalFlag(boolean isLocal) {
		this.mLocalFlag = isLocal;
	}
	
	public boolean isLocalFlag() {
		return this.mLocalFlag;
	}
	
	@Override
	public void initGuiComponents() {
		mServerNameText.setEnabled(!mLocalFlag);
		mServerPortText.setEnabled(!mLocalFlag);
		mFileNameSelectionText.setEnabled(mLocalFlag);
		mBrowseButton.setEnabled(mLocalFlag);
		
		if (!mLocalFlag) {
			mServerNameText.setText(getProperties().getProperty("client.serverhost", "localhost"));
			mServerPortText.setText(getProperties().getProperty("client.serverport", "1099"));
			mFileNameSelectionText.setText("");
		} else {
			mServerNameText.setText("");
			mServerPortText.setText("");
			mFileNameSelectionText.setText(getProperties().getProperty("client.localdbfile", "ucdb.db"));
		}
		
	}
	
	/**
	 * We are using GridBagLayout as it is the most flexible and powerful
	 * standard layout manager. This is specially suitable when you design the
	 * GUI without using and IDE. IDEs typically use hardcoded size and location
	 * values which make the GUI screen very brittle. However, you can use
	 * simpler layout managers as well.
	 */
	private void initGUI() {
		this.setSize(370, 240);
		this.getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = null;
		
		gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 0);
		
		String text = "Please specify or confirm the following server properties.";
		
		this.getContentPane().add(new JLabel(text), gbc);
		
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		
		gbc.weightx = 0.0;
		this.getContentPane().add(new JLabel("Database File"), gbc);
		gbc.weightx = 1.0;
		this.getContentPane().add(mFileNameSelectionText, gbc);
		gbc.weightx = 0.0;
		this.getContentPane().add(mBrowseButton, gbc);
		
		gbc.gridy = 3;
		this.getContentPane().add(new JLabel("Server Host"), gbc);
		this.getContentPane().add(mServerNameText, gbc);
		
		gbc.gridy = 4;
		this.getContentPane().add(new JLabel("Server port"), gbc);
		this.getContentPane().add(mServerPortText, gbc);
		
		JPanel temppanel = new JPanel();
		GridLayout gl = new GridLayout(1, 3);
		gl.setHgap(20);
		temppanel.setLayout(gl);
		temppanel.add(mOkButton);
		temppanel.add(mResetButton);
		temppanel.add(mCancelButton);
		
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.insets = new java.awt.Insets(5, 25, 15, 25);
		this.getContentPane().add(temppanel, gbc);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if (src == mOkButton) {
			if (isLocalFlag()) {
				getProperties().setProperty("client.localdbfile", mFileNameSelectionText.getText());
			} else {
				getProperties().setProperty("client.serverhost", mServerNameText.getText());
				getProperties().setProperty("client.serverport", mServerPortText.getText());
			}
			synchronized (this) {
				this.status = OK;
				this.notifyAll();
			}
		} else if (src == mCancelButton) {
			System.exit(EXIT_ON_CLOSE);
			// synchronized (this) {
			// this.status = CANCEL;
			// this.notifyAll();
			// }
		} else if (src == mResetButton) {
			mFileNameSelectionText.setText("");
			// magiccodeTf.setText("");
			mServerNameText.setText("");
			mServerPortText.setText("");
		} else if (src == mBrowseButton) {
			File f = new File("."); // go to current directory - i.e. the
									// directory from where the application has
									// been run
			JFileChooser chooser = new JFileChooser(f);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					mFileNameSelectionText.setText(chooser.getSelectedFile().getCanonicalPath());
				} catch (Exception e) {
					logger.fine("Exception in getting canonical path. Using getName()");
					mFileNameSelectionText.setText(chooser.getSelectedFile().getName());
				}
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		Properties props = new Properties();
		System.out.println(props);
		UBClientPropertiesDialog pd = new UBClientPropertiesDialog(null);
		pd.setLocalFlag(true);
		pd.setProperties(props);
		int status = pd.showDialog();
		System.out.println(props);
	}
}
