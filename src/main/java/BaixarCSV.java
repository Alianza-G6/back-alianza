import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class BaixarCSV {

    public static String NOME_BASE_BAIXADA;

    public void baixar(String NomeDataLake) throws IOException {
        S3Client s3Client = new ConexaoS3().getS3Client();

        try {
            String logMessage = "Iniciando download dos arquivos do bucket: " + NomeDataLake;
            Log.generateLog(logMessage);
            try {
                NotificacaoSlack.EnviarNotificacaoSlack(logMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<S3Object> objects = S3Client.listObjects(ListObjectsRequest.builder()
                    .bucket(NomeDataLake)
                    .build()).contents();

            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(NomeDataLake)
                        .key(object.key())
                        .build();

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());

                File destino = new File("src/" + object.key());
                destino.getParentFile().mkdirs(); // Cria diretórios, se necessário
                Files.copy(inputStream, destino.toPath());

                String successMessage = "Arquivo baixado com sucesso: " + object.key();
                Log.generateLog(successMessage);
                try {
                    NotificacaoSlack.EnviarNotificacaoSlack(successMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.generateLog("Arquivo baixado com sucesso: " + object.key());
                NOME_BASE_BAIXADA = object.key();
            }

            String completionMessage = "Download dos arquivos do bucket " + NomeDataLake + " finalizado.";
            Log.generateLog(completionMessage);
            try {
                NotificacaoSlack.EnviarNotificacaoSlack(completionMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException | S3Exception e) {
            e.printStackTrace();
            try {
                String errorMessage = "Erro ao fazer download dos arquivos do bucket: " + NomeDataLake + " - " + e.getMessage();
                Log.generateLog(errorMessage);
                try {
                    NotificacaoSlack.EnviarNotificacaoSlack(errorMessage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Log.generateLog("Erro ao fazer download dos arquivos do bucket: " + NomeDataLake + " - " + e.getMessage());
        }
    }
}
