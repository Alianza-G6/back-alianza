public class Aeroporto {

    private String sigla;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public String toString() {
        return "Aeroporto{" +
                "sigla='" + sigla + '\'' +
                '}';
    }
}
