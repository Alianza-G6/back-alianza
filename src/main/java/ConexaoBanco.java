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
            Log.generateLog("Inicializando conexão com o banco de dados...");

            dataSource.setUrl(databaseUrl);
            dataSource.setUsername(databaseUser);
            dataSource.setPassword(databasePassword);

            Log.generateLog("Conexão com o banco configurada com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Log.generateLog("Erro na configuração do banco de dados: " + e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static JdbcTemplate getConnection() {
        try {
            Log.generateLog("Tentando estabelecer conexão com o banco de dados...");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            Log.generateLog("Conexão estabelecida com sucesso.");
            return jdbcTemplate;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Log.generateLog("Erro ao conectar ao banco de dados: " + e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
    }
}
