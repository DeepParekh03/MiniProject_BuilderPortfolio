package builder.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Getter
public class ProjectTimeline {
    private long projectId;
    private String projectName;
    private int completedTasks;
    private int totalTasks;
    private LocalDate endDate;


    public int getRemainingTasks() {
        return totalTasks - completedTasks;
    }

    public long getDaysRemaining() {
        if (endDate == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public String getGanttChart() {
        if (totalTasks == 0) return "[No Tasks]";
        int completedBars = (int) (((double) completedTasks / totalTasks) * 20);
        return "[" + "=".repeat(completedBars) + " ".repeat(20 - completedBars) + "] "
                + completedTasks + "/" + totalTasks + " phases completed";
    }

}

