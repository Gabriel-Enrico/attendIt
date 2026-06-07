package attendIt.service;

import attendIt.model.Categoria;
import attendIt.dao.CategoriaDAO;
import java.sql.SQLException;
import java.util.List;

public class CategoriaService {

    private final CategoriaDAO dao = new CategoriaDAO();

    public void cadastrar(Categoria c) throws SQLException {
        if (c.getDescricao() == null || c.getDescricao().isBlank())
            throw new IllegalArgumentException("A descrição da categoria é obrigatória.");
        if (c.getSlaHoras() <= 0)
            throw new IllegalArgumentException("O SLA deve ser maior que zero.");
        dao.inserir(c);
    }

    public List<Categoria> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public void atualizar(Categoria c) throws SQLException {
        if (c.getDescricao() == null || c.getDescricao().isBlank())
            throw new IllegalArgumentException("A descrição não pode ser vazia.");
        if (c.getSlaHoras() <= 0)
            throw new IllegalArgumentException("O SLA deve ser maior que zero.");
        dao.atualizar(c);
    }

    public void excluir(int id) throws SQLException {
        int linhas = dao.excluir(id);
        if (linhas == 0)
            throw new IllegalArgumentException("Categoria #" + id + " não encontrada.");
    }
}
