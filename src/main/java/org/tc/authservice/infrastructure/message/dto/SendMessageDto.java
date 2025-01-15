package org.tc.authservice.infrastructure.message.dto;

public record SendMessageDto(
        String email,
        String title,
        String message
) {
}
