package org.tc.authservice.infrastructure.postgres.mappers;

import org.tc.authservice.infrastructure.postgres.dto.UserActivationSelectDto;
import org.tc.authservice.infrastructure.postgres.entities.UserActivationEntity;
import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;

public class UserActivationDatabaseMapper {

    private UserActivationDatabaseMapper() throws TCUtilityClassException {
        throw new TCUtilityClassException("Utility class");
    }
    public static UserActivationSelectDto fromEntity(UserActivationEntity userActivationEntity) {
        return new UserActivationSelectDto(
                userActivationEntity.getUserId(),
                userActivationEntity.getActivationToken(),
                userActivationEntity.getGenerationTime(),
                userActivationEntity.getInvalidationTime()
        );
    }

}
