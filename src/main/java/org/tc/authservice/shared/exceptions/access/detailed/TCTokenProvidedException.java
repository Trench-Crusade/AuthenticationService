package org.tc.authservice.shared.exceptions.access.detailed;

import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;

public class TCTokenProvidedException extends TCAccessDeniedException {
    public TCTokenProvidedException(String message) {
        super(message);
    }
}
