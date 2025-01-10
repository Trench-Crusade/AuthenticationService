package org.tc.loginservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.ports.external.SelectUserByEmailDatabaseQuery;
import org.tc.loginservice.infrastructure.postgres.dto.UserSelectDto;
import org.tc.loginservice.infrastructure.postgres.entities.UserEntity;
import org.tc.loginservice.infrastructure.postgres.mappers.UserDatabaseMapper;
import org.tc.loginservice.infrastructure.postgres.repositories.UserRepository;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryAdapter implements SelectUserByEmailDatabaseQuery {

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
}
