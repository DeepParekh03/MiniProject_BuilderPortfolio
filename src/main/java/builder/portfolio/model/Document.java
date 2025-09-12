package builder.portfolio.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Document {
    private long documentId;
    private Project project;
    private String type;
    private String filePath;
    private User uploadedBy;
}
