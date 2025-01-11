package org.tc.loginservice.infrastructure.postgres.mappers;

import org.tc.loginservice.core.domain.UserSnapshot;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.infrastructure.postgres.entities.UserEntity;
import org.tc.loginservice.shared.exceptions.TCIllegalStateException;

public class UserDatabaseMapper {

    private UserDatabaseMapper(){
        throw new TCIllegalStateException("Utility class");
    }

    public static UserSelectDto fromEntity(UserEntity userEntity) {
        return new UserSelectDto(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getAccountType(),
                userEntity.getPreferredLanguage(),
                userEntity.getAccountStatus(),
                userEntity.getPasswordHash(),
                userEntity.getLastLogin()
        );
    }

    public static UserEntity fromSnapshot(UserSnapshot userSnapshot) {
        return new UserEntity(
                null,
                userSnapshot.getUserId().userId(),
                userSnapshot.getUsername(),
                userSnapshot.getEmail(),
                userSnapshot.getAccountType(),
                userSnapshot.getPreferredLanguage(),
                userSnapshot.getAccountStatus(),
                userSnapshot.getPasswordHash(),
                userSnapshot.getLastLoginDate(),
                null,
                null
        );
    }
}
