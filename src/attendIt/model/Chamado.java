package attendIt.model;
import java.time.LocalDateTime;
public class Chamado {
    private int id;
    private int idUsuario;
    private int idCategoria;
    private String titulo;
    private String descricao;
    private String status;
    private String prioridade;
    private LocalDateTime dtAbertura;
    private LocalDateTime dtFechamento;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public LocalDateTime getDtAbertura() { return dtAbertura; }
    public void setDtAbertura(LocalDateTime dtAbertura) { this.dtAbertura = dtAbertura; }
    public LocalDateTime getDtFechamento() { return dtFechamento; }
    public void setDtFechamento(LocalDateTime dtFechamento) { this.dtFechamento = dtFechamento; }

    @Override
    public String toString() { return "#" + id + " - " + titulo + " [" + status + "]"; }
}