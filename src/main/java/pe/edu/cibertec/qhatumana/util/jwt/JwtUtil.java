package pe.edu.cibertec.qhatumana.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        String username = authentication.getPrincipal().toString(); //el usuario que se autentico
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        String jwtToken = JWT.create().withIssuer(userGenerator).withSubject(username)
                .withClaim("authorities", authorities).withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) //en media hora se vence el token
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return jwtToken;
    }

    public DecodedJWT validateToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(userGenerator).build();
            decodedJWT = verifier.verify(token);
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token Invalido, Sin Autorización");
        }
        return decodedJWT;
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    public String createRefreshToken(String username)  {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        return JWT.create()
                .withIssuer(userGenerator)
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) //7 dias mas.
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public DecodedJWT validateRefreshToken(String refreshToken) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(userGenerator).build();
            decodedJWT = verifier.verify(refreshToken);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Refresh token inválido");
        }
        return decodedJWT;
    }

    public String generateAccessToken(DecodedJWT decodedRefreshToken) {
        String username = decodedRefreshToken.getSubject();  //extraer el nombre de usuario del refresh token
        return createToken(new UsernamePasswordAuthenticationToken(username, null));
    }

}
