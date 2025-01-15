package org.tc.authservice.core.ports.external.database;

import java.util.UUID;

public interface InsertNewUserActivationDatabaseCommand {
    Boolean insertUserActivation(UUID userId, UUID validateToken);
}
