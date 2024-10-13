import java.util.List;

public class CompanhiaAerea {

    private List<Voo> voos;
    private String siglaICAO;
    private Integer id;

    public CompanhiaAerea() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public CompanhiaAerea(String siglaICAO, List<Voo> voos) {
        this.siglaICAO = siglaICAO;
        this.voos = voos;
    }


    public String getSiglaICAO(String razaoSocial) {
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
                ", voos=" + voos +
                '}';
    }
}
