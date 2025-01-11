package org.tc.loginservice.core.ports.external;

import java.util.UUID;

public interface InsertNewUserValidationDatabaseCommand {
    Boolean insertUserValidation(UUID userId, UUID validateToken);
}
