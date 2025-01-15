package org.tc.authservice.shared.exceptions.access.detailed;

import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;

public class TCTokenMalformedException extends TCAccessDeniedException {
    public TCTokenMalformedException(String message) {
        super(message);
    }
}
