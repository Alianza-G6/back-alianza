import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Log {
    private static final long MAX_SIZE = 4 * 1024;
    private static int fileCount = 1;
    private static BufferedWriter bw;
    private static FileWriter   fw;
    private static Path logFilePath;
    public static void generateLog(String mensagem) throws IOException {
        if (bw == null || Files.size(logFilePath) >= MAX_SIZE) {
            openNewLogFile();
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        bw.write(timestamp + " - " + mensagem);
        bw.newLine();
        bw.flush();
        System.out.println("Log Gerado com sucesso!");
    }
    private static void openNewLogFile() throws IOException {
        Path logDirectoryPath = Paths.get("C:\\Users\\Public\\LogsAlianza");
        if (!Files.exists(logDirectoryPath)) {
            Files.createDirectory(logDirectoryPath);
        }
        String logFileName = "log_" + fileCount + ".txt";
        logFilePath = logDirectoryPath.resolve(logFileName);
        fw = new FileWriter(logFilePath.toFile(), true);
        bw = new BufferedWriter(fw);
        String message = "/****************/\n" +
                "* Nome da Empresa: Alianza\n" +
                "* Tipo de Documento: Confidencial\n" +
                "* \n" +
                "* Este documento contém informações confidenciais\n" +
                "* da empresa SPECTRA. A divulgação, distribuição,\n" +
                "* ou cópia deste documento é estritamente proibida\n" +
                "* sem autorização prévia.\n" +
                "/****************/\n\n";
        bw.write(message);
        bw.flush();
        fileCount++;
    }
}