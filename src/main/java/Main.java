import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
    public static void main(String[] args) {
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate jdbcTemplate = conexaoBanco.getJdbcTemplate();
        InserirDadosVoo inserirDadosVoo = new InserirDadosVoo(jdbcTemplate);

        // Insira o caminho do seu arquivo CSV aqui
        String caminhoArquivo = "caminho/para/seu/arquivo.csv";
        inserirDadosVoo.inserirDadosDoArquivoCSV(caminhoArquivo);
    }
}
