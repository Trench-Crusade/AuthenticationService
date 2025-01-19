package org.tc.authservice.core.usecases;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.domain.User;
import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.core.ports.external.database.SelectUserByEmailDatabaseQuery;
import org.tc.authservice.core.ports.external.database.UpdateLastLoginDateDatabaseCommand;
import org.tc.authservice.core.ports.internal.LogInPort;
import org.tc.authservice.core.utility.GenerateTokenUtilityCase;
import org.tc.authservice.infrastructure.api.dto.request.LoginRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.TokenResponseDto;
import org.tc.authservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.exceptions.persistence.detailed.TCUpdateFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class LogInUserUseCase implements LogInPort {

    private final SelectUserByEmailDatabaseQuery selectUserByEmailDatabaseQuery;
    private final UpdateLastLoginDateDatabaseCommand updateLastLoginDateDatabaseCommand;
    private final GenerateTokenUtilityCase generateTokenUtilityCase;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public TokenResponseDto logIn(HttpServletRequest request, LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCUpdateFailedException {

        log.info("Attempted login for email: {}", loginRequestDto.email());
        UserSelectDto userSelectDto = selectUserByEmailDatabaseQuery.selectUserByEmail(loginRequestDto.email());
        log.info("User {} found by email", userSelectDto.email());
        if (! bCryptPasswordEncoder.matches(loginRequestDto.password(), userSelectDto.passwordHash())) {
            throw new TCInvalidRequestDataException("Wrong password provided for user "+userSelectDto.email());
        }

        User user = User.fromSelectDto(userSelectDto);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId().userId());
        claims.put("email", user.getEmail());
        claims.put("type", user.getUserSecurity().accountType());
        claims.put("language", user.getPreferredLanguage());

        String token = generateTokenUtilityCase.createToken(claims, user.getEmail());
        log.debug("Generated token {}", token);
        log.debug("Saving last login date for user {}", user.getEmail());
        UserSnapshot userSnapshot = user.toSnapshot();
        if(Boolean.TRUE.equals(updateLastLoginDateDatabaseCommand
                .updateLastLoginDateById(userSnapshot.getUserId().userId(), LocalDateTime.now()))){
            log.info("Last login date updated for user {}", userSnapshot.getUserId().userId());
        }
        else{
            throw new TCUpdateFailedException("Could not save last login date for user " + userSnapshot.getUserId().userId());
        }

        log.info("User logged in successfully {}", userSnapshot.getUsername());
        return new TokenResponseDto(token);
    }

}
