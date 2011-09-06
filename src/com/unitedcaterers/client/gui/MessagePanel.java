package com.unitedcaterers.client.gui;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.JLabel;

import com.unitedcaterers.client.MessageModel;

/**
 * This panel displays the messages to the user. It only contains a Label.
 */

public class MessagePanel extends BasePanel {
	private final JLabel	msgLabel	= new JLabel("Developed by N Radhakrishna Kalyan");
	
	/**
	 * MessagePanel constructor comment.
	 */
	public MessagePanel() {
		super();
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, msgLabel);
	}
	
	@Override
	public void update(Observable model, Object obj) {
		this.msgLabel.setText(((MessageModel) model).getMessage());
	}
}
