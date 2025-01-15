package org.tc.authservice.core.utility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.redis.SelectBlacklistTokenEntryByIdRedisQuery;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyTokenUtilityCase {

    private final SelectBlacklistTokenEntryByIdRedisQuery selectBlacklistTokenEntryByIdRedisQuery;
    private final RetrieveTokenUtilityCase retrieveTokenUtilityCase;

    public Boolean verifyToken(HttpServletRequest request) throws TCInvalidRequestDataException, TCTokenNotProvidedException, TCTokenExpiredException {
        String token = retrieveTokenUtilityCase.retrieveToken(request);
        log.info("Checking if token is not on blacklist");
        Optional<String> tokenBlacklistOptional = selectBlacklistTokenEntryByIdRedisQuery.selectBlacklistTokenEntryById(token);
        if(tokenBlacklistOptional.isPresent()){
            throw new TCInvalidRequestDataException("Token has been marked as used");
        }
        log.info("Token verified successfully");
        return true;
    }
}
