package builder.portfolio.model;

import builder.portfolio.model.enums.ReportType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Report {
    private long reportId;
    private Project project;
    private ReportType type;
}
