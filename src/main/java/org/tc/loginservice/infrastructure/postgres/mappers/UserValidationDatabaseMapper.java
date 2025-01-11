package org.tc.loginservice.infrastructure.postgres.mappers;

import org.tc.loginservice.infrastructure.postgres.dto.UserValidationSelectDto;
import org.tc.loginservice.infrastructure.postgres.entities.UserValidationEntity;
import org.tc.loginservice.shared.exceptions.TCIllegalStateException;

public class UserValidationDatabaseMapper {

    private UserValidationDatabaseMapper(){
        throw new TCIllegalStateException("Utility class");
    }
    public static UserValidationSelectDto fromEntity(UserValidationEntity userValidationEntity) {
        return new UserValidationSelectDto(
                userValidationEntity.getUserId(),
                userValidationEntity.getValidationToken(),
                userValidationEntity.getGenerationTime(),
                userValidationEntity.getInvalidationTime()
        );
    }

}
