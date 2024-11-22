public class Aeroporto extends Empresa {
    private Integer idAeroporto;
    private String nome;


    public Integer getIdAeroporto() {
        return idAeroporto;
    }

    public void setIdAeroporto(Integer idAeroporto) {
        this.idAeroporto = idAeroporto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Aeroporto{" +
                "idAeroporto=" + idAeroporto +
                ", nome='" + nome + '\'' +
                '}';
    }
}
