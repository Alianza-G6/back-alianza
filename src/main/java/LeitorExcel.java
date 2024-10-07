import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.net.URL;

public class LeitorExcel {
    private Workbook workbook;

    public LeitorExcel(String fileUrl) throws Exception {
        InputStream leitorExcel = new URL(fileUrl).openStream();
        this.workbook = new XSSFWorkbook(leitorExcel);
    }

    public Sheet getSheet(int index) {
        return workbook.getSheetAt(index);
    }

    public void close() throws Exception {
        workbook.close();
    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue().toString() : String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
