package builder.portfolio.util;

import builder.portfolio.model.AuditTrail;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriterUtil {

    private static final String FILE_PATH = "./src/main/resources/auditTrail.txt";


    public static void writeAuditTrail(AuditTrail auditTrail) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(auditTrail.toString());
            System.out.println(" AuditTrail written: " + auditTrail);

        } catch (IOException e) {
            System.out.println("Error writing AuditTrail: " + e.getMessage());
        }
    }
}

