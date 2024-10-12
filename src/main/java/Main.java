import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        JdbcTemplate connection = ConexaoBanco.getConnection();

        List<Voo> voosDoBanco = connection.query("SELECT * FROM voo", new BeanPropertyRowMapper<>(Voo.class));

        System.out.println("Voos no banco de dados:");

        for (Voo voo : voosDoBanco) {
            System.out.println(voo.toString());
        }
    }
}
