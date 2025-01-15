package org.tc.authservice.core.ports.external.database;

import org.tc.authservice.core.domain.UserSnapshot;

public interface InsertNewUserDatabaseCommand {
    Boolean insertUser(UserSnapshot userSnapshot);
}
