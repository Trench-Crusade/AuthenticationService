package org.tc.authservice.shared.exceptions.persistence.detailed;

import org.tc.authservice.shared.exceptions.persistence.TCPersistenceException;

public class TCInsertFailedException extends TCPersistenceException {
    public TCInsertFailedException(String message) {
        super(message);
    }
}
