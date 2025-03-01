package org.tc.authservice.infrastructure.redis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@RedisHash("token_blacklist")
@Getter
@AllArgsConstructor
public class TokenBlacklistEntry implements Serializable {
    @Id
    private String token;
    private UUID userId;
    private LocalDateTime expTime;
}

