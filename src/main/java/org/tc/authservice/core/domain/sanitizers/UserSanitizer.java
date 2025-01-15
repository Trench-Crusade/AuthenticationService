package org.tc.authservice.core.domain.sanitizers;

import org.tc.authservice.infrastructure.api.dto.request.RegisterRequestDto;
import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;

public class UserSanitizer {

    private UserSanitizer() throws TCUtilityClassException {
        throw new TCUtilityClassException("Utility class");
    }

    public static RegisterRequestDto sanitizeRegisterRequest(RegisterRequestDto registerRequestDto){
        return new RegisterRequestDto(
                sanitzeString(registerRequestDto.username()),
                sanitzeString(registerRequestDto.email()),
                registerRequestDto.password(),
                registerRequestDto.repeatedPassword(),
                registerRequestDto.preferredLanguage()
        );
    }

    private static String sanitzeString(String original){
        return original.trim();
    }
}
