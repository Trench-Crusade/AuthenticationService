package org.tc.authservice.shared.consts;

import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;

public class ConstValues {

    private ConstValues() throws TCUtilityClassException {
        throw new TCUtilityClassException("Utility class");
    }

    public static final Long TOKEN_LIFE_LENGTH = (long) (1000 * 60 * 30);
    public static final Integer ACCOUNT_ACTIVATION_TIME = 7;
    public static final int BLACKLIST_CLEAR_TIME = 1000 * 60 * 5;
    public static final int BLACKLIST_CLEAR_DELAY = 1000 * 60;

    public static final String AUTHORIZATION_HEADER = "Authorization";
}
