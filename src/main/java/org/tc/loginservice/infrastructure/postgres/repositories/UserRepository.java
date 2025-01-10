package org.tc.loginservice.infrastructure.postgres.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tc.loginservice.infrastructure.postgres.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "select u from UserEntity u where u.email =: email")
    Optional<UserEntity> findByEmail(String email);

    @Transactional
    @Query(value = "update UserEntity u set u.lastLogin =: lastLoginDate where u.userId =: userId")
    void updateLastLoginDate(UUID userId, LocalDateTime lastLoginDate);

    @Query
    List<String>
}
