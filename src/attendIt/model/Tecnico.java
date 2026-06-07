package attendIt.model;

public class Tecnico {
    private int     id;
    private String  nome;
    private String  especialidade;
    private String  nivel;
    private boolean disponivel;

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getNome()                 { return nome; }
    public void setNome(String v)           { this.nome = v; }
    public String getEspecialidade()        { return especialidade; }
    public void setEspecialidade(String v)  { this.especialidade = v; }
    public String getNivel()                { return nivel; }
    public void setNivel(String v)          { this.nivel = v; }
    public boolean isDisponivel()           { return disponivel; }
    public void setDisponivel(boolean v)    { this.disponivel = v; }

    @Override
    public String toString() { return nome; }
}

