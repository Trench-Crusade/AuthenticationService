package org.tc.loginservice.core.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.ports.external.SelectUserByEmailDatabaseQuery;
import org.tc.loginservice.core.ports.internal.RegisterPort;
import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.RegisterResponseDto;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterUseCase implements RegisterPort {

    private final SelectUserByEmailDatabaseQuery selectUserByEmailDatabaseQuery;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        return null;
    }
}
