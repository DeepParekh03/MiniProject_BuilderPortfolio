package builder.portfolio.model;


import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Timeline {
    private long timelineId;
    private Project project;
    private Date startDate;
    private Date endDate;
    private List<String> tasks;
}

