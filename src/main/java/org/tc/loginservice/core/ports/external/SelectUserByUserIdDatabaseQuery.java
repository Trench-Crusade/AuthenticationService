package org.tc.loginservice.core.ports.external;

import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;

import java.util.UUID;

public interface SelectUserByUserIdDatabaseQuery {
    UserSelectDto selectUser(UUID userId) throws TCEntityNotFoundException;
}
