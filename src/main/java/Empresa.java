public class Empresa {
    private String siglaICAO;

    public String getSiglaICAO() {
        return siglaICAO;
    }

    public void setSiglaICAO(String siglaICAO) {
        this.siglaICAO = siglaICAO;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "siglaICAO='" + siglaICAO + '\'' +
                '}';
    }
}

