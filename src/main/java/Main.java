import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

public class Main {
    public static void main(String[] args) throws Exception {
        try (ConexaoS3 conexaoS3 = new ConexaoS3(); ConexaoBanco conexaoBanco = new ConexaoBanco()) {

            System.out.println("Iniciando aplicação...");
            NotificacaoSlack.EnviarNotificacaoSlack("Iniciando aplicação...");

            JdbcTemplate jdbcTemplate = ConexaoBanco.getConnection();
            S3Client s3Client = conexaoS3.getS3Client();

            String bucketName = "s3-alianza";
            String prefix = "baseDeDados/";

            ListObjectsV2Request listObjects = ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix).build();
            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listObjects);

            LeitorExcel leitorExcel = new LeitorExcel(jdbcTemplate, s3Client);
            leitorExcel.lerEInserirDados();

            System.out.println("Leitura e inserção de dados concluídas.");
            NotificacaoSlack.EnviarNotificacaoSlack("Leitura e inserção de dados concluídas.");
            System.out.println("Finalizando aplicação com sucesso...");
            NotificacaoSlack.EnviarNotificacaoSlack("Finalizando aplicação com sucesso...");
        } catch (Exception e) {
            System.out.println("Erro durante a execução da aplicação: " + e.getMessage());
            NotificacaoSlack.EnviarNotificacaoSlack("Erro durante a execução da aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
