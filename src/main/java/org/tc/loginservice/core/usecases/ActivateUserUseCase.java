package org.tc.loginservice.core.usecases;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.domain.User;
import org.tc.loginservice.core.domain.UserSnapshot;
import org.tc.loginservice.core.ports.external.SelectUserByUserIdDatabaseQuery;
import org.tc.loginservice.core.ports.external.SelectUserValidationByValidationTokenDatabaseQuery;
import org.tc.loginservice.core.ports.external.UpdateUserAccountStatusDatabaseCommand;
import org.tc.loginservice.core.ports.internal.ActivateUserPort;
import org.tc.loginservice.core.utility.RetrieveTokenUtilityCase;
import org.tc.loginservice.infrastructure.controller.dto.request.ActivateUserRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.ActivateUserResponseDto;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.infrastructure.postgres.dto.UserValidationSelectDto;
import org.tc.loginservice.infrastructure.postgres.enums.AccountStatus;
import org.tc.loginservice.shared.exceptions.TCAccessDeniedException;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;
import org.tc.loginservice.shared.exceptions.TCIUpdateFailedException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivateUserUseCase implements ActivateUserPort {

    private final SelectUserValidationByValidationTokenDatabaseQuery selectUserValidationByValidationTokenDatabaseQuery;
    private final SelectUserByUserIdDatabaseQuery selectUserByUserIdDatabaseQuery;
    private final UpdateUserAccountStatusDatabaseCommand updateUserAccountStatusDatabaseCommand;
    private final RetrieveTokenUtilityCase retrieveTokenUtilityCase;

    @Override
    public ActivateUserResponseDto activateUser(HttpServletRequest httpServletRequest, ActivateUserRequestDto activateUserRequestDto) throws TCEntityNotFoundException, TCAccessDeniedException, TCInvalidRequestDataException, TCIUpdateFailedException {
        UUID userId = retrieveTokenUtilityCase.getUserId(httpServletRequest);
        log.info("Attempted activation for user: {}", userId);
        UserValidationSelectDto userValidationSelectDto = selectUserValidationByValidationTokenDatabaseQuery
                .selectUserValidation(activateUserRequestDto.validationToken());
        if(!userId.equals(userValidationSelectDto.userId())){
            throw new TCInvalidRequestDataException("Activation for user "+
                    userValidationSelectDto.userId() + " attempted by user "+ userId + " and suspended");
        }
        if(LocalDateTime.now().isAfter(userValidationSelectDto.invalidationTime())){
            throw new TCInvalidRequestDataException("Activation date is past the current date");
        }
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
            throw new TCIUpdateFailedException("Could not activate account for user "+userSnapshot.getUserId().userId());
        }

        return new ActivateUserResponseDto(userSnapshot.getUserId().userId());
    }
}
