package pets.authenticate.connector;

import java.net.URI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.authenticate.model.user.User;
import pets.authenticate.model.user.UserResponse;

@Component
public class UserConnector {

  private final RestTemplate restTemplate;
  private final String getUserByUsernameUrl;
  private final String saveNewUserUrl;

  public UserConnector(
      @Qualifier("restTemplate") RestTemplate restTemplate,
      String getUserByUsernameUrl,
      String saveNewUserUrl) {
    this.restTemplate = restTemplate;
    this.getUserByUsernameUrl = getUserByUsernameUrl;
    this.saveNewUserUrl = saveNewUserUrl;
  }

  public UserResponse getUserByUsername(String username) {
    String url =
        UriComponentsBuilder.fromHttpUrl(getUserByUsernameUrl).buildAndExpand(username).toString();

    ResponseEntity<UserResponse> responseEntity =
        restTemplate.getForEntity(url, UserResponse.class);

    return responseEntity.getBody();
  }

  public UserResponse saveNewUser(User user) {
    URI uri = UriComponentsBuilder.fromHttpUrl(saveNewUserUrl).build().toUri();

    ResponseEntity<UserResponse> responseEntity =
        restTemplate.exchange(
            uri, HttpMethod.POST, new HttpEntity<>(user, null), UserResponse.class);

    return responseEntity.getBody();
  }
}
