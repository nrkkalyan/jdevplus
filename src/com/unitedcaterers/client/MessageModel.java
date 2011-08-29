package com.unitedcaterers.client;


/**
 * The model is used by MessagePanel to show helpful status messages to the User.
 */
public class MessageModel extends java.util.Observable {
    
    private java.lang.String message;
    
    public MessageModel() {
    }
    
    /**
     * returns the message string.
     * @return String
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the new message
     * @param newMessage newMessage
     */
    public void setMessage(String newMessage) {
        message = newMessage;
    }
    
    /**
     * This updates the model and changes the status to updated. Models notify the observers only when they are changed. If setChanged() is not called, calling notifyObservers() will not send notification to observers.
     * @param newMessage New message to be displayed.
     */
    
    public void updateModel(String newMessage) {
        this.message = newMessage;
        setChanged();
    }
}
