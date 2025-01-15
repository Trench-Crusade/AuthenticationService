package org.tc.authservice.core.ports.internal;

import org.tc.authservice.infrastructure.api.dto.request.RegisterRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.RegisterResponseDto;
import org.tc.authservice.shared.exceptions.general.detailed.TCIllegalStateException;
import org.tc.authservice.shared.exceptions.out.detailed.TCEmailSendingFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCInsertFailedException;

public interface RegisterPort {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws TCInvalidRequestDataException, TCEmailSendingFailedException, TCInsertFailedException, TCIllegalStateException;
}
