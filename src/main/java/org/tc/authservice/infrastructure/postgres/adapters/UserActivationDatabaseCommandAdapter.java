package org.tc.authservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.database.InsertNewUserActivationDatabaseCommand;
import org.tc.authservice.infrastructure.postgres.entities.UserActivationEntity;
import org.tc.authservice.infrastructure.postgres.repositories.UserActivationRepository;
import org.tc.authservice.shared.consts.ConstValues;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserActivationDatabaseCommandAdapter implements InsertNewUserActivationDatabaseCommand {

    private final UserActivationRepository userActivationRepository;

    @Override
    public Boolean insertUserActivation(UUID userId, UUID activationToken) {
        try{
            UserActivationEntity userActivationEntity = new UserActivationEntity(
                    null,
                    userId,
                    activationToken,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(ConstValues.ACCOUNT_ACTIVATION_TIME)
            );
            userActivationRepository.save(userActivationEntity);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
