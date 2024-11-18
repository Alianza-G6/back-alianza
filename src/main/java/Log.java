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
    private static FileWriter fw;
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
//        System.out.println("Log Gerado com sucesso!");
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
                "* da empresa SPECTRA. A divulgação, distribuição,\n" + // spectra??
                "* ou cópia deste documento é estritamente proibida\n" +
                "* sem autorização prévia.\n" +
                "/****************/\n\n";
        bw.write(message);
        bw.flush();
        fileCount++;
    }
}

//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.core.sync.RequestBody;
//
//public class Log {
//    private static final long MAX_SIZE = 4 * 1024; // Tamanho máximo do arquivo (4 KB)
//    private static int fileCount = 1; // Contador para arquivos de log
//    private static BufferedWriter bw;
//    private static FileWriter fw;
//    private static Path logFilePath;
//
//    // Mudar variável para nome correto do bucket
//    private static final String BUCKET_NAME = "meu-bucket-de-logs";
//
//    // Instância de conexão com o S3
//    private static final ConexaoS3 conexaoS3 = new ConexaoS3();
//
//    // Método para gerar logs
//    public static void generateLog(String mensagem) throws IOException {
//        // Verifica se precisa rotacionar o arquivo de log
//        if (bw == null || Files.size(logFilePath) >= MAX_SIZE) {
//            closeLogFile(); // Fecha o arquivo atual antes de abrir um novo
//            openNewLogFile();
//        }
//
//        // Adiciona a mensagem com timestamp no log
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String timestamp = now.format(formatter);
//        bw.write(timestamp + " - " + mensagem);
//        bw.newLine();
//        bw.flush();
//    }
//
//    // Método para abrir um novo arquivo de log
//    private static void openNewLogFile() throws IOException {
//        Path logDirectoryPath = Paths.get("C:\\Users\\Public\\LogsAlianza");
//        if (!Files.exists(logDirectoryPath)) {
//            Files.createDirectory(logDirectoryPath);
//        }
//
//        String logFileName = "log_" + fileCount + ".txt";
//        logFilePath = logDirectoryPath.resolve(logFileName);
//
//        fw = new FileWriter(logFilePath.toFile(), true);
//        bw = new BufferedWriter(fw);
//
//        // Escreve cabeçalho inicial no arquivo de log
//        String message = "/****************/\n" +
//                "* Nome da Empresa: Alianza\n" +
//                "* Tipo de Documento: Confidencial\n" +
//                "* \n" +
//                "* Este documento contém informações confidenciais\n" +
//                "* da empresa Alianza. A divulgação, distribuição,\n" +
//                "* ou cópia deste documento é estritamente proibida\n" +
//                "* sem autorização prévia.\n" +
//                "/****************/\n\n";
//        bw.write(message);
//        bw.flush();
//
//        fileCount++;
//    }
//
//    // Método para fechar o arquivo atual de log
//    private static void closeLogFile() throws IOException {
//        if (bw != null) {
//            bw.close();
//            fw.close();
//
//            // Envia o arquivo de log para o bucket S3
//            uploadLogToS3();
//        }
//    }
//
//    // Método para enviar o arquivo de log ao S3
//    private static void uploadLogToS3() {
//        try {
//            S3Client s3Client = conexaoS3.getS3Client();
//            PutObjectRequest putRequest = PutObjectRequest.builder()
//                    .bucket(BUCKET_NAME)
//                    .key(logFilePath.getFileName().toString()) // Nome do arquivo no S3
//                    .build();
//
//            s3Client.putObject(putRequest, RequestBody.fromFile(logFilePath.toFile()));
//            System.out.println("Arquivo " + logFilePath.getFileName() + " enviado para o bucket S3 com sucesso.");
//        } catch (Exception e) {
//            System.err.println("Erro ao enviar arquivo para o S3: " + e.getMessage());
//        }
//    }
//}