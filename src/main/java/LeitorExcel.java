import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import java.time.format.DateTimeFormatter;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class LeitorExcel {
    private final JdbcTemplate jdbcTemplate;

    public LeitorExcel(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void lerEInserirDadosVoos(String caminhoArquivo) {
        System.out.println("Iniciando a leitura do arquivo Excel: " + caminhoArquivo);
        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Planilha carregada. Iniciando a leitura das linhas.");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    System.out.println("Linha " + i + " está vazia. Pulando.");
                    continue;
                }

                // Lendo dados do voo
                String siglaCompanhia = row.getCell(0).getStringCellValue();
                String numeroVoo = row.getCell(1).getStringCellValue();
                String codigoDI = row.getCell(2).getStringCellValue();
                String codigoTipoLinha = row.getCell(3).getStringCellValue();
                String siglaAeroportoOrigem = row.getCell(4).getStringCellValue();

                LocalDateTime partidaPrevista = converterData(row.getCell(5));
                LocalDateTime partidaReal = converterData(row.getCell(6));
                String siglaAeroportoDestino = row.getCell(7).getStringCellValue();
                LocalDateTime chegadaPrevista = converterData(row.getCell(8));
                LocalDateTime chegadaReal = converterData(row.getCell(9));
                String statusVoo = row.getCell(10).getStringCellValue();

                System.out.println("Inserindo dados do voo: " + numeroVoo);

                int fkCompanhia = obterOuInserirCompanhia(siglaCompanhia);
                int fkAeroportoOrigem = obterOuInserirAeroporto(siglaAeroportoOrigem);
                int fkAeroportoDestino = obterOuInserirAeroporto(siglaAeroportoDestino);

                inserirVoo(fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem,
                        partidaPrevista, partidaReal, fkAeroportoDestino, chegadaPrevista, chegadaReal, statusVoo);

            }
            System.out.println("Leitura e inserção de dados concluídas.");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LocalDateTime converterData(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        String dataString = cell.getStringCellValue();
        if (dataString.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dataConvertida = LocalDateTime.parse(dataString, formatter);
        return dataConvertida;
    }

    private int obterOuInserirCompanhia(String siglaCompanhia) {
        String sqlSelect = "SELECT idCompanhia FROM tbCompanhia WHERE siglaICAO = ?";
        try {
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaCompanhia);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Companhia não encontrada, inserindo nova: " + siglaCompanhia);
            String sqlInsert = "INSERT INTO tbCompanhia (siglaICAO) VALUES (?)";
            jdbcTemplate.update(sqlInsert, siglaCompanhia);
            int idCompanhia = jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaCompanhia);
            System.out.println("Nova companhia inserida com ID: " + idCompanhia);
            return idCompanhia;
        }
    }

    private int obterOuInserirAeroporto(String siglaAeroporto) {
        String sqlSelect = "SELECT idAeroporto FROM tbAeroporto WHERE siglaICAO = ?";
        try {
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaAeroporto);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Aeroporto não encontrado, inserindo novo: " + siglaAeroporto);
            String sqlInsert = "INSERT INTO tbAeroporto (siglaICAO) VALUES (?)";
            jdbcTemplate.update(sqlInsert, siglaAeroporto);
            int idAeroporto = jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaAeroporto);
            System.out.println("Novo aeroporto inserido com ID: " + idAeroporto);
            return idAeroporto;
        }
    }

    private void inserirVoo(int fkCompanhia, String numeroVoo, String codigoDI, String codigoTipoLinha,
                            int fkAeroportoOrigem, LocalDateTime partidaPrevista, LocalDateTime partidaReal,
                            int fkAeroportoDestino, LocalDateTime chegadaPrevista, LocalDateTime chegadaReal,
                            String statusVoo) {

        String sql = "INSERT INTO voo (fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem, partidaPrevista, partidaReal, fkAeroportoDestino, chegadaPrevista, chegadaReal, StatusVoo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String formattedPartidaPrevista = formatarData(partidaPrevista);
        String formattedPartidaReal = formatarData(partidaReal);
        String formattedChegadaPrevista = formatarData(chegadaPrevista);
        String formattedChegadaReal = formatarData(chegadaReal);

        jdbcTemplate.update(sql, fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem,
                formattedPartidaPrevista, formattedPartidaReal, fkAeroportoDestino, formattedChegadaPrevista,
                formattedChegadaReal, statusVoo);
        System.out.println("Inserido voo: " + numeroVoo + " com status: " + statusVoo);
    }

    private String formatarData(LocalDateTime data) {
        if (data == null) {
            return null;
        }
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String dataFormatada = data.format(outputFormatter);
        return dataFormatada;
    }
}
