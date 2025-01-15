package org.tc.authservice.core.ports.external.database;

import org.tc.authservice.core.domain.UserSnapshot;

public interface UpdateUserAccountStatusDatabaseCommand {
    Boolean updateUserAccountStatus(UserSnapshot userSnapshot);
}
