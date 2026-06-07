package attendIt.model;

public class Categoria {
    private int    id;
    private String descricao;
    private int    slaHoras;

    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }
    public String getDescricao()     { return descricao; }
    public void setDescricao(String v) { this.descricao = v; }
    public int getSlaHoras()         { return slaHoras; }
    public void setSlaHoras(int v)   { this.slaHoras = v; }

    @Override
    public String toString() { return descricao + " (SLA: " + slaHoras + "h)"; }
}

