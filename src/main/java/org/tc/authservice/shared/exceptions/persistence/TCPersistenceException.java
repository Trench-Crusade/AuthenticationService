package org.tc.authservice.shared.exceptions.persistence;

import org.tc.authservice.shared.exceptions.general.TCGeneralException;

public class TCPersistenceException extends TCGeneralException {
    public TCPersistenceException(String message) {
        super(message);
    }
}
