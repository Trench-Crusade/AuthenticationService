package org.tc.authservice.core.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.domain.User;
import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.core.domain.sanitizers.UserSanitizer;
import org.tc.authservice.core.domain.validators.UserValidator;
import org.tc.authservice.core.domain.vo.UserSecurity;
import org.tc.authservice.core.ports.external.database.InsertNewUserDatabaseCommand;
import org.tc.authservice.core.ports.external.database.InsertNewUserActivationDatabaseCommand;
import org.tc.authservice.core.ports.internal.RegisterPort;
import org.tc.authservice.infrastructure.api.dto.request.RegisterRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.RegisterResponseDto;
import org.tc.authservice.infrastructure.message.dto.SendMessageDto;
import org.tc.authservice.infrastructure.message.services.SendEmailService;
import org.tc.infrastructure.postgres.enums.AccountType;
import org.tc.authservice.shared.consts.EmailConsts;
import org.tc.authservice.shared.exceptions.general.detailed.TCIllegalStateException;
import org.tc.authservice.shared.exceptions.out.detailed.TCEmailSendingFailedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCInsertFailedException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterUseCase implements RegisterPort {

    private final InsertNewUserDatabaseCommand insertNewUserDatabaseCommand;
    private final InsertNewUserActivationDatabaseCommand insertNewUserActivationDatabaseCommand;
    private final SendEmailService sendEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws TCInvalidRequestDataException, TCEmailSendingFailedException, TCInsertFailedException, TCIllegalStateException {
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
            throw new TCInsertFailedException("Registration for user "  + user.getEmail() + " failed");
        }

        UUID validateToken = UUID.randomUUID();
        if(Boolean.TRUE.equals(insertNewUserActivationDatabaseCommand.insertUserActivation(userSnapshot.getUserId().userId(), validateToken))){
            log.info("Validation for user {} registered successfully", userSnapshot.getUserId().userId());
        }
        else {
            throw new TCInsertFailedException("Activation token not could not be prepared, administrator action is required!");
        }

        SendMessageDto sendMessageDto = new SendMessageDto(
                userSnapshot.getEmail(),
                EmailConsts.ACTIVATION_TITLE,
                EmailConsts.createActivationEmail(validateToken)

        );
        if(Boolean.TRUE.equals(sendEmailService.sendMessage(sendMessageDto))) {
            log.info("Activation email sent for user {}", userSnapshot.getUserId().userId());
        }
        else{
            throw new TCEmailSendingFailedException("Activation email not could not be sent, administrator action is required!");
        }

        return new RegisterResponseDto(userSnapshot.getUserId().userId());
    }
}
