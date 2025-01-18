package org.tc.authservice.core.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.domain.User;
import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.core.ports.external.database.SelectUserByUserIdDatabaseQuery;
import org.tc.authservice.core.ports.external.database.UpdateUserAccountStatusDatabaseCommand;
import org.tc.authservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.infrastructure.postgres.enums.AccountStatus;
import org.tc.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.exceptions.persistence.detailed.TCUpdateFailedException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivateAccountUtilityCase {

    private final UpdateUserAccountStatusDatabaseCommand updateUserAccountStatusDatabaseCommand;
    private final SelectUserByUserIdDatabaseQuery selectUserByUserIdDatabaseQuery;

    public UserSnapshot activateAccount(UUID userId) throws TCEntityNotFoundException, TCUpdateFailedException {
        log.info("Attempting to select user by id: {} ", userId);
        UserSelectDto userSelectDto = selectUserByUserIdDatabaseQuery.selectUser(userId);
        log.info("User found, proceeding to activate");
        User user = User.fromSelectDto(userSelectDto);
        user.setAccountStatus(AccountStatus.ACTIVE);
        UserSnapshot userSnapshot = user.toSnapshot();
        if(Boolean.TRUE.equals(updateUserAccountStatusDatabaseCommand.updateUserAccountStatus(userSnapshot))){
            log.info("User {} account activated", userSnapshot.getUserId().userId());
        }
        else{
            throw new TCUpdateFailedException("Could not activate account for user "+userSnapshot.getUserId().userId());
        }
        return userSnapshot;
    }
}
