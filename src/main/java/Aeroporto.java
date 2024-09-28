public class Aeroporto {
    private int idAeroporto;
    private String nome;
    private String sigla;
    private String cnpj;
    private String cep;
    private int funcionarioIdFuncionario; // Chave estrangeira

    // Getters e Setters
    public int getIdAeroporto() {
        return idAeroporto;
    }

    public void setIdAeroporto(int idAeroporto) {
        this.idAeroporto = idAeroporto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public int getFuncionarioIdFuncionario() {
        return funcionarioIdFuncionario;
    }

    public void setFuncionarioIdFuncionario(int funcionarioIdFuncionario) {
        this.funcionarioIdFuncionario = funcionarioIdFuncionario;
    }

    @Override
    public String toString() {
        return "Aeroporto{idAeroporto=" + idAeroporto + ", nome='" + nome + "', sigla='" + sigla + "', cnpj='" + cnpj + "', cep='" + cep + "'}";
    }
}
