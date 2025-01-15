package org.tc.authservice.core.ports.internal;

import jakarta.servlet.http.HttpServletRequest;
import org.tc.authservice.infrastructure.api.dto.response.TokenValidationResponseDto;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

public interface ValidateProvidedTokenPort {
    TokenValidationResponseDto validateProvidedToken(HttpServletRequest request) throws TCInvalidRequestDataException, TCTokenNotProvidedException, TCTokenExpiredException;
}
