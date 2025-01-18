package org.tc.authservice.infrastructure.redis.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.redis.SelectBlacklistTokenEntryByIdRedisQuery;
import org.tc.authservice.infrastructure.redis.repositories.TokenBlacklistEntryRepository;
import org.tc.infrastructure.redis.entries.TokenBlacklistEntry;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenBlacklistQueryAdapter implements SelectBlacklistTokenEntryByIdRedisQuery {


    private final TokenBlacklistEntryRepository tokenBlacklistEntryRepository;

    @Override
    public Optional<String> selectBlacklistTokenEntryById(String token) {
        return tokenBlacklistEntryRepository
                .findByToken(token)
                .map(TokenBlacklistEntry::getToken);
    }
}
