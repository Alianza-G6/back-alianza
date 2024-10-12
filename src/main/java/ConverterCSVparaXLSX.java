import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

public class ConverterCSVparaXLSX {

    public void converter() {
        String nomeCSV = BaixarCSV.NOME_BASE_BAIXADA;
        System.out.println("O NOME DA BASE É " + nomeCSV);

        try (Workbook workbook = new XSSFWorkbook();
             BufferedReader br = new BufferedReader(new FileReader(nomeCSV))) {

            Sheet planilha = workbook.createSheet("Dados");
            String linha;
            int indiceLinha = 0;

            if ((linha = br.readLine()) != null) {
                Row cabecalho = planilha.createRow(indiceLinha++);
                String[] nomesColunas = linha.split(";");
                for (int i = 0; i < nomesColunas.length; i++) {
                    cabecalho.createCell(i).setCellValue(nomesColunas[i].trim());
                }
            }

            while ((linha = br.readLine()) != null) {
                Row linhaAtual = planilha.createRow(indiceLinha++);
                String[] colunas = linha.split(";");

                for (int i = 0; i < colunas.length; i++) {
                    linhaAtual.createCell(i).setCellValue(colunas[i].trim());
                }
            }

            String nomeBase = new File(nomeCSV).getName();
            String nomeXLSX = nomeBase.substring(0, nomeBase.lastIndexOf('.')) + ".xlsx";

            try (FileOutputStream escritor = new FileOutputStream("src/" + nomeXLSX)) {
                workbook.write(escritor);
                System.out.println("Arquivo XLSX criado com sucesso: src/" + nomeXLSX);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
