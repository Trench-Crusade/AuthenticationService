package org.tc.loginservice.core.domain.sanitizers;

import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.shared.exceptions.TCIllegalStateException;

public class UserSanitizer {

    private UserSanitizer(){
        throw new TCIllegalStateException("Utility class");
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
