package attendIt.service;

import attendIt.dao.TecnicoDAO;
import attendIt.model.Tecnico;
import java.sql.SQLException;
import java.util.List;

public class TecnicoService {

    private final TecnicoDAO dao = new TecnicoDAO();

    public void cadastrar(Tecnico t) throws SQLException {
        if (t.getNome() == null || t.getNome().isBlank())
            throw new IllegalArgumentException("O nome do técnico é obrigatório.");
        if (t.getNivel() == null || t.getNivel().isBlank())
            throw new IllegalArgumentException("O nível do técnico é obrigatório.");
        dao.inserir(t);
    }

    public List<Tecnico> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<Tecnico> listarDisponiveis() throws SQLException {
        return dao.listarDisponiveis();
    }

    public void atualizar(Tecnico t) throws SQLException {
        if (t.getNome() == null || t.getNome().isBlank())
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        dao.atualizar(t);
    }

    public void excluir(int id) throws SQLException {
        int linhas = dao.excluir(id);
        if (linhas == 0)
            throw new IllegalArgumentException("Técnico #" + id + " não encontrado.");
    }
}
