package org.tc.loginservice.core.ports.external;

import org.tc.loginservice.infrastructure.postgres.dto.UserValidationSelectDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;

import java.util.UUID;

public interface SelectUserValidationByValidationTokenDatabaseQuery {
    UserValidationSelectDto selectUserValidation(UUID validationToken) throws TCEntityNotFoundException;
}
