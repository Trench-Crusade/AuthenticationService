package org.tc.authservice.core.ports.external.database;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UpdateLastLoginDateDatabaseCommand {
    Boolean updateLastLoginDateById(UUID userID, LocalDateTime lastLoginDate);
}
