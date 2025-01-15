package org.tc.authservice.core.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.internal.ClearBlacklistPort;
import org.tc.authservice.infrastructure.redis.adapters.TokenBlacklistCommandAdapter;

@Service
@RequiredArgsConstructor
public class ClearBlacklistUseCase implements ClearBlacklistPort {

    private final TokenBlacklistCommandAdapter tokenBlacklistCommandAdapter;

    @Override
    public void clearBlacklist() {
        tokenBlacklistCommandAdapter.deleteAllBlacklistTokensExpired();
    }
}
