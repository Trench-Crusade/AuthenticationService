package org.tc.loginservice.infrastructure.email.dto;

public record SendEmailDto(
        String email,
        String title,
        String message
) {
}
