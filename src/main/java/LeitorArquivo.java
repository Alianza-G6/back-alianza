import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

public abstract class LeitorArquivo {
    protected final JdbcTemplate jdbcTemplate;

    public LeitorArquivo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Método abstrato para ser implementado pela classe derivada
    public abstract void lerEInserirDados(String caminhoArquivo);

    protected int obterOuInserirCompanhia(String siglaCompanhia) {
        String sqlSelect = "SELECT idCompanhia FROM tbCompanhia WHERE siglaICAO = ?";
        try {
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaCompanhia);
        } catch (EmptyResultDataAccessException e) {
            String sqlInsert = "INSERT INTO tbCompanhia (siglaICAO) VALUES (?)";
            jdbcTemplate.update(sqlInsert, siglaCompanhia);
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaCompanhia);
        }
    }

    protected int obterOuInserirAeroporto(String siglaAeroporto) {
        String sqlSelect = "SELECT idAeroporto FROM tbAeroporto WHERE siglaICAO = ?";
        try {
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaAeroporto);
        } catch (EmptyResultDataAccessException e) {
            String sqlInsert = "INSERT INTO tbAeroporto (siglaICAO) VALUES (?)";
            jdbcTemplate.update(sqlInsert, siglaAeroporto);
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaAeroporto);
        }
    }
}
