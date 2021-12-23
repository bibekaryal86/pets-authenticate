package pets.authenticate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import pets.authenticate.model.user.User;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class TokenRequest {
    @Setter(AccessLevel.NONE)
    private String username;
    @ToString.Exclude @Setter(AccessLevel.NONE)
    private String password;
    private String sourceIp;
    private String token;
    private boolean logOut;
    // for save new user request
    private User user;
}
