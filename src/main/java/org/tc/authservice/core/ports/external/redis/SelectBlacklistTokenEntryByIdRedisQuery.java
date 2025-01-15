package org.tc.authservice.core.ports.external.redis;

import java.util.Optional;

public interface SelectBlacklistTokenEntryByIdRedisQuery {
    Optional<String> selectBlacklistTokenEntryById(String token);
}
