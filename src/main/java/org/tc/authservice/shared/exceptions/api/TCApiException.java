package org.tc.authservice.shared.exceptions.api;

import org.tc.authservice.shared.exceptions.general.TCGeneralException;

public class TCApiException extends TCGeneralException {
    public TCApiException(String message) {
        super(message);
    }
}
