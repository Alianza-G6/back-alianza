import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LeitorExcel extends LeitorArquivo {

    private final S3Client s3Client;
    private final String bucketName = "s3-alianza";
    private final String pasta = "baseDeDados";

    public LeitorExcel(JdbcTemplate jdbcTemplate, S3Client s3Client) {
        super(jdbcTemplate);
        this.s3Client = s3Client;
    }

    @Override
    public void lerEInserirDados() {
        try {
            System.out.println("Iniciando processamento...");
            String nomeArquivo = buscarArquivoMaisRecente();
            System.out.println("Arquivo mais recente encontrado: " + nomeArquivo);

            try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(builder -> builder.bucket(bucketName).key(nomeArquivo));
                 InputStream inputStream = s3Object;
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);
                System.out.println("Planilha carregada. Iniciando a leitura.");

                Map<String, Integer> cacheCompanhias = carregarCompanhiasExistentes();
                Map<String, Integer> cacheAeroportos = carregarAeroportosExistentes();

                List<Object[]> voosBatch = new ArrayList<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String siglaCompanhia = getStringValueFromCell(row.getCell(0));
                    String numeroVoo = getStringValueFromCell(row.getCell(1));
                    String codigoDI = getStringValueFromCell(row.getCell(2));
                    String codigoTipoLinha = getStringValueFromCell(row.getCell(3));
                    String siglaAeroportoOrigem = getStringValueFromCell(row.getCell(4));
                    LocalDateTime partidaPrevista = converterData(row.getCell(5));
                    LocalDateTime partidaReal = converterData(row.getCell(6));
                    String siglaAeroportoDestino = getStringValueFromCell(row.getCell(7));
                    LocalDateTime chegadaPrevista = converterData(row.getCell(8));
                    LocalDateTime chegadaReal = converterData(row.getCell(9));
                    String statusVoo = getStringValueFromCell(row.getCell(10));

                    int fkCompanhia = cacheCompanhias.computeIfAbsent(siglaCompanhia, this::obterOuInserirCompanhia);
                    int fkAeroportoOrigem = cacheAeroportos.computeIfAbsent(siglaAeroportoOrigem, this::obterOuInserirAeroporto);
                    int fkAeroportoDestino = cacheAeroportos.computeIfAbsent(siglaAeroportoDestino, this::obterOuInserirAeroporto);

                    voosBatch.add(new Object[]{
                            fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha,
                            fkAeroportoOrigem, formatarData(partidaPrevista), formatarData(partidaReal),
                            fkAeroportoDestino, formatarData(chegadaPrevista), formatarData(chegadaReal),
                            statusVoo
                    });

                    if (voosBatch.size() >= 1000) { // Insere em lotes de 1000 registros
                        logTempoInsercao();
                        inserirVoosEmLote(voosBatch);
                        voosBatch.clear();
                    }
                }

                // Insere os voos restantes
                if (!voosBatch.isEmpty()) {
                    logTempoInsercao();
                    inserirVoosEmLote(voosBatch);
                }

                System.out.println("Processamento concluído com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("Erro durante o processamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logTempoInsercao() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Inserindo lote de 1000 voos às " + agora.format(formatter));
    }

    private String buscarArquivoMaisRecente() {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder().bucket(bucketName).prefix(pasta + "/").build();
        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);

        return response.contents().stream()
                .max(Comparator.comparing(S3Object::lastModified))
                .orElseThrow(() -> new RuntimeException("Nenhum arquivo encontrado no S3"))
                .key();
    }

    private Map<String, Integer> carregarCompanhiasExistentes() {
        String sql = "SELECT siglaICAO, idCompanhia FROM tbCompanhia";
        return jdbcTemplate.query(sql, rs -> {
            Map<String, Integer> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("siglaICAO"), rs.getInt("idCompanhia"));
            }
            return map;
        });
    }

    private Map<String, Integer> carregarAeroportosExistentes() {
        String sql = "SELECT siglaICAO, idAeroporto FROM tbAeroporto";
        return jdbcTemplate.query(sql, rs -> {
            Map<String, Integer> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("siglaICAO"), rs.getInt("idAeroporto"));
            }
            return map;
        });
    }

    private void inserirVoosEmLote(List<Object[]> voosBatch) {
        String sql = "INSERT INTO voo (fkCompanhia, numeroVoo, codigoDI, codigoTipoLinha, fkAeroportoOrigem, " +
                "partidaPrevista, partidaReal, fkAeroportoDestino, chegadaPrevista, chegadaReal, StatusVoo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, voosBatch);
        System.out.println(voosBatch.size() + " voos inseridos em lote.");
    }

    private String getStringValueFromCell(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell) ?
                    cell.getDateCellValue().toString() : String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }

    private LocalDateTime converterData(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            return cell.getLocalDateTimeCellValue();
        } catch (Exception e) {
            System.err.println("Erro ao converter data: " + e.getMessage());
            return null;
        }
    }

    private String formatarData(LocalDateTime data) {
        return data == null ? null : data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int obterOuInserirCompanhia(String siglaCompanhia) {
        String sqlSelect = "SELECT idCompanhia FROM tbCompanhia WHERE siglaICAO = ?";
        try {
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaCompanhia);
        } catch (EmptyResultDataAccessException e) {
            String sqlInsert = "INSERT INTO tbCompanhia (siglaICAO) VALUES (?)";
            jdbcTemplate.update(sqlInsert, siglaCompanhia);
            return jdbcTemplate.queryForObject(sqlSelect, Integer.class, siglaCompanhia);
        }
    }

    public int obterOuInserirAeroporto(String siglaAeroporto) {
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
