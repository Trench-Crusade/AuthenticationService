package org.tc.authservice.infrastructure.redis.repositories;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tc.authservice.infrastructure.redis.entities.TokenBlacklistEntry;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenBlacklistEntryRepository extends CrudRepository<TokenBlacklistEntry, String> {

    Optional<TokenBlacklistEntry> findByToken(@NonNull String token);
    @Override
    @NonNull
    List<TokenBlacklistEntry> findAll();
}
