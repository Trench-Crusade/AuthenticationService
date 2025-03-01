package org.tc.authservice.core.ports.external.redis;

import org.tc.authservice.infrastructure.redis.entities.TokenBlacklistEntry;

public interface InsertBlacklistTokenEntryRedisCommand {
    Boolean insertBlacklistTokenEntry(TokenBlacklistEntry tokenBlacklistEntry);
}
