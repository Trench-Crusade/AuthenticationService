package org.tc.loginservice.core.ports.internal;

import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.RegisterResponseDto;
import org.tc.loginservice.shared.exceptions.TCEmailSendingFailedException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;
import org.tc.loginservice.shared.exceptions.TCRegistrationFailedException;

public interface RegisterPort {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws TCInvalidRequestDataException, TCRegistrationFailedException, TCEmailSendingFailedException;
}
