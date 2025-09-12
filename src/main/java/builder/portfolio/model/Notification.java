package builder.portfolio.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notification {
    private long notificationId;
    private String message;
    private User recipient;
}
