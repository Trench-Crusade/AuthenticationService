package org.tc.loginservice.infrastructure.postgres.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserValidationSelectDto(
        UUID userId,
        UUID validationToken,
        LocalDateTime generationTime,
        LocalDateTime invalidationTime
) {
}
