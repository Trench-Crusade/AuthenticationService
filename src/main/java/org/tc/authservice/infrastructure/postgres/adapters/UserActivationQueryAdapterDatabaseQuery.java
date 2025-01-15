package org.tc.authservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.database.SelectUserActivationByUserIdDatabaseQuery;
import org.tc.authservice.core.ports.external.database.SelectUserActivationByActivationTokenDatabaseQuery;
import org.tc.authservice.infrastructure.postgres.dto.UserActivationSelectDto;
import org.tc.authservice.infrastructure.postgres.entities.UserActivationEntity;
import org.tc.authservice.infrastructure.postgres.mappers.UserActivationDatabaseMapper;
import org.tc.authservice.infrastructure.postgres.repositories.UserActivationRepository;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserActivationQueryAdapterDatabaseQuery implements
        SelectUserActivationByActivationTokenDatabaseQuery,
        SelectUserActivationByUserIdDatabaseQuery {

    private final UserActivationRepository userActivationRepository;

    @Override
    public UserActivationSelectDto selectUserActivationByActivationToken(UUID activationToken) throws TCEntityNotFoundException {
        Optional<UserActivationEntity> userActivationEntityOptional = userActivationRepository.findByActivationToken(activationToken);
        if(userActivationEntityOptional.isEmpty()) {
            throw new TCEntityNotFoundException("Entity with activation token: "+ activationToken +" not found in the database.");
        }
        UserActivationEntity userActivationEntity = userActivationEntityOptional.get();
        return UserActivationDatabaseMapper.fromEntity(userActivationEntity);
    }

    @Override
    public UserActivationSelectDto selectUserActivationByUserId(UUID userId) throws TCEntityNotFoundException {
        Optional<UserActivationEntity> userActivationEntityOptional = userActivationRepository.findByUserId(userId);
        if(userActivationEntityOptional.isEmpty()) {
            throw new TCEntityNotFoundException("Entity with userId: "+userId+" not found in the database.");
        }
        UserActivationEntity userActivationEntity = userActivationEntityOptional.get();
        return UserActivationDatabaseMapper.fromEntity(userActivationEntity);
    }
}
