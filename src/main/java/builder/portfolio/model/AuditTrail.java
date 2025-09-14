package builder.portfolio.model;

import builder.portfolio.model.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class AuditTrail {
        private String action;
        private User performedBy;


    @Override
    public String toString() {
        return  action + "," + performedBy.getUserId() + "," + performedBy.getUserName()+ "," +performedBy.getRole();
    }

    public static AuditTrail fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) return null;
        String action = parts[0];

        User user = new User();
        user.setUserId(Long.parseLong(parts[1]));
        user.setUserName(parts[2]);
        user.setRole(UserRole.valueOf(parts[3]));

        return new AuditTrail(action, user);
    }
    }
