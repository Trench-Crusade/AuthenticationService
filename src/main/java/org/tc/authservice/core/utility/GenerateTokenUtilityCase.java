package org.tc.authservice.core.utility;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tc.authservice.shared.consts.ConstValues;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateTokenUtilityCase {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Map<String, Object> claims, String userName) {
        JwtBuilder builder = Jwts.builder();

        claims.forEach(builder::claim);
        return builder
                .subject(userName)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(ConstValues.TOKEN_LIFE_LENGTH, ChronoUnit.SECONDS)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

}
