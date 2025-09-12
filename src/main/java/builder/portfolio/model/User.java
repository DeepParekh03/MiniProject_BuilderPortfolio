package builder.portfolio.model;

import builder.portfolio.model.enums.UserRole;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    private long userId;
    private String email;
    private String password;
    private String userName;
    private UserRole role;
}
