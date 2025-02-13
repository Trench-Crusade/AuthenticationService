package org.tc.authservice.infrastructure.postgres.dto;

import org.tc.infrastructure.postgres.enums.AccountStatus;
import org.tc.infrastructure.postgres.enums.AccountType;
import org.tc.infrastructure.postgres.enums.LanguageEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSelectDto(
                UUID userId,
                String username,
                String email,
                AccountType accountType,
                LanguageEnum preferredLanguage,
                AccountStatus accountStatus,
                String passwordHash,
                LocalDateTime lastLogin
) {
}
