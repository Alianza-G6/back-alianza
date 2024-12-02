import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoBanco implements AutoCloseable {

    private static final String databaseUrl = System.getenv("DATABASE_URL");
    private static final String databaseUser = System.getenv("DATABASE_USER");
    private static final String databasePassword = System.getenv("DATABASE_PASSWORD");

    private static final BasicDataSource dataSource = new BasicDataSource();
    private static JdbcTemplate jdbcTemplate;

    static {
        try {

            dataSource.setUrl(databaseUrl);
            dataSource.setUsername(databaseUser);
            dataSource.setPassword(databasePassword);
            NotificacaoSlack.EnviarNotificacaoSlack("Conexão com o banco configurada com sucesso.");
            System.out.println("Conexão com o banco configurada com sucesso.");
            Log.generateLog("Conexão com o banco configurada com sucesso.");

        } catch (Exception e) {
            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Erro na configuração do banco de dados");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Erro na configuração do banco de dados");
            Log.tratarErroComLog(e);
            throw new RuntimeException(e);
        }
    }

    public ConexaoBanco() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static JdbcTemplate getConnection() {
        return jdbcTemplate;
    }

    @Override
    public void close() throws Exception {
        try {
            if (dataSource != null) {
                dataSource.close();
                System.out.println("Conexão com o banco fechada com sucesso.");
                Log.generateLog("Conexão com o banco fechada com sucesso.");
                NotificacaoSlack.EnviarNotificacaoSlack("Conexão com o banco fechada com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao fechar a conexão com o banco de dados");
            Log.tratarErroComLog(e);
            NotificacaoSlack.EnviarNotificacaoSlack("Erro ao fechar a conexão com o banco de dados");
        }
    }
}
