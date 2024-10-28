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
            String mensagemIniciar = "Iniciando download dos arquivos do bucket: " + NomeDataLake;
            Log.generateLog(mensagemIniciar);
            System.out.println(mensagemIniciar);

            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder()
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

                String mensagemBaixado = "Arquivo baixado com sucesso: " + object.key();
                Log.generateLog(mensagemBaixado);
                System.out.println(mensagemBaixado);
                NOME_BASE_BAIXADA = object.key();
            }

            String mensagemFinalizado = "Download dos arquivos do bucket " + NomeDataLake + " finalizado.";
            Log.generateLog(mensagemFinalizado);
            System.out.println(mensagemFinalizado);

        } catch (IOException | S3Exception e) {
            e.printStackTrace();
            String mensagemErro = "Erro ao fazer download dos arquivos do bucket: " + NomeDataLake + " - " + e.getMessage();
            Log.generateLog(mensagemErro);
            System.out.println(mensagemErro);
        }
    }
}
