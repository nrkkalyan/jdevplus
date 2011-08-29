package com.unitedcaterers.client.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;

import com.unitedcaterers.client.*;
/**
 * This panel displays the messages to the user. It only contains a Label.
 */

public class MessagePanel extends BasePanel {
    private JLabel msgLabel = new JLabel("Please use File Menu to View/Search Caterers");
    
    /**
     * MessagePanel constructor comment.
     */
    public MessagePanel() {
        super();
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, msgLabel);
        this.setForeground(Color.blue);
        this.msgLabel.setForeground(Color.blue);
    }
    
    @Override
    public void update(Observable model, Object obj) {
        this.msgLabel.setText(( (MessageModel) model).getMessage());
    }
}
