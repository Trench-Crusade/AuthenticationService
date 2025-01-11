package org.tc.loginservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.ports.external.InsertNewUserValidationDatabaseCommand;
import org.tc.loginservice.infrastructure.postgres.entities.UserValidationEntity;
import org.tc.loginservice.infrastructure.postgres.repositories.UserValidationRepository;
import org.tc.loginservice.shared.consts.ConstValues;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserValidationDatabaseCommandAdapter implements InsertNewUserValidationDatabaseCommand {

    private final UserValidationRepository userValidationRepository;

    @Override
    public Boolean insertUserValidation(UUID userId, UUID validateToken) {
        try{
            UserValidationEntity userValidationEntity = new UserValidationEntity(
                    null,
                    userId,
                    validateToken,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(ConstValues.ACCOUNT_VALIDATION_TIME)
            );
            userValidationRepository.save(userValidationEntity);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
