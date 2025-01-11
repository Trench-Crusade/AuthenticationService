package org.tc.loginservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.ports.external.SelectUserValidationByValidationTokenDatabaseQuery;
import org.tc.loginservice.infrastructure.postgres.dto.UserValidationSelectDto;
import org.tc.loginservice.infrastructure.postgres.entities.UserValidationEntity;
import org.tc.loginservice.infrastructure.postgres.mappers.UserValidationDatabaseMapper;
import org.tc.loginservice.infrastructure.postgres.repositories.UserValidationRepository;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserValidationQueryAdapterDatabaseQuery implements SelectUserValidationByValidationTokenDatabaseQuery {

    private final UserValidationRepository userValidationRepository;

    @Override
    public UserValidationSelectDto selectUserValidation(UUID validationToken) throws TCEntityNotFoundException {
        Optional<UserValidationEntity> userValidationEntityOptional = userValidationRepository.findByValidationToken(validationToken);
        if(userValidationEntityOptional.isEmpty()) {
            throw new TCEntityNotFoundException("Entity with validation token: "+validationToken+" not found in the database.");
        }
        UserValidationEntity userValidationEntity = userValidationEntityOptional.get();
        return UserValidationDatabaseMapper.fromEntity(userValidationEntity);
    }
}
