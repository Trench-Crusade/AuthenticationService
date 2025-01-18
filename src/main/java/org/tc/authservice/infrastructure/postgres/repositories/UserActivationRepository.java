package org.tc.authservice.infrastructure.postgres.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tc.infrastructure.postgres.entities.UserActivationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserActivationRepository extends CrudRepository<UserActivationEntity, Long> {

    @Query(value = "select v from UserActivationEntity v where v.activationToken = :activationToken")
    Optional<UserActivationEntity> findByActivationToken(UUID activationToken);

    @Query(value = "select v from UserActivationEntity v where v.userId = :userId")
    Optional<UserActivationEntity> findByUserId(UUID userId);
}
