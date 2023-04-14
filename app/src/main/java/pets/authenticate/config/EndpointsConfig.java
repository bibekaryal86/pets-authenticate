package pets.authenticate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointsConfig {

  @Bean
  public String getUserByUsernameUrl(
      @Value("${user_get_by_username}") String getUserByUsernameUrl) {
    return getUserByUsernameUrl;
  }

  @Bean
  public String saveNewUserUrl(@Value("${user_save_new_user}") String saveNewUserUrl) {
    return saveNewUserUrl;
  }
}
