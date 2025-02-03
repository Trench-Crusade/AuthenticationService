package org.tc.authservice.infrastructure.api.dto.request;

import org.tc.infrastructure.postgres.enums.LanguageEnum;

public record RegisterRequestDto(
        String username,
        String email,
        String password,
        String repeatedPassword,
        LanguageEnum preferredLanguage
) {
    @Override
    public String toString() {
        return "RegisterRequestDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", repeatedPassword='" + repeatedPassword + '\'' +
                ", preferredLanguage=" + preferredLanguage +
                '}';
    }
}
