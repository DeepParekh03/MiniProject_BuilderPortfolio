package builder.portfolio.model;

import builder.portfolio.model.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project {
    private long projectId;
    private String projectName;
    private Status status;
    private double plannedBudget;
    private double actualSpend;
    private long builderId;
    private long projectManagerId;
    private long clientId;
    private LocalDate endDate;
    private List<Document> document;
    private List<Task> timeline;
}
