package pets.authenticate.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pets.authenticate.model.TokenRequest;
import pets.authenticate.model.TokenResponse;
import pets.authenticate.model.UserDetails;
import pets.authenticate.service.TokenKeysService;
import pets.authenticate.service.UserService;

@RestController
@Slf4j
@RequestMapping("/")
public class UserController {

  private final UserService userService;
  private final TokenKeysService tokenKeysService;

  public UserController(UserService userService, TokenKeysService tokenKeysService) {
    this.userService = userService;
    this.tokenKeysService = tokenKeysService;
  }

  private boolean validateRequest(TokenRequest tokenRequest) {
    return tokenRequest != null
        && (tokenRequest.getUser() != null
            || (hasText(tokenRequest.getUsername()) && hasText(tokenRequest.getPassword())));
  }

  @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TokenResponse> login(
      @RequestBody TokenRequest tokenRequest, HttpServletRequest request) {
    if (!validateRequest(tokenRequest)) {
      return new ResponseEntity<>(TokenResponse.builder().build(), BAD_REQUEST);
    }

    UserDetails userDetails = userService.validateUser(tokenRequest);

    if (userDetails == null) {
      return new ResponseEntity<>(TokenResponse.builder().build(), UNAUTHORIZED);
    }

    if (!hasText(tokenRequest.getSourceIp())) {
      tokenRequest.setSourceIp(getSourceIp(request));
    }

    String token = tokenKeysService.createToken(tokenRequest);

    if (!hasText(token)) {
      return new ResponseEntity<>(TokenResponse.builder().build(), SERVICE_UNAVAILABLE);
    }

    TokenResponse tokenResponse =
        TokenResponse.builder().token(token).userDetails(userDetails).build();
    return new ResponseEntity<>(tokenResponse, OK);
  }

  @PostMapping(value = "/refresh", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TokenResponse> refresh(@RequestBody TokenRequest tokenRequest) {
    if (tokenRequest == null || !hasText(tokenRequest.getToken())) {
      return new ResponseEntity<>(TokenResponse.builder().build(), UNAUTHORIZED);
    }

    String newToken =
        tokenKeysService.refreshToken(tokenRequest.getToken(), tokenRequest.isLogOut());

    if (!hasText(newToken)) {
      return new ResponseEntity<>(TokenResponse.builder().build(), FORBIDDEN);
    }

    TokenResponse tokenResponse = TokenResponse.builder().token(newToken).build();
    return new ResponseEntity<>(tokenResponse, OK);
  }

  private String getSourceIp(HttpServletRequest request) {
    String remoteAddress = request.getRemoteAddr();

    if (request.getHeader("X-Forwarded-For") != null) {
      remoteAddress = request.getHeader("X-Forwarded-For");
    }

    return remoteAddress;
  }
}
