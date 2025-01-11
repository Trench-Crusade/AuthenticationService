package org.tc.loginservice.shared.exceptions;

public class TCEmailSendingFailedException extends TCGeneralException{
    public TCEmailSendingFailedException(String message) {
        super(message);
    }
}
