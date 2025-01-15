package org.tc.authservice.infrastructure.postgres.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserActivationSelectDto(
        UUID userId,
        UUID activationToken,
        LocalDateTime generationTime,
        LocalDateTime invalidationTime
) {
}
