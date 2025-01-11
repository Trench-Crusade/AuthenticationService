package org.tc.loginservice.infrastructure.controller.dto.request;

import java.util.UUID;

public record ActivateUserRequestDto(UUID validationToken) {
}
