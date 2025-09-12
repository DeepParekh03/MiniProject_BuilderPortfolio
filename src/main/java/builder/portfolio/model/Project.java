package builder.portfolio.model;

import builder.portfolio.model.enums.Status;
import lombok.*;
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
    private User builder;
    private User projectManager;
    private User client;
    private List<Document> document;
    private List<Timeline> timeline;
}
