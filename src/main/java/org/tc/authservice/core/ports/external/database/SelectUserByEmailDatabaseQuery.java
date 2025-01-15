package org.tc.authservice.core.ports.external.database;

import org.tc.authservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;

public interface SelectUserByEmailDatabaseQuery {

    UserSelectDto selectUserByEmail(String email) throws TCEntityNotFoundException;

}
