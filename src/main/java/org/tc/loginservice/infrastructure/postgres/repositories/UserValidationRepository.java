package org.tc.loginservice.infrastructure.postgres.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tc.loginservice.infrastructure.postgres.entities.UserValidationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserValidationRepository extends CrudRepository<UserValidationEntity, Long> {

    @Query(value = "select v from UserValidationEntity v where v.validationToken =: validationToken")
    Optional<UserValidationEntity> findByValidationToken(UUID validationToken);
}
