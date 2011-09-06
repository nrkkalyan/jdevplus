package com.unitedcaterers.client.gui;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class EightDigitsTextField extends JTextField {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final int	MAX_LENGTH			= 8;
	
	public EightDigitsTextField() {
	}
	
	@Override
	protected Document createDefaultModel() {
		return new EightDigitDocument();
	}
	
	public EightDigitsTextField(int cols) {
		super(cols);
	}
	
	final static String	badchars	= "-`~!@#$%^&*()_+=\\|\"':;?/>.<, ";
	
	@Override
	public void processKeyEvent(KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
			super.processKeyEvent(ev);
			return;
		}
		char c = ev.getKeyChar();
		
		if ((Character.isLetter(c) && !ev.isAltDown()) || badchars.indexOf(c) > -1) {
			ev.consume();
			return;
		}
		// if(c == '-' && getDocument().getLength() > 0) ev.consume();
		// else
		super.processKeyEvent(ev);
		
	}
	
	/**
	 * Returns true if the edit (ie change) on going is valid and does not break
	 * the maximum rule.
	 * 
	 * @return true if the edit is valid, else return false.
	 */
	public boolean isEditValid() {
		return getText() != null && getText().length() == MAX_LENGTH;
	}
	
	class EightDigitDocument extends PlainDocument {
		
		/**
		 * For the serialization process, a ID is necessary to avoid confusion
		 * in the class versions.
		 */
		private static final long	serialVersionUID	= 2283764677481377961L;
		
		/**
		 * Inserts some content into the document. Checks that the insertion is
		 * valid and does not break the maximum rule.
		 * 
		 * @param offs
		 *            the starting offset >= 0
		 * @param str
		 *            the string to insert; does nothing with null/empty strings
		 * @param a
		 *            the attributes for the inserted content
		 * @exception BadLocationException
		 *                the given insert position is not a valid position
		 *                within the document
		 * @see Document#insertString
		 */
		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			
			if (str == null) {
				return;
			}
			
			// We only accept digits
			char[] charArray = str.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (!Character.isDigit(charArray[i])) {
					return;
				}
			}
			if (getLength() + str.length() <= MAX_LENGTH) {
				super.insertString(offs, str, a);
			}
		}
		
	}
	
}
