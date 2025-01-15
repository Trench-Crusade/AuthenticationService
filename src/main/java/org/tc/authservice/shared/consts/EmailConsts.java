package org.tc.authservice.shared.consts;

import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;

import java.util.UUID;

public class EmailConsts {

    private EmailConsts() throws TCUtilityClassException {
        throw new TCUtilityClassException("Utility class");
    }

    public static final String ACTIVATION_TITLE = "Activate your Trench Crusade account!";
    public static final String ACTIVATION_EMAIL = """
            Welcome!
            
            This is your last step to activate Trench Crusade account!
            Please click on the link below to finish activation:
            http://localhost:8080/user/activate/{{token}}
            
            If you fail to activate your account within 7 days, data will be erased.
            
            Kind regards,
            Trench Crusade Team
            """;

    public static String createActivationEmail(UUID activationToken) {
        return ACTIVATION_EMAIL.replace("{{token}}", activationToken.toString());
    }

}
