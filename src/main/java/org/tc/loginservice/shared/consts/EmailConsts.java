package org.tc.loginservice.shared.consts;

import org.tc.loginservice.shared.exceptions.TCIllegalStateException;

import java.util.UUID;

public class EmailConsts {

    private EmailConsts() {
        throw new TCIllegalStateException("Utility class");
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

    public static String createActivationEmail(UUID validationToken) {
        return ACTIVATION_EMAIL.replace("{{token}}", validationToken.toString());
    }

}
