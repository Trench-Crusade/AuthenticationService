package org.tc.authservice.core.ports.internal;

import jakarta.servlet.http.HttpServletRequest;
import org.tc.authservice.infrastructure.api.dto.response.LogoutResponseDto;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.exceptions.persistence.detailed.TCInsertFailedException;

public interface LogoutPort {
    LogoutResponseDto logOut(HttpServletRequest httpServletRequest) throws TCTokenExpiredException, TCTokenNotProvidedException, TCInsertFailedException;
}
