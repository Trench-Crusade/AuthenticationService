package org.tc.loginservice.core.ports.internal;

import org.tc.loginservice.infrastructure.controller.dto.request.LoginRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.TokenResponseDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;

public interface LogInPort {
    TokenResponseDto logIn(LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException;
}
