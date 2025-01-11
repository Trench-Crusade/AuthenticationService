package org.tc.loginservice.infrastructure.controller.dto.request;

import org.tc.loginservice.infrastructure.postgres.enums.LanguageEnum;

public record RegisterRequestDto(
        String username,
        String email,
        String password,
        String repeatedPassword,
        LanguageEnum preferredLanguage
) {
}
