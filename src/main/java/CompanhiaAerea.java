public class CompanhiaAerea extends Empresa {
    private Integer idCompanhia;
    private String nome;

    public Integer getIdCompanhia() {
        return idCompanhia;
    }

    public void setIdCompanhia(Integer idCompanhia) {
        this.idCompanhia = idCompanhia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "CompanhiaAerea{" +
                "idCompanhia=" + idCompanhia +
                ", nome='" + nome + '\'' +
                '}';
    }
}
