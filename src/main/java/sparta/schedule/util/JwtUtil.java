package sparta.schedule.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.entity.common.enums.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : sparta.schedule.util
 * fileName       : JwtUtil
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Slf4j
@Component
public class JwtUtil {

    private final Key key;

    private String JWT_SECRET = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";

    public JwtUtil() {

        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createJwt(UserType userType, Long seq, String name,Long activeMinute) {

        Claims claims = Jwts.claims();
        claims.put("userType", userType);
        claims.put("name", name);
        claims.put("seq", seq);

        ZonedDateTime issueTime = ZonedDateTime.now();

        Date expiredDate = Date.from(issueTime.plusMinutes(activeMinute).toInstant());

        return Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(Date.from(issueTime.toInstant()))
                        .setExpiration(expiredDate)
                        .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
    }

    public Long getSeq(String jwt) {
        return parseClaims(jwt).get("seq", Long.class);
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
            throw new RuntimeException("This token is Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new RuntimeException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new RuntimeException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new RuntimeException("JWT claims string is empty");
        }
    }

    public Claims parseClaims(String jwt) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}