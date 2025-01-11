package org.tc.loginservice.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.tc.loginservice.core.domain.vo.UserId;
import org.tc.loginservice.core.domain.vo.UserSecurity;
import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.infrastructure.postgres.enums.AccountStatus;
import org.tc.loginservice.infrastructure.postgres.enums.LanguageEnum;
import org.tc.loginservice.shared.exceptions.TCIllegalStateException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User {
    private UserId userId;
    private String username;
    private String email;
    private LanguageEnum preferredLanguage;
    @Setter
    private AccountStatus accountStatus;
    private UserSecurity userSecurity;
    @Setter
    private LocalDateTime lastLoginDate;

    private User(UserId userId, String username, String email, LanguageEnum preferredLanguage,
                 AccountStatus accountStatus, UserSecurity userSecurity, LocalDateTime lastLoginDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.preferredLanguage = preferredLanguage;
        this.accountStatus = accountStatus;
        this.userSecurity = userSecurity;
        this.lastLoginDate = lastLoginDate;
    }

    public static User fromSelectDto(UserSelectDto userSelectDto) {
        return new User(
                new UserId(userSelectDto.userId()),
                userSelectDto.username(),
                userSelectDto.email(),
                userSelectDto.preferredLanguage(),
                userSelectDto.accountStatus(),
                new UserSecurity(
                        userSelectDto.accountType(),
                        userSelectDto.passwordHash()
                ),
                userSelectDto.lastLogin()
        );
    }

    public static User fromRegisterDto(RegisterRequestDto registerRequestDto){
        return new User(
                new UserId(UUID.randomUUID()),
                registerRequestDto.username(),
                registerRequestDto.email(),
                registerRequestDto.preferredLanguage(),
                AccountStatus.UNVERIFIED,
                null,
                null
        );
    }

    public void setUserSecurityForRegisteringUser(UserSecurity userSecurity){
        if(Objects.nonNull(this.userSecurity)){
            throw new TCIllegalStateException("User already has security provided");
        }
        this.userSecurity = userSecurity;
    }

    public UserSnapshot toSnapshot(){
        return new UserSnapshot(this);
    }
}
