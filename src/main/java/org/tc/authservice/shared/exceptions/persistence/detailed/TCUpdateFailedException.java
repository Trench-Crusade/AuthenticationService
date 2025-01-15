package org.tc.authservice.shared.exceptions.persistence.detailed;

import org.tc.authservice.shared.exceptions.persistence.TCPersistenceException;

public class TCUpdateFailedException extends TCPersistenceException {
    public TCUpdateFailedException(String message) {
        super(message);
    }
}
