import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();
        List<Funcionario> listaUsuario = connection.query("SELECT * FROM Funcionario;", new BeanPropertyRowMapper<>(Funcionario.class));

        System.out.println("Funcionário Existentes");
        for (Funcionario funcionario : listaUsuario) {
            System.out.println(funcionario);
        }

//        FileInputStream arquivoS3 = new FileInputStream("https://s3-alianza.s3.amazonaws.com/Base+de+dados+-+Voos.csv");
//        Workbook planilha = new HSSFWorkbook(arquivoS3);
//        Sheet tabela = planilha.getSheetAt(0);
    }
}
