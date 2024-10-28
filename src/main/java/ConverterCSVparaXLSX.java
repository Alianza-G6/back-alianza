import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

public class ConverterCSVparaXLSX {

    public void converter() throws IOException {
        String nomeCSV = BaixarCSV.NOME_BASE_BAIXADA;
        File csvFile = new File("src/" + nomeCSV);

        if (!csvFile.exists()) {
            Log.generateLog("Arquivo CSV não encontrado: " + csvFile.getAbsolutePath());
            System.out.println("Arquivo CSV não encontrado: " + csvFile.getAbsolutePath());
            return;
        }

        try (Workbook workbook = new XSSFWorkbook();
             BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            System.out.println("Iniciando conversão do arquivo CSV para XLSX: " + nomeCSV);
            Log.generateLog("Iniciando conversão do arquivo CSV para XLSX: " + nomeCSV);

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

            String nomeBase = csvFile.getName();
            String nomeXLSX = nomeBase.substring(0, nomeBase.lastIndexOf('.')) + ".xlsx";
            System.out.println("Nome do arquivo XLSX a ser criado: " + nomeXLSX);

            try (FileOutputStream escritor = new FileOutputStream("src/" + nomeXLSX)) {
                workbook.write(escritor);
                Log.generateLog("Arquivo XLSX criado com sucesso: " + nomeXLSX);
                System.out.println("Arquivo XLSX criado com sucesso: " + nomeXLSX);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.generateLog("Erro ao converter o arquivo CSV para XLSX: " + e.getMessage());
            System.out.println("Erro ao converter o arquivo CSV para XLSX: " + e.getMessage());
        }
    }
}
