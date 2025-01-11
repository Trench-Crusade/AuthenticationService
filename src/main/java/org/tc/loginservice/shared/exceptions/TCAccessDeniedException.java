package org.tc.loginservice.shared.exceptions;

public class TCAccessDeniedException extends TCGeneralException{
    public TCAccessDeniedException(String message) {
        super(message);
    }
}
