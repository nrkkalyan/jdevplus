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

import com.unitedcaterers.util.PropertiesDialog;

/**
 * The purpose of this class is to capture initialization parameters such a
 * server hostname, port, from the user.
 * 
 * @author Enthuware
 */
public class UCClientPropertiesDialog extends PropertiesDialog implements ActionListener {
	
	private final JButton	okBt	= new JButton("OK"), resetBt = new JButton("Reset"), cancelBt = new JButton(
											"Cancel"), browseBt = new JButton("Browse");
	private final JTextField	dbfileTf	= new JTextField(), magiccodeTf = new JTextField(),
			serverTf = new JTextField(), serverportTf = new JTextField();
	private boolean				localflag;
	
	public UCClientPropertiesDialog(JFrame parent) {
		super(parent);
		initGUI();
		okBt.addActionListener(this);
		cancelBt.addActionListener(this);
		resetBt.addActionListener(this);
		browseBt.addActionListener(this);
	}
	
	public void setLocal(boolean isLocal) {
		this.localflag = isLocal;
	}
	
	public boolean isLocal() {
		return this.localflag;
	}
	
	@Override
	public void initControls() {
		if (!localflag) {
			serverTf.setEnabled(true);
			serverportTf.setEnabled(true);
			serverTf.setText(getProperties().getProperty("client.serverhost", "localhost"));
			serverportTf.setText(getProperties().getProperty("client.serverport", "1099"));
			
			dbfileTf.setText("");
			magiccodeTf.setText("");
			dbfileTf.setEnabled(false);
			magiccodeTf.setEnabled(false);
			browseBt.setEnabled(false);
			
		} else {
			serverTf.setText("");
			serverportTf.setText("");
			serverTf.setEnabled(false);
			serverportTf.setEnabled(false);
			
			dbfileTf.setEnabled(true);
			magiccodeTf.setEnabled(true);
			browseBt.setEnabled(true);
			dbfileTf.setText(getProperties().getProperty("client.localdbfile", "ucdb.db"));
			magiccodeTf.setText(getProperties().getProperty("client.localdbmagiccode", "UCDB"));
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
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new java.awt.Insets(5, 5, 5, 5);
		
		String text = "<html><h3><font color='navy'>Please specify or confirm the following server properties.</font></h3></html>";
		
		this.getContentPane().add(new JLabel(text), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 10, 0, 0);
		this.getContentPane().add(new JLabel("Database File"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 0);
		this.getContentPane().add(dbfileTf, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 15);
		this.getContentPane().add(browseBt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 10, 0, 0);
		this.getContentPane().add(new JLabel("DB Magiccode"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 0);
		this.getContentPane().add(magiccodeTf, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 10, 0, 0);
		this.getContentPane().add(new JLabel("Server Host"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 0);
		this.getContentPane().add(serverTf, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 10, 0, 0);
		this.getContentPane().add(new JLabel("Server port"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 0, 0);
		this.getContentPane().add(serverportTf, gbc);
		
		JPanel temppanel = new JPanel();
		GridLayout gl = new GridLayout(1, 3);
		gl.setHgap(20);
		temppanel.setLayout(gl);
		temppanel.add(okBt);
		temppanel.add(resetBt);
		temppanel.add(cancelBt);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 25, 15, 25);
		this.getContentPane().add(temppanel, gbc);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if (src == okBt) {
			if (isLocal()) {
				getProperties().setProperty("client.localdbfile", dbfileTf.getText());
				getProperties().setProperty("client.localdbmagiccode", magiccodeTf.getText());
			} else {
				getProperties().setProperty("client.serverhost", serverTf.getText());
				getProperties().setProperty("client.serverport", serverportTf.getText());
			}
			synchronized (this) {
				this.status = OK;
				this.notifyAll();
			}
		} else if (src == cancelBt) {
			System.exit(EXIT_ON_CLOSE);
			// synchronized (this) {
			// this.status = CANCEL;
			// this.notifyAll();
			// }
		} else if (src == resetBt) {
			dbfileTf.setText("");
			magiccodeTf.setText("");
			serverTf.setText("");
			serverportTf.setText("");
		} else if (src == browseBt) {
			File f = new File("."); // go to current directory - i.e. the
									// directory from where the application has
									// been run
			JFileChooser chooser = new JFileChooser(f);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					dbfileTf.setText(chooser.getSelectedFile().getCanonicalPath());
				} catch (Exception e) {
					logger.fine("Exception in getting canonical path. Using getName()");
					dbfileTf.setText(chooser.getSelectedFile().getName());
				}
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		Properties props = new Properties();
		System.out.println(props);
		UCClientPropertiesDialog pd = new UCClientPropertiesDialog(null);
		pd.setLocal(true);
		pd.setProperties(props);
		int status = pd.showDialog();
		System.out.println(props);
	}
}
