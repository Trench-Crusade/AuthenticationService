package org.tc.loginservice.core.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.domain.User;
import org.tc.loginservice.core.ports.external.SelectUserByEmailDatabaseQuery;
import org.tc.loginservice.core.ports.external.UpdateLastLoginDateCommandQuery;
import org.tc.loginservice.core.ports.internal.LogInPort;
import org.tc.loginservice.infrastructure.controller.dto.request.LoginRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.TokenResponseDto;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class LogInUserUseCase implements LogInPort {

    private final SelectUserByEmailDatabaseQuery selectUserByEmailDatabaseQuery;
    private final UpdateLastLoginDateCommandQuery updateLastLoginDateCommandQuery;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public TokenResponseDto logIn(LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException {
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

        String token = generateTokenUseCase.createToken(claims, user.getEmail());
        log.debug("Generated token {}", token);
        log.debug("Saving last login date for user {}", user.getEmail());
        updateLastLoginDateCommandQuery.updateLastLoginDateById(user.getUserId().userId(), LocalDateTime.now());
        log.info("User logged in successfully {}", userSelectDto.username());
        return new TokenResponseDto(token);
    }

}
