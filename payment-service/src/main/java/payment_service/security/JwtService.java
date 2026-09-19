package payment_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey ;

    public JwtService(@Value("${jwt.secret}") String secret){
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }

    public UUID extractUserId(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload() ;

        return UUID.fromString(claims.getSubject());
    }

    public boolean isValid(String token)
    {
        try{
            extractUserId(token);
            return true ;
        }
        catch (Exception exception)
        {
            return false;
        }
    }
}
