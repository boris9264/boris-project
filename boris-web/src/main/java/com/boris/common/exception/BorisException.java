package com.boris.common.exception;

public class BorisException extends Exception {
    private static final long serialVersionUID = 1L;
    private int errorCode;

    public BorisException() {
    }

    public BorisException(BorisError error) {
        super(error.getErrorMsg());
        this.errroMsg(error);
    }

    public BorisException(BorisError error, String errorMessage) {
        super(error.getErrorMsg() + "," + errorMessage);
        this.errroMsg(error);
    }

    public BorisException(BorisError error, Throwable e) {
        super(error.getErrorMsg(), e);
        this.errroMsg(error);
    }

    public BorisException(String errorMessage) {
        super(errorMessage);
    }

    public BorisException(Exception e, String s) {
        super(s, e);
    }

    private void errroMsg(BorisError error) {
        this.errorCode = error.getErrorCode();
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
