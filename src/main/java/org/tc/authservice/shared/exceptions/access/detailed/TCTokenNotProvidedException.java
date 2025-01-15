package org.tc.authservice.shared.exceptions.access.detailed;

import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;

public class TCTokenNotProvidedException extends TCAccessDeniedException {
    public TCTokenNotProvidedException(String message) {
        super(message);
    }
}
