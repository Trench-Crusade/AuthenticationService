package org.tc.authservice.infrastructure.redis.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.external.redis.DeleteAllBlacklistTokensExpired;
import org.tc.authservice.core.ports.external.redis.InsertBlacklistTokenEntryRedisCommand;
import org.tc.authservice.infrastructure.redis.entries.TokenBlacklistEntry;
import org.tc.authservice.infrastructure.redis.repositories.TokenBlacklistEntryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistCommandAdapter implements
        InsertBlacklistTokenEntryRedisCommand,
        DeleteAllBlacklistTokensExpired {


    private final TokenBlacklistEntryRepository tokenBlacklistEntryRepository;

    @Override
    public Boolean insertBlacklistTokenEntry(TokenBlacklistEntry tokenBlacklistEntry) {
        try {
            tokenBlacklistEntryRepository.save(tokenBlacklistEntry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteAllBlacklistTokensExpired() {
        log.info("Cleaning up blacklist");
        List<TokenBlacklistEntry> tokenBlacklistEntryList =
                tokenBlacklistEntryRepository.findAll();
        tokenBlacklistEntryList = tokenBlacklistEntryList
                .stream()
                .filter(token -> token.getExpTime().isBefore(LocalDateTime.now()))
                .toList();
        Integer removedEntities = tokenBlacklistEntryList.size();
        log.info("Removing {} tokens that have expired", removedEntities);
        tokenBlacklistEntryRepository.deleteAll(tokenBlacklistEntryList);
    }
}
