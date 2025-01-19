package org.tc.authservice.core.ports.external.database;

import org.tc.authservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.exceptions.persistence.detailed.TCEntityNotFoundException;

import java.util.UUID;

public interface SelectUserByUserIdDatabaseQuery {
    UserSelectDto selectUser(UUID userId) throws TCEntityNotFoundException;
}
