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

    public void baixar(String NomeDataLake) {

        S3Client S3Client = new ConexaoS3().getS3Client();

        try {
            List<S3Object> objects = S3Client.listObjects(ListObjectsRequest.builder().bucket(NomeDataLake).build()).contents();
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(NomeDataLake)
                        .key(object.key())
                        .build();

                InputStream inputStream = S3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                Files.copy(inputStream, new File(object.key()).toPath());
                System.out.println("Arquivo baixado: " + object.key());
                NOME_BASE_BAIXADA = object.key();
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
        }

    }
}
