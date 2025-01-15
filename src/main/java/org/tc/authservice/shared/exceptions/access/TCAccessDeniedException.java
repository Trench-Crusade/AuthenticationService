package org.tc.authservice.shared.exceptions.access;

import org.tc.authservice.shared.exceptions.general.TCGeneralException;

public class TCAccessDeniedException extends TCGeneralException {
    public TCAccessDeniedException(String message) {
        super(message);
    }
}
