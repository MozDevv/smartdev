package app.moz.smartdev.configs;

import app.moz.smartdev.entity.CustomUserDetails;
import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private static final String SECRET_KEY = "4171ACC6E95C3187C51614D42C1CA22D814D67B0F0ECC7DCED8FDF28C28751E8";
    private UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
//     String username = extractUsername("tsgvvg");
    public String extractUsername(String token) {
        return extractSingleClaim(token, Claims::getSubject);
    }

    public <T> T extractSingleClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractClaims(token);
        return claimsTFunction.apply(claims);
    }
    public String extractUserId(String token) {
        return extractSingleClaim(token, claims -> claims.get("userId", String.class));
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        extraClaims.put("userId", user.getId().toString());
        extraClaims.put("email", user.getEmail());

        // Add other details as needed


        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60  * 2))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractSingleClaim(token, Claims::getExpiration);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}