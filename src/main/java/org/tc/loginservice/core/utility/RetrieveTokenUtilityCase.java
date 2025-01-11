package org.tc.loginservice.core.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tc.loginservice.shared.exceptions.TCAccessDeniedException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveTokenUtilityCase {

    @Value("${jwt.secret}")
    private String secret;

    public UUID getUserId(HttpServletRequest request) throws TCAccessDeniedException {
        String token = getAuthHeader(request);
        Claims claims = getAllClaims(token);
        validateToken(claims);
        return UUID.fromString(String.valueOf(claims.get("id")));
    }

    private static String getAuthHeader(HttpServletRequest request) throws TCAccessDeniedException {
        if(request.getHeader("Authorization") == null){
            throw new TCAccessDeniedException("No token provided");
        }
        return request.getHeaders("Authorization").nextElement();
    }

    private static Boolean validateToken(Claims claims){
        long epochTime = Long.parseLong(String.valueOf(claims.get("exp")));
        LocalDateTime expTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime),
                        TimeZone.getDefault().toZoneId());
        return expTime.isAfter(LocalDateTime.now());
    }

    private Claims getAllClaims(String token) throws TCAccessDeniedException {
        if(token.contains("bearer:")){
            token=token.substring(7);
        }

        Claims claims;
        try{
            claims = Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            throw new TCAccessDeniedException(e.getMessage());
        }
        return claims;
    }

}
