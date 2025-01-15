package org.tc.authservice.core.ports.internal;

import jakarta.servlet.http.HttpServletRequest;
import org.tc.authservice.infrastructure.api.dto.request.LoginRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.TokenResponseDto;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCUpdateFailedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCInsertFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

public interface LogInPort {
    TokenResponseDto logIn(HttpServletRequest request, LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCInsertFailedException, TCUpdateFailedException;
}
