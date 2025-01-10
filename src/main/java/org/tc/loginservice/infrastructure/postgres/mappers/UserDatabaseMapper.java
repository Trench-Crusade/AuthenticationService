package org.tc.loginservice.infrastructure.postgres.mappers;

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
                userEntity.getPasswordHash(),
                userEntity.getLastLogin()
        );
    }
}
