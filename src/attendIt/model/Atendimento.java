package attendIt.model;

import java.time.LocalDateTime;

public class Atendimento {
    private int id;
    private int idChamado;
    private int idTecnico;
    private LocalDateTime dtAtribuicao;
    private LocalDateTime dtResolucao;
    private String observacao;

    // nomes para exibição na tabela
    private String nomeTecnico;
    private String tituloChamado;

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }
    public int getIdChamado()                 { return idChamado; }
    public void setIdChamado(int v)           { this.idChamado = v; }
    public int getIdTecnico()                 { return idTecnico; }
    public void setIdTecnico(int v)           { this.idTecnico = v; }
    public LocalDateTime getDtAtribuicao()    { return dtAtribuicao; }
    public void setDtAtribuicao(LocalDateTime v) { this.dtAtribuicao = v; }
    public LocalDateTime getDtResolucao()     { return dtResolucao; }
    public void setDtResolucao(LocalDateTime v)  { this.dtResolucao = v; }
    public String getObservacao()             { return observacao; }
    public void setObservacao(String v)       { this.observacao = v; }
    public String getNomeTecnico()            { return nomeTecnico; }
    public void setNomeTecnico(String v)      { this.nomeTecnico = v; }
    public String getTituloChamado()          { return tituloChamado; }
    public void setTituloChamado(String v)    { this.tituloChamado = v; }
}