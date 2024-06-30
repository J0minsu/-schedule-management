package sparta.schedule.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserInfo {

    private String id;
    private String password;
    private Long seq;
    private String nickname;

}