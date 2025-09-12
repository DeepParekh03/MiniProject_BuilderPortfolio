package builder.portfolio.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuditTrail {
    private long auditId;
    private String action;
    private User performedBy;
}
