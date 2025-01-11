package org.tc.loginservice.shared.consts;

import org.tc.loginservice.shared.exceptions.TCIllegalStateException;

public class ConstValues {

    private ConstValues(){
        throw new TCIllegalStateException("Utility class");
    }

    public static final Integer TOKEN_LIFE_LENGTH = 1000 * 60 * 30;
    public static final Integer ACCOUNT_VALIDATION_TIME = 7;
}
