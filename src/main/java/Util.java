import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static void baixarArquivoCSV() {
        String url = "https://s3-alianza.s3.amazonaws.com/Base+de+dados+-+Voos.csv";
        String caminhoLocal = "src/main/resources/Base de dados - Voos.csv";

        logger.info("Iniciando o download do arquivo CSV...");
        try (InputStream in = new URL(url).openStream(); FileOutputStream out = new FileOutputStream(caminhoLocal)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
            logger.info("Arquivo CSV baixado com sucesso em: {}", caminhoLocal);
        } catch (Exception e) {
            logger.error("Erro ao baixar o arquivo CSV: {}", e.getMessage());
        }
    }

    public static void ConverterCSVparaXLSX() {
        String csvFile = "src/main/resources/Base de dados - Voos.csv";
        String xlsxFile = "src/main/resources/Base de dados - Voos.xlsx";

        logger.info("Iniciando a conversão de CSV para XLSX...");
        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvFile)); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dados CSV");
            String linha;
            int rowNum = 0;

            while ((linha = br.readLine()) != null) {
                Row row = sheet.createRow(rowNum++);
                String[] valores = linha.split(",");

                for (int i = 0; i < valores.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valores[i]);
                }
                logger.debug("Linha {} inserida: {}", rowNum, String.join(", ", valores));
            }

            try (FileOutputStream fileOut = new FileOutputStream(xlsxFile)) {
                workbook.write(fileOut);
                logger.info("Conversão para XLSX concluída em: {}", xlsxFile);
            }
        } catch (IOException e) {
            logger.error("Erro ao converter CSV para XLSX: {}", e.getMessage());
        }
    }
}
