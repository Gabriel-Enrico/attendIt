package attendIt.model;

public class Usuario {
    private int    id;
    private String nome;
    private String email;
    private String setor;
    private String perfil;

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }
    public String getNome()         { return nome; }
    public void setNome(String v)   { this.nome = v; }
    public String getEmail()        { return email; }
    public void setEmail(String v)  { this.email = v; }
    public String getSetor()        { return setor; }
    public void setSetor(String v)  { this.setor = v; }
    public String getPerfil()       { return perfil; }
    public void setPerfil(String v) { this.perfil = v; }

    @Override
    public String toString() { return nome + " (" + setor + ")"; }
}
