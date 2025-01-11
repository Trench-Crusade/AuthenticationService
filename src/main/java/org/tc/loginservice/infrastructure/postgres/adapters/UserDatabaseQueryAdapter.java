package org.tc.loginservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.ports.external.SelectUserByEmailDatabaseQuery;
import org.tc.loginservice.core.ports.external.SelectUserByUserIdDatabaseQuery;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.infrastructure.postgres.entities.UserEntity;
import org.tc.loginservice.infrastructure.postgres.mappers.UserDatabaseMapper;
import org.tc.loginservice.infrastructure.postgres.repositories.UserRepository;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDatabaseQueryAdapter implements
        SelectUserByEmailDatabaseQuery,
        SelectUserByUserIdDatabaseQuery
{

    private final UserRepository userRepository;

    @Override
    public UserSelectDto selectUserByEmail(String email) throws TCEntityNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if(userEntityOptional.isEmpty()) {
            throw new TCEntityNotFoundException("Entity with email: "+email+" not found in the database.");
        }
        UserEntity userEntity = userEntityOptional.get();
        return UserDatabaseMapper.fromEntity(userEntity);
    }

    @Override
    public UserSelectDto selectUser(UUID userId) throws TCEntityNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);
        if(userEntityOptional.isEmpty()) {
            throw new TCEntityNotFoundException("Entity with id: "+userId+" not found in the database.");
        }
        UserEntity userEntity = userEntityOptional.get();
        return UserDatabaseMapper.fromEntity(userEntity);
    }
}
