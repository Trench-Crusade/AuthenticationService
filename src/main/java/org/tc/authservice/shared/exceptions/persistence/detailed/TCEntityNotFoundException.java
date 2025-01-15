package org.tc.authservice.shared.exceptions.persistence.detailed;

import org.tc.authservice.shared.exceptions.persistence.TCPersistenceException;

public class TCEntityNotFoundException extends TCPersistenceException {
    public TCEntityNotFoundException(String message) {
        super(message);
    }
}
