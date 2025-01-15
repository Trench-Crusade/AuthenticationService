package org.tc.authservice.core.domain.vo;

import org.tc.authservice.infrastructure.postgres.enums.AccountType;

public record UserSecurity(
        AccountType accountType,
        String passwordHash
) {
}
