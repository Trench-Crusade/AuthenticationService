package org.tc.loginservice.core.ports.external;

import org.tc.loginservice.core.domain.UserSnapshot;

public interface UpdateUserAccountStatusDatabaseCommand {
    Boolean updateUserAccountStatus(UserSnapshot userSnapshot);
}
