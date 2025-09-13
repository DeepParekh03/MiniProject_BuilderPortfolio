package builder.portfolio.model;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Task {
    private long taskId;
    private long projectId;
    private String taskName;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}

