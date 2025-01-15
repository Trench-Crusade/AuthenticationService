package org.tc.authservice.infrastructure.postgres.mappers;

import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.authservice.infrastructure.postgres.entities.UserEntity;
import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;

public class UserDatabaseMapper {

    private UserDatabaseMapper() throws TCUtilityClassException {
        throw new TCUtilityClassException("Utility class");
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
