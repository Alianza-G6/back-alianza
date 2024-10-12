import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoBanco {

//    private static final String databaseUrl = "jdbc:mysql://localhost:3306/Alianza"; -> TESTE LOCALHOST


    private static final String databaseUrl = System.getenv("DATABASE_URL");
    private static final String databaseUser = System.getenv("DATABASE_USER");
    private static final String databasePassword = System.getenv("DATABASE_PASSWORD");

    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);
    }

    public static JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}
