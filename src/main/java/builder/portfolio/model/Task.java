package builder.portfolio.model;


import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
}

