package org.tc.authservice.core.domain.vo;

import org.tc.infrastructure.postgres.enums.AccountType;

public record UserSecurity(
        AccountType accountType,
        String passwordHash
) {
}
