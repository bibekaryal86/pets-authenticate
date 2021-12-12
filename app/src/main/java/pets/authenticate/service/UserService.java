package pets.authenticate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pets.authenticate.connector.UserConnector;
import pets.authenticate.model.TokenRequest;
import pets.authenticate.model.UserDetails;
import pets.authenticate.model.user.User;
import pets.authenticate.model.user.UserResponse;

@Service
@Slf4j
public class UserService {

    private final UserConnector userConnector;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserConnector userConnector) {
        this.userConnector = userConnector;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public UserDetails validateUser(TokenRequest tokenRequest) {
        UserDetails userDetails = null;

        try {
            UserResponse userResponse = userConnector.getUserByUsername(tokenRequest.getUsername());

            if (userResponse != null && userResponse.getUsers() != null) {
                User user = userResponse.getUsers().stream()
                        .filter(userDetail -> bCryptPasswordEncoder.matches(tokenRequest.getPassword(),
                                userDetail.getPassword()))
                        .findFirst()
                        .orElse(null);

                if (user != null) {
                    userDetails = UserDetails.builder().build();
                    BeanUtils.copyProperties(user, userDetails);
                }
            }
        } catch (Exception ex) {
            log.error("Error in Validate User: {}", tokenRequest.getUsername(), ex);
        }

        return userDetails;
    }
}
