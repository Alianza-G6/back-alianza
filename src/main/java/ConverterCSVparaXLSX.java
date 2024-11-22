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

        try (Workbook workbook = new XSSFWorkbook();
             BufferedReader br = new BufferedReader(new FileReader(nomeCSV))) {

            Log.generateLog("Iniciando conversão do arquivo CSV para XLSX: " + nomeCSV);
            String logMessage = "Iniciando conversão do arquivo CSV para XLSX: " + nomeCSV;
            Log.generateLog(logMessage);
            enviarParaSlack(logMessage);

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
                Log.generateLog("Arquivo XLSX criado com sucesso: " + nomeXLSX);
                String successMessage = "Arquivo XLSX criado com sucesso: " + nomeXLSX;
                Log.generateLog(successMessage);
                enviarParaSlack(successMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Log.generateLog("Erro ao converter o arquivo CSV para XLSX: " + e.getMessage());
                String errorMessage = "Erro ao converter o arquivo CSV para XLSX: " + e.getMessage();
                Log.generateLog(errorMessage);
                enviarParaSlack(errorMessage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void enviarParaSlack(String message) {
        try {
            NotificacaoSlack.EnviarNotificacaoSlack(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}