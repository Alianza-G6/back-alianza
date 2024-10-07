public class Voo {
    private int idVoo;
    private int companhiaId;
    private int aeroportoInicialId;
    private int aeroportoFinalId;
    private String prevChegada;
    private String chegada;
    private String prevSaida;
    private String saida;
    private String situacaoVoo;


    public int getIdVoo() {
        return idVoo;
    }

    public void setIdVoo(int idVoo) {
        this.idVoo = idVoo;
    }

    public int getCompanhiaId() {
        return companhiaId;
    }

    public void setCompanhiaId(int companhiaId) {
        this.companhiaId = companhiaId;
    }

    public int getAeroportoInicialId() {
        return aeroportoInicialId;
    }

    public void setAeroportoInicialId(int aeroportoInicialId) {
        this.aeroportoInicialId = aeroportoInicialId;
    }

    public int getAeroportoFinalId() {
        return aeroportoFinalId;
    }

    public void setAeroportoFinalId(int aeroportoFinalId) {
        this.aeroportoFinalId = aeroportoFinalId;
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
        return "Voo{" +
                "idVoo=" + idVoo +
                ", companhiaId=" + companhiaId +
                ", aeroportoInicialId=" + aeroportoInicialId +
                ", aeroportoFinalId=" + aeroportoFinalId +
                ", prevChegada='" + prevChegada + '\'' +
                ", chegada='" + chegada + '\'' +
                ", prevSaida='" + prevSaida + '\'' +
                ", saida='" + saida + '\'' +
                ", situacaoVoo='" + situacaoVoo + '\'' +
                '}';
    }
}
