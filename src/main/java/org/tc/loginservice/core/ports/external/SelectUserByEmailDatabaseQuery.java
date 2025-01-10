package org.tc.loginservice.core.ports.external;

import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;

public interface SelectUserByEmailDatabaseQuery {

    UserSelectDto selectUserByEmail(String email) throws TCEntityNotFoundException;

}
