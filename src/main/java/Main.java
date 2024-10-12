public class Main{
    public static void main(String[] args) {

        BaixarCSV baixarBase = new BaixarCSV();

        baixarBase.baixar("s3-alianza");

        ConverterCSVparaXLSX converterCSVparaXLSX = new ConverterCSVparaXLSX();

        converterCSVparaXLSX.converter();

    }
}