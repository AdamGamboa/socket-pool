package com.orthanc.commons.pool.socket.exceptions;

/**
 *
 * @author Adam M. Gamboa G
 */
public class SocketClientException extends RuntimeException {
    
    public SocketClientException(String message, Throwable t){
        super(message, t);
    }
}
