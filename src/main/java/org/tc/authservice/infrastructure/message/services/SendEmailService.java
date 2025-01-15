package org.tc.authservice.infrastructure.message.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.message.SendMessageCommand;
import org.tc.authservice.infrastructure.message.dto.SendMessageDto;

@RequiredArgsConstructor
@Service
public class SendEmailService implements SendMessageCommand {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    @Override
    public Boolean sendMessage(SendMessageDto sendMessageDto){
        try{
            SimpleMailMessage mailMsg = new SimpleMailMessage();
            mailMsg.setFrom(emailSender);
            mailMsg.setTo(sendMessageDto.email());
            mailMsg.setText(sendMessageDto.message());
            mailMsg.setSubject(sendMessageDto.title());
            javaMailSender.send(mailMsg);
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }
}
