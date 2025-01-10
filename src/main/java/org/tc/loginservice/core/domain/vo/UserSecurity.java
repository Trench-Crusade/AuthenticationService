package org.tc.loginservice.core.domain.vo;

import org.tc.loginservice.infrastructure.postgres.enums.AccountType;

public record UserSecurity(
        AccountType accountType,
        String passwordHash
) {
}
