package org.tc.authservice.core.ports.external.redis;

import org.tc.infrastructure.redis.entries.TokenBlacklistEntry;

public interface InsertBlacklistTokenEntryRedisCommand {
    Boolean insertBlacklistTokenEntry(TokenBlacklistEntry tokenBlacklistEntry);
}
