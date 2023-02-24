package org.example;

class DataServiceException extends RuntimeException {
    public DataServiceException(Throwable e) {
        super(e);
    }

    public DataServiceException(String message) {
        super(message);
    }
}