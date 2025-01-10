package org.tc.loginservice.core.ports.external;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UpdateLastLoginDateCommandQuery {
    Boolean updateLastLoginDateById(UUID userID, LocalDateTime lastLoginDate);
}
