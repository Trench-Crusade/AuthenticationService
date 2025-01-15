package org.tc.authservice.shared.exceptions.out.detailed;

import org.tc.authservice.shared.exceptions.out.TCOutboundException;

public class TCEmailSendingFailedException extends TCOutboundException {
    public TCEmailSendingFailedException(String message) {
        super(message);
    }
}
