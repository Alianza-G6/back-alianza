public class Companhia {
    private int idCompanhia;
    private String nome;
    private String sigla;
    private String cnpj;
    private int funcionarioIdFuncionario; // Chave estrangeira

    // Getters e Setters
    public int getIdCompanhia() {
        return idCompanhia;
    }

    public void setIdCompanhia(int idCompanhia) {
        this.idCompanhia = idCompanhia;
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

    public int getFuncionarioIdFuncionario() {
        return funcionarioIdFuncionario;
    }

    public void setFuncionarioIdFuncionario(int funcionarioIdFuncionario) {
        this.funcionarioIdFuncionario = funcionarioIdFuncionario;
    }

    @Override
    public String toString() {
        return "Companhia{idCompanhia=" + idCompanhia + ", nome='" + nome + "', sigla='" + sigla + "', cnpj='" + cnpj + "'}";
    }
}
