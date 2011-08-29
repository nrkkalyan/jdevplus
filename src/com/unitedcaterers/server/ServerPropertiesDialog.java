package com.unitedcaterers.server;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

import com.unitedcaterers.util.*;
/**
 * This class provides a simple GUI to capture parameters required to run RMI based server.
 *
 * @author Enthuware
 * @version 1.0
 */
public class ServerPropertiesDialog extends PropertiesDialog implements ActionListener {
    private JButton okBt = new JButton("OK"), resetBt = new JButton("Reset"), cancelBt = new JButton("Cancel"), browseBt = new JButton("Browse");
    private JTextField dbfileTf = new JTextField(), magiccodeTf = new JTextField(), serverportTf = new JTextField();
    public ServerPropertiesDialog(JFrame parent) {
        super(parent);
        initGUI();
        okBt.addActionListener(this);    cancelBt.addActionListener(this);    resetBt.addActionListener(this); browseBt.addActionListener(this);
    }
    @Override
    public void initControls() {
        Properties props = getProperties();
        dbfileTf.setText(props.getProperty("server.dbfile", "ucdb.db"));
        magiccodeTf.setText(props.getProperty("server.dbmagiccode", "UCDB"));
        serverportTf.setText(props.getProperty("server.port", "1099"));
    }
    
    private void initGUI() {
        this.setSize(370, 240);
        this.getContentPane().setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = null;
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(5,5,5,5);
        
        String text = "<html><h3><font color='navy'>Please specify or confirm the following server properties.</font></h3></html>";
        
        this.getContentPane().add(new JLabel(text), gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,10,0,0);
        this.getContentPane().add(new JLabel("Database File"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,5,0,0);
        this.getContentPane().add(dbfileTf, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,5,0,15);
        this.getContentPane().add(browseBt, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,10,0,0);
        this.getContentPane().add(new JLabel("DB Magiccode"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,5,0,0);
        this.getContentPane().add(magiccodeTf, gbc);
        
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,10,0,0);
        this.getContentPane().add(new JLabel("Server port"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,5,0,0);
        this.getContentPane().add(serverportTf, gbc);
        
        JPanel temppanel = new JPanel();
        GridLayout gl = new GridLayout(1, 3);
        gl.setHgap(20);
        temppanel.setLayout(gl);
        temppanel.add(okBt);temppanel.add(resetBt); temppanel.add(cancelBt);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3; gbc.gridheight = 1;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5,25,15,25);
        this.getContentPane().add(temppanel, gbc);
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if(src == okBt) {
            getProperties().setProperty("server.dbfile", dbfileTf.getText());
            getProperties().setProperty("server.dbmagiccode", magiccodeTf.getText());
            getProperties().setProperty("server.port", serverportTf.getText());
            synchronized(this) {
                this.status = OK;
                this.notifyAll();
            }
        } else if(src == cancelBt) {
            synchronized(this) {
                this.status = CANCEL;
                this.notifyAll();
            }
        } else if(src == resetBt) {
            dbfileTf.setText("");
            magiccodeTf.setText("");
            serverportTf.setText("");
        } else if(src == browseBt) {
            File f = new File(".");
            JFileChooser chooser = new JFileChooser(f);
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    dbfileTf.setText(chooser.getSelectedFile().getCanonicalPath());
                } catch(Exception e) {
                    logger.fine("Exception in getting canonical path. Using getName()");
                    dbfileTf.setText(chooser.getSelectedFile().getName());
                }
            }
            
            
        }
        
    }
    
    public static void main(String[] args) {
        Properties props = new Properties();
        System.out.println(props);
        ServerPropertiesDialog pd = new ServerPropertiesDialog(null);
        pd.setProperties(props);
        int status = pd.showDialog();
        System.out.println(props);
    }
    
}

