package org.tc.authservice.core.ports.internal;

import org.tc.authservice.infrastructure.api.dto.request.ActivateUserByTokenRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.ActivateUserResponseDto;
import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCUpdateFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

public interface ActivateUserByLinkPort {
    ActivateUserResponseDto activateUser(ActivateUserByTokenRequestDto activateUserByTokenRequestDto) throws TCEntityNotFoundException, TCAccessDeniedException, TCInvalidRequestDataException, TCUpdateFailedException;
}
