package org.tc.loginservice.infrastructure.postgres.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.loginservice.core.ports.external.UpdateLastLoginDateCommandQuery;
import org.tc.loginservice.infrastructure.postgres.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandAdapter implements UpdateLastLoginDateCommandQuery {

    private final UserRepository userRepository;

    @Override
    public Boolean updateLastLoginDateById(UUID userID, LocalDateTime lastLoginDate) {
        try{
            userRepository.updateLastLoginDate(userID, lastLoginDate);
            return true;
        }catch (Exception e){
            log.error("Exception occurred while updating last login date", e);
            return false;
        }

    }
}
