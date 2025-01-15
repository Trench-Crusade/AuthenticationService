package org.tc.authservice.shared.exceptions.out;

import org.tc.authservice.shared.exceptions.general.TCGeneralException;

public class TCOutboundException extends TCGeneralException {
    public TCOutboundException(String message) {
        super(message);
    }
}
