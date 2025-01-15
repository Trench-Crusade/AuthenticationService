package org.tc.authservice.core.usecases;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.internal.ValidateProvidedTokenPort;
import org.tc.authservice.core.utility.VerifyTokenUtilityCase;
import org.tc.authservice.infrastructure.api.dto.response.TokenValidationResponseDto;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;

@Service
@RequiredArgsConstructor
public class ValidateProvidedTokenUseCase implements ValidateProvidedTokenPort {

    private final VerifyTokenUtilityCase verifyTokenUtilityCase;
    @Override
    public TokenValidationResponseDto validateProvidedToken(HttpServletRequest request) throws TCInvalidRequestDataException, TCTokenNotProvidedException, TCTokenExpiredException {
        return new TokenValidationResponseDto(verifyTokenUtilityCase.verifyToken(request));
    }
}
