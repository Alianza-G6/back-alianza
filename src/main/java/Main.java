import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Main {
    private static final int LINHAS_POR_EXECUCAO = 1;
    private static final String CONTADOR_ARQUIVO = "contador.txt";
    private static final String ARQUIVO_EXCEL_URL = "https://s3-alianza.s3.amazonaws.com/Base%2Bde%2Bdados%2B-%2BVoos.xlsx";
    private static final String MYSQL_URL = "jdbc:mysql://54.227.225.77:3306/Alianza";
    private static final String USUARIO = "root";
    private static final String SENHA = "urubu100";

    public static void main(String[] args) {
        int ultimaLinhaLida = 0;
        try {
            S3Download downloader = new S3Download();
            downloader.downloadFile(ARQUIVO_EXCEL_URL, "src/main/resources/Base de dados - Voos.xlsx");

            LeitorExcel excelReader = new LeitorExcel("https://s3-alianza.s3.amazonaws.com/Base%2Bde%2Bdados%2B-%2BVoos.xlsx");
            ConexaoBanco dbHelper = new ConexaoBanco(MYSQL_URL, USUARIO, SENHA);
            InsertBanco insertBanco = new InsertBanco(dbHelper.getConnection());
            Contador contador = new Contador(CONTADOR_ARQUIVO);

            ultimaLinhaLida = contador.ler();
            Sheet sheet = excelReader.getSheet(0);

            for (int i = ultimaLinhaLida + 1; i < ultimaLinhaLida + 1 + LINHAS_POR_EXECUCAO && i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                insertBanco.inserirVoo(row);
                System.out.println("Linha " + (i + 1) + " processada.");
            }

            contador.atualizar(ultimaLinhaLida + LINHAS_POR_EXECUCAO);
            excelReader.close();
            dbHelper.close();

            System.out.println("Processamento concluído.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao processar dados.");
        }
    }
}
