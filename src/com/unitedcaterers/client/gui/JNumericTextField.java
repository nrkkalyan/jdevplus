package com.unitedcaterers.client.gui;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class JNumericTextField extends JTextField {
    
    public JNumericTextField(){}
    public JNumericTextField(int cols){ super(cols);}
    final static String badchars
            = "-`~!@#$%^&*()_+=\\|\"':;?/>.<, ";
    
    @Override
    public void processKeyEvent(KeyEvent ev) {
        if(ev.getKeyCode() == KeyEvent.VK_ENTER) {
            super.processKeyEvent(ev);
            return;
        }
        char c = ev.getKeyChar();
        
        if((Character.isLetter(c) && !ev.isAltDown())
        || badchars.indexOf(c) > -1) {
            ev.consume();
            return;
        }
        //if(c == '-' && getDocument().getLength() > 0) ev.consume();
        //else
        super.processKeyEvent(ev);
        
    }
}

