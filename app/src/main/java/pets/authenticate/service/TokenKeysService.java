package pets.authenticate.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pets.authenticate.model.TokenRequest;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class TokenKeysService {

    private final ObjectMapper objectMapper;
    private final String petsSecretKey;

    private static final long EXPIRATION = 1800000;    // 30 MINUTES

    public TokenKeysService(String petsSecretKey) {
        this.objectMapper = new ObjectMapper();
        this.petsSecretKey = petsSecretKey;
    }

    public String createToken(TokenRequest tokenRequest) {
        String token = null;

        try {
            TokenRequest tokenClaim = TokenRequest.builder()
                    .username(tokenRequest.getUsername())
                    .sourceIp(tokenRequest.getSourceIp())
                    .build();

            Map<String, Object> claims = objectMapper.convertValue(tokenClaim, new TypeReference<>() {});

            token = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, petsSecretKey)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .compact();
        } catch (Exception ex) {
            log.error("Error in creating token: {}", tokenRequest, ex);
        }

        return token;
    }

    public String refreshToken(String oldToken, boolean isLogOut) {
        String newToken = null;

        try {
            Date expirationDate;
            if (isLogOut) {
                expirationDate = new Date(System.currentTimeMillis() + 5000);
            } else {
                expirationDate = new Date(System.currentTimeMillis() + EXPIRATION);
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(petsSecretKey)
                    .parseClaimsJws(oldToken)
                    .getBody();
            // TokenRequest tokenRequest = objectMapper.convertValue(claims, TokenRequest.class);   // NOSONAR
            newToken = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, petsSecretKey)
                    .setExpiration(expirationDate)
                    .compact();
        } catch (Exception ex) {
            log.error("Error in decoding old token: {}", oldToken, ex);
        }

        return newToken;
    }
}
