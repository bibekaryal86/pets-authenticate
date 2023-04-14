package pets.authenticate.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class UserDetails implements Serializable {
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private static final long serialVersionUID = 1L;

  private String username;
  private String firstName;
  private String lastName;
  private String streetAddress;
  private String city;
  private String state;
  private String zipcode;
  private String email;
  private String phone;
  private String status;
}
