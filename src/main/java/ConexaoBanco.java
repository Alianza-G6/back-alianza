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
            // Log para indicar que a configuração do banco de dados está sendo iniciada
            System.out.println("Inicializando conexão com o banco de dados...");

            dataSource.setUrl(databaseUrl);
            dataSource.setUsername(databaseUser);
            dataSource.setPassword(databasePassword);

            // Log para indicar que a configuração foi realizada com sucesso
            System.out.println("Conexão com o banco configurada com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro na configuração do banco de dados");
            throw new RuntimeException(e);
        }
    }

    public ConexaoBanco() {
        // Inicia a conexão com o banco de dados
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static JdbcTemplate getConnection() {
        return jdbcTemplate;
    }

    @Override
    public void close() {
        // Fechar a conexão do datasource quando a classe for fechada
        try {
            if (dataSource != null) {
                dataSource.close();
                System.out.println("Conexão com o banco fechada com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao fechar a conexão com o banco de dados");
        }
    }
}
