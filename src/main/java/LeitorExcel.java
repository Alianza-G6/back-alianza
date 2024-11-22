import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.format.DateTimeFormatter;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class LeitorExcel extends LeitorArquivo {

    public LeitorExcel(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void lerEInserirDados(String caminhoArquivo) {
        System.out.println("Iniciando a leitura do arquivo Excel: " + caminhoArquivo);
        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Planilha carregada. Iniciando a leitura das linhas.");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

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

                System.out.println("Lendo dados do voo: " + numeroVoo);

                int fkCompanhia = obterOuInserirCompanhia(siglaCompanhia);
                int fkAeroportoOrigem = obterOuInserirAeroporto(siglaAeroportoOrigem);
                int fkAeroportoDestino = obterOuInserirAeroporto(siglaAeroportoDestino);

                inserirVoo(fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem,
                        partidaPrevista, partidaReal, fkAeroportoDestino, chegadaPrevista, chegadaReal, statusVoo);
                System.out.println("Voo inserido com sucesso: " + numeroVoo);
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
        return LocalDateTime.parse(dataString, formatter);
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
        return data.format(outputFormatter);
    }
}
