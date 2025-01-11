package org.tc.loginservice.core.ports.internal;

import jakarta.servlet.http.HttpServletRequest;
import org.tc.loginservice.infrastructure.controller.dto.request.ActivateUserRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.ActivateUserResponseDto;
import org.tc.loginservice.shared.exceptions.TCAccessDeniedException;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;
import org.tc.loginservice.shared.exceptions.TCIUpdateFailedException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;

public interface ActivateUserPort {
    ActivateUserResponseDto activateUser(HttpServletRequest httpServletRequest, ActivateUserRequestDto activateUserRequestDto) throws TCEntityNotFoundException, TCAccessDeniedException, TCInvalidRequestDataException, TCIUpdateFailedException;
}
