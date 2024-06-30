package sparta.schedule.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    SecurityDetailsService jwtUserDetailsService;
    JwtUtil jwtUtil;
//    StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            /**
             * check 'token is expired'
             */
            String jwt = authorizationHeader.substring(7);
//            if (stringRedisTemplate.opsForValue().get(jwt) == null) {
//               throw new BusinessException(HttpStatus.NOT_FOUND);
//            }

            if (jwtUtil.validateToken(jwt)) {

                Long seq = jwtUtil.getSeq(jwt);

                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(String.valueOf(seq));

                if (userDetails != null) {
                    // jwt이므로 2번째 파라미터 credentials(password)를 null로 넘겨도 문제 없음(이미 앞 단계에서 인증 완료됨)
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}