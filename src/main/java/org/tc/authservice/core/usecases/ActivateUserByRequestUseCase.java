package org.tc.authservice.core.usecases;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.core.ports.external.database.SelectUserActivationByUserIdDatabaseQuery;
import org.tc.authservice.core.ports.internal.ActivateUserByRequestPort;
import org.tc.authservice.core.utility.ActivateAccountUtilityCase;
import org.tc.authservice.core.utility.RetrieveTokenUtilityCase;
import org.tc.authservice.core.utility.VerifyTokenUtilityCase;
import org.tc.authservice.infrastructure.api.dto.response.ActivateUserResponseDto;
import org.tc.authservice.infrastructure.postgres.dto.UserActivationSelectDto;
import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCUpdateFailedException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivateUserByRequestUseCase implements ActivateUserByRequestPort {

    private final VerifyTokenUtilityCase verifyTokenUtilityCase;
    private final SelectUserActivationByUserIdDatabaseQuery selectUserActivationByUserIdDatabaseQuery;
    private final RetrieveTokenUtilityCase retrieveTokenUtilityCase;
    private final ActivateAccountUtilityCase activateAccountUtilityCase;

    @Override
    public ActivateUserResponseDto activateUser(HttpServletRequest request) throws TCEntityNotFoundException, TCAccessDeniedException, TCInvalidRequestDataException, TCUpdateFailedException {
        verifyTokenUtilityCase.verifyToken(request);
        log.info("Token verified successfully");
        UUID userId = retrieveTokenUtilityCase.getUserId(request);
        log.info("User Id: {} retrieved from token", userId);
        log.info("Attempted activation for user: {}", userId);
        UserActivationSelectDto userActivationSelectDto = selectUserActivationByUserIdDatabaseQuery
                .selectUserActivationByUserId(userId);
        if(!userId.equals(userActivationSelectDto.userId())){
            throw new TCInvalidRequestDataException("Activation for user "+
                    userActivationSelectDto.userId() + " attempted by user "+ userId + " and suspended");
        }
        if(LocalDateTime.now().isAfter(userActivationSelectDto.invalidationTime())){
            throw new TCInvalidRequestDataException("Activation date is past the current date");
        }
        log.info("Attempting to select user by id: {} ", userId);
        UserSnapshot userSnapshot = activateAccountUtilityCase.activateAccount(userId);
        log.info("Activated used: {}", userSnapshot.getUserId().userId());
        return new ActivateUserResponseDto(userSnapshot.getUserId().userId());
    }
}
