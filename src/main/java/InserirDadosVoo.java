import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InserirDadosVoo {

    private JdbcTemplate jdbcTemplate;

    public InserirDadosVoo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void lerEInserirDados(String arquivoPath) {
        try (FileInputStream file = new FileInputStream(arquivoPath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Ignora o cabeçalho
                }

                String siglaICAO = row.getCell(0).getStringCellValue();
                String numeroVoo = row.getCell(1).getStringCellValue();
                String codigoDI = row.getCell(2).getStringCellValue();
                String codigoTipoLinha = row.getCell(3).getStringCellValue();
                String siglaICAOAeroportoOrigem = row.getCell(4).getStringCellValue();
                String partidaPrevista = row.getCell(5).getStringCellValue();
                String partidaReal = row.getCell(6).getStringCellValue();
                String siglaICAOAeroportoDestino = row.getCell(7).getStringCellValue();
                String chegadaPrevista = row.getCell(8).getStringCellValue();
                String chegadaReal = row.getCell(9).getStringCellValue();
                String situacaoVoo = row.getCell(10).getStringCellValue();

                // Convertendo as strings de data para Timestamp
                Timestamp partidaPrevistaTs = converterParaTimestamp(partidaPrevista, sdf);
                Timestamp partidaRealTs = converterParaTimestamp(partidaReal, sdf);
                Timestamp chegadaPrevistaTs = converterParaTimestamp(chegadaPrevista, sdf);
                Timestamp chegadaRealTs = converterParaTimestamp(chegadaReal, sdf);

                // Inserir no banco de dados
                String sql = "INSERT INTO Voo (SiglaICAO, NumeroVoo, CodigoDI, CodigoTipoLinha, " +
                        "SiglaICAOAeroportoOrigem, PartidaPrevista, PartidaReal, " +
                        "SiglaICAOAeroportoDestino, ChegadaPrevista, ChegadaReal, SituacaoVoo) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(sql, siglaICAO, numeroVoo, codigoDI, codigoTipoLinha,
                        siglaICAOAeroportoOrigem, partidaPrevistaTs, partidaRealTs,
                        siglaICAOAeroportoDestino, chegadaPrevistaTs, chegadaRealTs, situacaoVoo);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private Timestamp converterParaTimestamp(String data, SimpleDateFormat sdf) throws ParseException {
        if (data == null || data.isEmpty()) {
            return null; // ou tratar como preferir
        }
        Date date = sdf.parse(data);
        return new Timestamp(date.getTime());
    }
}
