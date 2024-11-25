import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class ConexaoS3 implements AutoCloseable {

    private final AwsSessionCredentials credentials;
    private S3Client s3Client;

    public ConexaoS3() {
        try {
//            Log.logAndSlack("Validando credenciais do S3...");
            System.out.println("Validando credenciais do S3...");

            this.credentials = AwsSessionCredentials.create(
                    System.getenv("AWS_ACCESS_KEY_ID"),
                    System.getenv("AWS_SECRET_ACCESS_KEY"),
                    System.getenv("AWS_SESSION_TOKEN")
            );
//            Log.logAndSlack("Credenciais do S3 validadas com sucesso.");
            System.out.println("Credenciais do S3 validadas com sucesso");

        } catch (Exception e) {
            //                Log.logAndSlackErro("Erro ao validar credenciais do S3", e);
            System.out.println("Erro ao validar credenciais do S3");
            throw new RuntimeException(e);
        }
    }

    public S3Client getS3Client() {
        try {
            if (s3Client == null) {
//                Log.logAndSlack("Criando conexão com S3...");
                System.out.println("Criando conexão com S3...");
                s3Client = S3Client.builder()
                        .region(Region.US_EAST_1)
                        .credentialsProvider(() -> credentials)
                        .build();
//                Log.logAndSlack("Conexão com o S3 configurada com sucesso.");
                System.out.println("Conexão com o S3 configurada com SUCESSO");
            }
            return s3Client;
        } catch (Exception e) {
            //                Log.logAndSlackErro("Erro ao criar conexão com o S3", e);
            System.out.println("Erro ao criar conexão com o S3");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (s3Client != null) {
            try {
//                Log.logAndSlack("Encerrando conexão com o S3...");
                System.out.println("Encerrando conexão com o S3...");
                s3Client.close();
//                Log.logAndSlack("Conexão com o S3 encerrada com sucesso.");
                System.out.println("Conexão com o S3 encerrada com");
            } catch (Exception e) {
                //                    Log.logAndSlackErro("Erro ao encerrar conexão com o S3", e);
                System.out.println("Erro ao encerrar conexão com o S3");
            }
        }
    }
}
