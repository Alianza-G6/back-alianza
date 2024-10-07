import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;

public class ConexaoBanco {
    private static final String URL = "jdbc:mysql://3.88.249.82:3306/Alianza";
    private static final String USER = "seu_usuario";
    private static final String PASSWORD = "sua_senha";

    private JdbcTemplate jdbcTemplate;

    public ConexaoBanco() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}