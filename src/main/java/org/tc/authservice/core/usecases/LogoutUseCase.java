package org.tc.authservice.core.usecases;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.redis.InsertBlacklistTokenEntryRedisCommand;
import org.tc.authservice.core.ports.internal.LogoutPort;
import org.tc.authservice.core.utility.RetrieveTokenUtilityCase;
import org.tc.authservice.infrastructure.api.dto.response.LogoutResponseDto;
import org.tc.authservice.infrastructure.redis.entries.TokenBlacklistEntry;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCInsertFailedException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutUseCase implements LogoutPort {

    private final RetrieveTokenUtilityCase retrieveTokenUtilityCase;
    private final InsertBlacklistTokenEntryRedisCommand insertBlacklistTokenEntryRedisCommand;

    @Override
    public LogoutResponseDto logOut(HttpServletRequest request) throws TCTokenNotProvidedException, TCInsertFailedException {
        String token;
        try {
            token = retrieveTokenUtilityCase.retrieveToken(request);
            UUID userId = retrieveTokenUtilityCase.getUserIdFromToken(token);
            log.info("Logout request received for user: {}", userId);
            TokenBlacklistEntry tokenBlacklistEntry = new TokenBlacklistEntry(
                    token, userId, LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(retrieveTokenUtilityCase.getExpirationDate(token))),
                    TimeZone.getDefault().toZoneId()));
            log.info("Invalidating token: {}", token);
            if(Boolean.TRUE.equals(insertBlacklistTokenEntryRedisCommand.insertBlacklistTokenEntry(tokenBlacklistEntry))){
                log.info("Token blacklisted: {}", tokenBlacklistEntry);
            }
            else{
                throw new TCInsertFailedException("Token " + token + " could not be blacklisted");
            }
        } catch (TCTokenExpiredException ex) {
            log.error("Token expired");
        }
        return new LogoutResponseDto(true);


    }
}
