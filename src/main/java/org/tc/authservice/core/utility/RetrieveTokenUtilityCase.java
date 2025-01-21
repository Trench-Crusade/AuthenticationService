package org.tc.authservice.core.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tc.authservice.shared.consts.ConstValues;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveTokenUtilityCase {

    @Value("${jwt.secret}")
    private String secret;

    public UUID getUserId(HttpServletRequest request) throws TCTokenExpiredException, TCTokenNotProvidedException {
        String token = retrieveToken(request);
        return getUserIdFromToken(token);
    }

    public UUID getUserIdFromToken(String token) throws TCTokenExpiredException {
        Claims claims = getAllClaims(token);
        validateToken(claims);
        return UUID.fromString(String.valueOf(claims.get("id")));
    }

    public String retrieveToken(HttpServletRequest request) throws TCTokenNotProvidedException, TCTokenExpiredException {
        if(request.getHeader(ConstValues.AUTHORIZATION_HEADER) == null){
            throw new TCTokenNotProvidedException("No token provided");
        }
        String token = request.getHeader(ConstValues.AUTHORIZATION_HEADER);
        if(Boolean.FALSE.equals(validateToken(getAllClaims(token)))){
            throw new TCTokenExpiredException("Token is expired");
        }
        return token;
    }

    public String getExpirationDate(String token) throws TCTokenExpiredException {
        return getAllClaims(token).get("exp").toString();
    }

    private static Boolean validateToken(Claims claims){
        long epochTime = Long.parseLong(String.valueOf(claims.get("exp")));
        LocalDateTime expTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime),
                        TimeZone.getDefault().toZoneId());
        return expTime.isAfter(LocalDateTime.now());
    }

    private Claims getAllClaims(String token) throws TCTokenExpiredException {
        if(token.contains("Bearer ")){
            token=token.substring(7);
        }

        Claims claims;
        try{
            claims = Jwts.parser().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            throw new TCTokenExpiredException(e.getMessage());
        }
        return claims;
    }

}
