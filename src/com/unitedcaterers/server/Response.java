package com.unitedcaterers.server;

/**
 * There server calls the appropriate method depending on the request and wraps the result or exceptions into an object of this class.
 * The object is then serialized and sent to the client.
 */
public class Response implements java.io.Serializable {

    /**
     * Since this class is serializable, we define our own value of serialVersionUID.
     * If we don't define it manually, Java will assign one based on hashing the code
     * in the class, which may change every time we make a minor code change, which in turn will
     * cause a failure to unserialize objects that were serialized using a prior version
     * of the class before the change.
     * In this project, it doesn't affect much because we don't store serialized object for long term.
     */
    private static final long serialVersionUID = 1L;
    private Object value;
    private int status;
    private java.lang.Object error;
    public static final int COMPLETED = 1;
    public static final int NOT_COMPLETED = -1;
    public static final int UNKNOWN = 0;

    public Object getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public Object getValue() {
        return value;
    }

    public void setError(Object newError) {
        error = newError;
    }

    public void setStatus(int newStatus) {
        status = newStatus;
    }

    public void setValue(Object newValue) {
        value = newValue;
    }
}
