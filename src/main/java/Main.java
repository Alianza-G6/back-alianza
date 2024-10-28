import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");

            System.out.println("Aplicação iniciada.");

            JdbcTemplate jdbcTemplate = ConexaoBanco.getConnection();
            System.out.println("Conexão com o banco de dados estabelecida.");

            BaixarCSV baixarBase = new BaixarCSV();
            baixarBase.baixar("s3-alianza");
            System.out.println("Arquivo CSV baixado.");

            ConverterCSVparaXLSX converterCSVparaXLSX = new ConverterCSVparaXLSX();
            converterCSVparaXLSX.converter();
            System.out.println("Arquivo CSV convertido para XLSX.");

            LeitorExcel leitorExcel = new LeitorExcel(jdbcTemplate);
            String caminhoArquivo = "src/vra_2022_11.xlsx";
            leitorExcel.lerEInserirDadosVoos(caminhoArquivo);
            System.out.println("Dados do voo inseridos no banco de dados.");

            System.out.println("Aplicação finalizada.");

            File arquivoXLSX = new File("src/vra_2022_11.xlsx");
            File arquivoCSV = new File("vra_2022_11.csv");

            if (arquivoXLSX.delete()) {
                System.out.println("Arquivo XLSX deletado com sucesso.");
            } else {
                System.out.println("Falha ao deletar o arquivo XLSX.");
            }

            if (arquivoCSV.delete()) {
                System.out.println("Arquivo CSV deletado com sucesso.");
            } else {
                System.out.println("Falha ao deletar o arquivo CSV.");
            }

        } catch (IOException e) {
            System.out.println("Erro ao gerar log: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
