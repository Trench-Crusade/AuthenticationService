package org.tc.authservice.shared.exceptions.general.detailed;

import org.tc.authservice.shared.exceptions.general.TCGeneralException;

public class TCIllegalStateException extends TCGeneralException {
    public TCIllegalStateException(String s) {
        super(s);
    }
}
