package builder.portfolio.util;

import builder.portfolio.model.AuditTrail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {
    private static final String FILE_PATH = "./src/main/resources/auditTrail.txt";

    public static List<AuditTrail> readAuditTrails() {
        List<AuditTrail> auditList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                AuditTrail auditTrail = AuditTrail.fromString(line);
                if (auditTrail != null) {
                    auditList.add(auditTrail);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found, returning empty list.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return auditList;
    }
}
