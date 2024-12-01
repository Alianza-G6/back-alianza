import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            // Log inicial da aplicação
            Log.generateLog("Iniciando aplicação...");
            System.out.println("Iniciando aplicação...");
            NotificacaoSlack.EnviarNotificacaoSlack("Iniciando aplicação...");

            try (ConexaoS3 conexaoS3 = new ConexaoS3(); ConexaoBanco conexaoBanco = new ConexaoBanco()) {
                // Estabelecendo conexão com o banco de dados
                JdbcTemplate jdbcTemplate = ConexaoBanco.getConnection();
                Log.generateLog("Conexão com o banco de dados estabelecida com sucesso.");

                // Estabelecendo conexão com o S3
                S3Client s3Client = conexaoS3.getS3Client();
                Log.generateLog("Conexão com o serviço S3 estabelecida com sucesso.");

                // Configurando bucket e prefixo
                String bucketName = "s3-alianza";
                String prefix = "baseDeDados/";

                // Listando objetos do bucket
                ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .build();
                ListObjectsV2Response listResponse = s3Client.listObjectsV2(listObjects);
                Log.generateLog("Lista de objetos do bucket " + bucketName + " obtida com sucesso.");

                // Processando os dados do Excel
                LeitorExcel leitorExcel = new LeitorExcel(jdbcTemplate, s3Client);
                leitorExcel.lerEInserirDados();
                Log.generateLog("Leitura e inserção de dados concluídas com sucesso.");
            }

            // Finalizando aplicação
            Log.generateLog("Finalizando aplicação com sucesso.");
            System.out.println("Finalizando aplicação com sucesso...");
            NotificacaoSlack.EnviarNotificacaoSlack("Finalizando aplicação com sucesso.");

        } catch (Exception e) {
            // Log de erros e envio para o S3
            Log.tratarErroComLog(e);  // Usando o método para log de erro e envio para o S3
            System.out.println("Erro durante a execução da aplicação: " + e.getMessage());
            NotificacaoSlack.EnviarNotificacaoSlack("Erro durante a execução da aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
