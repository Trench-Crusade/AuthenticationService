package org.tc.loginservice.core.ports.internal;

import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.RegisterResponseDto;

public interface RegisterPort {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
}
