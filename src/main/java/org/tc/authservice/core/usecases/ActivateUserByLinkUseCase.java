package org.tc.authservice.core.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.core.ports.external.database.SelectUserActivationByActivationTokenDatabaseQuery;
import org.tc.authservice.core.ports.internal.ActivateUserByLinkPort;
import org.tc.authservice.core.utility.ActivateAccountUtilityCase;
import org.tc.authservice.infrastructure.api.dto.request.ActivateUserByTokenRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.ActivateUserResponseDto;
import org.tc.authservice.infrastructure.postgres.dto.UserActivationSelectDto;
import org.tc.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.exceptions.persistence.detailed.TCUpdateFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivateUserByLinkUseCase implements ActivateUserByLinkPort {

    private final SelectUserActivationByActivationTokenDatabaseQuery selectUserActivationByActivationTokenDatabaseQuery;
    private final ActivateAccountUtilityCase activateAccountUtilityCase;

    @Override
    public ActivateUserResponseDto activateUser(ActivateUserByTokenRequestDto activateUserByTokenRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCUpdateFailedException {
        log.info("Attempted activation for token: {} by link", activateUserByTokenRequestDto.activationToken());
        UserActivationSelectDto userActivationSelectDto = selectUserActivationByActivationTokenDatabaseQuery
                .selectUserActivationByActivationToken(activateUserByTokenRequestDto.activationToken());

        log.info("User for activation: {} has been found", userActivationSelectDto.userId());
        if(LocalDateTime.now().isAfter(userActivationSelectDto.invalidationTime())){
            throw new TCInvalidRequestDataException("Activation date is past the current date");
        }
        UUID userId = userActivationSelectDto.userId();
        UserSnapshot userSnapshot = activateAccountUtilityCase.activateAccount(userId);
        log.info("Activated user: {}", userSnapshot.getUserId().userId());
        return new ActivateUserResponseDto(userSnapshot.getUserId().userId());
    }
}
