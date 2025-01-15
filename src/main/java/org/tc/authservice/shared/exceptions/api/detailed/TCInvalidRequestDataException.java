package org.tc.authservice.shared.exceptions.api.detailed;

import org.tc.authservice.shared.exceptions.api.TCApiException;

public class TCInvalidRequestDataException extends TCApiException {
    public TCInvalidRequestDataException(String message) {
        super(message);
    }
}
