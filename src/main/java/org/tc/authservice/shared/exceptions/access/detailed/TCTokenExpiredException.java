package org.tc.authservice.shared.exceptions.access.detailed;

import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;

public class TCTokenExpiredException extends TCAccessDeniedException {
    public TCTokenExpiredException(String message) {
        super(message);
    }
}
