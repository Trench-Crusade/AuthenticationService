package org.tc.loginservice.core.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.domain.User;
import org.tc.loginservice.core.domain.UserSnapshot;
import org.tc.loginservice.core.ports.external.SelectUserByEmailDatabaseQuery;
import org.tc.loginservice.core.ports.external.UpdateLastLoginDateDatabaseCommand;
import org.tc.loginservice.core.ports.internal.LogInPort;
import org.tc.loginservice.core.utility.GenerateTokenUtilityCase;
import org.tc.loginservice.infrastructure.controller.dto.request.LoginRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.TokenResponseDto;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;
import org.tc.loginservice.shared.exceptions.TCIUpdateFailedException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;

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
    public TokenResponseDto logIn(LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCIUpdateFailedException {
        log.info("Attempted login for email: {}", loginRequestDto.email());
        UserSelectDto userSelectDto = selectUserByEmailDatabaseQuery.selectUserByEmail(loginRequestDto.email());
        log.info("User {} found by email", userSelectDto.email());
        String password = bCryptPasswordEncoder.encode(loginRequestDto.password());
        if (!userSelectDto.passwordHash().equals(password)) {
            throw new TCInvalidRequestDataException("Wrong password provided for user "+userSelectDto.email());
        }

        User user = User.fromSelectDto(userSelectDto);

        Map<String, Object> claims = new HashMap<>();
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
            throw new TCIUpdateFailedException("Could not save last login date for user " + userSnapshot.getUserId().userId());
        }

        log.info("User logged in successfully {}", userSnapshot.getUsername());
        return new TokenResponseDto(token);
    }

}
