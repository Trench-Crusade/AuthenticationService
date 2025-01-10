package org.tc.loginservice.core.usecases;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tc.loginservice.shared.consts.ConstValues;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateTokenUseCase {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ ConstValues.TOKEN_LIFE_LENGTH))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}
