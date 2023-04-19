package pets.authenticate.config;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secret-key.properties")
public class SecretKey {

  @Bean
  public static javax.crypto.SecretKey petsSecretKey(
      @Value("${pets_secret_key}") String petsSecretKey) {
    return Keys.hmacShaKeyFor(petsSecretKey.getBytes(StandardCharsets.UTF_8));
  }
}
