package sparta.schedule.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * packageName    : sparta.schedule.util
 * fileName       : CustomPasswordEncoder
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@Slf4j
@Configuration
public class CustomPasswordEncoder {

    @Value("${schedule.management.salt}")
    private String SALT;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {

                String saltedPassword = rawPassword.toString() + SALT;

                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hashedBytes = digest.digest(saltedPassword.getBytes());

                    return new String(Hex.encode(hashedBytes));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Error encoding password", e);
                }

            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {

                String hashedPassword = encode(rawPassword);

                return encodedPassword.equals(hashedPassword);
            }

        };

    }

}