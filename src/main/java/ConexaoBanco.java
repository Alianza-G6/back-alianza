import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private String url;
    private String usuario;
    private String senha;
    private Connection conexao;

    public ConexaoBanco(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    // Método para obter a conexão com o banco de dados
    public Connection getConnection() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection(url, usuario, senha);
        }
        return conexao;
    }

    // Método para fechar a conexão
    public void close() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }
}
