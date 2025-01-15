package org.tc.authservice.infrastructure.api.dto.request;

import java.util.UUID;

public record ActivateUserByTokenRequestDto(UUID activationToken) {
}
