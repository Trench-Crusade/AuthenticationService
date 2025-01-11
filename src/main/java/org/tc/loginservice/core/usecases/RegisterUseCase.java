package org.tc.loginservice.core.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.domain.User;
import org.tc.loginservice.core.domain.UserSnapshot;
import org.tc.loginservice.core.domain.sanitizers.UserSanitizer;
import org.tc.loginservice.core.domain.validators.UserValidator;
import org.tc.loginservice.core.domain.vo.UserSecurity;
import org.tc.loginservice.core.ports.external.InsertNewUserDatabaseCommand;
import org.tc.loginservice.core.ports.external.InsertNewUserValidationDatabaseCommand;
import org.tc.loginservice.core.ports.internal.RegisterPort;
import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.RegisterResponseDto;
import org.tc.loginservice.infrastructure.email.dto.SendEmailDto;
import org.tc.loginservice.infrastructure.email.services.SendEmailService;
import org.tc.loginservice.infrastructure.postgres.enums.AccountType;
import org.tc.loginservice.shared.consts.EmailConsts;
import org.tc.loginservice.shared.exceptions.TCEmailSendingFailedException;
import org.tc.loginservice.shared.exceptions.TCIllegalStateException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;
import org.tc.loginservice.shared.exceptions.TCRegistrationFailedException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterUseCase implements RegisterPort {

    private final InsertNewUserDatabaseCommand insertNewUserDatabaseCommand;
    private final InsertNewUserValidationDatabaseCommand insertNewUserValidationDatabaseCommand;
    private final SendEmailService sendEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws TCInvalidRequestDataException, TCRegistrationFailedException, TCEmailSendingFailedException {
        String password = bCryptPasswordEncoder.encode(registerRequestDto.password());
        if(!Objects.equals(registerRequestDto.password(), registerRequestDto.repeatedPassword())){
            throw new TCInvalidRequestDataException("Provided passwords do not match");
        }
        registerRequestDto = UserSanitizer.sanitizeRegisterRequest(registerRequestDto);
        log.debug("Creating user from request DTO");
        User user = User.fromRegisterDto(registerRequestDto);
        log.info("User created, id: {}", user.getUserId().userId());

        List<String> errors = UserValidator.validateCreatedUser(user);
        if(!errors.isEmpty()){
            String validationErrors = errors.stream().reduce(String::concat).toString();
            throw new TCInvalidRequestDataException("Validation failed, reasons: "+validationErrors);
        }
        log.info("User {} validated, proceeding.", user.getUserId().userId());
        UserSecurity userSecurity = new UserSecurity(
                AccountType.PLAYER,
                password
        );
        user.setUserSecurityForRegisteringUser(userSecurity);
        log.info("Security data set for user {}", user.getUserId().userId());
        UserSnapshot userSnapshot = user.toSnapshot();
        if(Boolean.TRUE.equals(insertNewUserDatabaseCommand.insertUser(userSnapshot))){
            log.info("User {} registered successfully", userSnapshot.getUserId().userId());
        }
        else{
            throw new TCRegistrationFailedException("Registration for user "  + user.getEmail() + " failed");
        }

        UUID validateToken = UUID.randomUUID();
        if(Boolean.TRUE.equals(insertNewUserValidationDatabaseCommand.insertUserValidation(userSnapshot.getUserId().userId(), validateToken))){
            log.info("Validation for user {} registered successfully", userSnapshot.getUserId().userId());
        }
        else {
            throw new TCIllegalStateException("Activation token not could not be prepared, administrator action is required!");
        }

        SendEmailDto sendEmailDto = new SendEmailDto(
                userSnapshot.getEmail(),
                EmailConsts.ACTIVATION_TITLE,
                EmailConsts.createActivationEmail(validateToken)

        );
        if(Boolean.TRUE.equals(sendEmailService.sendEmail(sendEmailDto))) {
            log.info("Activation email sent for user {}", userSnapshot.getUserId().userId());
        }
        else{
            throw new TCEmailSendingFailedException("Activation email not could not be sent, administrator action is required!");
        }

        return new RegisterResponseDto(userSnapshot.getUserId().userId());
    }
}
