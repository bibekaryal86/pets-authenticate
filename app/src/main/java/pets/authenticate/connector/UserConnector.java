package pets.authenticate.connector;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.authenticate.model.user.UserResponse;

@Component
public class UserConnector {

    private final RestTemplate restTemplate;
    private final String getUserByUsernameUrl;

    public UserConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                         String getUserByUsernameUrl) {
        this.restTemplate = restTemplate;
        this.getUserByUsernameUrl = getUserByUsernameUrl;
    }

    public UserResponse getUserByUsername(String username) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getUserByUsernameUrl)
                .buildAndExpand(username)
                .toString();

        ResponseEntity<UserResponse> responseEntity = restTemplate.getForEntity(url, UserResponse.class);

        return responseEntity.getBody();
    }
}
