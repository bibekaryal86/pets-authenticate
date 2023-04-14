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
    boolean isSaveNewUser = tokenRequest.getUser() != null;
    String password =
        isSaveNewUser ? tokenRequest.getUser().getPassword() : tokenRequest.getPassword();

    try {
      UserResponse userResponse;
      if (isSaveNewUser) {
        String newPassword = bCryptPasswordEncoder.encode(tokenRequest.getUser().getPassword());
        User user = User.builder().password(newPassword).build();
        BeanUtils.copyProperties(tokenRequest.getUser(), user, "password");
        userResponse = userConnector.saveNewUser(user);
      } else {
        userResponse = userConnector.getUserByUsername(tokenRequest.getUsername());
      }

      if (userResponse != null && userResponse.getUsers() != null) {
        User user =
            userResponse.getUsers().stream()
                .filter(
                    userDetail -> bCryptPasswordEncoder.matches(password, userDetail.getPassword()))
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
