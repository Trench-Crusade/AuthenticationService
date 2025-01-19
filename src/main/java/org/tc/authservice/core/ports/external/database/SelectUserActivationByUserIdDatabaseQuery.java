package org.tc.authservice.core.ports.external.database;

import org.tc.authservice.infrastructure.postgres.dto.UserActivationSelectDto;
import org.tc.exceptions.persistence.detailed.TCEntityNotFoundException;

import java.util.UUID;

public interface SelectUserActivationByUserIdDatabaseQuery {
    UserActivationSelectDto selectUserActivationByUserId(UUID userId) throws TCEntityNotFoundException;

}
