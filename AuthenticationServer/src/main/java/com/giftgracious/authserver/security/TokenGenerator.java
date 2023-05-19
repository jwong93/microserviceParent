package com.giftgracious.authserver.security;

import com.giftgracious.authserver.dto.TokenDTO;
import com.giftgracious.authserver.model.User;
import com.nimbusds.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TokenGenerator {

    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder jwtRefreshTokenEncoder;

    private String createAccessToken (Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant curent = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("GiftGracious")
                .issuedAt(curent)
                .expiresAt(curent.plus(30, ChronoUnit.HOURS))
                .subject(user.getId())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createRefreshToken (Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant curent = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("GiftGracious")
                .issuedAt(curent)
                .expiresAt(curent.plus(30, ChronoUnit.HOURS))
                .subject(user.getId())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public TokenDTO createToken(Authentication auth){
        if (!(auth.getPrincipal() instanceof User user)){
            throw new BadCredentialsException("Principal not user type");
        }
        TokenDTO tokenDTO = TokenDTO.builder()
                        .userId(user.getId())
                                .accessToken(createAccessToken(auth))
                .build();

        String refreshToken;
        if (auth.getCredentials() instanceof Jwt jwt){
            Instant current = Instant.now();
            Instant expires = jwt.getExpiresAt();
            Duration duration = Duration.between(current,expires);
            long daysUntilExpiry = duration.toDays();
            if (daysUntilExpiry < 5){
                refreshToken = createRefreshToken(auth);
            }
            else{
                refreshToken = jwt.getTokenValue();
            }
        }else{
            refreshToken =createRefreshToken(auth);
        }
        tokenDTO.setRefreshToken(refreshToken);
        return tokenDTO;
    }


}
