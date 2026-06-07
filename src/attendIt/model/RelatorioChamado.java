package attendIt.model;

import java.time.LocalDateTime;

public class RelatorioChamado {
    private int           idChamado;
    private String        titulo;
    private String        status;
    private String        prioridade;
    private String        solicitante;
    private String        setor;
    private String        categoria;
    private int           slaHoras;
    private String        tecnico;
    private String        nivelTecnico;
    private LocalDateTime dtAbertura;
    private LocalDateTime dtFechamento;
    private LocalDateTime dtAtribuicao;
    private LocalDateTime dtResolucao;
    private String        observacao;

    public int getIdChamado() { return idChamado; }
    public void setIdChamado(int v)                  { this.idChamado = v; }
    public String getTitulo()                        { return titulo; }
    public void setTitulo(String v)                  { this.titulo = v; }
    public String getStatus()                        { return status; }
    public void setStatus(String v)                  { this.status = v; }
    public String getPrioridade()                    { return prioridade; }
    public void setPrioridade(String v)              { this.prioridade = v; }
    public String getSolicitante()                   { return solicitante; }
    public void setSolicitante(String v)             { this.solicitante = v; }
    public String getSetor()                         { return setor; }
    public void setSetor(String v)                   { this.setor = v; }
    public String getCategoria()                     { return categoria; }
    public void setCategoria(String v)               { this.categoria = v; }
    public int getSlaHoras()                         { return slaHoras; }
    public void setSlaHoras(int v)                   { this.slaHoras = v; }
    public String getTecnico()                       { return tecnico; }
    public void setTecnico(String v)                 { this.tecnico = v; }
    public String getNivelTecnico()                  { return nivelTecnico; }
    public void setNivelTecnico(String v)            { this.nivelTecnico = v; }
    public LocalDateTime getDtAbertura()             { return dtAbertura; }
    public void setDtAbertura(LocalDateTime v)       { this.dtAbertura = v; }
    public LocalDateTime getDtFechamento()           { return dtFechamento; }
    public void setDtFechamento(LocalDateTime v)     { this.dtFechamento = v; }
    public LocalDateTime getDtAtribuicao()           { return dtAtribuicao; }
    public void setDtAtribuicao(LocalDateTime v)     { this.dtAtribuicao = v; }
    public LocalDateTime getDtResolucao()            { return dtResolucao; }
    public void setDtResolucao(LocalDateTime v)      { this.dtResolucao = v; }
    public String getObservacao()                    { return observacao; }
    public void setObservacao(String v)              { this.observacao = v; }
}