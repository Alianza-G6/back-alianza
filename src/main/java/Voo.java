public class Voo {
    private int idVoo;
    private int companhiaIdCompanhia; // Chave estrangeira
    private int idAeroportoInicial; // Chave estrangeira
    private int idAeroportoFinal; // Chave estrangeira
    private String prevChegada;
    private String chegada;
    private String prevSaida;
    private String saida;
    private String situacaoVoo;

    // Getters e Setters
    public int getIdVoo() {
        return idVoo;
    }

    public void setIdVoo(int idVoo) {
        this.idVoo = idVoo;
    }

    public int getCompanhiaIdCompanhia() {
        return companhiaIdCompanhia;
    }

    public void setCompanhiaIdCompanhia(int companhiaIdCompanhia) {
        this.companhiaIdCompanhia = companhiaIdCompanhia;
    }

    public int getIdAeroportoInicial() {
        return idAeroportoInicial;
    }

    public void setIdAeroportoInicial(int idAeroportoInicial) {
        this.idAeroportoInicial = idAeroportoInicial;
    }

    public int getIdAeroportoFinal() {
        return idAeroportoFinal;
    }

    public void setIdAeroportoFinal(int idAeroportoFinal) {
        this.idAeroportoFinal = idAeroportoFinal;
    }

    public String getPrevChegada() {
        return prevChegada;
    }

    public void setPrevChegada(String prevChegada) {
        this.prevChegada = prevChegada;
    }

    public String getChegada() {
        return chegada;
    }

    public void setChegada(String chegada) {
        this.chegada = chegada;
    }

    public String getPrevSaida() {
        return prevSaida;
    }

    public void setPrevSaida(String prevSaida) {
        this.prevSaida = prevSaida;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }

    public String getSituacaoVoo() {
        return situacaoVoo;
    }

    public void setSituacaoVoo(String situacaoVoo) {
        this.situacaoVoo = situacaoVoo;
    }

    @Override
    public String toString() {
        return "Voo{idVoo=" + idVoo + ", companhiaIdCompanhia=" + companhiaIdCompanhia + ", idAeroportoInicial=" + idAeroportoInicial + ", idAeroportoFinal=" + idAeroportoFinal + ", prevChegada='" + prevChegada + "', chegada='" + chegada + "', prevSaida='" + prevSaida + "', saida='" + saida + "', situacaoVoo='" + situacaoVoo + "'}";
    }
}
