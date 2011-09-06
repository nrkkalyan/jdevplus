package com.unitedcaterers.client.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.unitedcaterers.UCServer;
import com.unitedcaterers.client.ClientController;

/**
 * Booking dialog box. This dialog box asks for a customer ID (8 digits). If
 * this format is correct, the user can book the room specified with this
 * customer ID. With this condition, the book command is sent to the controller.
 * 
 * @author Seb
 * 
 */
public class BookingDialog extends JDialog {
	
	/**
	 * For the serialization process, a ID is necessary to avoid confusion in
	 * the class versions.
	 */
	private static final long			serialVersionUID	= 1L;
	private static final String			OPTION_PANE_TEXT	= "Enter customer ID (8 digits only):";
	private static final String			DIALOG_TITLE		= "Complete booking:";
	
	private final EightDigitsTextField	customerIDTextField;
	private JOptionPane					optionPane;
	private final JFrame				frame;
	private final ClientController		clientController;
	private final String[]				data;
	private UCServer					currentServer;
	
	/**
	 * Instantiates a Booking Dialog to book the Room in parameter with the
	 * GuiController specified.
	 * 
	 * @param frame
	 *            The calling {@link ApplicationWindow}. The table of this frame
	 *            will be updated when the booking is done. This JDialog is also
	 *            relative to this {@link ApplicationWindow}.
	 * @param controller
	 *            The {@link GuiController} used to interact with the DB
	 *            connector.
	 * @param room
	 *            the Room to book.
	 */
	public BookingDialog(JFrame frame, ClientController clientController, String[] data) {
		super(frame, true);
		
		this.frame = frame;
		this.clientController = clientController;
		this.data = data;
		
		// Create the text field for the customerID with the custom JTextField
		// (EightDigitsTextField)
		customerIDTextField = new EightDigitsTextField();
		
		setResizable(false);
		setTitle(DIALOG_TITLE);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// Setup the option pane
		setupOptionPane();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change
				 * the JOptionPane's value property.
				 */
				optionPane.setValue(JOptionPane.CANCEL_OPTION);
			}
		});
		
		// Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				customerIDTextField.requestFocusInWindow();
			}
		});
		
	}
	
	/**
	 * Displays this BookingDialog. The location is relative to the frame
	 * specified in the constructor.
	 */
	public void display() {
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	private void setupOptionPane() {
		Object[] arrayMessage = { OPTION_PANE_TEXT, customerIDTextField };
		optionPane = new JOptionPane(arrayMessage, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		optionPane.addPropertyChangeListener(new OptionSelectionListener(frame));
		
		setContentPane(optionPane);
	}
	
	private class OptionSelectionListener implements PropertyChangeListener {
		
		private final JFrame	frame;
		
		/**
		 * Instantiates a PropertyChangeListener. It captures the customer ID
		 * specified and send the booking request through the controller. At the
		 * end, the ApplicationWindow table is refreshed.
		 * 
		 * @param frame
		 *            the ApplicationWindow which called the BookingDialog.
		 */
		public OptionSelectionListener(JFrame frame) {
			this.frame = frame;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String prop = evt.getPropertyName();
			if (isVisible() && (evt.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop))) {
				Object value = optionPane.getValue();
				if (value instanceof Integer && JOptionPane.OK_OPTION == (Integer) value) {
					if (customerIDTextField.isEditValid()) {
						// Do the booking process
						// We can get the customerID with getText() because we
						// know
						// that it's valid.
						String customerID = customerIDTextField.getText();
						try {
							boolean status = currentServer.bookCaterer(customerID, data);
							if (status) {
								// JOptionPane.showMessageDialog(appFrame,
								// "Thank you, " +
								// data[1] + " has been booked.",
								// "Book Caterer",
								// JOptionPane.INFORMATION_MESSAGE);
								refreshView(currentQuery, currentHotelName, currentLocation);
							} else {
								JOptionPane.showMessageDialog(appFrame, "Sorry, Unable to book this caterer.",
										"Book Caterer", JOptionPane.INFORMATION_MESSAGE);
							}
						} catch (Exception e) {
							JOptionPane.showMessageDialog(appFrame, "Unable to book the caterer. " + e.getMessage(),
									"Book Caterer", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						setVisible(false);
						setupOptionPane();
						setVisible(true);
					}
				} else {
					// Hide the dialog
					setVisible(false);
				}
			}
		}
	}
}
