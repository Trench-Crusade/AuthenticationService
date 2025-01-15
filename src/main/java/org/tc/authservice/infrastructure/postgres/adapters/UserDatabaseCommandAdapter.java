package org.tc.authservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.domain.UserSnapshot;
import org.tc.authservice.core.ports.external.database.InsertNewUserDatabaseCommand;
import org.tc.authservice.core.ports.external.database.UpdateLastLoginDateDatabaseCommand;
import org.tc.authservice.core.ports.external.database.UpdateUserAccountStatusDatabaseCommand;
import org.tc.authservice.infrastructure.postgres.entities.UserEntity;
import org.tc.authservice.infrastructure.postgres.mappers.UserDatabaseMapper;
import org.tc.authservice.infrastructure.postgres.repositories.UserRepository;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDatabaseCommandAdapter implements
        UpdateLastLoginDateDatabaseCommand,
        InsertNewUserDatabaseCommand,
        UpdateUserAccountStatusDatabaseCommand
{

    private final UserRepository userRepository;

    @Override
    public Boolean updateLastLoginDateById(UUID userID, LocalDateTime lastLoginDate) {
        try{
            userRepository.updateLastLoginDate(userID, lastLoginDate);
            return true;
        } catch (Exception e){
            log.error("Exception occurred while updating last login date ", e);
            return false;
        }

    }

    @Override
    public Boolean insertUser(UserSnapshot userSnapshot) {
        try{
            UserEntity userEntity = UserDatabaseMapper.fromSnapshot(userSnapshot);
            userRepository.save(userEntity);
            return true;
        } catch (Exception e){
            log.error("Exception occurred while registering user ", e);
            return false;
        }
    }

    @Override
    public Boolean updateUserAccountStatus(UserSnapshot userSnapshot) {
        try{
            Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userSnapshot.getUserId().userId());
            if(userEntityOptional.isEmpty()) {
                throw new TCEntityNotFoundException("Entity with id: "+userSnapshot.getUserId().userId()+" not found in the database.");
            }
            UserEntity userEntity = userEntityOptional.get();
            UserEntity userEntityFromSnapshot = UserDatabaseMapper.fromSnapshot(userSnapshot);
            userEntityFromSnapshot.setId(userEntity.getId());
            userRepository.save(userEntityFromSnapshot);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
