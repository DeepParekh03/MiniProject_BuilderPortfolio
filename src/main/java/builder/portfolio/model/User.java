package builder.portfolio.model;

import builder.portfolio.model.enums.UserRole;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Getter @Setter private long userId;
    @Getter @Setter private String email;
    @Getter @Setter private String password;
    @Getter @Setter private String userName;
    @Getter @Setter private UserRole role;
}
