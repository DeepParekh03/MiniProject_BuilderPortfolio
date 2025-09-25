package builder.portfolio.util;

import builder.portfolio.model.AuditTrail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading audit trail data from a file.
 *
 * This class provides a single static method {@link #readAuditTrails()} that reads
 * audit trail records from a file located at {@code ./src/main/resources/auditTrail.txt}
 * and returns them as a list of {@link AuditTrail} objects.
 *
 * Each line in the file is expected to represent a single audit trail entry, which
 * is parsed using {@link AuditTrail#fromString(String)}.
 *
 * If the file is not found or an error occurs while reading, the method prints a message
 * to the console and returns an empty list.
 *
 * Example usage:
 * List<AuditTrail> auditTrails = FileReaderUtil.readAuditTrails();
 * auditTrails.forEach(System.out::println);
 */
public class FileReaderUtil {

    /** Path to the audit trail file. */
    private static final String FILE_PATH = "./src/main/resources/auditTrail.txt";

    /**
     * Reads the audit trail entries from the file and returns them as a list.
     *
     * @return a {@link List} of {@link AuditTrail} objects read from the file;
     *         an empty list if the file is not found or an error occurs during reading
     */
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
