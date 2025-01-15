package org.tc.authservice.core.ports.external.message;

import org.tc.authservice.infrastructure.message.dto.SendMessageDto;

public interface SendMessageCommand {
    Boolean sendMessage(SendMessageDto emailDto);
}
