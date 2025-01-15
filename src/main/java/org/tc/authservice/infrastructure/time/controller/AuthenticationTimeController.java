package org.tc.authservice.infrastructure.time.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tc.authservice.core.ports.internal.ClearBlacklistPort;
import org.tc.authservice.shared.consts.ConstValues;

@Service
@RequiredArgsConstructor
public class AuthenticationTimeController {

    private final ClearBlacklistPort clearBlacklistUseCase;

    @Scheduled(fixedRate = ConstValues.BLACKLIST_CLEAR_TIME, initialDelay = ConstValues.BLACKLIST_CLEAR_DELAY)
    private void clearBlacklist(){
        clearBlacklistUseCase.clearBlacklist();
    }
}
