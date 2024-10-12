import java.util.List;

public class CompanhiaAerea {

    private List<Voo> voos;

    private String siglaICAO;

    public String getSiglaICAO() {
        return siglaICAO;
    }

    public void setSiglaICAO(String siglaICAO) {
        this.siglaICAO = siglaICAO;
    }

    public List<Voo> getVoos() {
        return voos;
    }

    public void setVoos(List<Voo> voos) {
        this.voos = voos;
    }

    @Override
    public String toString() {
        return "CompanhiaAerea{" +
                "siglaICAO='" + siglaICAO + '\'' +
                '}';
    }
}
