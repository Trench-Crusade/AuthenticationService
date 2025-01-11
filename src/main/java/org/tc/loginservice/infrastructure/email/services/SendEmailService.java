package org.tc.loginservice.infrastructure.email.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.tc.loginservice.infrastructure.email.dto.SendEmailDto;

@RequiredArgsConstructor
@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    public Boolean sendEmail(SendEmailDto sendEmailDto){
        try{
            SimpleMailMessage mailMsg = new SimpleMailMessage();
            mailMsg.setFrom(emailSender);
            mailMsg.setTo(sendEmailDto.email());
            mailMsg.setText(sendEmailDto.message());
            mailMsg.setSubject(sendEmailDto.title());
            javaMailSender.send(mailMsg);
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }
}
