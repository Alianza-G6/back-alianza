import java.io.*;
import java.net.URL;
import java.sql.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    private static final int LINHAS_POR_EXECUCAO = 100;
    private static final String CONTADOR_ARQUIVO = "contador.txt"; // Onde sera armazenado a ultima linha lida pelo for

    public static void main(String[] args) {
        String caminhoArquivo = "https://s3-alianza.s3.amazonaws.com/Base%2Bde%2Bdados%2B-%2BVoos.xlsx";
        String urlMySQL = "jdbc:mysql://3.95.157.243:3306/Alianza";
        String usuario = "root";
        String senha = "urubu100";

        try (Connection conexao = DriverManager.getConnection(urlMySQL, usuario, senha)) {
            System.out.println("Conexão com o banco de dados estabelecida.");
            System.out.println("Iniciando leitura do arquivo Excel.");
            InputStream leitorExcel = new URL(caminhoArquivo).openStream();
            Workbook planilha = new XSSFWorkbook(leitorExcel);
            Sheet tabela = planilha.getSheetAt(0);

            int ultimaLinhaLida = lerContador();

            System.out.println("INFO - Iniciando a inserção de dados...");
            System.out.println("Isso pode durar alguns segundos.");

            for (int i = ultimaLinhaLida + 1; i < ultimaLinhaLida + 1 + LINHAS_POR_EXECUCAO; i++) {
                if (i > tabela.getLastRowNum()) {
                    System.out.println("Não há mais linhas para processar.");
                    break;
                }
                Row linha = tabela.getRow(i);
                System.out.println("Lendo e inserindo linha " + (i + 1) + " do arquivo Excel.");

                // Lê os valores das células
                String siglaEmpresaAerea = getCellValueAsString(linha.getCell(0));
                String numeroVoo = getCellValueAsString(linha.getCell(1));
                String codigoDI = getCellValueAsString(linha.getCell(2));
                String codigoTipoLinha = getCellValueAsString(linha.getCell(3));
                String siglaAeroportoOrigem = getCellValueAsString(linha.getCell(4)); //FK AEROPORTO ORIGEM
                String partidaPrevista = getCellValueAsString(linha.getCell(5));
                String partidaReal = getCellValueAsString(linha.getCell(6));
                String siglaAeroportoDestino = getCellValueAsString(linha.getCell(7)); //FK AEROPORTO DESTINO
                String chegadaPrevista = getCellValueAsString(linha.getCell(8));
                String chegadaReal = getCellValueAsString(linha.getCell(9));
                String situacaoVoo = getCellValueAsString(linha.getCell(10));

                String queryVerificaAeroportoDestino = "SELECT idAeroporto FROM aeroporto WHERE sigla = ?";
                int idAeroportoDestino = 0;
                try (PreparedStatement VerificaAeroporto = conexao.prepareStatement(queryVerificaAeroportoDestino)) {
                    VerificaAeroporto.setString(1, siglaAeroportoDestino);
                    ResultSet rsAeroportoDestino = VerificaAeroporto.executeQuery();

                    if (rsAeroportoDestino.next()) {
                        idAeroportoDestino = rsAeroportoDestino.getInt(1);
                    }

                }

                // Verifica se a SIGLA AEROPORTO já existe na tabela aeroporto
                String queryVerificaAeroportoOrigem = "SELECT idAeroporto FROM aeroporto WHERE sigla = ?";
                int idAeroportoOrigem = 0;
                try (PreparedStatement VerificaAeroporto = conexao.prepareStatement(queryVerificaAeroportoOrigem)) {
                    VerificaAeroporto.setString(1, siglaAeroportoOrigem);
                    ResultSet rsAeroporto = VerificaAeroporto.executeQuery();
                    if (rsAeroporto.next()) {
                        idAeroportoOrigem = rsAeroporto.getInt("idAeroporto");  // Já existe, obtém o idAeroporto
                        System.out.println("Sigla de Aeroporto já existe na tabela Aeroporto, idMatriz: " + rsAeroporto);
                    } else {
                        // Insere na tabela Aeroporto
                        String tabelaAeroporto = "INSERT INTO aeroporto (sigla) VALUES (?)";
                        try (PreparedStatement executarComandoAeroporto = conexao.prepareStatement(tabelaAeroporto, Statement.RETURN_GENERATED_KEYS)) {
                            executarComandoAeroporto.setString(1, siglaAeroportoOrigem);
                            executarComandoAeroporto.executeUpdate();
                            ResultSet rsNovoAeroporto = executarComandoAeroporto.getGeneratedKeys();
                            if (rsNovoAeroporto.next()) {
                                idAeroportoOrigem = rsNovoAeroporto.getInt(1);
                            }
                        }
                    }
                }

                String queryVerificaCompanhia = "SELECT idCompanhia FROM companhia WHERE sigla = ?";
                int idCompanhia = 0;
                try (PreparedStatement VerificaCompanhia = conexao.prepareStatement(queryVerificaCompanhia)) {
                    VerificaCompanhia.setString(1, siglaEmpresaAerea);
                    ResultSet rsCompanhia = VerificaCompanhia.executeQuery();
                    if (rsCompanhia.next()) {
                        idCompanhia = rsCompanhia.getInt("idCompanhia");  // Já existe, obtém o idCompanhia
                        System.out.println("CNPJ já existe na tabela Matriz, idMatriz: " + rsCompanhia);
                    } else {
                        // Insere na tabela Companhia
                        String tabelaCompanhia = "INSERT INTO companhia (sigla) VALUES (?)";
                        try (PreparedStatement executarComandoCompanhia = conexao.prepareStatement(tabelaCompanhia, Statement.RETURN_GENERATED_KEYS)) {
                            executarComandoCompanhia.setString(1, siglaEmpresaAerea);
                            executarComandoCompanhia.executeUpdate();
                            ResultSet rsNovoCompanhia = executarComandoCompanhia.getGeneratedKeys();
                            if (rsNovoCompanhia.next()) {
                                idCompanhia = rsNovoCompanhia.getInt(1);
                            }
                        }
                    }
                }

                // Insere na tabela ConsumoDados
                String ComandoInsert = "INSERT INTO voo (fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem, partidaPrevista, partidaReal, fkAeroportoDestino, chegadaPrevista, chegadaReal, situacaoVoo) VALUES (?, ?, ?, ?, ?, ?, ?,?, ? ,? ,?)";
                try (PreparedStatement executarComandoInsert = conexao.prepareStatement(ComandoInsert)) {
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

            atualizarContador(ultimaLinhaLida + LINHAS_POR_EXECUCAO);
            planilha.close();
            System.out.println("SUCESSO - Dados inseridos com êxito!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FALHA - Houve um erro ao inserir os dados.");
        }
    }

    // Método para reiniciar o contador
    private static void reiniciarContador() {
        atualizarContador(0); // Define o contador para 0
    }

    // Método para ler o contador do arquivo
    private static int lerContador() {
        try (BufferedReader br = new BufferedReader(new FileReader(CONTADOR_ARQUIVO))) {
            String linha = br.readLine();
            return linha != null ? Integer.parseInt(linha) : 0; // Retorna 0 se o arquivo estiver vazio
        } catch (IOException e) {
            return 0; // Retorna 0 se houver erro ao ler o arquivo
        }
    }

    // Método para atualizar o contador no arquivo
    private static void atualizarContador(int novaLinha) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONTADOR_ARQUIVO))) {
            bw.write(String.valueOf(novaLinha));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return 0.0;  // Retorna 0.0 se a célula estiver vazia
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();  // Retorna o valor como Double
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());  // Converte String para Double
                } catch (NumberFormatException e) {
                    return 0.0;  // Retorna 0.0 se a conversão falhar
                }
            default:
                return 0.0;
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
