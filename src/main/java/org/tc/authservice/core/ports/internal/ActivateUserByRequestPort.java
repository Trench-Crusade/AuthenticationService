package org.tc.authservice.core.ports.internal;

import jakarta.servlet.http.HttpServletRequest;
import org.tc.authservice.infrastructure.api.dto.response.ActivateUserResponseDto;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCUpdateFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

public interface ActivateUserByRequestPort {
    ActivateUserResponseDto activateUser(HttpServletRequest httpServletRequest) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCUpdateFailedException, TCTokenExpiredException, TCTokenNotProvidedException;
}
