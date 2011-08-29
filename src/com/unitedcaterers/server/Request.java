package com.unitedcaterers.server;

import java.io.*;

/**
 * This class wraps the action (method name) and the parameters into an object which is serialized and sent to the server.
 * The server unserializes the object and calls the method indicated by action using reflection.
 */
public class Request implements Serializable {

    /**
     * Since this class is serializable, we define our own value of serialVersionUID.
     * If we don't define it manually, Java will assign one based on hashing the code
     * in the class, which may change every time we make a minor code change, which in turn will
     * cause a failure to unserialize objects that were serialized using a prior version
     * of the class before the change.
     * In this project, it doesn't affect much because we don't store serialized object for long term.
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private Object[] params;

    public String getAction() {
        return action;
    }

    public Object[] getParams() {
        return params;
    }

    public void setAction(String newAction) {
        action = newAction;
    }

    public void setParams(Object[] newParams) {
        params = newParams;
    }
}
