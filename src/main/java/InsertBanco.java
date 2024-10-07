import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.Row;

public class InsertBanco {
    private Connection conexao;

    public InsertBanco(Connection conexao) {
        this.conexao = conexao;
    }

    public void inserirVoo(Row linha) throws SQLException {
        // Lê os valores das células
        String siglaEmpresaAerea = getCellValueAsString(linha.getCell(0));
        String numeroVoo = getCellValueAsString(linha.getCell(1));
        String codigoDI = getCellValueAsString(linha.getCell(2));
        String codigoTipoLinha = getCellValueAsString(linha.getCell(3));
        String siglaAeroportoOrigem = getCellValueAsString(linha.getCell(4));
        String partidaPrevista = getCellValueAsString(linha.getCell(5));
        String partidaReal = getCellValueAsString(linha.getCell(6));
        String siglaAeroportoDestino = getCellValueAsString(linha.getCell(7));
        String chegadaPrevista = getCellValueAsString(linha.getCell(8));
        String chegadaReal = getCellValueAsString(linha.getCell(9));
        String situacaoVoo = getCellValueAsString(linha.getCell(10));

        // Verifica se o aeroporto de origem existe ou insere
        int idAeroportoOrigem = verificarOuInserirAeroporto(siglaAeroportoOrigem);

        // Verifica se o aeroporto de destino existe ou insere
        int idAeroportoDestino = verificarOuInserirAeroporto(siglaAeroportoDestino);

        // Verifica se a companhia existe ou insere
        int idCompanhia = verificarOuInserirCompanhia(siglaEmpresaAerea);

        // Insere na tabela voo
        String comandoInsert = "INSERT INTO voo (fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem, partidaPrevista, partidaReal, fkAeroportoDestino, chegadaPrevista, chegadaReal, situacaoVoo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement executarComandoInsert = conexao.prepareStatement(comandoInsert)) {
            executarComandoInsert.setInt(1, idCompanhia);
            executarComandoInsert.setString(2, numeroVoo);
            executarComandoInsert.setString(3, codigoDI);
            executarComandoInsert.setString(4, codigoTipoLinha);
            executarComandoInsert.setInt(5, idAeroportoOrigem);
            executarComandoInsert.setString(6, partidaPrevista);
            executarComandoInsert.setString(7, partidaReal);
            executarComandoInsert.setInt(8, idAeroportoDestino);
            executarComandoInsert.setString(9, chegadaPrevista);
            executarComandoInsert.setString(10, chegadaReal);
            executarComandoInsert.setString(11, situacaoVoo);
            executarComandoInsert.executeUpdate();
        }
    }

    private int verificarOuInserirAeroporto(String siglaAeroporto) throws SQLException {
        String queryVerificaAeroporto = "SELECT idAeroporto FROM aeroporto WHERE sigla = ?";
        try (PreparedStatement verificarAeroporto = conexao.prepareStatement(queryVerificaAeroporto)) {
            verificarAeroporto.setString(1, siglaAeroporto);
            ResultSet rsAeroporto = verificarAeroporto.executeQuery();
            if (rsAeroporto.next()) {
                return rsAeroporto.getInt("idAeroporto");
            } else {
                // Insere na tabela Aeroporto
                String tabelaAeroporto = "INSERT INTO aeroporto (sigla) VALUES (?)";
                try (PreparedStatement executarComandoAeroporto = conexao.prepareStatement(tabelaAeroporto, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    executarComandoAeroporto.setString(1, siglaAeroporto);
                    executarComandoAeroporto.executeUpdate();
                    ResultSet rsNovoAeroporto = executarComandoAeroporto.getGeneratedKeys();
                    if (rsNovoAeroporto.next()) {
                        return rsNovoAeroporto.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    private int verificarOuInserirCompanhia(String siglaEmpresaAerea) throws SQLException {
        String queryVerificaCompanhia = "SELECT idCompanhia FROM companhia WHERE sigla = ?";
        try (PreparedStatement verificarCompanhia = conexao.prepareStatement(queryVerificaCompanhia)) {
            verificarCompanhia.setString(1, siglaEmpresaAerea);
            ResultSet rsCompanhia = verificarCompanhia.executeQuery();
            if (rsCompanhia.next()) {
                return rsCompanhia.getInt("idCompanhia");
            } else {
                // Insere na tabela Companhia
                String tabelaCompanhia = "INSERT INTO companhia (sigla) VALUES (?)";
                try (PreparedStatement executarComandoCompanhia = conexao.prepareStatement(tabelaCompanhia, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    executarComandoCompanhia.setString(1, siglaEmpresaAerea);
                    executarComandoCompanhia.executeUpdate();
                    ResultSet rsNovoCompanhia = executarComandoCompanhia.getGeneratedKeys();
                    if (rsNovoCompanhia.next()) {
                        return rsNovoCompanhia.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    // Método auxiliar para obter o valor da célula como String
    private String getCellValueAsString(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }
        return cell.toString();
    }
}
