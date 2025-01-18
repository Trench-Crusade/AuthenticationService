package org.tc.authservice.core.domain;

import lombok.Getter;
import org.tc.authservice.core.domain.vo.UserId;
import org.tc.infrastructure.postgres.enums.AccountStatus;
import org.tc.infrastructure.postgres.enums.AccountType;
import org.tc.infrastructure.postgres.enums.LanguageEnum;

import java.time.LocalDateTime;

@Getter
public class UserSnapshot {
    private final UserId userId;
    private final String username;
    private final String email;
    private final LanguageEnum preferredLanguage;
    private final AccountStatus accountStatus;
    private final AccountType accountType;
    private final String passwordHash;
    private final LocalDateTime lastLoginDate;

    public UserSnapshot(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.preferredLanguage = user.getPreferredLanguage();
        this.accountStatus = user.getAccountStatus();
        this.accountType = user.getUserSecurity().accountType();
        this.passwordHash = user.getUserSecurity().passwordHash();
        this.lastLoginDate = user.getLastLoginDate();

    }
}
