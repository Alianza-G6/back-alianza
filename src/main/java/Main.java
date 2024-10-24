import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        BaixarCSV baixarBase = new BaixarCSV();

        baixarBase.baixar("s3-alianza");

        ConverterCSVparaXLSX converterCSVparaXLSX = new ConverterCSVparaXLSX();

        converterCSVparaXLSX.converter();

        JdbcTemplate jdbcTemplate = ConexaoBanco.getConnection();

        LeitorExcel leitorExcel = new LeitorExcel(jdbcTemplate);

        String caminhoArquivo = "src\\vra_2022_11.xlsx";

        leitorExcel.lerEInserirDadosVoos(caminhoArquivo);


    }
}
