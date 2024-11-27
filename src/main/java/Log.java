import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

public class Log {
    private static final long MAX_SIZE = 4 * 1024; // Tamanho máximo do arquivo (4 KB)
    private static int fileCount = 1; // Contador para arquivos de log
    private static BufferedWriter bw;
    private static FileWriter fw;
    private static Path logFilePath;

    // Nome do bucket S3 e pasta
    private static final String BUCKET_NAME = "s3-alianza";
    private static final String LOGS_FOLDER = "logs/"; // Pasta dentro do bucket

    // Instância de conexão com o S3
    private static final ConexaoS3 conexaoS3 = new ConexaoS3();

    // Método para gerar logs
    public static void generateLog(String mensagem) throws IOException {
        // Verifica se precisa rotacionar o arquivo de log
        if (bw == null || Files.size(logFilePath) >= MAX_SIZE) {
            closeLogFile(); // Fecha o arquivo atual antes de abrir um novo
            openNewLogFile();
        }

        // Adiciona a mensagem com timestamp no log
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        bw.write(timestamp + " - " + mensagem);
        bw.newLine();
        bw.flush();
    }

    // Método para abrir um novo arquivo de log
    private static void openNewLogFile() throws IOException {
        // Define o diretório de logs
        Path logDirectoryPath = Paths.get("/app/logs");

        // Verifica se o diretório existe, cria se não existir
        if (!Files.exists(logDirectoryPath)) {
            Files.createDirectories(logDirectoryPath);
            System.out.println("Diretório de logs criado: " + logDirectoryPath.toAbsolutePath());
        }

        // Gera o nome do arquivo com base em um contador ou timestamp
        String logFileName = "log_" + fileCount + ".txt";
        logFilePath = logDirectoryPath.resolve(logFileName);

        boolean isNewFile = !Files.exists(logFilePath);

        // Inicializa o FileWriter e BufferedWriter
        fw = new FileWriter(logFilePath.toFile(), true); // Abre em modo "append"
        bw = new BufferedWriter(fw);

        // Escreve cabeçalho inicial apenas se o arquivo for novo
        if (isNewFile) {
            String header = """
            /****************/
            * Nome da Empresa: Alianza
            * Tipo de Documento: Confidencial
            * 
            * Este documento contém informações confidenciais
            * da empresa Alianza. A divulgação, distribuição,
            * ou cópia deste documento é estritamente proibida
            * sem autorização prévia.
            /****************/
            
            """;
            bw.write(header);
            bw.flush();
            System.out.println("Cabeçalho escrito no novo arquivo de log.");
        }

        // Incrementa o contador para futuros arquivos
        fileCount++;
        System.out.println("Arquivo de log aberto: " + logFilePath.toAbsolutePath());
    }

    // Método para fechar o arquivo atual de log
    private static void closeLogFile() throws IOException {
        if (bw != null) {
            bw.close();
            fw.close();

            // Envia o arquivo de log para o bucket S3
            uploadLogToS3();
        }
    }

    // Método para enviar o arquivo de log ao S3
    private static void uploadLogToS3() {
        try {
            S3Client s3Client = conexaoS3.getS3Client();
            String s3Key = LOGS_FOLDER + logFilePath.getFileName().toString(); // Caminho completo no bucket
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(s3Key)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromFile(logFilePath.toFile()));
            System.out.println("Arquivo " + logFilePath.getFileName() + " enviado para o bucket S3 na pasta 'logs' com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao enviar arquivo para o S3: " + e.getMessage());
        }
    }
}
