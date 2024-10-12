import java.time.LocalDateTime;

public class Voo {
    private Integer idVoo;
    private Integer fkCompanhia;
    private String numeroVoo;
    private String codigoDI;
    private String codigoTipoLinha;
    private Integer fkAeroportoOrigem;
    private LocalDateTime partidaPrevista;
    private LocalDateTime partidaReal;
    private Integer fkAeroportoDestino;
    private LocalDateTime chegadaPrevista;
    private LocalDateTime chegadaReal;
    private String StatusVoo;

    public Integer getIdVoo() {
        return idVoo;
    }

    public void setIdVoo(Integer idVoo) {
        this.idVoo = idVoo;
    }

    public Integer getFkCompanhia() {
        return fkCompanhia;
    }

    public void setFkCompanhia(Integer fkCompanhia) {
        this.fkCompanhia = fkCompanhia;
    }

    public String getNumeroVoo() {
        return numeroVoo;
    }

    public void setNumeroVoo(String numeroVoo) {
        this.numeroVoo = numeroVoo;
    }

    public String getCodigoDI() {
        return codigoDI;
    }

    public void setCodigoDI(String codigoDI) {
        this.codigoDI = codigoDI;
    }

    public String getCodigoTipoLinha() {
        return codigoTipoLinha;
    }

    public void setCodigoTipoLinha(String codigoTipoLinha) {
        this.codigoTipoLinha = codigoTipoLinha;
    }

    public Integer getFkAeroportoOrigem() {
        return fkAeroportoOrigem;
    }

    public void setFkAeroportoOrigem(Integer fkAeroportoOrigem) {
        this.fkAeroportoOrigem = fkAeroportoOrigem;
    }

    public LocalDateTime getPartidaPrevista() {
        return partidaPrevista;
    }

    public void setPartidaPrevista(LocalDateTime partidaPrevista) {
        this.partidaPrevista = partidaPrevista;
    }

    public LocalDateTime getPartidaReal() {
        return partidaReal;
    }

    public void setPartidaReal(LocalDateTime partidaReal) {
        this.partidaReal = partidaReal;
    }

    public Integer getFkAeroportoDestino() {
        return fkAeroportoDestino;
    }

    public void setFkAeroportoDestino(Integer fkAeroportoDestino) {
        this.fkAeroportoDestino = fkAeroportoDestino;
    }

    public LocalDateTime getChegadaPrevista() {
        return chegadaPrevista;
    }

    public void setChegadaPrevista(LocalDateTime chegadaPrevista) {
        this.chegadaPrevista = chegadaPrevista;
    }

    public LocalDateTime getChegadaReal() {
        return chegadaReal;
    }

    public void setChegadaReal(LocalDateTime chegadaReal) {
        this.chegadaReal = chegadaReal;
    }

    public String getStatusVoo() {
        return StatusVoo;
    }

    public void setStatusVoo(String statusVoo) {
        StatusVoo = statusVoo;
    }

    @Override
    public String toString() {
        return "Voo{" +
                "idVoo=" + idVoo +
                ", fkCompanhia=" + fkCompanhia +
                ", numeroVoo='" + numeroVoo + '\'' +
                ", codigoDI='" + codigoDI + '\'' +
                ", codigoTipoLinha='" + codigoTipoLinha + '\'' +
                ", fkAeroportoOrigem=" + fkAeroportoOrigem +
                ", partidaPrevista=" + partidaPrevista +
                ", partidaReal=" + partidaReal +
                ", fkAeroportoDestino=" + fkAeroportoDestino +
                ", chegadaPrevista=" + chegadaPrevista +
                ", chegadaReal=" + chegadaReal +
                ", StatusVoo='" + StatusVoo + '\'' +
                '}';
    }
}
