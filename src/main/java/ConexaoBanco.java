import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

public class ConexaoBanco {

    private static final String databaseUrl = System.getenv("DATABASE_URL");
    private static final String databaseUser = System.getenv("DATABASE_USER");
    private static final String databasePassword = System.getenv("DATABASE_PASSWORD");

    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        try {
            String logMessage = "Inicializando conexão com o banco de dados...";
            Log.generateLog(logMessage);
            try {
                NotificacaoSlack.EnviarNotificacaoSlack(logMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dataSource.setUrl(databaseUrl);
            dataSource.setUsername(databaseUser);
            dataSource.setPassword(databasePassword);

            String successMessage = "Conexão com o banco configurada com sucesso.";
            Log.generateLog(successMessage);
            try {
                NotificacaoSlack.EnviarNotificacaoSlack(successMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                String errorMessage = "Erro na configuração do banco de dados: " + e.getMessage();
                Log.generateLog(errorMessage);
                NotificacaoSlack.EnviarNotificacaoSlack(errorMessage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static JdbcTemplate getConnection() {
        try {
            String logMessage = "Tentando estabelecer conexão com o banco de dados...";
            Log.generateLog(logMessage);
            try {
                NotificacaoSlack.EnviarNotificacaoSlack(logMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            String successMessage = "Conexão estabelecida com sucesso.";
            Log.generateLog(successMessage);
            try {
                NotificacaoSlack.EnviarNotificacaoSlack(successMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jdbcTemplate;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                String errorMessage = "Erro ao conectar ao banco de dados: " + e.getMessage();
                Log.generateLog(errorMessage);
                NotificacaoSlack.EnviarNotificacaoSlack(errorMessage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
    }
}
