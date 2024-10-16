import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

public class    Main {
    public static void main(String[] args) {
        try {
            Log.generateLog("Aplicação iniciada.");

            Log.generateLog("Conexão com o banco de dados estabelecida.");
            JdbcTemplate jdbcTemplate = ConexaoBanco.getConnection();

            BaixarCSV baixarBase = new BaixarCSV();
            baixarBase.baixar("s3-alianza");
            Log.generateLog("Arquivo CSV baixado.");

            ConverterCSVparaXLSX converterCSVparaXLSX = new ConverterCSVparaXLSX();
            converterCSVparaXLSX.converter();
            Log.generateLog("Arquivo CSV convertido para XLSX.");

            Log.generateLog("Aplicação finalizada.");

        } catch (IOException e) {
            e.printStackTrace();
            try {
                Log.generateLog("Erro ao gerar log: " + e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

