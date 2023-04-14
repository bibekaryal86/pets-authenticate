package pets.authenticate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secret-key.properties")
public class SecretKey {

  @Bean
  public String petsSecretKey(@Value("${pets_secret_key}") String petsSecretKey) {
    return petsSecretKey;
  }
}
