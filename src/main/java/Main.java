import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Log.generateLog("Aplicação iniciada.");

            JdbcTemplate jdbcTemplate = ConexaoBanco.getConnection();
            Log.generateLog("Conexão com o banco de dados estabelecida.");

            BaixarCSV baixarBase = new BaixarCSV();
            baixarBase.baixar("s3-alianza");
            Log.generateLog("Arquivo CSV baixado.");

            ConverterCSVparaXLSX converterCSVparaXLSX = new ConverterCSVparaXLSX();
            converterCSVparaXLSX.converter();
            Log.generateLog("Arquivo CSV convertido para XLSX.");

            LeitorExcel leitorExcel = new LeitorExcel(jdbcTemplate);
            String caminhoArquivo = "src\\vra_2022_11.xlsx";
            leitorExcel.lerEInserirDadosVoos(caminhoArquivo);

            Log.generateLog("Aplicação finalizada.");


            File arquivoXLSX = new File("src/vra_2022_11.xlsx");
            File arquivoCSV = new File("vra_2022_11.csv");

            if (arquivoXLSX.delete()) {
                Log.generateLog("Arquivo XLSX deletado com sucesso.");
            } else {
                Log.generateLog("Falha ao deletar o arquivo XLSX.");
            }

            if (arquivoCSV.delete()) {
                Log.generateLog("Arquivo CSV deletado com sucesso.");
            } else {
                Log.generateLog("Falha ao deletar o arquivo CSV.");
            }

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
