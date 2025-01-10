package org.tc.loginservice.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.tc.loginservice.core.domain.vo.UserId;
import org.tc.loginservice.core.domain.vo.UserSecurity;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.infrastructure.postgres.enums.LanguageEnum;

import java.time.LocalDateTime;

@Getter
public class User {
    private UserId userId;
    private String username;
    private String email;
    private LanguageEnum preferredLanguage;
    private UserSecurity userSecurity;
    @Setter
    private LocalDateTime lastLoginDate;

    private User(UserId userId, String username, String email, LanguageEnum preferredLanguage,
                 UserSecurity userSecurity, LocalDateTime lastLoginDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.preferredLanguage = preferredLanguage;
        this.userSecurity = userSecurity;
        this.lastLoginDate = lastLoginDate;
    }

    public static User fromSelectDto(UserSelectDto userSelectDto) {
        return new User(
                new UserId(userSelectDto.userId()),
                userSelectDto.username(),
                userSelectDto.email(),
                userSelectDto.preferredLanguage(),
                new UserSecurity(
                        userSelectDto.accountType(),
                        userSelectDto.passwordHash()
                ),
                userSelectDto.lastLogin()
        );
    }
}
