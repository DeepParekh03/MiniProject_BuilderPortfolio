package builder.portfolio.util;

import builder.portfolio.model.AuditTrail;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility class for writing audit trail entries to a file.
 *
 * This class provides a single static method {@link #writeAuditTrail(AuditTrail)}
 * that appends a given {@link AuditTrail} object to the audit trail file
 * located at {@code ./src/main/resources/auditTrail.txt}.
 *
 * Each audit trail entry is written as a new line using the {@link AuditTrail#toString()}
 * representation. If an error occurs while writing to the file, an error message is printed
 * to the console.
 *
 * Example usage:
 * AuditTrail audit = new AuditTrail("User logged in", "admin", "2025-09-25T10:00:00");
 * FileWriterUtil.writeAuditTrail(audit);
 */
public class FileWriterUtil {

    /** Path to the audit trail file. */
    private static final String FILE_PATH = "./src/main/resources/auditTrail.txt";

    /**
     * Writes an {@link AuditTrail} entry to the audit trail file.
     * The entry is appended to the end of the file. If the file does not exist,
     * it will be created automatically.
     *
     * @param auditTrail the {@link AuditTrail} object to write to the file
     */
    public static void writeAuditTrail(AuditTrail auditTrail) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(auditTrail.toString());
            System.out.println("AuditTrail written: " + auditTrail);

        } catch (IOException e) {
            System.out.println("Error writing AuditTrail: " + e.getMessage());
        }
    }
}
